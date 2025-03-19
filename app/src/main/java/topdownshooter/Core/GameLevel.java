package topdownshooter.Core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import topdownshooter.Core.ConfigHandler.LevelProperties;
import topdownshooter.Weapon.WeaponType;
import topdownshooter.Zombie.Zombie;
import topdownshooter.Zombie.ZombieFactory;
import topdownshooter.Zombie.ZombieType;

public class GameLevel implements Serializable {
    public enum GameLevelStatus {
        UNDEFINED,
        STARTED,
        SUSPENDED,
        ENDED,
    }

    private int levelBonus = 10;
    private int level = 0;
    private GameLevelStatus gameLevelStatus;

    private ConfigHandler config;
    private int waveDuration;
    private int ordinaryZombieCount;
    private int crawlerZombieCount;
    private int tankZombieCount;
    private int acidZombieCount;
    private int spawnPeriod;
    private TimeTick spawnTick;
    private TimeTick waveTick;
    private TimeTick newWaveSuspendTick;

    private List<ZombieType> zombieHorde = new ArrayList<>();
    private int currentZombieTypeIndex = 0;

    private static Random random = new Random(); // Reuse random instance

    public GameLevel(ConfigHandler config) {
        if (config == null) {
            throw new IllegalStateException("ConfigHandler cannot be null!");
        }

        this.config = config;

        this.gameLevelStatus = GameLevelStatus.UNDEFINED;
        this.waveDuration = 0;
        this.ordinaryZombieCount = 0;
        this.crawlerZombieCount = 0;
        this.tankZombieCount = 0;
        this.acidZombieCount = 0;
        this.spawnPeriod = 0;
        this.currentZombieTypeIndex = 0;

        this.spawnTick = null;
        this.waveTick = null;
        this.newWaveSuspendTick = new TimeTick(Globals.Time2GameTick(Globals.WAVE_SUSPEND_DURATION_MS), () -> startWaveInvokeLater());
    }

    public void setConfig(ConfigHandler config) {
        this.config = config;
    }

    private WeaponType loadLevel(long level) {
        if (this.config==null) {
            System.err.println("Cannot load level since no configuration is defined!");
            return null;
        }
        
        if (this.gameLevelStatus == GameLevelStatus.STARTED) {
            return null;
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
            
            for (int i = 0; i < levelProperties.ordinaryZombieCount(); i++) this.zombieHorde.add(ZombieType.ORDINARY);
            for (int i = 0; i < levelProperties.crawlerZombieCount(); i++) this.zombieHorde.add(ZombieType.CRAWLER);
            for (int i = 0; i < levelProperties.tankZombieCount(); i++) this.zombieHorde.add(ZombieType.TANK);
            for (int i = 0; i < levelProperties.acidZombieCount(); i++) this.zombieHorde.add(ZombieType.ACID);

            // Shuffle to randomize zombie types
            Collections.shuffle(this.zombieHorde, GameLevel.random);
            this.currentZombieTypeIndex = 0;

            int totalZombies = getRemainingZombies();
            if (totalZombies > 0) {
                // Calculate zombie spawn period (Seconds per zombie) by normalizing the wave duration
                this.spawnPeriod = this.waveDuration / totalZombies;
            }

            this.spawnTick = new TimeTick(Globals.Time2GameTick(this.spawnPeriod * 1000));
            this.spawnTick.setRepeats(-1);  // Repeats indefinetly
            this.waveTick = new TimeTick(Globals.Time2GameTick(this.waveDuration * 1000));
            this.waveTick.setRepeats(0);  // Repeats indefinetly
            return levelProperties.weaponPrize();
        }
        return null;
    }

    public Zombie update(final int maxWidth, final int maxHeight) {
        if (this.gameLevelStatus == GameLevelStatus.SUSPENDED) {
                this.newWaveSuspendTick.updateTick();
                return null;
            }

        if (this.spawnTick==null) return null;

        if (this.gameLevelStatus == GameLevelStatus.STARTED) {
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

        // Get zombie type from randomized list
        ZombieType currentZombieType = null;
        try {
            currentZombieType = this.zombieHorde.get(this.currentZombieTypeIndex++);    
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
        this.spawnTick.reset();  // Reset spawn timer

        // Spawn the zombie
        switch(currentZombieType) {
            case ORDINARY:
                this.ordinaryZombieCount--;
                return ZombieFactory.createZombie(this.config, ZombieType.ORDINARY, x, y);
            case CRAWLER:
                this.crawlerZombieCount--;
                return ZombieFactory.createZombie(this.config, ZombieType.CRAWLER, x, y);
            case TANK:
                this.tankZombieCount--;
                return ZombieFactory.createZombie(this.config, ZombieType.TANK, x, y);
            case ACID:
                this.acidZombieCount--;
                return ZombieFactory.createZombie(this.config, ZombieType.ACID, x, y);
            default:
                return null;
        }
    }

    private void startWaveInvokeLater() {
        this.gameLevelStatus = GameLevelStatus.STARTED;
        this.newWaveSuspendTick.reset();
    }

    public WeaponType startWave() {

        if (this.gameLevelStatus == GameLevelStatus.STARTED || 
            this.gameLevelStatus == GameLevelStatus.SUSPENDED)
            return null;

        this.gameLevelStatus = GameLevelStatus.SUSPENDED;
        WeaponType weaponPrize = loadLevel(++this.level);
        return weaponPrize;
    }

    public void endWave() {
        this.gameLevelStatus = GameLevelStatus.ENDED;
    }

    public boolean isWaveStarted() {
        return this.gameLevelStatus == GameLevelStatus.STARTED;
    }

    public boolean isWaveOver() {
        return this.gameLevelStatus == GameLevelStatus.ENDED;
    }

    public int getLevel() {
        return level;
    }

    public int getRemainingTime() {
        return Globals.GameTick2Time(this.waveTick==null? 0 : this.waveTick.getTick());
    }

    public GameLevelStatus getWaveStatus() {
        return this.gameLevelStatus;
    }

    public int calculateLevelBonus() {
        if (this.level == 0) return 0;
        this.levelBonus += this.levelBonus;
        return this.levelBonus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GameLevel{");
        sb.append("level=" + this.level + ", ");
        sb.append("gameLevelStatus=" + this.gameLevelStatus);
        sb.append("waveDuration=" + this.waveDuration + ", ");
        sb.append("ordinaryZombieCount=" + this.ordinaryZombieCount + ", ");
        sb.append("crawlerZombieCount=" + this.crawlerZombieCount + ", ");
        sb.append("tankZombieCount=" + this.tankZombieCount + ", ");
        sb.append("acidZombieCount=" + this.acidZombieCount + ", ");
        sb.append("spawnPeriod=" + this.spawnPeriod + ", ");
        sb.append("spawnTick=" + this.spawnTick + ", ");
        sb.append("waveTick=" + this.waveTick);
        sb.append("newWaveSuspendTick=" + this.newWaveSuspendTick);
        sb.append("}");

        return sb.toString();
    }
}
