package topdownshooter.Weapon;

import java.io.Serializable;

import topdownshooter.Weapon.Projectiles.Projectile;

public interface Weapon extends Serializable{
    Projectile fire(int x, int y, double r);

    void reload();

    void update();

    int getAmmo();

    int getMagazineCapacity();

    int getMagazineCount();

    int getReloadDuration();

    int getFireRate();

    int getDamage();

    WeaponType getType();
}

