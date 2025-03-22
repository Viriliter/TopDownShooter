/*
 * @file Player.java
 * @brief This file defines the `Player` class.
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

package topdownshooter.Player;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import topdownshooter.Weapon.Weapon;
import topdownshooter.Core.ConfigHandler;
import topdownshooter.Core.Globals;
import topdownshooter.Core.RectangleBound;
import topdownshooter.Core.ConfigHandler.PlayerProperties;
import topdownshooter.Core.SequencialSoundFX;
import topdownshooter.Core.SpriteAnimation;
import topdownshooter.Core.SpriteAnimation.Offset;
import topdownshooter.Weapon.WeaponFactory;
import topdownshooter.Weapon.WeaponType;
import topdownshooter.Weapon.Projectiles.Projectile;

/**
 * The Player class represents the player in the game, handling the player's state,
 * inventory, health, weapons, and actions.
 * It extends {@link javax.swing.JPanel} to render the player's visual representation.
 */
public class Player extends JPanel {
    /**
     * @enum PlayerState
     * @brief Enumeration for defining the types of player action.
     * 
     * The available item types are:
     * - IDLE: Player is idle, not moving or shooting
     * - MOVE: Player is moving
     * - SHOOT: Player is shooting.
     */
    private enum PlayerState {
        IDLE,
        MOVE,
        SHOOT,
    }

    private int score = 0;                                          // Player's score
    private double health = 0.0;                                    // Player's health
    private int x, y, dx, dy;                                       // Player's position and movement direction
    private double r;                                               // Rotation angle of the player (in radians)
    private int speed;                                              // Player's movement speed
    private final int WIDTH = 80;                                   // Width of the player's rectangle
    private final int HEIGHT = 68;                                  // Height of the player's rectangle
    private LinkedHashMap<WeaponType, Weapon> inventory;            // Weapon inventory of the player
    private WeaponType currentWeaponType = WeaponType.UNDEFINED;    // Current weapon type

    private transient Map<WeaponType, Map<PlayerState, SpriteAnimation>> spriteAnimations = null;    // Weapon state animations
    private transient SequencialSoundFX walkSoundFX = null;         // Sound effect for walking
    
    // Default constructor
    public Player() {}

    /**
     * Constructor to create a player with specified properties.
     *
     * @param properties The properties used to configure the players's inital health, speed.
     */
    public Player(ConfigHandler config) {
        PlayerProperties playerProperties = config.getPlayerProperties();
        this.x = playerProperties.startingX();
        this.y = playerProperties.startingY();
        this.r = 0;
        this.dx = 0;
        this.dy = 0;
        this.health = playerProperties.startingHealth();
        this.speed = playerProperties.speed();
        this.score = 0;

        // Initialize sprite animations
        createSpriteAnimations();

        this.inventory = new LinkedHashMap<WeaponType, Weapon>();
        // Every player starts with a pistol
        addNewWeapon(config, WeaponType.PISTOL);
        //addNewWeapon(config, WeaponType.ASSAULTRIFLE);
        //addNewWeapon(config, WeaponType.SHOTGUN);
        //addNewWeapon(config, WeaponType.SNIPERRIFLE);
        //addNewWeapon(config, WeaponType.ROCKETLAUNCHER);

        // Initialize walking sound effects
        this.walkSoundFX = new SequencialSoundFX(Globals.HUNTER_SOUND_FX_PATH);
    }

    
    /**
     *  Method to create the sprite animations for different weapons and states
     * 
     */
    public void createSpriteAnimations() {
        this.spriteAnimations = new HashMap<>();

        Map<PlayerState, SpriteAnimation> pistolAnimations = new HashMap<>();
        pistolAnimations.put(PlayerState.IDLE, new SpriteAnimation(Globals.HUNTER_PISTOL_IDLE));
        pistolAnimations.put(PlayerState.MOVE, new SpriteAnimation(Globals.HUNTER_PISTOL_MOVE));
        pistolAnimations.put(PlayerState.SHOOT, new SpriteAnimation(Globals.HUNTER_PISTOL_SHOOT));
        this.spriteAnimations.put(WeaponType.PISTOL, pistolAnimations);

        Map<PlayerState, SpriteAnimation> assultRifleAnimations = new HashMap<>();
        assultRifleAnimations.put(PlayerState.IDLE, new SpriteAnimation(Globals.HUNTER_RIFLE_IDLE));
        assultRifleAnimations.put(PlayerState.MOVE, new SpriteAnimation(Globals.HUNTER_RIFLE_MOVE));
        assultRifleAnimations.put(PlayerState.SHOOT, new SpriteAnimation(Globals.HUNTER_RIFLE_SHOOT));
        this.spriteAnimations.put(WeaponType.ASSAULTRIFLE, assultRifleAnimations);

        Map<PlayerState, SpriteAnimation> shotgunAnimations = new HashMap<>();
        shotgunAnimations.put(PlayerState.IDLE, new SpriteAnimation(Globals.HUNTER_SHOTGUN_IDLE));
        shotgunAnimations.put(PlayerState.MOVE, new SpriteAnimation(Globals.HUNTER_SHOTGUN_MOVE));
        shotgunAnimations.put(PlayerState.SHOOT, new SpriteAnimation(Globals.HUNTER_SHOTGUN_SHOOT));
        this.spriteAnimations.put(WeaponType.SHOTGUN, shotgunAnimations);

        Map<PlayerState, SpriteAnimation> sniperRifleAnimations = new HashMap<>();
        sniperRifleAnimations.put(PlayerState.IDLE, new SpriteAnimation(Globals.HUNTER_RIFLE_IDLE));
        sniperRifleAnimations.put(PlayerState.MOVE, new SpriteAnimation(Globals.HUNTER_RIFLE_MOVE));
        sniperRifleAnimations.put(PlayerState.SHOOT, new SpriteAnimation(Globals.HUNTER_RIFLE_SHOOT));
        this.spriteAnimations.put(WeaponType.SNIPERRIFLE, sniperRifleAnimations);
        
        Map<PlayerState, SpriteAnimation> rocketLauncherAnimations = new HashMap<>();
        rocketLauncherAnimations.put(PlayerState.IDLE, new SpriteAnimation(Globals.HUNTER_ROCKET_LAUNCHER_MOVE));
        rocketLauncherAnimations.put(PlayerState.MOVE, new SpriteAnimation(Globals.HUNTER_ROCKET_LAUNCHER_IDLE));
        rocketLauncherAnimations.put(PlayerState.SHOOT, new SpriteAnimation(Globals.HUNTER_ROCKET_LAUNCHER_SHOOT));
        this.spriteAnimations.put(WeaponType.ROCKETLAUNCHER, rocketLauncherAnimations);

        for (Map<PlayerState, SpriteAnimation> weaponAnimation : spriteAnimations.values()) {
            for (SpriteAnimation stateAnimation : weaponAnimation.values()) {
                stateAnimation.setTargetSize(WIDTH, HEIGHT);
            }
        }
    }

