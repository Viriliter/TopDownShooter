/*
 * @file Zombie.java
 * @brief This file defines the `Zombie` interface class.
 * 
 * This interface class defines basic behaviors and properties of different types 
 * of zombie.
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

package topdownshooter.Zombie;

import java.util.List;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import topdownshooter.Core.Position;
import topdownshooter.Core.RectangleBound;
import topdownshooter.Player.Loot;
import topdownshooter.Weapon.WeaponType;

/**
 * @interface Zombie
 * @brief Represents a Zombie entity in the game.
 *
 * This interface defines the key behaviors for zombies such as updating its state,
 * taking damage, drawing, and etc. An inherited child class should define its 
 * implementation to be Serializable.
 */
public interface Zombie extends Serializable{
    /**
     * Updates the zombie's state based on the player's position.
     *
     * This method is called to update its behavior according to position of the player.
     *
     * @param playerBound The player's bounding rectangle which represents player's position and its size.
     */
    public void update(RectangleBound playerBound);

    /**
     * Draws the zombie on the screen.
     *
     * @param g The Graphics object used for rendering the zombie.
     */
    public void draw(Graphics g);

    /**
     * Gets the bounding rectangle of the zombie.
     *
     * @return The bounding rectangle representing the zombie's position and size.
     */
    public Rectangle getBounds();
    
    /**
     * Gets the bounding rectangle of the zombie. It may be probably smaller than the actual bounds.
     *
     * @return The bounding rectangle representing the zombie's position and size.
     */
    public RectangleBound getTargetBounds();

    /**
     * Gets the points awarded for killing this zombie.
     *
     * @return The points value of the zombie.
     */
    public int getPoints();

    /**
     * Gets the current health of the zombie.
     *
     * @return The current health of the zombie.
     */
    public double getHealth();

    /**
     * Takes damage, and reduces the zombie's health.
     *
     * @param damage The amount of damage to be affected.
     * @return True if the zombie is still alive after taking damage, otherwise false.
     */
    public boolean takeDamage(double damage);

    /**
     * Gets the damage dealt by this zombie.
     *
     * @return The amount of damage the zombie deals.
     */
    public int giveDamage();

    /**
     * Gets the type of this zombie.
     *
     * @return The type of the zombie
     */
    public ZombieType getType();

    /**
     * Kills the zombie and returns loot based on the available weapons.
     *
     * @param weaponList The list of weapons that are available by player
     * @return The loot dropped by the zombie.
     */
    public Loot kill(List<WeaponType> weaponList);

    /**
     * Gets the current position of the zombie.
     *
     * @return The position of the zombie.
     */
    public Position getPosition();

    /**
     * Gets the X-coordinate of the zombie.
     *
     * @return The X-coordinate of the zombie.
     */
    public int getX();
    
        /**
     * Gets the Y-coordinate of the zombie.
     *
     * @return The Y-coordinate of the zombie.
     */
    public int getY();

    /**
     * Custom deserialization of the zombie object.
     *
     * @param in The ObjectInputStream for deserialization.
     * @throws IOException If an I/O error occurs during deserialization.
     * @throws ClassNotFoundException If the class of the object cannot be found.
     */
    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException;
}
