package topdownshooter.Weapon.Projectiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import topdownshooter.Core.Globals;
import topdownshooter.Core.TextureFX;
import topdownshooter.Core.TextureFXStruct;

public class AcidSpit extends Projectile {
    private static final int EFFECTIVE_RANGE = 50;
    protected int speed = 5;
    protected int size = 15;

    public AcidSpit (int x, int y, double r, int damage) {
        super(x, y, r, damage);
        this.type = ProjectileType.ACID_SPIT;

        this.projectileEffect = new TextureFX(new TextureFXStruct(Globals.ACID_SPIT_TEXTURE_PATH, -25, 0, 3));
        this.projectileEffect.setTargetSize(size, size);
    }

    public int getEffectiveRange() {
        return AcidSpit.EFFECTIVE_RANGE;
    }

    @Override   
    public void draw(Graphics g) {
        this.projectileEffect.draw(g, x, y, r);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AcidSpit{");
        sb.append("x=" + this.x + ", ");
        sb.append("y=" + this.y + ", ");
        sb.append("r=" + this.r + ", ");
        sb.append("damage=" + this.damage + ", ");
        sb.append("}");

        return sb.toString();
    }
}
