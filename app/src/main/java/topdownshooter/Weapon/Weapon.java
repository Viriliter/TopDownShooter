package topdownshooter.Weapon;

import java.awt.Graphics;
import java.io.Serializable;

import topdownshooter.Weapon.Projectiles.Projectile;

public interface Weapon extends Serializable{
    Projectile fire(int x, int y, double r);

    void reload();

    void update();

    void draw(Graphics g, int x, int y, double r);

    void addMagazine(int magazineCount);

    int getAmmo();

    int getMagazineCapacity();

    int getMagazineCount();

    int getReloadDuration();

    int getFireRate();

    int getDamage();

    WeaponType getType();
}

