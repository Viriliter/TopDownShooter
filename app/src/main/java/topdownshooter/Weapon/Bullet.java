package topdownshooter.Weapon;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

public class Bullet implements Serializable{
    private int x, y;
    private double r;
    private int damage;
    private final int SIZE = 5;
    private final int SPEED = 10;

    public Bullet(int x, int y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.damage = 0;
    }

    public Bullet(int x, int y, double r, int damage) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.damage = damage;
    }

    public void move() {
        x += (int) (SPEED * Math.cos(this.r));
        y += (int) (SPEED * Math.sin(this.r));
    }

    public void draw(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);

        // Save current transformation
        AffineTransform oldTransform = g2d.getTransform();

        // Move to bullet's position
        g2d.translate(x, y);
        g2d.rotate(this.r); // Rotate bullet

        // Draw bullet as a small rotated rectangle
        g2d.fillRect(-SIZE / 2, -SIZE / 2, SIZE, SIZE);

        // Restore previous transformation
        g2d.setTransform(oldTransform);

    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public boolean isOutOfBounds(int width, int height) {
        return this.x < 0 || this.x > width || this.y < 0 || this.y > height;
    }

    public int getDamage() {
        return this.damage;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bullet{");
        sb.append("x=" + this.x + ", ");
        sb.append("y=" + this.y + ", ");
        sb.append("r=" + this.r + ", ");
        sb.append("damage=" + this.damage + ", ");
        sb.append("}");

        return sb.toString();
    }
}
