package topdownshooter;

public class Pistol implements Weapon {
    private int damage = 1;
    private int fireRate = 10;
    private int reloadTime = 100;
    private int ammo = 12;
    private int maxAmmo = 12;
    private int reloadTimer = 0;
    private int fireTimer = 0;

    public void fire() {
        if (fireTimer <= 0 && ammo > 0) {
            ammo--;
            fireTimer = fireRate;
        }
    }

    public void reload() {
        if (reloadTimer <= 0 && ammo < maxAmmo) {
            ammo = maxAmmo;
            reloadTimer = reloadTime;
        }
    }

    public void update() {
        if (fireTimer > 0) fireTimer--;
        if (reloadTimer > 0) reloadTimer--;
    }

    public int getAmmo() {
        return ammo;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public int getReloadTimer() {
        return reloadTimer;
    }

    public int getFireTimer() {
        return fireTimer;
    }

    public int getDamage() {
        return damage;
    }
}