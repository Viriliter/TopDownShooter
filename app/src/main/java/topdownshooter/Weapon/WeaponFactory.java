package topdownshooter.Weapon;

import topdownshooter.Core.ConfigHandler;

public class WeaponFactory {
    public static Weapon createWeapon(ConfigHandler config, WeaponType type) {
        if (config == null) {
            throw new IllegalStateException("ConfigHandler cannot be null!");
        }
        if (type == null) {
            throw new IllegalArgumentException("Weapon type cannot be null!");
        }

        switch (type) {
            case PISTOL:
                return new Pistol(config.getPistolProperties());
            case ASSAULTRIFLE:
                return new AssaultRifle(config.getAssaultRifleProperties());
            case SHOTGUN:
                return new Shotgun(config.getShotgunProperties());
            case SNIPERRIFLE:
                return new SniperRifle(config.getSniperRifleProperties());
            case ROCKETLAUNCHER:
                return new RocketLauncher(config.getRocketLauncherProperties());
            default:
                return null;
        }
    }
}
