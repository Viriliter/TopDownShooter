/*
 * @file BlastFX.java
 * @brief This file defines the `BlastFX` class.
 *
 * Created on Thu Mar 20 2025
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

import java.awt.Graphics;
import java.util.Random;

/**
 * @class BlastFX
 * @brief Represents a blast effect in the game.
 * 
 * This class handles the visual representation of explosions,
 * including explosive and toxic blasts. It initializes animations, updates
 * their state, and renders them with randomized rotation.
 */
public class BlastFX {
    /**
     * @enum BlastType
     * @brief Defines different types of blast effects.
     */
    public enum BlastType {
        UNDEFINED,          /**< Default undefined blast type. */
        EXPLOSIVE_BLAST,    /**< A standard explosive blast (e.g., RPG explosion). */
        TOXIC_BLAST         /**< A toxic blast, typically from acid-based zombies. */
    }

    private BlastType type = BlastType.UNDEFINED;   /** The type of the blast effect. */
    private int originX = 0, originY = 0;           /** The X-coordinate of the blast's origin. */
    private double r = 0.0;                         /** Rotation angle for the explosion animation. */
    private int perimeter = 0;                      /** The perimeter of the blast effect in pixels. */

    private SpriteAnimation blastAnimation = null;  /** The animation associated with the blast. */
    private static Random random = new Random();    /** Random number generator for effect randomization. */

    /**
     * @brief Constructs a new BlastFX object.
     * @param type The type of the blast (explosive or toxic).
     * @param x The X-coordinate where the blast occurs.
     * @param y The Y-coordinate where the blast occurs.
     * @param perimeter The size of the blast effect.
     */
    public BlastFX(BlastType type, int x, int y, int perimeter) {
        this.type = type;
        this.perimeter = perimeter;

        if (this.type == BlastType.TOXIC_BLAST) {
            this.blastAnimation = new SpriteAnimation(Globals.TOXIC_BLAST_ANIMATION);
        } else {
            this.blastAnimation = new SpriteAnimation(Globals.EXPLOSIVE_BLAST_ANIMATION);
        }

        this.originX = x - this.perimeter / 2;
        this.originY = y - this.perimeter / 2;
        this.r = random.nextDouble(Globals.degToRad(360));  // Randomize rotation of the animation to make explosion unique

        this.blastAnimation.setTargetSize(this.perimeter, this.perimeter);
        this.blastAnimation.setRepeat(1);  // Explosions does not repeat
    }

    /**
     * @brief Updates the animation state of the blast.
     * @return True if the animation is still running, false if completed.
     */
    public boolean update() {
        if(this.blastAnimation!=null) return this.blastAnimation.update();
        return false;        
    }
    
    /**
     * @brief Draws the blast effect on the screen.
     * @param g The Graphics object to render the explosion.
     */
    public void draw(Graphics g) {
        if(this.blastAnimation!=null) this.blastAnimation.draw(g, this.originX, this.originY, this.r);        
    }
}
