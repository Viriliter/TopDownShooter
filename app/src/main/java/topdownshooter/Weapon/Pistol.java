package topdownshooter.Weapon;

import topdownshooter.Core.ConfigHandler.WeaponProperties;
import topdownshooter.Core.TimeTick;

public class Pistol extends AbstractWeapon {
    public Pistol(WeaponProperties properties) {
        super(properties);
    }

    public Pistol(int damage, int magazineCapacity, int magazineCount, 
                       int fireRate, int reloadDuration, int ammo, TimeTick reloadTick, 
                       TimeTick fireTick, WeaponType type) {
        super(damage, magazineCapacity, magazineCount, fireRate, reloadDuration, ammo, reloadTick, fireTick, type);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pistol{");
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