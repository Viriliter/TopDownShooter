package topdownshooter.Weapon.Projectiles;

import java.awt.Graphics;

import topdownshooter.Core.Globals;
import topdownshooter.Core.TextureFX;
import topdownshooter.Core.TextureFXStruct;

public class ArmorPiercingBullet extends Projectile {
    public ArmorPiercingBullet(int x, int y, double r) {
        super(x, y, r, 0);

        this.type = ProjectileType.ARMOR_PIERCING_BULLET;
        this.projectileEffect = new TextureFX(new TextureFXStruct(Globals.BULLET_TEXTURE_PATH, -25, 0, 3));
        this.projectileEffect.setTargetSize(50, 20);
    }

    public ArmorPiercingBullet(int x, int y, double r, int damage) {
        super(x, y, r, damage);

        this.type = ProjectileType.ARMOR_PIERCING_BULLET;
        this.projectileEffect = new TextureFX(new TextureFXStruct(Globals.BULLET_TEXTURE_PATH, -25, 0, 3));
        this.projectileEffect.setTargetSize(50, 20);
    }

    @Override   
    public void draw(Graphics g) {
        if (this.projectileEffect!=null) this.projectileEffect.draw(g, this.x, this.y, this.r);        
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ArmorPiercingBullet{");
        sb.append("x=" + this.x + ", ");
        sb.append("y=" + this.y + ", ");
        sb.append("r=" + this.r + ", ");
        sb.append("damage=" + this.damage + ", ");
        sb.append("}");

        return sb.toString();
    }
}