    /**
     * Set the rotation angle of the player.
     * @param rRad The rotation angle in radians.
     */
    public void rotate(double rRad) {
        this.r = rRad;
    }

    /**
     * Update the player's location, weapons, and sprite animations based on movement.
     * @param maxWidth The maximum width of the screen.
     * @param maxHeight The maximum height of the screen.
     */
    public void update(final int maxWidth, final int maxHeight) {
        // Update location
        this.x = this.x + this.dx > maxWidth-WIDTH ? maxWidth-WIDTH : this.x + this.dx;
        this.x = this.x + this.dx < 0 ? 0 : this.x + this.dx;
        this.y = this.y + this.dy > maxHeight-HEIGHT ? maxHeight-HEIGHT : this.y + this.dy;
        this.y = this.y + this.dy < 0 ? 0 : this.y + this.dy;

        // Update weapons
        for (Weapon w : this.inventory.values()) {
            w.update();
        }

        // Update sprite animation
        WeaponType currentWeaponType = this.inventory.get(this.currentWeaponType).getType();
        if (currentWeaponType != WeaponType.UNDEFINED) this.spriteAnimations.get(currentWeaponType).get(PlayerState.IDLE).update();
    }

    /**
     * Draws the player on the screen.
     * 
     * @param g The graphics context to use for drawing.
     */
    public void draw(Graphics g) {
        // Draw sprite animations of player according to weapon type
        PlayerState playerState = PlayerState.IDLE;
        WeaponType currentWeaponType = this.inventory.get(this.currentWeaponType).getType();

        if (currentWeaponType == WeaponType.UNDEFINED) return;

        this.spriteAnimations.get(currentWeaponType).get(playerState).draw(g, this.x, this.y, this.r);

        // Draw weapon animation
        this.inventory.get(this.currentWeaponType).draw(g, this.x + WIDTH / 2, this.y + HEIGHT / 2, this.r);

    }

    /**
     * Decrease the player's horizontal speed and update sound effect.
     */
    public void decrementDx() { this.dx = -this.speed; this.walkSoundFX.update();}

    /**
     * Increase the player's horizontal speed and update sound effect.
     */
    public void incrementDx() { this.dx = this.speed; this.walkSoundFX.update();}
    
    /**
     * Decrease the player's vertical speed and update sound effect.
     */
    public void decrementDy() { this.dy = -this.speed; this.walkSoundFX.update();}

    /**
     * Increase the player's vertical speed and update sound effect.
     */
    public void incrementDy() { this.dy = +this.speed; this.walkSoundFX.update();}

