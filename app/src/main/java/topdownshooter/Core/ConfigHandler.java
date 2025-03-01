package topdownshooter.Core;

import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;

public class ConfigHandler {
    public record WindowProperties (
        int windowWidth,
        int windowHeight
    ) {}

    public record WeaponProperties (
        int weaponDamage,
        int weaponFireRate,
        int weaponCapacity,
        int reloadTime
    ) {}

    public record PlayerProperties (
        int playerStartingHealth,
        int playerSpeed
    ) {}

    public record ZombieProperties (
        int zombieHealth, 
        int zombieSpeed,
        int zombieDamage,
        int zombieRange
    ) {}

    public record LevelProperties (
        int levelWaveDuration,
        int levelOrdinaryZombieCount,
        int levelCrawlerZombieCount,
        int levelTankZombieCount,
        int levelAcidZombieCount
    ) {}

    private static Ini ini;

    public ConfigHandler(String path) {
        try {
            ConfigHandler.ini = new Ini(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    WindowProperties getWindowProperties() {
        Integer windowWidth = Integer.parseInt(ConfigHandler.ini.get("window", "Width"));
        Integer windowHeight = Integer.parseInt(ConfigHandler.ini.get("window", "Height"));
        return new WindowProperties(windowWidth, windowHeight);

    }

    WeaponProperties getPistolProperties() {
        Integer weaponDamage = Integer.parseInt(ConfigHandler.ini.get("Pistol", "Damage"));
        Integer weaponFireRate = Integer.parseInt(ConfigHandler.ini.get("Pistol", "FireRate"));
        Integer weaponCapacity = Integer.parseInt(ConfigHandler.ini.get("Pistol", "Capacity"));
        Integer reloadTime = Integer.parseInt(ConfigHandler.ini.get("Pistol", "ReloadTime"));
        return new WeaponProperties(weaponDamage, weaponFireRate, weaponCapacity, reloadTime);
    }

    WeaponProperties getAssultRifleProperties() {
        Integer weaponDamage = Integer.parseInt(ConfigHandler.ini.get("AssultRifle", "Damage"));
        Integer weaponFireRate = Integer.parseInt(ConfigHandler.ini.get("AssultRifle", "FireRate"));
        Integer weaponCapacity = Integer.parseInt(ConfigHandler.ini.get("AssultRifle", "Capacity"));
        Integer reloadTime = Integer.parseInt(ConfigHandler.ini.get("AssultRifle", "ReloadTime"));
        return new WeaponProperties(weaponDamage, weaponFireRate, weaponCapacity, reloadTime);
    }

    WeaponProperties getShotgunProperties() {
        Integer weaponDamage = Integer.parseInt(ConfigHandler.ini.get("Shotgun", "Damage"));
        Integer weaponFireRate = Integer.parseInt(ConfigHandler.ini.get("Shotgun", "FireRate"));
        Integer weaponCapacity = Integer.parseInt(ConfigHandler.ini.get("Shotgun", "Capacity"));
        Integer reloadTime = Integer.parseInt(ConfigHandler.ini.get("Shotgun", "ReloadTime"));
        return new WeaponProperties(weaponDamage, weaponFireRate, weaponCapacity, reloadTime);
    }

    WeaponProperties getSniperRifleProperties() {
        Integer weaponDamage = Integer.parseInt(ConfigHandler.ini.get("SniperRifle", "Damage"));
        Integer weaponFireRate = Integer.parseInt(ConfigHandler.ini.get("SniperRifle", "FireRate"));
        Integer weaponCapacity = Integer.parseInt(ConfigHandler.ini.get("SniperRifle", "Capacity"));
        Integer reloadTime = Integer.parseInt(ConfigHandler.ini.get("SniperRifle", "ReloadTime"));
        return new WeaponProperties(weaponDamage, weaponFireRate, weaponCapacity, reloadTime);
    }

    WeaponProperties getRocketLauncherProperties() {
        Integer weaponDamage = Integer.parseInt(ConfigHandler.ini.get("Rocketlauncher", "Damage"));
        Integer weaponFireRate = Integer.parseInt(ConfigHandler.ini.get("Rocketlauncher", "FireRate"));
        Integer weaponCapacity = Integer.parseInt(ConfigHandler.ini.get("Rocketlauncher", "Capacity"));
        Integer reloadTime = Integer.parseInt(ConfigHandler.ini.get("Rocketlauncher", "ReloadTime"));
        return new WeaponProperties(weaponDamage, weaponFireRate, weaponCapacity, reloadTime);
    }

    PlayerProperties getPlayerProperties() {
        Integer playerStartingHealth = Integer.parseInt(ConfigHandler.ini.get("Player", "StartingHealth"));
        Integer playerSpeed = Integer.parseInt(ConfigHandler.ini.get("Player", "Speed"));
        return new PlayerProperties(playerStartingHealth, playerSpeed);
    }

    ZombieProperties getOrdinaryZombieProperties() {
        Integer zombieHealth = Integer.parseInt(ConfigHandler.ini.get("OrdinaryZombie", "Health"));
        Integer zombieSpeed = Integer.parseInt(ConfigHandler.ini.get("OrdinaryZombie", "Speed"));
        Integer zombieDamage = Integer.parseInt(ConfigHandler.ini.get("OrdinaryZombie", "Damage"));
        Integer zombieRange = Integer.parseInt(ConfigHandler.ini.get("OrdinaryZombie", "Range"));
        return new ZombieProperties(zombieHealth, zombieSpeed, zombieDamage, zombieRange);
    }

    ZombieProperties getCrawlerZombieProperties() {
        Integer zombieHealth = Integer.parseInt(ConfigHandler.ini.get("CrawlerZombie", "Health"));
        Integer zombieSpeed = Integer.parseInt(ConfigHandler.ini.get("CrawlerZombie", "Speed"));
        Integer zombieDamage = Integer.parseInt(ConfigHandler.ini.get("CrawlerZombie", "Damage"));
        Integer zombieRange = Integer.parseInt(ConfigHandler.ini.get("CrawlerZombie", "Range"));
        return new ZombieProperties(zombieHealth, zombieSpeed, zombieDamage, zombieRange);
    }

    ZombieProperties getTankZombieProperties() {
        Integer zombieHealth = Integer.parseInt(ConfigHandler.ini.get("TankZombie", "Health"));
        Integer zombieSpeed = Integer.parseInt(ConfigHandler.ini.get("TankZombie", "Speed"));
        Integer zombieDamage = Integer.parseInt(ConfigHandler.ini.get("TankZombie", "Damage"));
        Integer zombieRange = Integer.parseInt(ConfigHandler.ini.get("TankZombie", "Range"));
        return new ZombieProperties(zombieHealth, zombieSpeed, zombieDamage, zombieRange);
    }

    ZombieProperties getAcidZombieProperties() {
        Integer zombieHealth = Integer.parseInt(ConfigHandler.ini.get("AcidZombie", "Health"));
        Integer zombieSpeed = Integer.parseInt(ConfigHandler.ini.get("AcidZombie", "Speed"));
        Integer zombieDamage = Integer.parseInt(ConfigHandler.ini.get("AcidZombie", "Damage"));
        Integer zombieRange = Integer.parseInt(ConfigHandler.ini.get("AcidZombie", "Range"));
        return new ZombieProperties(zombieHealth, zombieSpeed, zombieDamage, zombieRange);
    }

    LevelProperties getLevel1Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level1", "WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level1", "OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level1", "CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level1", "TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level1", "AcidZombieCount"));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount);
    }

    LevelProperties getLevel2Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level2", "WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level2", "OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level2", "CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level2", "TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level2", "AcidZombieCount"));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount);
    }

    LevelProperties getLevel3Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level3", "WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level3", "OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level3", "CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level3", "TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level3", "AcidZombieCount"));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount);
    }

