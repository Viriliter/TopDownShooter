/*
 * @file AcidSpit.java
 * @brief This file defines the `AcidSpit` class.
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

import topdownshooter.Core.Globals;
import topdownshooter.Core.TextureFX;
import topdownshooter.Core.TextureFXStruct;
import topdownshooter.Zombie.AcidZombie;

/**
 * @class AcidSpit
 * @brief Represents acid spit which is throwed by acid zombie.
 * 
 * This class is `AcidSpit` class that damages the player only. 
 * It is destroyed either it is contacted by the player or after it reaches out of the game ragion.
 * 
 * @see AcidZombie
 * @see Projectile
 */
public class AcidSpit extends Projectile {
    private static final int EFFECTIVE_RANGE = 50;  // The range at which the acid spit is effective
    protected int size = 15;                        // Size of the acid spit sprite

    /**
     * Constructor to initialize the acid spit with its position, rotation, and damage.
     * The texture effect for the acid spit is also initialized here.
     * 
     * @param x The x-coordinate of the acid spit.
     * @param y The y-coordinate of the acid spit.
     * @param r The rotation angle of the acid spit in radians.
     * @param damage The damage value the acid spit will deal.
     */
    public AcidSpit (int x, int y, double r, int damage) {
        super(x, y, r, damage);
        this.type = ProjectileType.ACID_SPIT;
        this.speed = 5;

        this.projectileEffect = new TextureFX(new TextureFXStruct(Globals.ACID_SPIT_TEXTURE_PATH, -25, 0, 3));
        this.projectileEffect.setTargetSize(size, size);
    }

    /**
     * Getter for the effective range of the acid spit.
     * 
     * @return The effective range in pixels.
     */
    public int getEffectiveRange() {
        return AcidSpit.EFFECTIVE_RANGE;
    }

    @Override   
    public void draw(Graphics g) {
        this.projectileEffect.draw(g, x, y, r);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AcidSpit{");
        sb.append("x=" + this.x + ", ");
        sb.append("y=" + this.y + ", ");
        sb.append("r=" + this.r + ", ");
        sb.append("damage=" + this.damage + ", ");
        sb.append("}");

        return sb.toString();
    }
}
