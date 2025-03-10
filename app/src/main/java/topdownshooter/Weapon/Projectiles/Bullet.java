package topdownshooter.Weapon.Projectiles;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

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
