package topdownshooter.Panels;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import topdownshooter.Core.Globals;
import topdownshooter.Core.PlayerItem;
import topdownshooter.Core.TileGenerator;
import topdownshooter.Core.ConfigHandler;
import topdownshooter.Core.GameLevel;
import topdownshooter.Core.TimeTick;
import topdownshooter.Player.Player;
import topdownshooter.Weapon.WeaponType;
import topdownshooter.Weapon.Projectiles.AcidSpit;
import topdownshooter.Weapon.Projectiles.Bullet;
import topdownshooter.Weapon.Projectiles.Projectile;
import topdownshooter.Weapon.Projectiles.ProjectileType;
import topdownshooter.Weapon.Projectiles.Rocket;
import topdownshooter.Weapon.Projectiles.ShotgunPellets;
import topdownshooter.Zombie.AbstractZombie;
import topdownshooter.Zombie.AcidZombie;
import topdownshooter.Zombie.Zombie;
import topdownshooter.Zombie.ZombieType;
import topdownshooter.Panels.NotificationPanel;

public class GameAreaPanel extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener{
    private GamePanel parentPanel = null;
    private Player player = null;
    private ArrayList<Zombie> zombies = null;
    private ArrayList<Projectile> projectiles = null;

    private boolean isGamePaused = false;
    private Timer gameTimer;
    private TimeTick fireRateTick = null;
    private TimeTick waveSuspendTick = null;

    private TileGenerator playgroundTileGenerator = null;
    private ConfigHandler config;

    private NotificationPanel nPanel = null;
    private GameLevel gameLevel = null;