    LevelProperties getLevel4Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level4", "WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level4", "OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level4", "CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level4", "TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level4", "AcidZombieCount"));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount);
    }

    LevelProperties getLevel5Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level5", "WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level5", "OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level5", "CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level5", "TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level5", "AcidZombieCount"));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount);
    }

    LevelProperties getLevel6Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level6", "WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level6", "OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level6", "CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level6", "TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level6", "AcidZombieCount"));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount);
    }

    LevelProperties getLevel7Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level7", "WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level7", "OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level7", "CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level7", "TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level7", "AcidZombieCount"));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount);
    }

    LevelProperties getLevel8Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level8", "WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level8", "OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level8", "CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level8", "TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level8", "AcidZombieCount"));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount);
    }

    LevelProperties getLevel9Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level9", "WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level9", "OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level9", "CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level9", "TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level9", "AcidZombieCount"));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount);
    }

    LevelProperties getLevel10Properties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level10", "WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level10", "OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level10", "CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level10", "TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level10", "AcidZombieCount"));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount);
    }

    LevelProperties getLevel10PlusProperties() {
        Integer levelWaveDuration = Integer.parseInt(ConfigHandler.ini.get("Level10+", "WaveDuration"));
        Integer levelOrdinaryZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level10+", "OrdinaryZombieCount"));
        Integer levelCrawlerZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level10+", "CrawlerZombieCount"));
        Integer levelTankZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level10+", "TankZombieCount"));
        Integer levelAcidZombieCount = Integer.parseInt(ConfigHandler.ini.get("Level10+", "AcidZombieCount"));
        return new LevelProperties(levelWaveDuration, levelOrdinaryZombieCount, levelCrawlerZombieCount, levelTankZombieCount, levelAcidZombieCount);
    }
}
