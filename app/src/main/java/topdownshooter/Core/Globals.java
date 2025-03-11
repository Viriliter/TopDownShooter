package topdownshooter.Core;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public interface Globals {
    public static final String CONFIGURATION_FILE = "config.ini";
    public static final int GAME_TICK_MS = 10;
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final int WAVE_SUSPEND_DURATION_MS = 5000;
    public static final int FULL_DAMAGE_PERIOD = 500;  // Period of zombie gives full damage if attacks continue  

    public static int FRAME_DELAY = 5;

    public static final String PLAYGROUND_TILE_PATH = "Textures/ground.jpg";

    // Path of In-Game Icons
    public static final String ICON_PATH_ASSULT_RIFLE_INACTIVE = "Icons/assult-rifle-inactive.png";
    public static final String ICON_PATH_ASSULT_RIFLE_UNSELECTED = "Icons/assult-rifle-selected.png";
    public static final String ICON_PATH_ASSULT_RIFLE_SELECTED = "Icons/assult-rifle-unselected.png";
    public static final String ICON_PATH_PISTOL_INACTIVE = "Icons/pistol-inactive.png";
    public static final String ICON_PATH_PISTOL_UNSELECTED = "Icons/pistol-selected.png";
    public static final String ICON_PATH_PISTOL_SELECTED = "Icons/pistol-unselected.png";
    public static final String ICON_PATH_ROCKET_LAUNCHER_INACTIVE = "Icons/rocket-launcher-inactive.png";
    public static final String ICON_PATH_ROCKET_LAUNCHER_UNSELECTED = "Icons/rocket-launcher-selected.png";
    public static final String ICON_PATH_ROCKET_LAUNCHER_SELECTED = "Icons/rocket-launcher-unselected.png";
    public static final String ICON_PATH_SHOTGUN_INACTIVE = "Icons/shotgun-inactive.png";
    public static final String ICON_PATH_SHOTGUN_UNSELECTED = "Icons/shotgun-selected.png";
    public static final String ICON_PATH_SHOTGUN_SELECTED = "Icons/shotgun-unselected.png";
    public static final String ICON_PATH_SNIPER_RIFLE_INACTIVE = "Icons/sniper-rifle-inactive.png";
    public static final String ICON_PATH_SNIPER_RIFLE_UNSELECTED = "Icons/sniper-rifle-selected.png";
    public static final String ICON_PATH_SNIPER_RIFLE_SELECTED = "Icons/sniper-rifle-unselected.png";


    public static final SpriteAnimationStruct HUNTER_PISTOL_IDLE = new SpriteAnimationStruct("Textures/Survivor/Pistol/survivor_pistol_idle.png", 20, FRAME_DELAY, 5, 4, 30, 12);
    public static final SpriteAnimationStruct HUNTER_PISTOL_MOVE = new SpriteAnimationStruct("Textures/Survivor/Pistol/survivor_pistol_move.png", 20, FRAME_DELAY, 3, 7, 30, 12);
    public static final SpriteAnimationStruct HUNTER_PISTOL_SHOOT = new SpriteAnimationStruct("Textures/Survivor/Pistol/survivor_pistol_shoot.png", 3, FRAME_DELAY, 1, 3, 30, 12);
    public static final SpriteAnimationStruct HUNTER_RIFLE_IDLE = new SpriteAnimationStruct("Textures/Survivor/Rifle/survivor_rifle_idle.png", 20, FRAME_DELAY, 5, 4, 30, 12);
    public static final SpriteAnimationStruct HUNTER_RIFLE_MOVE = new SpriteAnimationStruct("Textures/Survivor/Rifle/survivor_rifle_move.png", 20, FRAME_DELAY, 3, 7, 30, 12);
    public static final SpriteAnimationStruct HUNTER_RIFLE_SHOOT = new SpriteAnimationStruct("Textures/Survivor/Rifle/survivor_rifle_shoot.png", 3, FRAME_DELAY, 1, 3, 30, 12);
    public static final SpriteAnimationStruct HUNTER_SHOTGUN_IDLE = new SpriteAnimationStruct("Textures/Survivor/Shotgun/survivor_shotgun_idle.png", 20, FRAME_DELAY, 5, 4, 30, 12);
    public static final SpriteAnimationStruct HUNTER_SHOTGUN_MOVE = new SpriteAnimationStruct("Textures/Survivor/Shotgun/survivor_shotgun_move.png", 20, FRAME_DELAY, 3, 7, 30, 12);
    public static final SpriteAnimationStruct HUNTER_SHOTGUN_SHOOT = new SpriteAnimationStruct("Textures/Survivor/Shotgun/survivor_shotgun_shoot.png", 3, FRAME_DELAY, 1, 3, 30, 12);

    public static final SpriteAnimationStruct ORDINARY_ZOMBIE_MOVE = new SpriteAnimationStruct("Textures/Zombie/ordinary_zombie_move.png", 17, FRAME_DELAY, 6, 3, 30, 12);
    public static final SpriteAnimationStruct ORDINARY_ZOMBIE_ATTACK = new SpriteAnimationStruct("Textures/Zombie/ordinary_zombie_attack.png", 9, FRAME_DELAY, 3, 3, 30, 12);
    public static final SpriteAnimationStruct ACID_ZOMBIE_MOVE = new SpriteAnimationStruct("Textures/Zombie/acid_zombie_move.png", 17, FRAME_DELAY, 6, 3, 30, 12);
    public static final SpriteAnimationStruct ACID_ZOMBIE_ATTACK = new SpriteAnimationStruct("Textures/Zombie/acid_zombie_attack.png", 9, FRAME_DELAY, 3, 3, 30, 12);

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
}
