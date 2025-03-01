package topdownshooter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import topdownshooter.Core.Globals;
import topdownshooter.Core.ConfigHandler;

import topdownshooter.Player.Player;
import topdownshooter.Weapon.Bullet;
import topdownshooter.Zombie.Zombie;

public class GamePanel  extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
    private Player player;
    private ArrayList<Zombie> zombies;
    private ArrayList<Bullet> bullets;

    private Timer gameTimer;
    private Timer fireTimer;

    private Random random;
    private ConfigHandler config;

    public GamePanel(JFrame frame, ConfigHandler config) {
        setFocusable(true);
        requestFocusInWindow();
        setBackground(Color.BLACK);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        this.config = config;

        player = new Player(config);
        zombies = new ArrayList<>();
        bullets = new ArrayList<>();
        random = new Random();

        gameTimer = new Timer(Globals.GAME_TICK_MS, this);
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
    public void actionPerformed(ActionEvent e) {
        player.update();

        updateZombies();

        updateBullets ();

        checkCollisions();

        repaint();
    }

    private void updateZombies() {
        if (zombies.size() < 1) spawnZombie();

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

    private void spawnZombie() {
        if (random.nextInt(100) < 2) {
            int spawnEdge = random.nextInt(4); // 0 = top, 1 = bottom, 2 = left, 3 = right
            int x = 0, y = 0;
        
            switch (spawnEdge) {
                case 0: // Top edge
                    x = random.nextInt(getWidth()); 
                    y = 0;
                    break;
                case 1: // Bottom edge
                    x = random.nextInt(getWidth());
                    y = getHeight();
                    break;
                case 2: // Left edge
                    x = 0;
                    y = random.nextInt(getHeight());
                    break;
                case 3: // Right edge
                    x = getWidth();
                    y = random.nextInt(getHeight());
                    break;
            }
        
            zombies.add(new Zombie(x, y));
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
                    zombieIterator.remove();
                    break;
                }
            }
        }
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
