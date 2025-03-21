/*
 * @file GameLevel.java
 * @brief This file defines the `GameLevel` class.
 *
 * Created on Wed Mar 19 2025
 *
 * @copyright MIT License
 *
 * Copyright (c) 2025 Mert LIMONCUOGLU
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

/**
 * @class GameLevel
 * @brief Manages the game level, zombie spawning, and wave progression.
 */
public class GameLevel implements Serializable {
    /**
     * @enum GameLevelStatus
     * @brief Represents the current status of the game level.
     */
    public enum GameLevelStatus {
        UNDEFINED,
        STARTED,
        SUSPENDED,
        ENDED,
    }

    private int levelBonus = 10;                /**< Bonus awarded per level. */
    private int level = 0;                      /**< Current level number. */
    private GameLevelStatus gameLevelStatus;    /**< Current status of the level. */

    private ConfigHandler config;               /**< Configuration handler. */
    private int waveDuration;                   /**< Duration of the wave in seconds. */
    private int ordinaryZombieCount;            /**< Count of ordinary zombies. */
    private int crawlerZombieCount;             /**< Count of crawler zombies. */
    private int tankZombieCount;                /**< Count of tank zombies. */
    private int acidZombieCount;                /**< Count of acid zombies. */
    private int spawnPeriod;                    /**< Time interval between zombie spawns in seconds. */
    private TimeTick spawnTick;                 /**< Timer for zombie spawning. */
    private TimeTick waveTick;                  /**< Timer for wave duration. */
    private TimeTick newWaveSuspendTick;        /**< Timer for suspending new wave after finishing the last one. */

    private List<ZombieType> zombieHorde = new ArrayList<>();   /**< List of zombie types for the level. */
    private int currentZombieTypeIndex = 0;                     /**< Current index of the zombieHorde. */

    private static Random random = new Random();                /**< Random number generator. */

    /**
     * Constructs a GameLevel with the specified configuration.
     * @param config The configuration handler.
     * @throws IllegalStateException if the configuration is null.
     */
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

    /**
     * Sets the configuration handler.
     * @param config The new configuration handler.
     */
    public void setConfig(ConfigHandler config) {
        this.config = config;
    }

    /**
     * Loads the specified level configuration.
     * @param level The level number.
     * @return The weapon awarded for completing the level.
     */
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
    
    /**
     * Updates the game state and spawns zombies if needed.
     * @param maxWidth The maximum width of the game field.
     * @param maxHeight The maximum height of the game field.
     * @return The newly spawned zombie, or null if no zombie is spawned.
     */
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

    /**
     * Retrieves the number of remaining zombies in the wave.
     * @return The count of remaining zombies.
     */
    public int getRemainingZombies() {
        return this.ordinaryZombieCount + this.crawlerZombieCount + this.tankZombieCount + this.acidZombieCount;
    }

    /**
     * Spawns a zombie at a random location.
     * @param maxWidth The maximum width of the game field.
     * @param maxHeight The maximum height of the game field.
     * @return The spawned zombie, or null if spawning conditions are not met.
     */
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

    /**
     * Initiates the wave start sequence after suspension.
     */
    private void startWaveInvokeLater() {
        this.gameLevelStatus = GameLevelStatus.STARTED;
        this.newWaveSuspendTick.reset();
    }

    /**
     * Starts a new wave.
     * @return The weapon prize for completing the wave.
     */
    public WeaponType startWave() {

        if (this.gameLevelStatus == GameLevelStatus.STARTED || 
            this.gameLevelStatus == GameLevelStatus.SUSPENDED)
            return null;

        this.gameLevelStatus = GameLevelStatus.SUSPENDED;
        WeaponType weaponPrize = loadLevel(++this.level);
        return weaponPrize;
    }

    /**
     * Ends the current wave.
     */
    public void endWave() {
        this.gameLevelStatus = GameLevelStatus.ENDED;
    }

    /**
     * Checks if the wave has started.
     * @return True if the wave is in progress, false otherwise.
     */
    public boolean isWaveStarted() {
        return this.gameLevelStatus == GameLevelStatus.STARTED;
    }

    /**
     * Checks if the wave is over.
     * @return True if the wave has ended, false otherwise.
     */
    public boolean isWaveOver() {
        return this.gameLevelStatus == GameLevelStatus.ENDED;
    }

    /**
     * Gets the current game level.
     * @return The current level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the remaining time in the wave.
     * @return The remaining time in milliseconds.
     */
    public int getRemainingTime() {
        return Globals.GameTick2Time(this.waveTick==null? 0 : this.waveTick.getTick());
    }

    /**
     * Gets the current status of the wave.
     * @return The current wave status.
     */
    public GameLevelStatus getWaveStatus() {
        return this.gameLevelStatus;
    }

    /**
     * Calculates and updates the level bonus.
     * @return The new level bonus.
     */
    public int calculateLevelBonus() {
        if (this.level == 0) return 0;
        this.levelBonus += this.levelBonus;
        return this.levelBonus;
    }

    /**
     * Converts the GameLevel object to a string representation.
     * @return The string representation of the object.
     */
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