    /**
     * Set the horizontal speed of the player.
     * @param dx The horizontal speed to set.
     */
    public void setDx(int dx) { this.dx = dx; }

    /**
     * Set the vertical speed of the player.
     * @param dy The vertical speed to set.
     */
    public void setDy(int dy) { this.dy = dy; }
    
    /**
     * Get the player's X coordinate (center).
     * @return The X coordinate of the player.
     */
    public int getX() { return this.x + WIDTH / 2; }
    
    /**
     * Get the player's Y coordinate (center).
     * @return The Y coordinate of the player.
     */
    public int getY() { return this.y + HEIGHT / 2; }
    
    /**
     * Get the player's rotation angle.
     * @return The rotation angle of the player in radians.
     */
    public double getR() { return this.r; }

    /**
     * Move the player along the X axis based on the current speed.
     */
    public void moveX() { this.x += this.dx;}

    /**
     * Move the player along the Y axis based on the current speed.
     */
    public void moveY() { this.y += this.dy;}

    /**
     * Heal the player by a specified number of points.
     * @param healPoints The number of health points to heal.
     */
    public void heal(int healPoints) {this.health = this.health+healPoints > 100 ? 100: this.health+healPoints;}

    /**
     * Get the current health of the player.
     * @return The player's health.
     */
    public double getHealth() {return this.health;}

    /**
     * Add score points to the player, ensuring non-negative points.
     * @param points The number of points to add to the score.
     */
    public void addScore(int points) {
        this.score += points<0 ? 0 : points;
    }

    /**
     * Add ammunition to a specific weapon type in the player's inventory.
     * @param type The weapon type to add ammunition to.
     * @param magazineCount The number of magazines to add.
     */
    public void addAmmo(WeaponType type, int magazineCount) {
        for (Weapon w: this.inventory.values()) {
            if (w.getType() == type) {
                w.addMagazine(magazineCount);
            }
        }
    }
    /**
     * 
     * Adds loot to the player based on the item type in the loot.
     * - If the item is of type AMMUNITION, the player's ammo is updated.
     * - If the item is of type SMALL_MEDIC_PACK or LARGE_MEDIC_PACK, the player is healed.
     * @param loot The loot object containing the item to add.
     */
    public void addLoot(Loot loot) {
        if (loot == null) return;

        PlayerItem item = loot.getItem();

        if (item == null) return;

        switch(item.getItemType()) {
            case AMMUNITION:
                AmmunitionItem ammo = (AmmunitionItem) item;
                addAmmo(ammo.type, ammo.magazineCount);
                break;
            case SMALL_MEDIC_PACK:
                SmallMedicPackItem smallMedicPack = (SmallMedicPackItem) item;
                heal(smallMedicPack.headlingPoints);
                break;
            case LARGE_MEDIC_PACK:
                LargeMedicPackItem largeMedicPack = (LargeMedicPackItem) item;
                heal(largeMedicPack.headlingPoints);
                break;
            default:
                return;
        }

    }

    /**
     * Returns the current score of the player.
     * @return The player's score.
     */
    public int getScore() {return this.score;}
    
    /**
     * Reduces the player's health by the specified damage value.
     * If the resulting health is less than or equal to 0, the health is set to 0.
     * @param damage The amount of damage to apply to the player.
     */
    public void takeDamage(double damage) {this.health = this.health-damage <= 0 ? 0.0: this.health-damage;}

    /**
     * Switches the player's current weapon to the next available weapon in the inventory.
     * The weapons are cycled in a circular manner (after the last weapon, it goes back to the first).
     */
    public void switchWeapon() {
        List<WeaponType> availableWeapons = new ArrayList<>(this.inventory.keySet());
        int currentWeaponIndex = availableWeapons.indexOf(this.currentWeaponType);
        int nextWeaponIndex = (currentWeaponIndex + 1) % availableWeapons.size();

        this.currentWeaponType = availableWeapons.get(nextWeaponIndex);
    }
    
    /**
     * Returns the current weapon equipped by the player.
     * @return The current weapon.
     */
    public Weapon getCurrentWeapon() {return this.inventory.get(currentWeaponType);}

    /**
     * Fires projectile from the current weapon.
     * Calculates the projectile's firing position and angle based on the player's rotation and weapon offset.
     * @return The projectile fired by the current weapon.
     */
    public Projectile fire() {
        PlayerState playerState = PlayerState.IDLE;
        WeaponType currentWeaponType = this.inventory.get(this.currentWeaponType).getType();
        Offset offset = this.spriteAnimations.get(currentWeaponType).get(playerState).getOffset();

        double translatedX = this.x + WIDTH / 2 + offset.getX() * Math.cos(this.r) - offset.getY() * Math.sin(this.r);
        double translatedY = this.y + HEIGHT / 2 + offset.getX() * Math.sin(this.r) + offset.getY() * Math.cos(this.r);
        return this.getCurrentWeapon().fire((int) translatedX, (int) translatedY, this.r);
    }

