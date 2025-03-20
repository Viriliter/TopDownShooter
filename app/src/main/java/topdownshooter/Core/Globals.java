/*
 * @file Globals.java
 * @brief This file defines the `Globals` class.
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

import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public interface Globals {
    /**
     * IN-GAME CONSTANTS
     */
    public static final String CONFIGURATION_FILE = "config.ini";
    public static final int GAME_TICK_MS = 10;

    public static final String GAME_TITLE = "Survival Echo";
    public static final int WINDOW_WIDTH = 1600;  // Default: 1600px
    public static final int WINDOW_HEIGHT = (int) ((double) WINDOW_WIDTH * 0.625);  // Default: 1000px
    public static final int PLAYER_WIDTH = (int) ((WINDOW_WIDTH * 0.05));  // Default: 80px
    public static final int PLAYER_HEIGHT = (int) ((double) PLAYER_WIDTH * 0.85);  // Default: 68px
    public static final int ZOMBIE_WIDTH = (int) ((WINDOW_WIDTH * 0.05));  // Default: 80px
    public static final int ZOMBIE_HEIGHT = (int) ((double) ZOMBIE_WIDTH * 0.85);  // Default: 68px

    public static final int NOTIFICATION_WINDOW_WIDTH = WINDOW_WIDTH / 4;  // Default: 400px
    public static final int NOTIFICATION_WINDOW_HEIGHT = (int) ((double) NOTIFICATION_WINDOW_WIDTH * 0.375);  // Default: 150px

    public static final int BULLET_SIZE = 5;  // Default: 5px

    public static final int WAVE_SUSPEND_DURATION_MS = 5000;
    public static final int FULL_DAMAGE_PERIOD = 500;  // Period of zombie gives full damage if attacks continue  

    public static final int MAX_LOOT_DURATION = 30000;  // Maximum duration (in miliseconds) of loot objects are visible after it is dropped

    public static final int FRAME_DELAY = 5;

    public static final String WALLPAPER_PATH = "wallpaper.png";

    /**
     * PLAYGROUND TEXTURES
     */
    public static final String PLAYGROUND_TILE_PATH = "Textures/ground2.png";

    /**
     * ICONS
     */
    public static final String ICON_PATH_BULLET = "Icons/bullet.png";
    public static final String ICON_PATH_MAGAZINE = "Icons/magazine.png";

    public static final String ICON_PATH_PISTOL_EMPTY = "Icons/pistol-empty.png";
    public static final String ICON_PATH_PISTOL_UNSELECTED = "Icons/pistol-unselected.png";
    public static final String ICON_PATH_PISTOL_SELECTED = "Icons/pistol-selected.png";
    public static final String ICON_PATH_ASSAULT_RIFLE_EMPTY = "Icons/assaultrifle-empty.png";
    public static final String ICON_PATH_ASSAULT_RIFLE_UNSELECTED = "Icons/assaultrifle-unselected.png";
    public static final String ICON_PATH_ASSAULT_RIFLE_SELECTED = "Icons/assaultrifle-selected.png";
    public static final String ICON_PATH_SHOTGUN_EMPTY = "Icons/shotgun-empty.png";
    public static final String ICON_PATH_SHOTGUN_UNSELECTED = "Icons/shotgun-unselected.png";
    public static final String ICON_PATH_SHOTGUN_SELECTED = "Icons/shotgun-selected.png";
    public static final String ICON_PATH_SNIPER_RIFLE_EMPTY = "Icons/sniperrifle-empty.png";
    public static final String ICON_PATH_SNIPER_RIFLE_UNSELECTED = "Icons/sniperrifle-unselected.png";
    public static final String ICON_PATH_SNIPER_RIFLE_SELECTED = "Icons/sniperrifle-selected.png";
    public static final String ICON_PATH_ROCKET_LAUNCHER_EMPTY = "Icons/rocketlauncher-empty.png";
    public static final String ICON_PATH_ROCKET_LAUNCHER_UNSELECTED = "Icons/rocketlauncher-unselected.png";
    public static final String ICON_PATH_ROCKET_LAUNCHER_SELECTED = "Icons/rocketlauncher-selected.png";

    /**
     * CHARACTER TEXTURES
     */
    public static final SpriteAnimationStruct HUNTER_PISTOL_IDLE = new SpriteAnimationStruct("Textures/Survivor/Pistol/survivor_pistol_idle.png", 20, FRAME_DELAY, 5, 4, 36, 15);
    public static final SpriteAnimationStruct HUNTER_PISTOL_MOVE = new SpriteAnimationStruct("Textures/Survivor/Pistol/survivor_pistol_move.png", 20, FRAME_DELAY, 3, 7, 36, 15);
    public static final SpriteAnimationStruct HUNTER_PISTOL_SHOOT = new SpriteAnimationStruct("Textures/Survivor/Pistol/survivor_pistol_shoot.png", 3, FRAME_DELAY, 1, 3, 36, 15);
    public static final SpriteAnimationStruct HUNTER_RIFLE_IDLE = new SpriteAnimationStruct("Textures/Survivor/Rifle/survivor_rifle_idle.png", 20, FRAME_DELAY, 7, 3, 36, 15);
    public static final SpriteAnimationStruct HUNTER_RIFLE_MOVE = new SpriteAnimationStruct("Textures/Survivor/Rifle/survivor_rifle_move.png", 20, FRAME_DELAY, 7, 3, 36, 15);
    public static final SpriteAnimationStruct HUNTER_RIFLE_SHOOT = new SpriteAnimationStruct("Textures/Survivor/Rifle/survivor_rifle_shoot.png", 3, FRAME_DELAY, 1, 3, 36, 15);
    public static final SpriteAnimationStruct HUNTER_SHOTGUN_IDLE = new SpriteAnimationStruct("Textures/Survivor/Shotgun/survivor_shotgun_idle.png", 20, FRAME_DELAY, 7, 3, 30, 12);
    public static final SpriteAnimationStruct HUNTER_SHOTGUN_MOVE = new SpriteAnimationStruct("Textures/Survivor/Shotgun/survivor_shotgun_move.png", 20, FRAME_DELAY, 7, 3, 30, 12);
    public static final SpriteAnimationStruct HUNTER_SHOTGUN_SHOOT = new SpriteAnimationStruct("Textures/Survivor/Shotgun/survivor_shotgun_shoot.png", 3, FRAME_DELAY, 1, 3, 30, 12);
    public static final SpriteAnimationStruct HUNTER_ROCKET_LAUNCHER_IDLE = new SpriteAnimationStruct("Textures/Survivor/Rocket/survivor_rocket_idle.png", 20, FRAME_DELAY, 7, 3, 30, 12);
    public static final SpriteAnimationStruct HUNTER_ROCKET_LAUNCHER_MOVE = new SpriteAnimationStruct("Textures/Survivor/Rocket/survivor_rocket_move.png", 20, FRAME_DELAY, 7, 3, 30, 12);
    public static final SpriteAnimationStruct HUNTER_ROCKET_LAUNCHER_SHOOT = new SpriteAnimationStruct("Textures/Survivor/Rocket/survivor_rocket_shoot.png", 3, FRAME_DELAY, 1, 3, 30, 12);

    public static final SpriteAnimationStruct ORDINARY_ZOMBIE_MOVE = new SpriteAnimationStruct("Textures/Zombie/ordinary_zombie_move.png", 17, FRAME_DELAY, 6, 3, 30, 12);
    public static final SpriteAnimationStruct ORDINARY_ZOMBIE_ATTACK = new SpriteAnimationStruct("Textures/Zombie/ordinary_zombie_attack.png", 9, FRAME_DELAY, 3, 3, 30, 12);
    public static final SpriteAnimationStruct CRAWLER_ZOMBIE_MOVE = new SpriteAnimationStruct("Textures/Zombie/crawler_zombie_move.png", 23, FRAME_DELAY, 5, 5, 30, 12);
    public static final SpriteAnimationStruct CRAWLER_ZOMBIE_ATTACK = new SpriteAnimationStruct("Textures/Zombie/crawler_zombie_attack.png", 23, FRAME_DELAY, 5, 5, 30, 12);
    public static final SpriteAnimationStruct ACID_ZOMBIE_MOVE = new SpriteAnimationStruct("Textures/Zombie/acid_zombie_move.png", 17, FRAME_DELAY, 6, 3, 30, 12);
    public static final SpriteAnimationStruct ACID_ZOMBIE_ATTACK = new SpriteAnimationStruct("Textures/Zombie/acid_zombie_attack.png", 9, FRAME_DELAY, 3, 3, 30, 12);
    public static final SpriteAnimationStruct TANK_ZOMBIE_MOVE = new SpriteAnimationStruct("Textures/Zombie/tank_zombie_move.png", 25, FRAME_DELAY, 5, 5, 30, 12);
    public static final SpriteAnimationStruct TANK_ZOMBIE_ATTACK = new SpriteAnimationStruct("Textures/Zombie/tank_zombie_attack.png", 25, FRAME_DELAY, 5, 5, 30, 12);

    /**
     * MISCS TEXTURES
     */
    public static final SpriteAnimationStruct AMMO_ANIMATION = new SpriteAnimationStruct("Textures/Miscs/ammo.png", 1, FRAME_DELAY, 1, 1, 0, 0);
    public static final SpriteAnimationStruct LARGE_MEDIC_ANIMATION = new SpriteAnimationStruct("Textures/Miscs/large_medic_pack.png", 35, 1, 7, 5, 0, 0);
    public static final SpriteAnimationStruct SMALL_MEDIC_ANIMATION = new SpriteAnimationStruct("Textures/Miscs/small_medic_pack.png", 42, 1, 9, 5, 0, 0);

    public static final SpriteAnimationStruct MUZZLE_ANIMATION = new SpriteAnimationStruct("Textures/Miscs/fire.png", 16, FRAME_DELAY, 4, 4, 40, 12);

    public static final SpriteAnimationStruct EXPLOSIVE_BLAST_ANIMATION = new SpriteAnimationStruct("Textures/Miscs/explosive_blast.png", 96, 1, 20, 5, 0, 0);
    public static final SpriteAnimationStruct TOXIC_BLAST_ANIMATION = new SpriteAnimationStruct("Textures/Miscs/toxic_blast.png", 96, 1, 20, 5, 0, 0);

    public static final String BULLET_TEXTURE_PATH = "Textures/Miscs/bullet.png";
    public static final String ROCKET_TEXTURE_PATH = "Textures/Miscs/rocket_shell.png";
    public static final String ACID_SPIT_TEXTURE_PATH = "Textures/Miscs/acid_spit.png";
    public static final String PROJECTILE_TEXTURE_PATH = "Textures/Miscs/projectile.png";

    /**
     * SOUND EFFECTS
     */
    public static final String MENU_MUSIC_PATH = "Sounds/SurvivalEchoes.wav";

    public static final List<String> HUNTER_SOUND_FX_PATH = Arrays.asList("Sounds/Survivor/step1.wav",
                                                                               "Sounds/Survivor/step2.wav",
                                                                               "Sounds/Survivor/step3.wav",
                                                                               "Sounds/Survivor/step4.wav",
                                                                               "Sounds/Survivor/step5.wav");

    public static final String FIRE_PISTOL_SOUND_FX_PATH = "Sounds/Weapons/fire-pistol.wav";
    public static final String FIRE_RIFLE_SOUND_FX_PATH = "Sounds/Weapons/fire-rifle.wav";
    public static final String FIRE_SHOTGUN_SOUND_FX_PATH = "Sounds/Weapons/fire-shotgun.wav";
    public static final String FIRE_SNIPER_RIFLE_SOUND_FX_PATH = "Sounds/Weapons/fire-sniper-rifle.wav";
    public static final String FIRE_ROCKET_LAUNCHER_SOUND_FX_PATH = "Sounds/Weapons/fire-rocket-launcher.wav";
    public static final String EMPTY_GUN_CLICK_SOUND_FX_PATH = "Sounds/Weapons/empty-gun-click.wav";
    
    public static final String BACKGROUND_SOUND_FX_PATH = "Sounds/Background/wind.wav";

    /**
     * UTILITY FUNCTIONS
     */
    static int Time2GameTick(int durationMs) {
        return durationMs / GAME_TICK_MS;
    }

    static int GameTick2Time(int tick) {
        return tick * GAME_TICK_MS;
    }

    static ImageIcon loadPNGIcon(String path, int targetWidth, int targetHeight) {
        InputStream inputStream = Globals.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            System.err.println("PNG file not found: " + path);
            return null;
        }

        try {
            ImageIcon icon = new ImageIcon(inputStream.readAllBytes());
            Image image = icon.getImage().getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);  // Resize image
            return new ImageIcon(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static Image loadTexture(String fileName) {
        try {
            InputStream inputStream = Globals.class.getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                System.err.println("Texture file not found: " + fileName);
                return null;
            }
            return ImageIO.read(inputStream);  // Load as BufferedImage
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double radToDeg(double radians) {
        return Math.toDegrees(radians);
    }

    // Convert degrees to radians
    public static double degToRad(double degrees) {
        return Math.toRadians(degrees);
    }

    public static boolean isObjectsCollided(Rectangle rect1, Rectangle rect2) {
        return rect1.getBounds().intersects(rect2.getBounds()) || 
               rect2.getBounds().contains(rect1.getBounds()) || 
               rect1.getBounds().contains(rect2.getBounds());
    }
}
