package topdownshooter.Weapon;

import topdownshooter.Core.ConfigHandler.WeaponProperties;
import topdownshooter.Core.Globals;
import topdownshooter.Core.SoundFX;
import topdownshooter.Core.TimeTick;
import topdownshooter.Weapon.Projectiles.ArmorPiercingBullet;

public class SniperRifle extends AbstractWeapon {
    public SniperRifle(WeaponProperties properties) {
        super(properties);

        this.type = WeaponType.SNIPERRIFLE;
        this.firingSoundFX = new SoundFX(Globals.FIRE_RIFLE_SOUND_FX_PATH);
    }

    public SniperRifle(int damage, int magazineCapacity, int magazineCount, 
                       int fireRate, int reloadDuration, int ammo, TimeTick reloadTick, 
                       TimeTick fireTick, WeaponType type) {
        super(damage, magazineCapacity, magazineCount, fireRate, reloadDuration, ammo, reloadTick, fireTick, type);
        this.firingSoundFX = new SoundFX(Globals.FIRE_RIFLE_SOUND_FX_PATH);
    }

    @Override
    public ArmorPiercingBullet fire(int x, int y, double r) {
        if (this.fireTick.isTimeOut() && this.ammo > 0) {
            fireTick.reset();
            this.ammo--;
            applySoundFX();
            return new ArmorPiercingBullet(x, y, r, this.damage);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SniperRifle{");
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
