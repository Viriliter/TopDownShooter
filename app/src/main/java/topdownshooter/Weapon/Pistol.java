package topdownshooter.Weapon;

import topdownshooter.Core.ConfigHandler.WeaponProperties;
import topdownshooter.Core.Globals;
import topdownshooter.Core.SoundFX;
import topdownshooter.Core.SpriteAnimation;
import topdownshooter.Core.TimeTick;
import topdownshooter.Weapon.Projectiles.Bullet;

public class Pistol extends AbstractWeapon {
    public Pistol(WeaponProperties properties) {
        super(properties);

        this.type = WeaponType.PISTOL;
        this.firingSoundFX = new SoundFX(Globals.FIRE_PISTOL_SOUND_FX_PATH);
        
        this.flashAnimation = new SpriteAnimation(Globals.MUZZLE_ANIMATION);
        this.flashAnimation.setTargetSize(20, 20);
        this.flashAnimation.setRotationOffset(Globals.degToRad(-90));
        this.flashAnimation.setRepeat(0);
    }

    public Pistol(int damage, int magazineCapacity, int magazineCount, 
                       int fireRate, int reloadDuration, int ammo, TimeTick reloadTick, 
                       TimeTick fireTick, WeaponType type) {
        super(damage, magazineCapacity, magazineCount, fireRate, reloadDuration, ammo, reloadTick, fireTick, type);
        this.firingSoundFX = new SoundFX(Globals.FIRE_PISTOL_SOUND_FX_PATH);
        
        this.flashAnimation = new SpriteAnimation(Globals.MUZZLE_ANIMATION);
        this.flashAnimation.setTargetSize(20, 20);
        this.flashAnimation.setRotationOffset(Globals.degToRad(-90));
        this.flashAnimation.setRepeat(0);
    }

    @Override
    public Bullet fire(int x, int y, double r) {
        if (this.ammo == 0) {
            this.emptyClickSoundFX.play(false);
            return null;
        }

        if (this.fireTick.isTimeOut() && this.ammo > 0) {
            fireTick.reset();
            this.ammo--;
            applySoundFX();
            this.flashAnimation.setRepeat(1);  // Only repeat animation once
            return new Bullet(x, y, r, this.damage);
        }
        return null;
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