    /**
     * Adds a new weapon to the player's inventory if the weapon type is not already present.
     * If the inventory is empty, the new weapon will also become the current weapon.
     * @param config The configuration handler used to create the weapon.
     * @param type The type of the weapon to add.
     * @return True if the weapon was successfully added, false if the weapon type already exists.
     */
    public boolean addNewWeapon(ConfigHandler config, WeaponType type) {
        // Only if that type is not available in the inventory, add the weapon 
        for (Weapon weaponItem: this.inventory.values()) {
            if (weaponItem.getType() == type)
                return false;
        }

        Weapon weapon = WeaponFactory.createWeapon(config, type);
        if (this.inventory.isEmpty() && weapon!=null) this.currentWeaponType = weapon.getType();
        if (weapon!=null) this.inventory.put(weapon.getType(), weapon);

        return true;
    }

    /**
     * Returns an InventoryInfo object containing details of the player's inventory.
     * Includes ammo and magazine counts for each weapon type.
     * @return The player's inventory information.
     */
    public InventoryInfo getInventoryInfo() {
        InventoryInfo inventoryInfo = new InventoryInfo();
        
        inventoryInfo.selectedWeaponType = this.currentWeaponType;

        for (Weapon weapon: this.inventory.values()) {
            switch (weapon.getType()) {
                case UNDEFINED:
                    break;
                case PISTOL:
                    inventoryInfo.pistolAmmo = weapon.getAmmo();
                    inventoryInfo.pistolMagazine = weapon.getMagazineCount();
                    break;
                case ASSAULTRIFLE:
                    inventoryInfo.assaultRifleAmmo = weapon.getAmmo();
                    inventoryInfo.assaultRifleMagazine = weapon.getMagazineCount();
                    break;
                case SHOTGUN:
                    inventoryInfo.shotgunAmmo = weapon.getAmmo();
                    inventoryInfo.shotgunMagazine = weapon.getMagazineCount();
                    break;
                case SNIPERRIFLE:
                    inventoryInfo.sniperRifleAmmo = weapon.getAmmo();
                    inventoryInfo.sniperRifleMagazine = weapon.getMagazineCount();
                    break;
                case ROCKETLAUNCHER:
                    inventoryInfo.rocketLauncherAmmo = weapon.getAmmo();
                    inventoryInfo.rocketLauncherMagazine = weapon.getMagazineCount();
                    break;
                default:
                    break;
            }
        }

        return inventoryInfo;
    }

    /**
     * Returns a list of the available weapon types in the player's inventory.
     * @return A list of weapon types.
     */
    public List<WeaponType> getAvailableWeapons() {
        List<WeaponType> weaponTypes = new ArrayList<>();

        for(Weapon weapon: this.inventory.values()) {
            weaponTypes.add(weapon.getType());
        }
        return weaponTypes;
    }

    /**
     * Returns a rectangle representing the bounds of the player.
     * 
     * @return A rectangle representing the player's bounds.
     */
    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, this.WIDTH, this.HEIGHT);
    }

    /**
     * Returns a rectangle representing the target bounds of the player.
     * The size of this rectangle is slightly smaller than the player's bounds.
     * @return A rectangle representing the target bounds.
     */
    public RectangleBound getTargetBounds() {
        double width = this.WIDTH * 0.8;
        double height = this.HEIGHT * 0.8;

        return new RectangleBound(this.x + 6, this.y + 10, (int) width, (int) height, this.r);
    }

    /**
     * Returns a string representation of the player, including score, health, position, and inventory details.
     * @return A string representation of the player.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player{");
        sb.append("score=" + this.score + ", ");
        sb.append("health=" + this.health + ", ");
        sb.append("x=" + this.x + ", ");
        sb.append("y=" + this.y + ", ");
        sb.append("dx=" + this.dx + ", ");
        sb.append("dy=" + this.dy + ", ");
        sb.append("r=" + this.r + ", ");
        sb.append("speed=" + this.speed + ", ");
        sb.append("inventory=" + this.inventory + ", ");
        sb.append("currentWeaponType=" + this.currentWeaponType);
        sb.append("}");

        return sb.toString();
    }

    /**
     * Reads the player's object data from the input stream.
     * Initializes sprite animations and sound effects after deserialization.
     * @param in The input stream to read the player's object data from.
     * @throws IOException If an I/O error occurs during reading.
     * @throws ClassNotFoundException If the class definition cannot be found.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        createSpriteAnimations();

        this.walkSoundFX = new SequencialSoundFX(Globals.HUNTER_SOUND_FX_PATH);
    }
}