    public GameAreaPanel(ConfigHandler config) {
        setFocusable(true);
        requestFocusInWindow();
        setBackground(Color.BLACK);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        this.config = config;

        gameLevel = new GameLevel(this.config);

        player = new Player(this.config);
        zombies = new ArrayList<>();
        projectiles = new ArrayList<>();

        gameTimer = new Timer(Globals.GAME_TICK_MS, e -> this.update());
        gameTimer.setRepeats(true);

        this.playgroundTileGenerator = new TileGenerator(Globals.PLAYGROUND_TILE_PATH);

        waveSuspendTick = new TimeTick(Globals.Time2GameTick(Globals.WAVE_SUSPEND_DURATION_MS), () -> startWave());
        gameTimer.start();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
     
        this.playgroundTileGenerator.draw(g, getWidth(), getHeight());
        player.draw(g);

        for (Zombie z : zombies) {
            z.draw(g);
        }

        for (Projectile projectile : projectiles) {
            if (projectile == null) continue;
            projectile.draw(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    public void setParentPanel(GamePanel panel) {
        this.parentPanel = panel;
    }

    private void startWave() {
        if (this.zombies.size() == 0) {
            this.nPanel = new NotificationPanel(this, "New Wave comming...");
            this.nPanel.show(3000);

            WeaponType weaponPrize = this.gameLevel.startWave();

            if (weaponPrize!=null && weaponPrize!=WeaponType.UNDEFINED) {
                boolean isAdded = this.player.addNewWeapon(config, weaponPrize);
            }
        }
    }

    public void update() {
        updatePlayer();

        updateGameLevel();

        updateZombies();

        updateProjectiles();

        checkCollisions();

        if (this.waveSuspendTick!=null) this.waveSuspendTick.updateTick();
        if (this.fireRateTick!=null) this.fireRateTick.updateTick();
        
        updateGameInfo();

        repaint();
    }

    private void updatePlayer() {
        if (this.player==null) return;

        this.player.update(getWidth(), getHeight());

        if (this.player.getHealth() <= 0) {
            this.endGame();
        }
    }

    private void updateGameLevel() {
        if (this.gameLevel==null) return;

        if (this.gameLevel.isWaveOver() && waveSuspendTick.isTimeOut()) {
            this.player.addScore(this.gameLevel.calculateLevelBonus());  // Add level bonus into player's score
            waveSuspendTick.reset();
        }

        Zombie zombie = this.gameLevel.update(getWidth(), getHeight());
        if (zombie != null) zombies.add(zombie);
    }

    private void updateZombies() {
        // Move zombies towards player
        for (Zombie z : zombies) {
            z.update(player.getX(), player.getY());


            // ACID zombies have special ranged attack
            if (z.getType() == ZombieType.ACID) {
                AcidZombie acidZombie = (AcidZombie) z;
                Projectile projectile = (Projectile) acidZombie.rangedAttack();
                if (projectile != null)
                this.projectiles.add(projectile);
            }
        }
    }

    private void updateProjectiles() {
        Iterator<Projectile> iterator = this.projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();

            // If projectile is out of bounds, remove it
            if (projectile.isOutOfBounds(getWidth(), getHeight())) {
                iterator.remove();
            } else {
                projectile.move();
            }
        }
    }

    private void checkProjectileCollisions() {
        ListIterator<Projectile> projectileIterator = this.projectiles.listIterator();
        
        while (projectileIterator.hasNext()) {
            Projectile projectile = projectileIterator.next();

            if (projectile.getType() == ProjectileType.BULLET) {
                Bullet bullet = (Bullet) projectile;

                ListIterator<Zombie> zombieIterator = zombies.listIterator();
                while (zombieIterator.hasNext()) {
                    Zombie zombie = zombieIterator.next();                   
                    if (isObjectsCollided(bullet.getBounds(), zombie.getBounds())) {
                        zombie.takeDamage(bullet.getDamage());
                        projectileIterator.remove();  // After damaging zombie, remove it.

                        // If health of zombie is non positive, it is killed 
                        if (zombie.getHealth() <= 0) {
                            killZombie(zombie, zombieIterator);
                        }
                        break;
                    }
                }
            } else if (projectile.getType() == ProjectileType.ARMOR_PIERCING_BULLET) {
                Bullet bullet = (Bullet) projectile;

                ListIterator<Zombie> zombieIterator = zombies.listIterator();
                while (zombieIterator.hasNext()) {
                    Zombie zombie = zombieIterator.next();
                    if (isObjectsCollided(bullet.getBounds(), zombie.getBounds())) {
                        zombie.takeDamage(bullet.getDamage());
                        // Do not remove armor piercing bullets on collision with zombie.

                        // If health of zombie is non positive, it is killed 
                        if (zombie.getHealth() <= 0) {
                            killZombie(zombie, zombieIterator);
                        }
                    }
                }
            } else if (projectile.getType() == ProjectileType.ROCKET) {
                Rocket rocket = (Rocket) projectile;
                
                Boolean isProjectileDetonated = false;
                ListIterator<Zombie> zombieIterator = zombies.listIterator();
                while (zombieIterator.hasNext()) {
                    Zombie zombie = zombieIterator.next();
                    if (isObjectsCollided(rocket.getBounds(), zombie.getBounds())) {
                        // Rocket damages its surrounded area
                        System.out.println("Rocket damages its surrounded area");
                        System.out.println("X:" + rocket.getX() + " Y:" + rocket.getY() + " D:" + rocket.getDamage() + " R:" +  rocket.getEffectiveRange());
                        damageZombies(rocket.getX(), rocket.getY(), rocket.getDamage(), rocket.getEffectiveRange());
                        isProjectileDetonated = true;
                        break;  // No need to continue since explosion applies for all zombies in range
                    }
                }
                if (isProjectileDetonated) projectileIterator.remove();  // After detonation, remove it.

            } else if (projectile.getType() == ProjectileType.SHOTGUN_PELLETS) {
                ShotgunPellets pellets = (ShotgunPellets) projectile;

                ListIterator<Zombie> zombieIterator = zombies.listIterator();
                while (zombieIterator.hasNext()) {
                    Zombie zombie = zombieIterator.next();

                    ListIterator<Bullet> pelletIterator = pellets.getPellets().listIterator();
                    while (pelletIterator.hasNext()) {
                        Bullet bullet = pelletIterator.next();
                        if (isObjectsCollided(bullet.getBounds(), zombie.getBounds())) {
                            zombie.takeDamage(bullet.getDamage());

                            // If health of zombie is non positive, it is killed 
                            if (zombie.getHealth() <= 0) {
                                killZombie(zombie, zombieIterator);
                            }
                            pelletIterator.remove();  // After damaging zombie, remove it.

                        }
                    }
                }
            } else if (projectile.getType() == ProjectileType.ACID_SPIT) {
                AcidSpit acidSpit = (AcidSpit) projectile;

                if (isObjectsCollided(acidSpit.getBounds(), player.getBounds())) {
                    // Damage zombies and player if they are within the effective range of toxic explosion
                    damageZombies(acidSpit.getX(), acidSpit.getY(), acidSpit.getDamage(), acidSpit.getEffectiveRange());
                    damagePlayer(acidSpit.getX(), acidSpit.getY(), acidSpit.getDamage(), acidSpit.getEffectiveRange());
                    projectileIterator.remove();  // After detonation, remove it.
                }

            }
        }
    }

    private void checkCollisions() {
        // Projectile collisions
        checkProjectileCollisions();

        // Zombie collisions to the survivor (aka zombie attack)
        Iterator<Zombie> zombieIterator = zombies.iterator();
        while (zombieIterator.hasNext()) {
            Zombie zombie = zombieIterator.next();
            // If zombie collides with the player, it gives damage by attacking
            boolean isColliding = isObjectsCollided(zombie.getBounds(), player.getBounds());

            if (isColliding) {
                // Normalize damage according to game tick (Full damage is taken by player in 500ms)
                double damagePerTick = (double) zombie.giveDamage() * ((double) Globals.GAME_TICK_MS / Globals.FULL_DAMAGE_PERIOD);
                this.player.takeDamage(damagePerTick);
            }
        }
    }

    private double getDistanceSquared(int x1, int y1, int x2, int y2) {
        int dx = x1 - x2;
        int dy = y1 - y2;
        return dx * dx + dy * dy;
    }

    private void damageZombies(int originX, int originY, int originDamage, int effectiveRange) {
        double effectiveRangeSquared = effectiveRange * effectiveRange;

        ListIterator<Zombie> zombieIterator = zombies.listIterator();
        while (zombieIterator.hasNext()) {
            Zombie zombie = zombieIterator.next();
            topdownshooter.Core.Position position = zombie.getPosition();
            double distanceSquared = getDistanceSquared(position.x(), position.y(), originX, originY);  // Calculate distance from origin point of detonation

            // If calculate detonation damage with the function of the distance from origin
            double damage = 0;
            if (distanceSquared == 0) {
                damage = originDamage;
            } else if (distanceSquared < effectiveRangeSquared && effectiveRange > 0) {
                damage = (double) originDamage * (1 - (Math.sqrt(distanceSquared) / (4 * effectiveRange)));
            } else if (distanceSquared == effectiveRangeSquared) {
                damage = (double) originDamage * 0.25;
            } else { 
                damage = 0;
            }
            System.out.println("Zombie Normalized Damage: " + damage + " Origin Damage: " + originDamage  + " Distance Squared: " + distanceSquared);
            if (damage>0) {
                zombie.takeDamage(damage);
                // If health of zombie is non positive, it is killed 
                if (zombie.getHealth() <= 0) {
                    killZombie(zombie, zombieIterator);
                }    
            }
        }
    }

    private void damagePlayer(int originX, int originY, int originDamage, int effectiveRange) {
        double effectiveRangeSquared = effectiveRange * effectiveRange;

        double distanceSquared = getDistanceSquared(this.player.getX(), this.player.getY(), originX, originY);  // Calculate distance from origin point of detonation

        // If calculate detonation damage with the function of the distance from origin
        double damage = 0;
        if (distanceSquared == 0) {
            damage = originDamage;
        } else if (distanceSquared < effectiveRangeSquared && effectiveRange > 0) {
            damage = (double) originDamage * (1 - (Math.sqrt(distanceSquared) / (4 * effectiveRange)));
        } else if (distanceSquared == effectiveRangeSquared) {
            damage = (double) originDamage * 0.25;
        } else { 
            damage = 0;
        }
        System.out.println("Player Normalized Damage: " + damage + " Origin Damage: " + originDamage  + " Distance Squared: " + distanceSquared);

        if (damage>0) {
            this.player.takeDamage(damage);
        }
    }

    private void killZombie(Zombie zombie, ListIterator<Zombie> zombieIterator) {
        // Killing zombie will give score, and possibility of dropping player items. 
        Map.Entry<Integer, PlayerItem> tuple = zombie.kill();
        int points = tuple.getKey();
        PlayerItem playerItem = tuple.getValue();

        this.player.addScore(points);
        this.player.addPlayerItem(playerItem);
        
        ZombieType zombieType = zombie.getType();
        int zombieX = zombie.getX();
        int zombieY = zombie.getY();
        int zombieDamage = zombie.giveDamage();

        zombieIterator.remove();  // First kill the zombie 
        // Acid zombies damages their surrounds when they killed
        if (zombieType == ZombieType.ACID) {
            damageZombies(zombieX, zombieY, zombieDamage, 200);
            damagePlayer(zombieX, zombieY, zombieDamage, 200);
        }        
    }
    
    private boolean isObjectsCollided(Rectangle rect1, Rectangle rect2) {
        return rect1.getBounds().intersects(rect2.getBounds()) || 
               rect2.getBounds().contains(rect1.getBounds()) || 
               rect1.getBounds().contains(rect2.getBounds());
    }

    private void updateGameInfo() {
        GameInfoPanel gameInfoPanel = this.parentPanel.getGameInfoPanel();
        gameInfoPanel.updatePlayerHealth(this.player==null ? 0 : this.player.getHealth());
        gameInfoPanel.updatePlayerScore(this.player==null ? 0 : this.player.getScore());
        gameInfoPanel.updateGameLevel(this.gameLevel==null ? 0 : this.gameLevel.getLevel());
        gameInfoPanel.updateGameRemainingTime(this.gameLevel==null ? 0 : this.gameLevel.getRemainingTime());
        gameInfoPanel.updateRemainingZombieCount(this.gameLevel==null ? 0 : this.gameLevel.getRemainingZombies());
        gameInfoPanel.updatePlayerInventory(this.player.getInventoryInfo());
    }

    public void pauseGame() {
        this.isGamePaused = true;
        gameTimer.stop();
    }

    public void resumeGame() {
        requestFocus();

        gameTimer.start();
        this.isGamePaused = false;
    }

    private void showGameOverDialog() {
        GameOverPanel gameOverPanel = this.parentPanel.getGameOverPanel();
        gameOverPanel.setPlayerScore(this.player.getScore());
        gameOverPanel.setGameLevel(this.gameLevel.getLevel());
        gameOverPanel.fadeIn();  // Animated pop up 
    }

    private void resetGame() {
        this.projectiles.clear();
        this.zombies.clear();
        this.player = null;

        this.gameLevel = null;
    }

    private void serializeGame(ObjectOutputStream os) throws IOException{
        // Serialize player object
        os.writeObject(this.player);
        
        // Serialize game level
        os.writeObject(this.gameLevel);

        // Serialize zombie objects
        for (Zombie z : this.zombies) {
            os.writeObject(z);
        }
    }

    private void createGameObjects(ObjectInputStream os) throws IOException{
        try {
            do {
                Object object = os.readObject();
                if (object instanceof GameLevel) {
                    this.gameLevel = (GameLevel) object;
                    this.gameLevel.setConfig(this.config);
                } else if (object instanceof Player) {
                    this.player = (Player) object;
                } else if (object instanceof AbstractZombie) {
                    Zombie zombie = (Zombie) object;
                    this.zombies.add(zombie);                   
                } else {
                    System.err.println("Illegal class to be serialized!");
                }
            } while(true);
            

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open File");

        int userSelection = fileChooser.showOpenDialog(null); // Open "Open File" dialog

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();

            // Read content from the selected file
            try {
                FileInputStream loadedFile = new FileInputStream(fileToOpen);
                try {
                    ObjectInputStream os = new ObjectInputStream(loadedFile);
                    
                    resetGame();

                    createGameObjects(os);
                
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
    
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveGame() {
        SwingUtilities.invokeLater(() -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save File"); // Set dialog title

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
            String formattedDatetime = LocalDateTime.now().format(formatter);
            String defaultFilename = formattedDatetime + ".dat";
    
            fileChooser.setSelectedFile(new File(defaultFilename));

            int userSelection = fileChooser.showSaveDialog(null); // Open "Save File" dialog

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile(); // Get selected file

                // Save content to the file
                try {
                    FileOutputStream savedFile = new FileOutputStream(fileToSave);
                    try {
                        ObjectOutputStream os = new ObjectOutputStream(savedFile);

                        serializeGame(os);
                    
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
        
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void startFireTimer() {
        if (this.fireRateTick == null || this.fireRateTick.isTimeOut()) {
            this.fireRateTick = new TimeTick(Globals.Time2GameTick(Globals.GAME_TICK_MS), () -> triggerFireFunction()); // Calls function every 15ms
            this.fireRateTick.setRepeats(0);  // Repeat only once
        }
    }

    public void stopFire() {
        this.fireRateTick = null;
    }

    public void triggerFireFunction() {
        Projectile projectile = player.fire();
        if (projectile != null)
            this.projectiles.add(projectile);
    }

    public void endGame() {
        this.gameTimer.stop();
        if (this.fireRateTick != null) this.fireRateTick.reset();
        if (this.waveSuspendTick != null) this.waveSuspendTick.reset();
        
        this.isGamePaused = true;  // Set to true to prevent player rotation on mouse movement
        
        showGameOverDialog();
    }

    private void openInGameMenu() {
        pauseGame();

        InGameMenuPanel inGameMenuPanel = this.parentPanel.getInGameMenuPanel();
        inGameMenuPanel.fadeIn();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player.decrementDx();
            case KeyEvent.VK_D -> player.incrementDx();
            case KeyEvent.VK_W -> player.decrementDy();
            case KeyEvent.VK_S -> player.incrementDy();
            case KeyEvent.VK_R -> player.getCurrentWeapon().reload();
            case KeyEvent.VK_Q -> player.switchWeapon();
            case KeyEvent.VK_ESCAPE -> openInGameMenu();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) player.setDx(0);
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S) player.setDy(0);
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        startFireTimer();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        stopFire();
    }
    
    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        if (this.isGamePaused) return;  // Do not move player when game is paused

        double rRad = Math.atan2(e.getY() - player.getY(), e.getX() - player.getX());
        player.rotate(rRad);
    }
}
