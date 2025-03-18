package topdownshooter.Weapon.Projectiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import topdownshooter.Core.Globals;
import topdownshooter.Core.TextureFX;
import topdownshooter.Core.TextureFXStruct;

public class Bullet extends Projectile {
    public Bullet(int x, int y, double r, int damage) {
        super(x, y, r, damage);

        this.type = ProjectileType.BULLET;
        this.projectileEffect = new TextureFX(new TextureFXStruct(Globals.BULLET_TEXTURE_PATH, -25, 0, 3));
        this.projectileEffect.setTargetSize(50, 20);
    }

    @Override   
    public void draw(Graphics g) {
        if (this.projectileEffect!=null) this.projectileEffect.draw(g, this.x, this.y, this.r);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);

        // Save current transformation
        AffineTransform oldTransform = g2d.getTransform();

        // Move to bullet's position
        g2d.translate(x, y);
        g2d.rotate(this.r); // Rotate bullet

        // Draw bullet as a small rotated rectangle
        g2d.fillRect(-size / 2, -size / 2, size, size);
        
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
