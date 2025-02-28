package topdownshooter;

public interface Weapon {
    int damage = 0;
    int fireRate = 0;
    int reloadTime = 0;
    int ammo = 0;
    int maxAmmo = 0;
    int reloadTimer = 0;
    int fireTimer = 0;

    void fire();

    void reload();

    void update();

    int getAmmo();

    int getMaxAmmo();

    int getReloadTimer();

    int getFireTimer();

    int getDamage();
}

