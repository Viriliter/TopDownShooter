package topdownshooter.Weapon;

import java.io.Serializable;

public interface Weapon extends Serializable{
    Bullet fire(int x, int y, double r);

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

