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
import java.util.Map;

import topdownshooter.Core.Globals;
import topdownshooter.Core.PlayerItem;
import topdownshooter.Core.ConfigHandler;
import topdownshooter.Core.GameLevel;
import topdownshooter.Core.TimeTick;
import topdownshooter.Player.Player;
import topdownshooter.Weapon.Bullet;
import topdownshooter.Zombie.AbstractZombie;
import topdownshooter.Zombie.Zombie;

public class GameAreaPanel extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener{
    private GamePanel parentPanel = null;
    private Player player = null;
    private ArrayList<Zombie> zombies = null;
    private ArrayList<Bullet> bullets = null;

    private boolean isGamePaused = false;
    private Timer gameTimer;
    private TimeTick fireRateTick = null;
    private TimeTick waveSuspendTick = null;

    private ConfigHandler config;

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
        bullets = new ArrayList<>();

        gameTimer = new Timer(Globals.GAME_TICK_MS, e -> this.update());
        gameTimer.setRepeats(true);

        waveSuspendTick = new TimeTick(Globals.Time2GameTick(Globals.WAVE_SUSPEND_DURATION_MS), () -> this.gameLevel.startWave());
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
        player.draw(g);

        for (Zombie z : zombies) {
            z.draw(g);
        }

        for (Bullet bullet : bullets) {
            if (bullet == null) continue;
            bullet.draw(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    public void setParentPanel(GamePanel panel) {
        this.parentPanel = panel;
    }

    public void update() {
        updatePlayer();

        updateGameLevel();

        updateZombies();

        updateBullets ();

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
            waveSuspendTick.reset();
        }

        Zombie zombie = this.gameLevel.update(getWidth(), getHeight());
        if (zombie != null) zombies.add(zombie);
    }

    private void updateZombies() {
        // Move zombies towards player
        for (Zombie z : zombies) {
            z.update(player.getX(), player.getY());
        }
    }

    private void updateBullets() {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            // If bullet is out of bounds, remove it
            if (bullet.isOutOfBounds(getWidth(), getHeight())) {
                iterator.remove();
            } else {
                bullet.move();
            }
        }
    }

    private void checkCollisions() {
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Iterator<Zombie> zombieIterator = zombies.iterator();
            while (zombieIterator.hasNext()) {
                Zombie zombie = zombieIterator.next();
                if (bullet.getBounds().intersects(zombie.getBounds())) {
                    zombie.takeDamage(bullet.getDamage());
                    bulletIterator.remove();  // After damaging zombie, remove it.
                    // If health of zombie is non positive, it is killed 
                    if (zombie.getHealth() <= 0) {
                        // Killing zombie will give score, and possibility of dropping player items. 
                        Map.Entry<Integer, PlayerItem> tuple = zombie.kill();
                        int points = tuple.getKey();
                        PlayerItem playerItem = tuple.getValue();

                        this.player.addScore(points);
                        this.player.addPlayerItem(playerItem);

                        zombieIterator.remove();
                    }
                    break;
                }
            }
        }

        Iterator<Zombie> zombieIterator = zombies.iterator();
        while (zombieIterator.hasNext()) {
            Zombie zombie = zombieIterator.next();
            // If zombie attacked the player it gives damage
            boolean isColliding = zombie.getBounds().intersects(player.getBounds()) || 
                                  player.getBounds().contains(zombie.getBounds()) || 
                                  zombie.getBounds().contains(player.getBounds());

            if (isColliding) {
                // Normalize damage according to game tick (Full damage is taken by player in 500ms)
                double damagePerTick = (double) zombie.giveDamage() * ((double) Globals.GAME_TICK_MS / Globals.FULL_DAMAGE_PERIOD);
                this.player.takeDamage(damagePerTick);
            }
        }
    }

    private void updateGameInfo() {
        GameInfoPanel gameInfoPanel = this.parentPanel.getGameInfoPanel();
        gameInfoPanel.updatePlayerHealth(this.player==null ? 0 : this.player.getHealth());
        gameInfoPanel.updatePlayerScore(this.player==null ? 0 : this.player.getScore());
        gameInfoPanel.updateGameLevel(this.gameLevel==null ? 0 : this.gameLevel.getLevel());
        gameInfoPanel.updateGameRemainingTime(this.gameLevel==null ? 0 : this.gameLevel.getRemainingTime());
        gameInfoPanel.updateRemainingZombieCount(this.gameLevel==null ? 0 : this.gameLevel.getRemainingZombies());
        //gameInfoPanel.updatePlayerInventory(this.player.getInventory());
        
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
        this.bullets.clear();
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
        Bullet bullet = player.fire();
        if (bullet != null)
            bullets.add(bullet);
    }

    public void endGame() {
        this.gameTimer.stop();
        if (this.fireRateTick != null) this.fireRateTick.reset();
        if (this.waveSuspendTick != null) this.waveSuspendTick.reset();

        System.out.println("GAME OVER");
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
