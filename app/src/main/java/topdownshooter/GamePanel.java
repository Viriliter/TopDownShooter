package topdownshooter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GamePanel  extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
    private Player player;
    private ArrayList<Zombie> zombies;
    private ArrayList<Bullet> bullets;
    private Timer timer;
    private Random random;

    public GamePanel(JFrame frame) {
        setFocusable(true);
        requestFocusInWindow();
        setBackground(Color.BLACK);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        player = new Player(400, 400);
        zombies = new ArrayList<>();
        bullets = new ArrayList<>();
        random = new Random();

        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        player.draw(g);

        for (Zombie z : zombies) {
            z.draw(g);
        }

        for (Bullet b : bullets) {
            b.draw(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.move();

        spawnZombie();

        for (Zombie z : zombies) {
            z.moveTowards(player.getX(), player.getY());
        }

        for (Bullet b : bullets) {
            b.move();
        }

        checkCollisions();

        repaint();
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
        System.out.println("Key pressed: " + e.getKeyCode());
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player.setDx(-5);
            case KeyEvent.VK_D -> player.setDx(5);
            case KeyEvent.VK_W -> player.setDy(-5);
            case KeyEvent.VK_S -> player.setDy(5);
            case KeyEvent.VK_R -> player.getCurrentWeapon().reload();
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
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {
        bullets.add(new Bullet(player.getX(), player.getY(), player.getR()));
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
