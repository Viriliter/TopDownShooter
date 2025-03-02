package topdownshooter.Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import topdownshooter.Core.Globals;
import topdownshooter.Core.PlayerItem;
import topdownshooter.Core.ConfigHandler;
import topdownshooter.Core.GameLevel;
import topdownshooter.Player.Player;
import topdownshooter.Weapon.Bullet;
import topdownshooter.Zombie.Zombie;

public class GameAreaPanel extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener{
    private Player player;
    private ArrayList<Zombie> zombies;
    private ArrayList<Bullet> bullets;

    private Timer gameTimer;
    private Timer fireTimer;
    private Timer waveSuspendTimer;

    private ConfigHandler config;

    private GameLevel gameLevel;

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

        waveSuspendTimer = new Timer(Globals.WAVE_SUSPEND_DURATION_MS, e -> this.gameLevel.startWave());
        waveSuspendTimer.setRepeats(false);

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

    public void update() {
        this.player.update();

        if (this.player.getHealth() <= 0) {
            this.endGame();
        }

        if (this.gameLevel.isWaveOver()) {
            waveSuspendTimer.start();
        }

        Zombie zombie = this.gameLevel.update(getWidth(), getHeight());
        if (zombie != null) zombies.add(zombie);

        updateZombies();

        updateBullets ();

        checkCollisions();

        repaint();
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
                    bulletIterator.remove();
                    zombie.takeDamage(10);
                    // If health of zombie is non positive, it is killed 
                    if (zombie.getHealth() <= 0) {
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
            if (zombie.getBounds().intersects(this.player.getBounds())) {
                this.player.takeDamage(zombie.giveDamage());
            }
        }
    }

    public void startFireTimer() {
        if (this.fireTimer == null || !this.fireTimer.isRunning()) {
            this.fireTimer = new Timer(Globals.GAME_TICK_MS, e -> triggerFireFunction()); // Calls function every 100ms
            this.fireTimer.start();
        }
    }

    public void stopFireTimer() {
        if (this.fireTimer != null) {
            this.fireTimer.stop();
        }
    }

    public void triggerFireFunction() {
        Bullet bullet = player.fire();
        if (bullet != null)
            bullets.add(bullet);
    }

    public void endGame() {
        this.fireTimer.stop();
        this.gameTimer.stop();
        this.waveSuspendTimer.stop();

        System.out.println("GAME OVER");
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
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) player.setDx(0);
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S) player.setDy(0);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("Key typed: " + e.getKeyCode());

    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        startFireTimer();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        stopFireTimer();
    }
    
    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        double rRad = Math.atan2(e.getY() - player.getY(), e.getX() - player.getX());
        player.rotate(rRad);
    }
}
