package topdownshooter.Weapon.Projectiles;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;

public abstract class Projectile implements Serializable{
    protected int x, y;
    protected double r;  // Rotation angle in radians
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

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void move() {
        x += (int) (this.speed * Math.cos(this.r));
        y += (int) (this.speed * Math.sin(this.r));
    }

    abstract public void draw(Graphics g);

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
