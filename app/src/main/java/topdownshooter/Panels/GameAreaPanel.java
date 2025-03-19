/*
 * @file GameAreaPanel.java
 * @brief This file defines the ${fileNameNoExt} class.
 *
 * Created on Wed Mar 19 2025
 *
 * @copyright MIT License
 *
 * Copyright (c) 2025 Mert LIMONCUOGLU
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package topdownshooter.Panels;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.EOFException;
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

import topdownshooter.Core.Globals;
import topdownshooter.Core.SoundFX;
import topdownshooter.Core.TileGenerator;
import topdownshooter.Core.ConfigHandler;
import topdownshooter.Core.GameLevel;
import topdownshooter.Core.TimeTick;
import topdownshooter.Player.Loot;
import topdownshooter.Player.Player;
import topdownshooter.Weapon.WeaponType;
import topdownshooter.Weapon.Projectiles.AcidSpit;
import topdownshooter.Weapon.Projectiles.ArmorPiercingBullet;
import topdownshooter.Weapon.Projectiles.Bullet;
import topdownshooter.Weapon.Projectiles.Projectile;
import topdownshooter.Weapon.Projectiles.ProjectileType;
import topdownshooter.Weapon.Projectiles.Rocket;
import topdownshooter.Weapon.Projectiles.ShotgunPellets;
import topdownshooter.Zombie.AcidZombie;
import topdownshooter.Zombie.Zombie;
import topdownshooter.Zombie.ZombieType;

public class GameAreaPanel extends JPanel implements ActionListener, KeyListener{
    private GamePanel parentPanel = null;
    private ConfigHandler config;

    private GameLevel gameLevel = null;
    private Player player = null;
    private ArrayList<Zombie> zombies = null;
    private ArrayList<Projectile> projectiles = null;
    private ArrayList<Loot> loots = null;

    private static boolean isGamePaused = false;
    private Timer gameTimer;
    private TimeTick fireRateTick = null;

    private TileGenerator playgroundTileGenerator = null;

    private SoundFX backgroundSoundFX = null;

    public GameAreaPanel(ConfigHandler config) {
        setFocusable(true);
        requestFocusInWindow();
        setBackground(Color.BLACK);
        addKeyListener(this);

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (GameAreaPanel.isGamePaused) return;  // Do not move player when game is paused

                double rRad = Math.atan2(e.getY() - player.getY(), e.getX() - player.getX());
                player.rotate(rRad);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (GameAreaPanel.isGamePaused) return;  // Do not move player when game is paused

                double rRad = Math.atan2(e.getY() - player.getY(), e.getX() - player.getX());
                player.rotate(rRad);
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startFireTimer();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                stopFire();
            }
        });
        
        this.config = config;

        // Create game objects
        gameLevel = new GameLevel(this.config);

        player = new Player(this.config);
        zombies = new ArrayList<>();
        projectiles = new ArrayList<>();
        loots = new ArrayList<>();

        gameTimer = new Timer(Globals.GAME_TICK_MS, e -> this.update());
        gameTimer.setRepeats(true);

        this.playgroundTileGenerator = new TileGenerator(Globals.PLAYGROUND_TILE_PATH);

        this.backgroundSoundFX = new SoundFX(Globals.BACKGROUND_SOUND_FX_PATH);
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
        this.player.draw(g);

        for (Zombie z : this.zombies) {
            z.draw(g);
        }

        for (Projectile projectile : this.projectiles) {
            if (projectile == null) continue;
            projectile.draw(g);
        }

        for (Loot loot : this.loots) {
            if (loot == null) continue;
            loot.draw(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    public void setParentPanel(GamePanel panel) {
        this.parentPanel = panel;
    }

    private void startWave() {
        if (this.gameLevel.getWaveStatus()== GameLevel.GameLevelStatus.UNDEFINED ||
            this.gameLevel.getWaveStatus()== GameLevel.GameLevelStatus.ENDED)  {

            this.backgroundSoundFX.stop();

            this.player.addScore(this.gameLevel.calculateLevelBonus());  // Add level bonus into player's score

            WeaponType weaponPrize = this.gameLevel.startWave();                

            if (weaponPrize!=null && weaponPrize!=WeaponType.UNDEFINED) {
                this.player.addNewWeapon(config, weaponPrize);
            }
            
            String message = "Wave " + this.gameLevel.getLevel() + " Approaching!";
            this.parentPanel.getNotificationPanel().show(message, Globals.WAVE_SUSPEND_DURATION_MS);
            this.backgroundSoundFX.delayedPlay(true, Globals.WAVE_SUSPEND_DURATION_MS);
        }
    }

    public void update() {
        updatePlayer();

        updateGameLevel();

        updateZombies();

        updateProjectiles();

        updateLoots();

        checkCollisions();

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
        if (this.gameLevel==null || this.player == null || this.zombies == null) return;

        if (this.zombies.size() == 0) {
            startWave();
        }

        Zombie zombie = this.gameLevel.update(getWidth(), getHeight());
        if (zombie != null) zombies.add(zombie);
    }

    private void updateZombies() {
        if (this.player==null ||this.zombies==null) return;

        // Move zombies towards player
        for (Zombie z : zombies) {
            z.update(player.getBounds());

            // Acid zombies have special ranged attack
            if (z.getType() == ZombieType.ACID) {
                AcidZombie acidZombie = (AcidZombie) z;
                Projectile projectile = (Projectile) acidZombie.rangedAttack();
                if (projectile != null) this.projectiles.add(projectile);
            }
        }
    }

    private void updateProjectiles() {
        if (this.projectiles==null) return;

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

    private void updateLoots() {
        if (this.player==null || this.loots==null) return;

        for (Loot loot : this.loots) {
            loot.update();
        }

        // Remove old loots
        ListIterator<Loot> lootIterator = this.loots.listIterator();
        while (lootIterator.hasNext()) {
            Loot loot = lootIterator.next();
            if (Globals.GameTick2Time(loot.getLootAge()) > Globals.MAX_LOOT_DURATION) {
                lootIterator.remove();
            }
        }
    }

    private void checkProjectileCollisions() {
        if (this.player==null ||this.zombies==null || this.projectiles==null) return;

        ListIterator<Projectile> projectileIterator = this.projectiles.listIterator();
        
        while (projectileIterator.hasNext()) {
            Projectile projectile = projectileIterator.next();

            if (projectile.getType() == ProjectileType.BULLET) {
                Bullet bullet = (Bullet) projectile;

                ListIterator<Zombie> zombieIterator = this.zombies.listIterator();
                while (zombieIterator.hasNext()) {
                    Zombie zombie = zombieIterator.next();                   
                    if (Globals.isObjectsCollided(bullet.getBounds(), zombie.getBounds())) {
                        zombie.takeDamage(bullet.getDamage());
                        projectileIterator.remove();  // After damaging zombie, remove it.
                        break;
                    }
                }
            } else if (projectile.getType() == ProjectileType.ARMOR_PIERCING_BULLET) {
                ArmorPiercingBullet bullet = (ArmorPiercingBullet) projectile;

                ListIterator<Zombie> zombieIterator = this.zombies.listIterator();
                while (zombieIterator.hasNext()) {
                    Zombie zombie = zombieIterator.next();
                    if (Globals.isObjectsCollided(bullet.getBounds(), zombie.getBounds())) {
                        zombie.takeDamage(bullet.getDamage());
                        // Do not remove armor piercing bullets on collision with zombie.
                    }
                }
            } else if (projectile.getType() == ProjectileType.ROCKET) {
                Rocket rocket = (Rocket) projectile;
                
                Boolean isProjectileDetonated = false;
                ListIterator<Zombie> zombieIterator = this.zombies.listIterator();
                while (zombieIterator.hasNext()) {
                    Zombie zombie = zombieIterator.next();
                    if (Globals.isObjectsCollided(rocket.getBounds(), zombie.getBounds())) {
                        // Rocket damages its surrounded area
                        damageZombies(rocket.getX(), rocket.getY(), rocket.getDamage(), rocket.getEffectiveRange());
                        isProjectileDetonated = true;
                        break;  // No need to continue since explosion applies for all zombies in range
                    }
                }
                if (isProjectileDetonated) projectileIterator.remove();  // After detonation, remove it.

            } else if (projectile.getType() == ProjectileType.SHOTGUN_PELLETS) {
                ShotgunPellets pellets = (ShotgunPellets) projectile;
                
                // If all pellets are destroyed remove the projectile
                if (pellets.getPellets().size() == 0) {
                    projectileIterator.remove();
                    continue;
                }

                ListIterator<Zombie> zombieIterator = this.zombies.listIterator();
                while (zombieIterator.hasNext()) {
                    Zombie zombie = zombieIterator.next();

                    ListIterator<Bullet> pelletIterator = pellets.getPellets().listIterator();
                    while (pelletIterator.hasNext()) {
                        Bullet bullet = pelletIterator.next();
                        if (Globals.isObjectsCollided(bullet.getBounds(), zombie.getBounds())) {
                            zombie.takeDamage(bullet.getDamage());
                            pelletIterator.remove();  // After damaging zombie, remove it.
                            break;
                        }
                    }
                }
            } else if (projectile.getType() == ProjectileType.ACID_SPIT) {
                AcidSpit acidSpit = (AcidSpit) projectile;

                if (Globals.isObjectsCollided(acidSpit.getBounds(), this.player.getBounds())) {
                    this.player.takeDamage(acidSpit.getDamage());
                    projectileIterator.remove();  // After contact with player, remove it.
                }

            }
        }
    
        ListIterator<Zombie> zombieIterator = this.zombies.listIterator();
        while (zombieIterator.hasNext()) {
            Zombie zombie = zombieIterator.next();

            // If health of zombie is non positive, it is killed
            if (zombie.getHealth() < 0) {              
                ZombieType zombieType = zombie.getType();
                int zombieX = zombie.getX();
                int zombieY = zombie.getY();
                int zombieDamage = zombie.giveDamage();

                // Retrieve loot and score when zombie killed.
                Loot loot = zombie.kill(this.player.getAvailableWeapons());  // To generate loot, available weapon types are needed.
                
                if (loot != null) {
                    this.player.addScore(loot.getScore());
                    // If loot does not have an item, then do not add to the list
                    if (loot.getItem() != null) this.loots.add(loot);
                }

                zombieIterator.remove();

                // Acid zombies damages their surrounds when they killed
                if (zombieType == ZombieType.ACID) {
                    damageZombies(zombieX, zombieY, zombieDamage, 200);
                    damagePlayer(zombieX, zombieY, zombieDamage, 200);
                }
            }
        }
    }

    private void checkZombieCollisions() {
        if (this.player==null ||this.zombies==null) return;

        Iterator<Zombie> zombieIterator = this.zombies.iterator();
        while (zombieIterator.hasNext()) {
            Zombie zombie = zombieIterator.next();
            // If zombie collides with the player, it gives damage by attacking
            boolean isColliding = Globals.isObjectsCollided(zombie.getBounds(), player.getBounds());

            if (isColliding) {
                // Normalize damage according to game tick (Full damage is taken by player in 500ms)
                double damagePerTick = (double) zombie.giveDamage() * ((double) Globals.GAME_TICK_MS / Globals.FULL_DAMAGE_PERIOD);
                this.player.takeDamage(damagePerTick);
            }
        }
    }

    private void checkLootCollisions() {
        ListIterator<Loot> lootIterator = this.loots.listIterator();

        while (lootIterator.hasNext()) {
            Loot loot = lootIterator.next();
            if (Globals.isObjectsCollided(loot.getBounds(), this.player.getBounds())) {
                this.player.addLoot(loot);
                lootIterator.remove();  // Remove the loot after player takes it
            }
        }
    }

    private void checkCollisions() {
        // Projectile collisions
        checkProjectileCollisions();

        // Zombie collisions to the survivor (aka zombie attack)
        checkZombieCollisions();

        // Player collision to loot objects
        checkLootCollisions();
    }

    private double getDistanceSquared(int x1, int y1, int x2, int y2) {
        int dx = x1 - x2;
        int dy = y1 - y2;
        return dx * dx + dy * dy;
    }
    
    private double calculateDamage(int originDamage, double distanceSquared, double effectiveRangeSquared, int effectiveRange) {
        if (distanceSquared == 0) {
            return originDamage;
        } else if (distanceSquared < effectiveRangeSquared && effectiveRange > 0) {
            return (double) originDamage * (1 - (Math.sqrt(distanceSquared) / (4 * effectiveRange)));
        } else if (distanceSquared == effectiveRangeSquared) {
            return (double) originDamage * 0.25;
        }
        return 0;
    }

    private void damageZombies(int originX, int originY, int originDamage, int effectiveRange) {
        double effectiveRangeSquared = effectiveRange * effectiveRange;

        ListIterator<Zombie> zombieIterator = zombies.listIterator();
        while (zombieIterator.hasNext()) {
            Zombie zombie = zombieIterator.next();
            topdownshooter.Core.Position position = zombie.getPosition();
            double distanceSquared = getDistanceSquared(position.x(), position.y(), originX, originY);  // Calculate distance from origin point of detonation

            // Calculate detonation damage with the function of the distance from origin
            double damage = calculateDamage(originDamage, distanceSquared, effectiveRangeSquared, effectiveRange);

            if (damage>0) {
                zombie.takeDamage(damage);
            }
        }
    }

    private void damagePlayer(int originX, int originY, int originDamage, int effectiveRange) {
        double effectiveRangeSquared = effectiveRange * effectiveRange;

        double distanceSquared = getDistanceSquared(this.player.getX(), this.player.getY(), originX, originY);  // Calculate distance from origin point of detonation

        // Calculate detonation damage with the function of the distance from origin
        double damage = calculateDamage(originDamage, distanceSquared, effectiveRangeSquared, effectiveRange);

        if (damage>0) {
            this.player.takeDamage(damage);
        }
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

    public void startGame() {
        GameAreaPanel.isGamePaused = false;
        gameTimer.start();
    }

    public void pauseGame() {
        GameAreaPanel.isGamePaused = true;
        gameTimer.stop();
        this.backgroundSoundFX.stop();
    }

    public void resumeGame() {
        requestFocus();

        gameTimer.start();
        GameAreaPanel.isGamePaused = false;
        this.backgroundSoundFX.play(true);
    }

    private void showGameOverDialog() {
        GameOverPanel gameOverPanel = this.parentPanel.getGameOverPanel();
        gameOverPanel.setPlayerScore(this.player.getScore());
        gameOverPanel.setGameLevel(this.gameLevel.getLevel());
        gameOverPanel.fadeIn();  // Animated pop up 
    }

    private void resetGame() {
        this.loots.clear();
        this.projectiles.clear();
        this.zombies.clear();

        this.player = null;
        this.gameLevel = null;
    }

    private void serializeGameObjects(ObjectOutputStream os) throws IOException{
        // Serialize player object
        os.writeObject(this.player);
        
        // Serialize game level
        os.writeObject(this.gameLevel);

        // Serialize zombie objects
        os.writeObject(this.zombies);

        // Serialize projectiles
        os.writeObject(this.projectiles);

        // Serialize loots
        os.writeObject(this.loots);
    }

    @SuppressWarnings("unchecked")
    private void createGameObjects(ObjectInputStream os) throws IOException, ClassNotFoundException{
        try {
            this.player = (Player) os.readObject();
        
            this.gameLevel = (GameLevel) os.readObject();
            this.gameLevel.setConfig(this.config);
        
            this.zombies = (ArrayList<Zombie>) os.readObject();
            
            this.projectiles = (ArrayList<Projectile>) os.readObject();
            
            this.loots = (ArrayList<Loot>) os.readObject();
            
            /*
            do {
                Object object = os.readObject();
                if (object instanceof GameLevel) {
                    this.gameLevel = (GameLevel) object;
                    this.gameLevel.setConfig(this.config);
                } else if (object instanceof Player) {
                    this.player = (Player) object;
                } else if (object instanceof ArrayList<?>) {
                    ArrayList<?> list = (ArrayList<?>) object;
                    // Get first element of the list to deduce its type 
                    if (!list.isEmpty()) {
                        Object firstElement = list.get(0);
                        
                        if (firstElement instanceof Zombie) {
                            this.zombies = (ArrayList<Zombie>) list;
                        } else if (firstElement instanceof Projectile) {
                            this.projectiles = (ArrayList<Projectile>) list;
                        } else if (firstElement instanceof Loot) {
                            this.loots = (ArrayList<Loot>) list;
                        } else {
                            System.err.println("Array list is illegal to be serialized:" + firstElement.getClass().getName());
                        }
                    } else {
                        System.err.println("Trying to serialize empty array list");
                    }
                } else {
                    System.err.println("Illegal class to be serialized:" + object.getClass().getName());
                }
            } while(true);
            */
        } catch (EOFException e) {
            // Reached to end of file
        }
    }

    public void loadGame(FileInputStream inputStream) throws IOException, ClassNotFoundException{
        if (inputStream==null) return;

        ObjectInputStream os = new ObjectInputStream(inputStream);
        
        resetGame();

        createGameObjects(os);
    
        os.close();
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

                        serializeGameObjects(os);
                    
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
        if (this.gameTimer!=null) this.gameTimer.stop();
        if (this.fireRateTick != null) this.fireRateTick.reset();
        
        GameAreaPanel.isGamePaused = true;  // Set to true to prevent player rotation on mouse movement

        if (this.backgroundSoundFX!=null) this.backgroundSoundFX.stop();

        showGameOverDialog();
    }

    public void exit() {
        if (this.gameTimer!=null) this.gameTimer.stop();
        if (this.fireRateTick != null) this.fireRateTick.reset();
        
        GameAreaPanel.isGamePaused = true;  // Set to true to prevent player rotation on mouse movement
        if (this.backgroundSoundFX!=null) this.backgroundSoundFX.stop();
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
}
