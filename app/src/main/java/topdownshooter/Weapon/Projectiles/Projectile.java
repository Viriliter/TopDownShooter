/*
 * @file Projectile.java
 * @brief This file defines the `Projectile` class.
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

package topdownshooter.Weapon.Projectiles;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import topdownshooter.Core.TextureFX;
/**
 * @class Projectile
 * @brief This abstract class represents a projectile in the game. It is a base class for different types of projectiles 
 *        (e.g., bullets, shotgun pellets) and defines basic properties and behaviors that all projectiles share.
 * 
 * The class includes properties for position, rotation, damage, speed, and type of the projectile. It also provides methods
 * to move the projectile, check for out-of-bounds conditions, and retrieve its collision bounds. Subclasses must define 
 * their own drawing behavior via the abstract `draw()` method.
 * 
 * @see Bullet
 * @see ShotgunPellets
 * @see ArmorPiercingBullet
 * @see Rocket
 * @see AcidSpit
 * @see ProjectileType
 */
public abstract class Projectile implements Serializable{
    protected int x, y;                                         // Position of the projectile
    protected double r;                                         // Rotation angle of the projectile in radians
    protected int damage = 0;                                   // Damage dealt by the projectile
    public static final int size = 5;                           // Size of the projectile
    protected int speed = 30;                                   // Speed of the projectile 
                                                                // (It should be meaningful value for collision detection)
    protected ProjectileType type = ProjectileType.UNDEFINED;   // Type of the projectile (default: UNDEFINED)

    protected transient TextureFX projectileEffect = null;      // Visual effect of the projectile 

    /**
     * Default constructor for subclasses.
     */
    public Projectile() {

    }

    /**
     * Constructor to create a projectile with specified position, rotation, and damage.
     * 
     * @param x The x-coordinate of the projectile.
     * @param y The y-coordinate of the projectile.
     * @param r The rotation angle of the projectile in radians.
     * @param damage The damage dealt by the projectile.
     */
    public Projectile(int x, int y, double r, int damage) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.damage = damage;
    }

    /**
     * Getter for the x-coordinate of the projectile.
     * 
     * @return The x-coordinate of the projectile.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Getter for the y-coordinate of the projectile.
     * 
     * @return The y-coordinate of the projectile.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Moves the projectile based on its speed and rotation.
     * Updates its position by adding velocity components to the x and y coordinates.
     * Also updates the projectile's effect if it has one.
     */
    public void move() {
        x += (int) (this.speed * Math.cos(this.r));
        y += (int) (this.speed * Math.sin(this.r));

        if (this.projectileEffect != null) this.projectileEffect.update();
    }

    /**
     * @brief Draws the projectile on the screen.
     * 
     * Subclasses must implement this method to provide specific render of the projectile.
     *
     * @param g The Graphics object used for rendering the projectile.
     */
    abstract public void draw(Graphics g);

    /**
     * @brief Gets the bounding rectangle of the projectile.
     *
     * @return The bounding rectangle representing the projectile's position and size.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    /**
     * Checks if the projectile is out of bounds, meaning it has gone beyond the specified area (width and height).
     * 
     * @param width The width of the game area.
     * @param height The height of the game area.
     * @return true if the projectile is out of bounds, false otherwise.
     */
    public boolean isOutOfBounds(int width, int height) {
        return this.x < 0 || this.x > width || this.y < 0 || this.y > height;
    }

    /**
     * Getter for the damage value of the projectile.
     * 
     * @return The damage value of the projectile.
     */
    public int getDamage() {
        return this.damage;
    }

    /**
     * Getter for the type of the projectile.
     * 
     * @return The type of the projectile (e.g., bullet, shotgun pellet).
     */
    public ProjectileType getType() {
        return this.type;
    }

    /**
     * Abstract method to return a string representation of the projectile. 
     * Subclasses must implement this method to provide specific details about the projectile.
     * 
     * @return A string representation of the projectile.
     */
    @Override
    abstract public String toString();
   
    /**
     * @brief Custom deserialization of the projectile object.
     *
     * @param in The ObjectInputStream for deserialization.
     * @throws IOException If an I/O error occurs during deserialization.
     * @throws ClassNotFoundException If the class of the object cannot be found.
     */
    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
