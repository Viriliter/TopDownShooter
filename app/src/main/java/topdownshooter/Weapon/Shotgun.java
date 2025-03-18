package topdownshooter.Weapon;

import topdownshooter.Core.ConfigHandler.WeaponProperties;
import topdownshooter.Core.Globals;
import topdownshooter.Core.SoundFX;
import topdownshooter.Weapon.Projectiles.ShotgunPellets;

public class Shotgun extends AbstractWeapon {
    public Shotgun(WeaponProperties properties) {
        super(properties);

        this.type = WeaponType.SHOTGUN;
        this.firingSoundFX = new SoundFX(Globals.FIRE_SHOTGUN_SOUND_FX_PATH);
    }

    @Override
    public ShotgunPellets fire(int x, int y, double r) {
        if (this.ammo == 0) {
            this.emptyClickSoundFX.play(false);
            return null;
        }

        if (this.fireTick.isTimeOut() && this.ammo > 0) {
            fireTick.reset();
            this.ammo--;
            applySoundFX();
            return new ShotgunPellets(x, y, r, this.damage);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Shotgun{");
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
