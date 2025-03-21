/*
 * @file AbstractZombie.java
 * @brief This file defines the `AbstractZombie` class which implements some common 
 * zombie behaviors.
 * 
 * The `AbstractZombie` class implements `Zombie` interface class.
 * It implements the required behavior of common zombie properties.
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

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import topdownshooter.Core.ConfigHandler.ZombieProperties;
import topdownshooter.Player.Loot;
import topdownshooter.Player.PlayerItem;
import topdownshooter.Weapon.WeaponType;
import topdownshooter.Core.Globals;
import topdownshooter.Core.Position;
import topdownshooter.Core.SpriteAnimation;

/**
 * @brief Abstract class representing a generic Zombie in the game.
 *
 * This class defines the common properties and behaviors of all zombie types,
 * including health, speed, damage, and the ability to move toward the player and take damage.
 * Specific zombie types (e.g., TankZombie, OrdinaryZombie) extend this class and provide
 * their own unique behavior.
 */
public abstract class AbstractZombie implements Zombie {
    protected int x = 0, y = 0;                         /**< X and Y coordinates of the zombie. */
    protected double r = 0.0;                           /* Rotation angle of the zombie in radians */
    protected int WIDTH = 80;                           /**< Width of the zombie sprite. */
    protected int HEIGHT = 67;                          /**< Height of the zombie sprite. */
    protected double health = 0;                        /**< Height of the zombie sprite. */
    protected int speed = 0;                            /**< Speed of the zombie. */
    protected int damage = 0;                           /**< Damage dealt by the zombie. */
    protected int points = 0;                           /**< Points awarded when the zombie is killed. */
    protected ZombieType type;                          /**< Type of the zombie (e.g., Tank, Ordinary). */
    protected SpriteAnimation spriteAnimation = null;   /**< Animation for the zombie's sprite. */

    /**
     * Default constructor for AbstractZombie.
     *
     */
    public AbstractZombie() {}

    /**
     * Constructor to create a zombie with specified properties.
     *
     * @param properties The properties used to configure the zombie's health, speed, damage, and points.
     */
    public AbstractZombie(ZombieProperties properties) {
        this.x = 0;
        this.y = 0;
        this.r = 0;
        this.health = properties.health();
        this.speed = properties.speed();
        this.damage = properties.damage();
        this.points = properties.points();
        
        this.spriteAnimation = new SpriteAnimation(Globals.ORDINARY_ZOMBIE_MOVE);
        this.spriteAnimation.setTargetSize(WIDTH, HEIGHT);
    }

    /**
     * Copy constructor of the `AbstractZombie` that clones from another instance.
     *
     * @param other The other AbstractZombie instance to clone.
     */
    public AbstractZombie(AbstractZombie other) {
        this.x = other.x;
        this.y = other.y;
        this.r = other.r;
        this.health = other.health;
        this.speed = other.speed;
        this.damage = other.damage;
        this.points = other.points;
        this.type = other.type;

        this.spriteAnimation = other.spriteAnimation;
        this.spriteAnimation.setTargetSize(WIDTH, HEIGHT);
    }

    @Override
    public void update(Rectangle playerBounds) {
        // Try to catch the player
        int playerX = (int) playerBounds.getX();
        int playerY = (int) playerBounds.getY();
        
        // If objects collided, which means zombie catched the player, do not update position of the zombie
        if (!Globals.isObjectsCollided(this.getBounds(), playerBounds)) {
            int dx = playerX - this.x;
            int dy = playerY - this.y;
            
            // Need to normalize speed according to the speed vector
            double distance = Math.sqrt(dx * dx + dy * dy);
            int normalizedSpeedX = 0, normalizedSpeedY = 0;
            if (distance != 0) {
                normalizedSpeedX = (int) (this.speed * Math.abs((double) dx / distance));
                normalizedSpeedY = (int) (this.speed * Math.abs((double) dy / distance));
            }

            if (this.x < playerX) this.x += normalizedSpeedX;
            if (this.x > playerX) this.x -= normalizedSpeedX;
            if (this.y < playerY) this.y += normalizedSpeedY;
            if (this.y > playerY) this.y -= normalizedSpeedY;    

            // Rotate the zombie towards player
            this.r = Math.atan2(dy, dx);
        }
        
        // Update sprite animation
        this.spriteAnimation.update();
    }

    @Override
    abstract public void draw(Graphics g);

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    @Override
    public int getPoints() {
        return this.points;
    }

    @Override
    public double getHealth() {
        return this.health;
    }

    @Override
    public boolean takeDamage(double damage) {
        this.health -= damage;
        
        if (this.health <= 0.0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int giveDamage() {
        return this.damage;
    }

    @Override
    public ZombieType getType() {
        return this.type;
    }

    @Override
    public Loot kill(List<WeaponType> weaponList) {
        PlayerItem item = PlayerItem.generatePlayerItem(this.points, weaponList);
        return new Loot(this.x, this.y, this.points, item);
    }
    
    @Override
    public Position getPosition() {
        return new Position(this.x + WIDTH / 2, this.y + HEIGHT / 2);
    }
    
    @Override
    public int getX() {
        return this.x + WIDTH / 2;
    }
    
    @Override
    public int getY() {
        return this.y + HEIGHT / 2;
    }

    @Override
    abstract public String toString();

    @Override
    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
