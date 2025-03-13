package topdownshooter.Weapon;

import java.util.Random;

import topdownshooter.Core.ConfigHandler.WeaponProperties;
import topdownshooter.Core.Globals;
import topdownshooter.Core.TimeTick;
import topdownshooter.Weapon.Projectiles.Bullet;

public class AssaultRifle extends AbstractWeapon {
    private Random spreadRandom;   
    private final double MAX_SPREAD_ANGLE = 30;  // In degree

    public AssaultRifle(WeaponProperties properties) {
        super(properties);

        this.type = WeaponType.ASSAULTRIFLE;

        this.spreadRandom = new Random();
    }

    public AssaultRifle(int damage, int magazineCapacity, int magazineCount, 
                       int fireRate, int reloadDuration, int ammo, TimeTick reloadTick, 
                       TimeTick fireTick, WeaponType type) {
        super(damage, magazineCapacity, magazineCount, fireRate, reloadDuration, ammo, reloadTick, fireTick, type);

        this.spreadRandom = new Random();
    }

    @Override
    public Bullet fire(int x, int y, double r) {
        if (this.fireTick.isTimeOut() && this.ammo > 0) {
            double spreadAngle = spreadRandom.nextDouble(MAX_SPREAD_ANGLE) - (MAX_SPREAD_ANGLE / 2.0);  // In degree
            fireTick.reset();
            this.ammo--;
            return new Bullet(x, y, r + Globals.degToRad(spreadAngle) , this.damage);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AssaultRifle{");
        sb.append("damage=" + this.damage + ", ");
        sb.append("magazineCapacity=" + this.magazineCapacity + ", ");
        sb.append("magazineCount=" + this.magazineCount + ", ");
        sb.append("fireRate=" + this.fireRate + ", ");
        sb.append("reloadDuration=" + this.reloadDuration + ", ");
        sb.append("ammo=" + this.ammo + ", ");
        sb.append("reloadTick=" + this.reloadTick + ", ");
        sb.append("fireTick=" + this.fireTick + ", ");
        sb.append("type=" + this.type);
        sb.append("}");

        return sb.toString();
    }
}
