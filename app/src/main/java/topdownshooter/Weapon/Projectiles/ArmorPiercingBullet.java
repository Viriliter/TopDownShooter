/*
 * @file ArmorPiercingBullet.java
 * @brief This file defines the `ArmorPiercingBullet` class.
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

/**
 * @class ArmorPiercingBullet
 * @brief Represents armor piercing bullet fired by a sniper rifle.
 * 
 * This class is a armor-piercing projectile class that passes through the zombies. 
 * It is destroyed after it reaches out of the game ragion.
 * 
 * @see Bullet
 * @see Projectile
 */
public class ArmorPiercingBullet extends Projectile {
    /**
     * Constructor to initialize armor-piercing bullet.
     * Creates `ArmorPiercingBullet` objects with given position, rotation, and damage properties.
     * 
     * @param x The initial x-coordinate of the shotgun shot.
     * @param y The initial y-coordinate of the shotgun shot.
     * @param r The direction of fire (in radians).
     * @param damage The base damage of the armor-piercing bullet.
     */
    public ArmorPiercingBullet(int x, int y, double r, int damage) {
        super(x, y, r, damage);

        this.type = ProjectileType.ARMOR_PIERCING_BULLET;
        this.projectileEffect = new TextureFX(new TextureFXStruct(Globals.PROJECTILE_TEXTURE_PATH, -25, 0, 3));
        this.projectileEffect.setTargetSize(50, 20);
    }

    @Override   
    public void draw(Graphics g) {
        if (this.projectileEffect!=null) this.projectileEffect.draw(g, this.x, this.y, this.r);        
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ArmorPiercingBullet{");
        sb.append("x=" + this.x + ", ");
        sb.append("y=" + this.y + ", ");
        sb.append("r=" + this.r + ", ");
        sb.append("damage=" + this.damage + ", ");
        sb.append("}");

        return sb.toString();
    }
}
