package topdownshooter.Weapon.Projectiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Bullet extends Projectile {

    public Bullet(int x, int y, double r) {
        super(x, y, r, 0);

        this.type = ProjectileType.BULLET;
    }

    public Bullet(int x, int y, double r, int damage) {
        super(x, y, r, damage);

        this.type = ProjectileType.BULLET;
    }

    @Override   
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
