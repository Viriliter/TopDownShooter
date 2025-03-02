package topdownshooter.Zombie;

import topdownshooter.Core.ConfigHandler;

public class ZombieFactory {
    public static Zombie createZombie(ConfigHandler config, ZombieType type, int x, int y) {
        if (config == null) {
            throw new IllegalStateException("ConfigHandler cannot be null!");
        }
        if (type == null) {
            throw new IllegalArgumentException("Zombie type cannot be null!");
        }

        switch (type) {
            case ORDINARY:
                return new OrdinaryZombie(config.getOrdinaryZombieProperties(), x, y);
            case CRAWLER:
                return new CrawlerZombie(config.getCrawlerZombieProperties(), x, y);
            case TANK:
                return new TankZombie(config.getTankZombieProperties(), x, y);
            case ACID:
                return new AcidZombie(config.getAcidZombieProperties(), x, y);
            default:
                return null;
        }
    }
}
