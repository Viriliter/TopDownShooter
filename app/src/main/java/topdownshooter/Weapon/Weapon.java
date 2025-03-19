/*
 * @file Weapon.java
 * @brief This file defines the `Weapon` interface class.
 * 
 * This interface class defines basic behaviors and properties of different types 
 * of weapon.
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

package topdownshooter.Weapon;

import java.awt.Graphics;
import java.io.Serializable;

import topdownshooter.Weapon.Projectiles.Projectile;

/**
 * @interface Weapon
 * @brief Represents a Weapon entity in the game.
 *
 * This interface is implemented by all weapon types in the game and provides the 
 * necessary methods for interacting with the weapon's behavior, such as firing, 
 * reloading, and updating its state. 
 * It also provides methods for managing ammunition and determining weapon properties
 *  like fire rate and damage.
 *
 * @see Projectile
 */
public interface Weapon extends Serializable{
    /**
     * Fires the weapon, creating a projectile at the specified position and rotation.
     * 
     * @param x The x-coordinate of the firing position.
     * @param y The y-coordinate of the firing position.
     * @param r The angle between direction of gun and x-axis in radians.
     * @return The projectile created by the weapon's fire.
     */
    Projectile fire(int x, int y, double r);

    /**
     * Reloads the weapon, replenishing its ammunition.
     */
    void reload();

    /**
     * Updates the weapon's state, called every frame.
     */
    void update();

    /**
     * Draws the weapon on the screen at the specified position and rotation.
     * 
     * @param g The graphics context to use for drawing.
     * @param x The x-coordinate where the weapon will be drawn.
     * @param y The y-coordinate where the weapon will be drawn.
     * @param r The rotation angle of the weapon in radians.
     */
    void draw(Graphics g, int x, int y, double r);

    /**
     * Adds the specified number of magazines to the weapon's ammo supply.
     * 
     * @param magazineCount The number of magazines to add.
     */
    void addMagazine(int magazineCount);

    /**
     * Gets the current ammo count of the weapon.
     * 
     * @return The current ammo count.
     */
    int getAmmo();

    /**
     * Gets the maximum capacity of the weapon's magazine.
     * 
     * @return The magazine capacity.
     */
    int getMagazineCapacity();

    /**
     * Gets the number of magazines currently available for the weapon.
     * 
     * @return The number of magazines.
     */
    int getMagazineCount();

    /**
     * Gets the reload duration of the weapon, in milliseconds.
     * 
     * @return The reload duration.
     */
    int getReloadDuration();

    /**
     * Gets the fire rate of the weapon, in rounds per minute (RPM).
     * 
     * @return The fire rate.
     */
    int getFireRate();

    /**
     * Gets the damage dealt by the weapon with each shot.
     * 
     * @return The weapon's damage.
     */
    int getDamage();

    /**
     * Gets the type of the weapon.
     * 
     * @return The weapon's type.
     */
    WeaponType getType();
}

