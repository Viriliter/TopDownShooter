package topdownshooter.Weapon;

import topdownshooter.Core.Globals;
import topdownshooter.Core.TimeTick;
import topdownshooter.Core.ConfigHandler.WeaponProperties;
import topdownshooter.Weapon.Projectiles.Bullet;

public abstract class AbstractWeapon implements Weapon {
    protected int damage;
    protected int magazineCapacity;
    protected int magazineCount;
    protected int fireRate;
    protected int reloadDuration;
    protected int ammo;
    protected TimeTick reloadTick;
    protected TimeTick fireTick;
    protected WeaponType type;

    public AbstractWeapon() {}

    public AbstractWeapon(WeaponProperties properties) {
        this.damage = properties.damage();
        this.fireRate = properties.fireRate() > 0 ? properties.fireRate(): 1;  // FireRate (1/min) cannot be zero
        this.magazineCapacity = properties.magazineCapacity();
        this.magazineCount = properties.magazineCount();
        this.reloadDuration = properties.reloadDuration();  // In seconds

        this.ammo = this.magazineCapacity;  // Create the weapon fully loaded
        this.fireTick = new TimeTick(Globals.Time2GameTick(60*1000/this.fireRate));
        this.fireTick.setRepeats(-1);  // Repeates indefinetly
        this.reloadTick = new TimeTick(0/*Globals.Time2Tick(1000/this.reloadDuration)*/);
        this.reloadTick.setRepeats(-1);  // Repeates indefinetly
    }

    public AbstractWeapon(int damage, int magazineCapacity, int magazineCount, 
                          int fireRate, int reloadDuration, int ammo, TimeTick reloadTick, 
                          TimeTick fireTick, WeaponType type) {
        this.damage = damage;
        this.magazineCapacity = magazineCapacity;
        this.magazineCount = magazineCount;
        this.fireRate = fireRate;
        this.reloadDuration = reloadDuration;
        this.ammo = ammo;
        this.reloadTick = reloadTick;
        this.reloadTick.setAction(null);
        this.fireTick = fireTick;
        this.fireTick.setAction(null);
        this.type = type;
    }

    @Override
    public Bullet fire(int x, int y, double r) {
        if (this.fireTick.isTimeOut() && this.ammo > 0) {
            fireTick.reset();
            this.ammo--;
            return new Bullet(x, y, r, this.damage);
        }
        return null;
    }

    @Override
    public void reload() {
        if (reloadTick.isTimeOut() && ammo < this.magazineCapacity) {
            reloadTick.reset();
            if (magazineCount == -1) {  // Infinity number of magazine count
                this.ammo = this.magazineCapacity;
            } else if (magazineCount > 0){
                this.magazineCount--;
                this.ammo = this.magazineCapacity;
            } else {
                System.out.println("Out of ammo");
            }
        }
    }

    @Override
    public void update() {
        this.fireTick.updateTick();
        this.reloadTick.updateTick();
    }

    @Override
    public int getAmmo() {
        return this.ammo;
    }
   
    @Override
    public int getMagazineCapacity() {
        return this.magazineCapacity;
    }

    @Override
    public int getMagazineCount() {
        return this.magazineCount;
    }

    @Override
    public int getReloadDuration() {
        return this.reloadDuration;
    }

    @Override
    public int getFireRate() {
        return this.fireRate;
    }

    @Override
    public int getDamage() {
        return this.damage;
    }

    @Override
    public WeaponType getType() {
        return this.type;
    }

    @Override
    abstract public String toString();
}
