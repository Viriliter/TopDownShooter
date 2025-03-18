package topdownshooter.Weapon;

import topdownshooter.Core.ConfigHandler.WeaponProperties;
import topdownshooter.Core.Globals;
import topdownshooter.Core.SoundFX;
import topdownshooter.Weapon.Projectiles.Rocket;

public class RocketLauncher extends AbstractWeapon {
    public RocketLauncher(WeaponProperties properties) {
        super(properties);

        this.type = WeaponType.ROCKETLAUNCHER;
        this.firingSoundFX = new SoundFX(Globals.FIRE_ROCKET_LAUNCHER_SOUND_FX_PATH);
    }

    @Override
    public Rocket fire(int x, int y, double r) {
        if (this.ammo == 0) {
            this.emptyClickSoundFX.play(false);
            return null;
        }

        if (this.fireTick.isTimeOut() && this.ammo > 0) {
            fireTick.reset();
            this.ammo--;
            applySoundFX();
            return new Rocket(x, y, r, this.damage);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RocketLauncher{");
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
