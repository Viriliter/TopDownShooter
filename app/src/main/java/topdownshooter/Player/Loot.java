/*
 * @file Loot.java
 * @brief This file defines the `Loot` class.
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

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

import topdownshooter.Core.Globals;
import topdownshooter.Core.SpriteAnimation;
import topdownshooter.Player.PlayerItem.ItemType;

/**
 * The Loot class represents an item of loot in the game. It contains information
 * about the loot's position, associated score, and item type, along with animation 
 * handling for different types of loot (e.g., ammunition, small medic pack, large medic pack).
 */
public class Loot extends JPanel {
    private int x, y;
    private final int WIDTH = 50;                               // The width of the loot item.
    private final int HEIGHT = 50;                              // The height of the loot item.

    private int score;                                          // The score associated with this loot item.
    private PlayerItem item = null;                             // The item represented by this loot
    private int lootAge = 0;                                    // The age of the loot (how long it has been in the game world)

    private SpriteAnimation spriteAnimationAmmo = null;         // The animation for the ammunition loot item.
    private SpriteAnimation spriteAnimationSmallMedic = null;   // The animation for the small medic pack loot item.
    private SpriteAnimation spriteAnimationLargeMedic = null;   // The animation for the large medic pack loot item.

    /**
     * Constructs a new Loot object at the specified position with the given score and item.
     * Initializes the appropriate animation for the loot type.
     * 
     * @param x The x-coordinate of the loot item.
     * @param y The y-coordinate of the loot item.
     * @param score The score associated with the loot item.
     * @param item The PlayerItem associated with the loot.
     */
    public Loot(int x, int y, int score, PlayerItem item) {
        this.x = x;
        this.y = y;
        this.score = score;
        this.item = item;

        // No loot item may exists
        if (this.item == null) return;

        if (this.item.lootType == ItemType.AMMUNITION) {
            this.spriteAnimationAmmo = new SpriteAnimation(Globals.AMMO_ANIMATION);
            this.spriteAnimationAmmo.setTargetSize(WIDTH, HEIGHT); 
        } else if (this.item.lootType == ItemType.SMALL_MEDIC_PACK) {
            this.spriteAnimationSmallMedic = new SpriteAnimation(Globals.SMALL_MEDIC_ANIMATION);
            this.spriteAnimationSmallMedic.setTargetSize(WIDTH, HEIGHT); 
        } else if (this.item.lootType == ItemType.LARGE_MEDIC_PACK) {
            this.spriteAnimationLargeMedic = new SpriteAnimation(Globals.LARGE_MEDIC_ANIMATION);
            this.spriteAnimationLargeMedic.setTargetSize(WIDTH, HEIGHT); 
        } else {}
    }

    /**
     * Draws the loot item on the provided graphics context.
     * 
     * @param g The graphics context used for drawing the loot item.
     */
    public void draw(Graphics g) {
        if (this.item == null) return;

        if (this.item.lootType == ItemType.AMMUNITION) {
            this.spriteAnimationAmmo.draw(g, this.x, this.y, 0.0);
        } else if (this.item.lootType == ItemType.SMALL_MEDIC_PACK) {
            this.spriteAnimationSmallMedic.draw(g, this.x, this.y, 0.0);
        } else if (this.item.lootType == ItemType.LARGE_MEDIC_PACK) {
            this.spriteAnimationLargeMedic.draw(g, this.x, this.y, 0.0);
        } else {

        }
    }

    /**
     * Updates the loot item, advancing its animation and increasing its age.
     */
    public void update() {
        if (this.item == null) return;

        if (this.item.lootType == ItemType.AMMUNITION) {
            this.spriteAnimationAmmo.update();
        } else if (this.item.lootType == ItemType.SMALL_MEDIC_PACK) {
            this.spriteAnimationSmallMedic.update();
        } else if (this.item.lootType == ItemType.LARGE_MEDIC_PACK) {
            this.spriteAnimationLargeMedic.update();
        }
        lootAge++;
    } 

    /**
     * Returns the score associated with this loot item.
     * 
     * @return The score associated with the loot item.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Returns the PlayerItem associated with this loot item.
     * 
     * @return The PlayerItem associated with this loot.
     */
    public PlayerItem getItem() {
        return this.item;
    }

    /**
     * Returns the age of the loot item (how long it has been in the game world).
     * 
     * @return The age of the loot item.
     */
    public int getLootAge() {
        return this.lootAge;
    }

    /**
     * Returns the x-coordinate of the loot item.
     * 
     * @return The x-coordinate of the loot item.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Returns the y-coordinate of the loot item.
     * 
     * @return The y-coordinate of the loot item.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Returns a rectangle representing the bounds of the loot item.
     * 
     * @return The bounds of the loot item as a Rectangle.
     */
    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, WIDTH, HEIGHT);
    }

    /**
     * Returns a string representation of the Loot object, including its position, score, and item.
     * 
     * @return A string representation of the Loot object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Loot{");
        sb.append("x=" + this.x + ", ");
        sb.append("y=" + this.y + ", ");
        sb.append("score=" + this.score + ", ");
        sb.append("item=" + this.item + ", ");
        sb.append("}");

        return sb.toString();
    }
}
