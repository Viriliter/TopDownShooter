package topdownshooter.Zombie;

import java.awt.*;

public class Zombie {
    private int x, y;
    private double r; // Rotation angle in radians
    private final int SIZE = 30;
    private int speed;

    public Zombie(int x, int y) {
        this.x = x;
        this.y = y;
        this.r = 0;
        this.speed = 1;
    }

    public void update(int px, int py) {
        if (x < px) x += 1;
        if (x > px) x -= 1;
        if (y < py) y += 1;
        if (y > py) y -= 1;
    
        int dx = px - this.x;
        int dy = py - this.y;

        this.r = Math.atan2(dy, dx); // Radians (used for rotation)
        /*
            double length = Math.sqrt(dx * dx + dy * dy);
            if (length != 0) {
                this.x += (int) (speed * (dx / length));
                this.y += (int) (speed * (dy / length));
            }
        */
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g; // Enable rotation
        g2d.setColor(Color.GREEN);

        // Apply rotation around the zombie's center
        g2d.translate(x, y);
        g2d.rotate(r);  // Rotate to face the player

        // Draw rotated rectangle as zombie
        g2d.fillRect(-SIZE / 2, -SIZE / 2, SIZE, SIZE);

        // Reset transformation
        g2d.rotate(-r);
        g2d.translate(-x, -y);

        // Outline for better visibility
        //g2d.setColor(Color.BLACK);
        //g2d.drawRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
    }

    public Rectangle getBounds() {
        return new Rectangle(x-SIZE/2, y-SIZE/2, SIZE, SIZE);
    }
}
