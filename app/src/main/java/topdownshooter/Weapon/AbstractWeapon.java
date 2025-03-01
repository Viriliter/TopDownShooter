package topdownshooter.Weapon;

import topdownshooter.Core.Globals;
import topdownshooter.Core.ConfigHandler.WeaponProperties;

public abstract class AbstractWeapon implements Weapon {
    protected int damage;
    protected int fireRateTick;
    protected int magazineCapacity;
    protected int magazineCount;
    protected int reloadTimeTick;
    protected int ammo;
    protected int reloadTimer;
    protected int fireTimer;
    long initTime = 0;


    public AbstractWeapon(WeaponProperties properties) {
        this.damage = properties.damage();
        this.fireRateTick = (1000 * 60) / (Globals.GAME_TICK_MS * properties.fireRate());  // Need to convert from 1/min to game ticks
        this.magazineCapacity = properties.magazineCapacity();
        this.magazineCount = properties.magazineCount();
        this.reloadTimeTick = 1; //(1000) / (Globals.GAME_TICK_MS * properties.reloadTime());  // Need to convert from seconds to game ticks

        this.ammo = this.magazineCapacity;  // Create the weapon fully loaded
        this.reloadTimer = 0;
        this.fireTimer = 0;
    }

    @Override
    public Bullet fire(int x, int y, double r) {
        if (this.fireTimer <= 0 && this.ammo > 0) {
            this.ammo--;
            this.fireTimer = this.fireRateTick;
            return new Bullet(x, y, r);
        }
        return null;
    }

    @Override
    public void reload() {
        if (reloadTimer <= 0 && ammo < this.magazineCapacity) {
            if (magazineCount == -1) {  // Infinity number of magazine count
                this.ammo = this.magazineCapacity;
                this.reloadTimer = this.reloadTimeTick;
            } else if (magazineCount > 0){
                this.magazineCount--;
                this.ammo = this.magazineCapacity;
                this.reloadTimer = this.reloadTimeTick;
            } else {
                System.out.println("Out of ammo");
            }
        }
    }

    @Override
    public void update() {
        if (this.fireTimer == this.fireRateTick) {
            initTime = (long) System.currentTimeMillis();
        }
        if (this.fireTimer == 0 && initTime != 0) {
            System.out.println("Fire rate: " + (System.currentTimeMillis() - this.initTime));
            initTime = 0;
        }
        
        if (this.fireTimer > 0) this.fireTimer--;
        if (this.reloadTimer > 0) this.reloadTimer--;
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
    public int getReloadTimer() {
        return this.reloadTimer;
    }

    @Override
    public int getFireTimer() {
        return this.fireTimer;
    }

    @Override
    public int getDamage() {
        return this.damage;
    }
}
