package topdownshooter.Core;

import java.util.Random;

import topdownshooter.Core.ConfigHandler.LevelProperties;
import topdownshooter.Zombie.Zombie;
import topdownshooter.Zombie.ZombieFactory;
import topdownshooter.Zombie.ZombieType;

public class GameLevel {
    private static int level = 0;
    private static boolean waveOver = true;
    private static boolean waveStarted = false;

    private ConfigHandler config;
    private int waveDuration;
    private int ordinaryZombieCount;
    private int crawlerZombieCount;
    private int tankZombieCount;
    private int acidZombieCount;
    private int spawnPeriod;
    private int spawnTimer;
    private int waveTimer;

    private Random random;

    public GameLevel(ConfigHandler config) {
        if (config == null) {
            throw new IllegalStateException("ConfigHandler cannot be null!");
        }

        this.config = config;
        this.random = new Random();

        this.waveDuration = 0;
        this.ordinaryZombieCount = 0;
        this.crawlerZombieCount = 0;
        this.tankZombieCount = 0;
        this.acidZombieCount = 0;

        this.spawnTimer = 0;
        this.spawnPeriod = 0;

        this.waveTimer = 0;
    }

    private void loadLevel(long level) {
        if (GameLevel.waveStarted) {
            return;
        }

        LevelProperties levelProperties = null;
        switch ((int) level) {
            case 1:
                levelProperties = this.config.getLevel1Properties();
                break;
            case 2:
                levelProperties = this.config.getLevel2Properties();
                break;
            case 3:
                levelProperties = this.config.getLevel3Properties();
                break;
            case 4:
                levelProperties = this.config.getLevel4Properties();
                break;
            case 5:
                levelProperties = this.config.getLevel5Properties();
                break;
            case 6:
                levelProperties = this.config.getLevel6Properties();
                break;
            case 7:
                levelProperties = this.config.getLevel7Properties();
                break;
            case 8:
                levelProperties = this.config.getLevel8Properties();
                break;
            case 9:
                levelProperties = this.config.getLevel9Properties();
                break;
            case 10:
                levelProperties = this.config.getLevel10Properties();
                break;
            default:
                levelProperties = this.config.getLevel10PlusProperties();
                break;
        }

        if (levelProperties != null) {
            this.waveDuration = levelProperties.waveDuration();
            this.ordinaryZombieCount = levelProperties.ordinaryZombieCount();
            this.crawlerZombieCount = levelProperties.crawlerZombieCount();
            this.tankZombieCount = levelProperties.tankZombieCount();
            this.acidZombieCount = levelProperties.acidZombieCount();

            int totalZombies = getReaminingZombies();
            if (totalZombies > 0) {
                this.spawnPeriod = this.waveDuration / totalZombies;
            }
            this.spawnTimer = this.spawnPeriod * 1000 / Globals.GAME_TICK_MS ;
            this.waveTimer = this.waveDuration * 1000 / Globals.GAME_TICK_MS ;
        }
    }

    public Zombie update(final int maxWidth, final int maxHeight) {
        if (GameLevel.waveStarted) {
            if (this.spawnTimer>0) this.spawnTimer--;
            if (this.waveTimer>0) this.waveTimer--;

            Zombie zombie = spawnZombie(maxWidth, maxHeight);
            if (getReaminingZombies() <= 0) {
                endWave();
            }
            return zombie;
        }
        return null;
    }

    private int getReaminingZombies() {
        return this.ordinaryZombieCount + this.crawlerZombieCount + this.tankZombieCount + this.acidZombieCount;
    }

    public Zombie spawnZombie(final int maxWidth, final int maxHeight) {
        if (spawnTimer > 0) return null;

        // Randomize spawn edge
        int spawnEdge = random.nextInt(4); // 0 = top, 1 = bottom, 2 = left, 3 = right
        int x = 0, y = 0;
    
        switch (spawnEdge) {
            case 0: // Top edge
                x = random.nextInt(maxWidth); 
                y = 0;
                break;
            case 1: // Bottom edge
                x = random.nextInt(maxWidth);
                y = maxHeight;
                break;
            case 2: // Left edge
                x = 0;
                y = random.nextInt(maxHeight);
                break;
            case 3: // Right edge
                x = maxWidth;
                y = random.nextInt(maxHeight);
                break;
        }

        // Check if there are any zombies left to spawn
        if (getReaminingZombies() <= 0) {
            return null;
        }

        // If there are no zombies of the current type left, randomize again
        int randZombieType = 0;
        do {
            randZombieType = random.nextInt(5);  // 0 = ordinary, 1 = crawler, 2 = tank, 3 = acid
            if (randZombieType == 0 && this.ordinaryZombieCount > 0) break;
            if (randZombieType == 1 && this.crawlerZombieCount > 0) break;
            if (randZombieType == 2 && this.tankZombieCount > 0) break;
            if (randZombieType == 3 && this.acidZombieCount > 0) break;
    
        } while (getReaminingZombies() > 0); // Retry if no zombies left for that type

        this.spawnTimer = this.spawnPeriod * 1000 / Globals.GAME_TICK_MS ;  // Reset spawn timer
        // Spawn the zombie
        if (randZombieType == 0 && this.ordinaryZombieCount > 0) {
            this.ordinaryZombieCount--;
            return ZombieFactory.createZombie(this.config, ZombieType.ORDINARY, x, y);
        }
        if (randZombieType == 1 && this.crawlerZombieCount > 0) {
            this.crawlerZombieCount--;
            return ZombieFactory.createZombie(this.config, ZombieType.CRAWLER, x, y);
        }
        if (randZombieType == 2 && this.tankZombieCount > 0) {
            this.tankZombieCount--;
            return ZombieFactory.createZombie(this.config, ZombieType.TANK, x, y);
        }
        if (randZombieType == 3 && this.acidZombieCount > 0) {
            this.acidZombieCount--;
            return ZombieFactory.createZombie(this.config, ZombieType.ACID, x, y);
        }
        return null;
    }

    public void startWave() {
        if (GameLevel.waveStarted) return;

        System.out.println("Starting new level: " + (GameLevel.level + 1));
        loadLevel(++GameLevel.level);
        GameLevel.waveStarted = true;
        GameLevel.waveOver = false;
    }

    public void endWave() {
        GameLevel.waveStarted = false;
        GameLevel.waveOver = true;
    }

    public boolean isWaveOver() {
        return GameLevel.waveOver;
    }

    public int getLevel() {
        return level;
    }
}
