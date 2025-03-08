package topdownshooter.Core;

import java.io.Serializable;
import java.util.Random;

import topdownshooter.Core.ConfigHandler.LevelProperties;
import topdownshooter.Zombie.Zombie;
import topdownshooter.Zombie.ZombieFactory;
import topdownshooter.Zombie.ZombieType;

public class GameLevel implements Serializable {
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
    private TimeTick spawnTick;
    private TimeTick waveTick;

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
        this.spawnPeriod = 0;

        this.spawnTick = null;
        this.waveTick = null;
    }

    public GameLevel(int level, boolean waveOver, boolean waveStarted, int waveDuration, int ordinaryZombieCount, int crawlerZombieCount, int tankZombieCount,
                     int acidZombieCount, int spawnPeriod, TimeTick spawnTick, TimeTick waveTick) {
        this.random = new Random();
        
        GameLevel.level = level;
        GameLevel.waveOver = waveOver;
        GameLevel.waveStarted = waveStarted;
        this.waveDuration = 0;
        this.ordinaryZombieCount = 0;
        this.crawlerZombieCount = 0;
        this.tankZombieCount = 0;
        this.acidZombieCount = 0;
        this.spawnPeriod = 0;

        this.spawnTick = spawnTick;
        this.waveTick = waveTick;
    }

    public void setConfig(ConfigHandler config) {
        this.config = config;
    }

    private void loadLevel(long level) {
        if (this.config==null) {
            System.err.println("Cannot load level since no configuration is defined!");
            return;
        }
        
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

            int totalZombies = getRemainingZombies();
            if (totalZombies > 0) {
                // Calculate zombie spawn period (Seconds per zombie) by normalizing the wave duration
                this.spawnPeriod = this.waveDuration / totalZombies;
            }

            this.spawnTick = new TimeTick(Globals.Time2GameTick(this.spawnPeriod * 1000));
            this.spawnTick.setRepeats(-1);  // Repeats indefinetly
            this.waveTick = new TimeTick(Globals.Time2GameTick(this.waveDuration * 1000));
            this.waveTick.setRepeats(0);  // Repeats indefinetly
        }
    }

    public Zombie update(final int maxWidth, final int maxHeight) {
        if (this.spawnTick==null) return null;

        if (GameLevel.waveStarted) {
            this.spawnTick.updateTick();
            this.waveTick.updateTick();

            Zombie zombie = null;
            zombie = spawnZombie(maxWidth, maxHeight);
            if (getRemainingZombies() <= 0) {
                endWave();
            }
            return zombie;
        }
        return null;
    }

    public int getRemainingZombies() {
        return this.ordinaryZombieCount + this.crawlerZombieCount + this.tankZombieCount + this.acidZombieCount;
    }

    public Zombie spawnZombie(final int maxWidth, final int maxHeight) {
        // If no enough time is elapsed to spawn the zombie, do not create a new one.
        if (!this.spawnTick.isTimeOut()) return null;

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
        if (getRemainingZombies() <= 0) {
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
    
        } while (getRemainingZombies() > 0); // Retry if no zombies left for that type

        this.spawnTick.reset();  // Reset spawn timer

        // Spawn the zombie
        if (randZombieType == 0 && this.ordinaryZombieCount > 0) {
            this.ordinaryZombieCount--;
            return ZombieFactory.createZombie(this.config, ZombieType.ORDINARY, x, y);
        } else if (randZombieType == 1 && this.crawlerZombieCount > 0) {
            this.crawlerZombieCount--;
            return ZombieFactory.createZombie(this.config, ZombieType.CRAWLER, x, y);
        } else if (randZombieType == 2 && this.tankZombieCount > 0) {
            this.tankZombieCount--;
            return ZombieFactory.createZombie(this.config, ZombieType.TANK, x, y);
        } else if (randZombieType == 3 && this.acidZombieCount > 0) {
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

    public int getRemainingTime() {
        return Globals.GameTick2Time(this.waveTick==null? 0 : this.waveTick.getTick());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GameLevel{");
        sb.append("level=" + GameLevel.level + ", ");
        sb.append("waveOver=" + GameLevel.waveOver + ", ");
        sb.append("waveStarted=" + GameLevel.waveStarted + ", ");       
        sb.append("waveDuration=" + this.waveDuration + ", ");
        sb.append("ordinaryZombieCount=" + this.ordinaryZombieCount + ", ");
        sb.append("crawlerZombieCount=" + this.crawlerZombieCount + ", ");
        sb.append("tankZombieCount=" + this.tankZombieCount + ", ");
        sb.append("acidZombieCount=" + this.acidZombieCount + ", ");
        sb.append("spawnPeriod=" + this.spawnPeriod + ", ");
        sb.append("spawnTick=" + this.spawnTick + ", ");
        sb.append("waveTick=" + this.waveTick);
        sb.append("}");

        return sb.toString();
    }
}
