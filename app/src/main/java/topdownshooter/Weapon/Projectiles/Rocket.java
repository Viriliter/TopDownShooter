package topdownshooter.Weapon.Projectiles;

import java.awt.Graphics;

import topdownshooter.Core.Globals;
import topdownshooter.Core.TextureFX;
import topdownshooter.Core.TextureFXStruct;

public class Rocket extends Projectile {
    private static final int EFFECTIVE_RANGE = 200;
    public static final int size = 15;

    public Rocket(int x, int y, double r, int damage) {
        super(x, y, r, damage);

        this.type = ProjectileType.ROCKET;
        this.projectileEffect = new TextureFX(new TextureFXStruct(Globals.BULLET_TEXTURE_PATH, -25, 0, 3));
        this.projectileEffect.setTargetSize(50, 20);
    }

    public int getEffectiveRange() {
        return Rocket.EFFECTIVE_RANGE;
    }

    @Override   
    public void draw(Graphics g) {
        if (this.projectileEffect!=null) this.projectileEffect.draw(g, this.x, this.y, this.r);        
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Rocket{");
        sb.append("x=" + this.x + ", ");
        sb.append("y=" + this.y + ", ");
        sb.append("r=" + this.r + ", ");
        sb.append("damage=" + this.damage + ", ");
        sb.append("}");

        return sb.toString();
    }
}
