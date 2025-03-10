package topdownshooter.Weapon.Projectiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

public abstract class Projectile implements Serializable{
    protected int x, y;
    protected double r;
    protected int damage = 0;
    protected int size = 5;
    protected int speed = 30;
    protected ProjectileType type = ProjectileType.UNDEFINED;


    public Projectile(int x, int y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.damage = 0;
    }

    public Projectile(int x, int y, double r, int damage) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.damage = damage;
    }

    public Projectile() {

    }
    
    public void move() {
        x += (int) (this.speed * Math.cos(this.r));
        y += (int) (this.speed * Math.sin(this.r));
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
        g2d.fillRect(-this.size / 2, -this.size / 2, this.size, this.size);

        // Restore previous transformation
        g2d.setTransform(oldTransform);

    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public boolean isOutOfBounds(int width, int height) {
        return this.x < 0 || this.x > width || this.y < 0 || this.y > height;
    }

    public int getDamage() {
        return this.damage;
    }

    public ProjectileType getType() {
        return this.type;
    }

    @Override
    abstract public String toString();
}
