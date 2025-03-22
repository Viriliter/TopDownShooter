/*
 * @file ConfigHandler.java
 * @brief This file defines the `ConfigHandler` class.
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

import org.ini4j.Ini;

import topdownshooter.Weapon.WeaponType;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @class ConfigHandler
 * @brief Handles the loading, parsing, and retrieval of configuration values from a file.
 */
public final class ConfigHandler implements Serializable{

    public record WindowProperties (
        int windowWidth,
        int windowHeight,
        int x,
        int y
    ) {}

    public record WeaponProperties (
        int damage,
        int fireRate,
        int magazineCapacity,
        int magazineCount,
        int reloadDuration
    ) {}

    public record PlayerProperties (
        int startingX,
        int startingY,
        int startingHealth,
        int speed,
        WeaponType[] startingWeapons
    ) {}

    public record ZombieProperties (
        int health, 
        int speed,
        int damage,
        int points
    ) {}

    public record LevelProperties (
        int waveDuration,
        int ordinaryZombieCount,
        int crawlerZombieCount,
        int tankZombieCount,
        int acidZombieCount,
        WeaponType weaponPrize
    ) {}

    private static Ini ini;

    /**
     * Constructor that loads and parses the configuration file.
     * @param filePath The path to the configuration file.
     */
    public ConfigHandler(String path) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(path)) {
            if (input == null) {
                throw new IOException(path + " is not found in resources");
            }
            ConfigHandler.ini = new Ini(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets value for the key of the configuration parameter in provided section.
     * @param section The section where the key locates.
     * @param key The key of the configuration parameter.
     * @param value The value to set.
     */
    public <T> void setProperty(String section, String key, T value) {
        ConfigHandler.ini.put(section, key, value);
    }

    /**
     * Returns weapon type according to provided String formatted type.
     * @param type String representation of the weapon type.
     * @return The weapon type.
     */
    private WeaponType getWeaponType(String type) {
        if (type == "PISTOL")
            return WeaponType.PISTOL;
        else if (type.equals("ASSAULT_RIFLE"))
            return WeaponType.ASSAULTRIFLE;
        else if (type.equals("SHOTGUN"))
            return WeaponType.SHOTGUN;
        else if (type.equals("SNIPER_RIFLE"))
            return WeaponType.SNIPERRIFLE;
        else if (type.equals("ROCKET_LAUNCHER"))
            return WeaponType.ROCKETLAUNCHER;
        else
            return WeaponType.UNDEFINED;
    }

    public static WeaponType[] parseWeapon(String weaponString) {
        if (weaponString == null || weaponString.trim().isEmpty()) {
            return new WeaponType[0]; // Return empty array if field is missing
        }

        String[] weaponNames = weaponString.split(",");
        List<WeaponType> weaponList = new ArrayList<>();

        for (String weaponName : weaponNames) {
            weaponName = weaponName.trim();
            if (weaponName.equals("PISTOL"))
                weaponList.add(WeaponType.PISTOL);
            else if (weaponName.equals("ASSAULT_RIFLE"))
                weaponList.add(WeaponType.ASSAULTRIFLE);
            else if (weaponName.equals("SHOTGUN"))
                weaponList.add(WeaponType.SHOTGUN);
            else if (weaponName.equals("SNIPER_RIFLE"))
                weaponList.add(WeaponType.SNIPERRIFLE);
            else if (weaponName.equals("ROCKET_LAUNCHER"))
                weaponList.add(WeaponType.ROCKETLAUNCHER);
            else 
                System.err.println("Unknown weapon: " + weaponName.trim());
        }
        return weaponList.toArray(new WeaponType[0]);
    }    

    /**
     * Returns window properties of the game.
     * @return The weapon properties.
     */
    public WindowProperties getWindowProperties() {
        Integer windowWidth = Integer.parseInt(ConfigHandler.ini.get("Window").get("Width"));
        Integer windowHeight = Integer.parseInt(ConfigHandler.ini.get("Window").get("Height"));
        Integer x = Integer.parseInt(ConfigHandler.ini.get("Window").get("X"));
        Integer y = Integer.parseInt(ConfigHandler.ini.get("Window").get("Y"));
        return new WindowProperties(windowWidth, windowHeight, x ,y);
    }

    /**
     * Returns weapon properties of the pistol.
     * @return The weapon properties.
     */
    public WeaponProperties getPistolProperties() {
        Integer weaponDamage = Integer.parseInt(ConfigHandler.ini.get("Pistol").get("Damage"));
        Integer weaponFireRate = Integer.parseInt(ConfigHandler.ini.get("Pistol").get("FireRate"));
        Integer weaponMagazineCapacity = Integer.parseInt(ConfigHandler.ini.get("Pistol").get("MagazineCapacity"));
        Integer weaponMagazineCount = Integer.parseInt(ConfigHandler.ini.get("Pistol").get("MagazineCount"));
        Integer reloadDuration = Integer.parseInt(ConfigHandler.ini.get("Pistol").get("ReloadDuration"));
        return new WeaponProperties(weaponDamage, weaponFireRate, weaponMagazineCapacity, weaponMagazineCount, reloadDuration);
    }

    /**
     * Returns weapon properties of the assault rifle.
     * @return The weapon properties.
     */
    public WeaponProperties getAssaultRifleProperties() {
        Integer weaponDamage = Integer.parseInt(ConfigHandler.ini.get("AssaultRifle").get("Damage"));
        Integer weaponFireRate = Integer.parseInt(ConfigHandler.ini.get("AssaultRifle").get("FireRate"));
        Integer weaponMagazineCapacity = Integer.parseInt(ConfigHandler.ini.get("AssaultRifle").get("MagazineCapacity"));
        Integer weaponMagazineCount = Integer.parseInt(ConfigHandler.ini.get("AssaultRifle").get("MagazineCount"));
        Integer reloadDuration = Integer.parseInt(ConfigHandler.ini.get("AssaultRifle").get("ReloadDuration"));
        return new WeaponProperties(weaponDamage, weaponFireRate, weaponMagazineCapacity, weaponMagazineCount, reloadDuration);
    }

    /**
     * Returns weapon properties of the shotgun.
     * @return The weapon properties.
     */
    public WeaponProperties getShotgunProperties() {
        Integer weaponDamage = Integer.parseInt(ConfigHandler.ini.get("Shotgun").get("Damage"));
        Integer weaponFireRate = Integer.parseInt(ConfigHandler.ini.get("Shotgun").get("FireRate"));
        Integer weaponMagazineCapacity = Integer.parseInt(ConfigHandler.ini.get("Shotgun").get("MagazineCapacity"));
        Integer weaponMagazineCount = Integer.parseInt(ConfigHandler.ini.get("Shotgun").get("MagazineCount"));
        Integer reloadDuration = Integer.parseInt(ConfigHandler.ini.get("Shotgun").get("ReloadDuration"));
        return new WeaponProperties(weaponDamage, weaponFireRate, weaponMagazineCapacity, weaponMagazineCount, reloadDuration);
    }

    /**
     * Returns weapon properties of the sniper rifle.
     * @return The weapon properties.
     */
    public WeaponProperties getSniperRifleProperties() {
        Integer weaponDamage = Integer.parseInt(ConfigHandler.ini.get("SniperRifle").get("Damage"));
        Integer weaponFireRate = Integer.parseInt(ConfigHandler.ini.get("SniperRifle").get("FireRate"));
        Integer weaponMagazineCapacity = Integer.parseInt(ConfigHandler.ini.get("SniperRifle").get("MagazineCapacity"));
        Integer weaponMagazineCount = Integer.parseInt(ConfigHandler.ini.get("SniperRifle").get("MagazineCount"));
        Integer reloadDuration = Integer.parseInt(ConfigHandler.ini.get("SniperRifle").get("ReloadDuration"));
        return new WeaponProperties(weaponDamage, weaponFireRate, weaponMagazineCapacity, weaponMagazineCount, reloadDuration);
    }

    /**
     * Returns weapon properties of the rocket launcher.
     * @return The weapon properties.
     */
    public WeaponProperties getRocketLauncherProperties() {
        Integer weaponDamage = Integer.parseInt(ConfigHandler.ini.get("Rocketlauncher").get("Damage"));
        Integer weaponFireRate = Integer.parseInt(ConfigHandler.ini.get("Rocketlauncher").get("FireRate"));
        Integer weaponMagazineCapacity = Integer.parseInt(ConfigHandler.ini.get("Rocketlauncher").get("MagazineCapacity"));
        Integer weaponMagazineCount = Integer.parseInt(ConfigHandler.ini.get("Rocketlauncher").get("MagazineCount"));
        Integer reloadDuration = Integer.parseInt(ConfigHandler.ini.get("Rocketlauncher").get("ReloadDuration"));
        return new WeaponProperties(weaponDamage, weaponFireRate, weaponMagazineCapacity, weaponMagazineCount, reloadDuration);
    }

    /**
     * Returns player properties.
     * @return The player properties.
     */
    public PlayerProperties getPlayerProperties() {
        Integer playerStartingX = Integer.parseInt(ConfigHandler.ini.get("Player").get("StartingX"));
        Integer playerStartingY = Integer.parseInt(ConfigHandler.ini.get("Player").get("StartingY"));
        Integer playerStartingHealth = Integer.parseInt(ConfigHandler.ini.get("Player").get("StartingHealth"));
        Integer playerSpeed = Integer.parseInt(ConfigHandler.ini.get("Player").get("Speed"));
        WeaponType[] startingWeapons = ConfigHandler.parseWeapon(ConfigHandler.ini.get("Player").get("StartingWeapons"));
        return new PlayerProperties(playerStartingX, playerStartingY, playerStartingHealth, playerSpeed, startingWeapons);
    }

    /**
     * Returns zombie properties of the ordinary type.
     * @return The zombie properties.
     */
    public ZombieProperties getOrdinaryZombieProperties() {
        Integer zombieHealth = Integer.parseInt(ConfigHandler.ini.get("OrdinaryZombie").get("Health"));
        Integer zombieSpeed = Integer.parseInt(ConfigHandler.ini.get("OrdinaryZombie").get("Speed"));
        Integer zombieDamage = Integer.parseInt(ConfigHandler.ini.get("OrdinaryZombie").get("Damage"));
        Integer zombiePoints = Integer.parseInt(ConfigHandler.ini.get("OrdinaryZombie").get("Points"));
        return new ZombieProperties(zombieHealth, zombieSpeed, zombieDamage, zombiePoints);
    }

    /**
     * Returns zombie properties of the crawler type.
     * @return The zombie properties.
     */
    public ZombieProperties getCrawlerZombieProperties() {
        Integer zombieHealth = Integer.parseInt(ConfigHandler.ini.get("CrawlerZombie").get("Health"));
        Integer zombieSpeed = Integer.parseInt(ConfigHandler.ini.get("CrawlerZombie").get("Speed"));
        Integer zombieDamage = Integer.parseInt(ConfigHandler.ini.get("CrawlerZombie").get("Damage"));
        Integer zombiePoints = Integer.parseInt(ConfigHandler.ini.get("CrawlerZombie").get("Points"));
        return new ZombieProperties(zombieHealth, zombieSpeed, zombieDamage, zombiePoints);
    }

    /**
     * Returns zombie properties of the tank type.
     * @return The zombie properties.
     */
    public ZombieProperties getTankZombieProperties() {
        Integer zombieHealth = Integer.parseInt(ConfigHandler.ini.get("TankZombie").get("Health"));
        Integer zombieSpeed = Integer.parseInt(ConfigHandler.ini.get("TankZombie").get("Speed"));
        Integer zombieDamage = Integer.parseInt(ConfigHandler.ini.get("TankZombie").get("Damage"));
        Integer zombiePoints = Integer.parseInt(ConfigHandler.ini.get("TankZombie").get("Points"));
        return new ZombieProperties(zombieHealth, zombieSpeed, zombieDamage, zombiePoints);
    }

    /**
     * Returns zombie properties of the acid type.
     * @return The zombie properties.
     */
    public ZombieProperties getAcidZombieProperties() {
        Integer zombieHealth = Integer.parseInt(ConfigHandler.ini.get("AcidZombie").get("Health"));
        Integer zombieSpeed = Integer.parseInt(ConfigHandler.ini.get("AcidZombie").get("Speed"));
        Integer zombieDamage = Integer.parseInt(ConfigHandler.ini.get("AcidZombie").get("Damage"));
        Integer zombiePoints = Integer.parseInt(ConfigHandler.ini.get("AcidZombie").get("Points"));
        return new ZombieProperties(zombieHealth, zombieSpeed, zombieDamage, zombiePoints);
    }

    /**
     * Returns level properties of level 1.
     * @return The level properties.
     */
    public LevelProperties getLevel1Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level1").get("WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level1").get("OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level1").get("CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level1").get("TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level1").get("AcidZombieCount"));
        WeaponType weaponPrize = getWeaponType(ConfigHandler.ini.get("Level1").get("WeaponPrize", ""));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount, weaponPrize);
    }

    /**
     * Returns level properties of level 2.
     * @return The level properties.
     */
    public LevelProperties getLevel2Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level2").get("WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level2").get("OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level2").get("CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level2").get("TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level2").get("AcidZombieCount"));
        WeaponType weaponPrize = getWeaponType(ConfigHandler.ini.get("Level2").get("WeaponPrize", ""));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount, weaponPrize);
    }

    /**
     * Returns level properties of level 3.
     * @return The level properties.
     */
    public LevelProperties getLevel3Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level3").get("WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level3").get("OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level3").get("CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level3").get("TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level3").get("AcidZombieCount"));
        WeaponType weaponPrize = getWeaponType(ConfigHandler.ini.get("Level3").get("WeaponPrize", ""));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount, weaponPrize);
    }

    /**
     * Returns level properties of level 4.
     * @return The level properties.
     */
    public LevelProperties getLevel4Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level4").get("WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level4").get("OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level4").get("CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level4").get("TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level4").get("AcidZombieCount"));
        WeaponType weaponPrize = getWeaponType(ConfigHandler.ini.get("Level4").get("WeaponPrize", ""));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount, weaponPrize);
    }

    /**
     * Returns level properties of level 5.
     * @return The level properties.
     */
    public LevelProperties getLevel5Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level5").get("WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level5").get("OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level5").get("CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level5").get("TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level5").get("AcidZombieCount"));
        WeaponType weaponPrize = getWeaponType(ConfigHandler.ini.get("Level5").get("WeaponPrize", ""));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount, weaponPrize);
    }

    /**
     * Returns level properties of level 6.
     * @return The level properties.
     */
    public LevelProperties getLevel6Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level6").get("WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level6").get("OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level6").get("CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level6").get("TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level6").get("AcidZombieCount"));
        WeaponType weaponPrize = getWeaponType(ConfigHandler.ini.get("Level6").get("WeaponPrize", ""));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount, weaponPrize);
    }

    /**
     * Returns level properties of level 7.
     * @return The level properties.
     */
    public LevelProperties getLevel7Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level7").get("WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level7").get("OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level7").get("CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level7").get("TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level7").get("AcidZombieCount"));
        WeaponType weaponPrize = getWeaponType(ConfigHandler.ini.get("Level7").get("WeaponPrize", ""));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount, weaponPrize);
    }

    /**
     * Returns level properties of level 8.
     * @return The level properties.
     */
    public LevelProperties getLevel8Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level8").get("WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level8").get("OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level8").get("CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level8").get("TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level8").get("AcidZombieCount"));
        WeaponType weaponPrize = getWeaponType(ConfigHandler.ini.get("Level8").get("WeaponPrize", ""));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount, weaponPrize);
    }

    /**
     * Returns level properties of level 9.
     * @return The level properties.
     */
    public LevelProperties getLevel9Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level9").get("WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level9").get("OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level9").get("CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level9").get("TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level9").get("AcidZombieCount"));
        WeaponType weaponPrize = getWeaponType(ConfigHandler.ini.get("Level9").get("WeaponPrize", ""));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount, weaponPrize);
    }

    /**
     * Returns level properties of level 10.
     * @return The level properties.
     */
    public LevelProperties getLevel10Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level10").get("WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level10").get("OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level10").get("CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level10").get("TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level10").get("AcidZombieCount"));
        WeaponType weaponPrize = getWeaponType(ConfigHandler.ini.get("Level10").get("WeaponPrize", ""));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount, weaponPrize);
    }

    /**
     * Returns level properties after level 10.
     * @return The level properties.
     */
    public LevelProperties getLevel10PlusProperties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level10+").get("WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level10+").get("OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level10+").get("CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level10+").get("TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level10+").get("AcidZombieCount"));
        WeaponType weaponPrize = getWeaponType(ConfigHandler.ini.get("Level10+").get("WeaponPrize", ""));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount, weaponPrize);
    }
}
