package topdownshooter.Weapon;

public interface Weapon {
    Bullet fire(int x, int y, double r);

    void reload();

    void update();

    int getAmmo();

    int getMagazineCapacity();

    int getMagazineCount();

    int getReloadTimer();

    int getFireTimer();

    int getDamage();
}

