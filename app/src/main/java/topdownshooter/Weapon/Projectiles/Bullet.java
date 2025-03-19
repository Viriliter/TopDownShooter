/*
 * @file Bullet.java
 * @brief This file defines the `Bullet` class.
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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import topdownshooter.Core.Globals;
import topdownshooter.Core.TextureFX;
import topdownshooter.Core.TextureFXStruct;

/**
 * @class Bullet
 * @brief Represents bullet fired by a pistol and assault rifle.
 * 
 * This class is a defacto projectile class that can be implemented for different type of firearms.
 * 
 * @see Bullet
 * @see Projectile
 */
public class Bullet extends Projectile {
    /**
     * Constructor to initialize bullet.
     * Creates `Bullet` objects with given position, rotation, and damage properties.
     * 
     * @param x The initial x-coordinate of the shotgun shot.
     * @param y The initial y-coordinate of the shotgun shot.
     * @param r The direction of fire (in radians).
     * @param damage The base damage of the bullet.
     */
    public Bullet(int x, int y, double r, int damage) {
        super(x, y, r, damage);

        this.type = ProjectileType.BULLET;
        this.projectileEffect = new TextureFX(new TextureFXStruct(Globals.PROJECTILE_TEXTURE_PATH, -25, 0, 3));
        this.projectileEffect.setTargetSize(50, 20);
    }

    @Override   
    public void draw(Graphics g) {
        if (this.projectileEffect!=null) this.projectileEffect.draw(g, this.x, this.y, this.r);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);

        // Save current transformation
        AffineTransform oldTransform = g2d.getTransform();

        // Move to bullet's position
        g2d.translate(x, y);
        g2d.rotate(this.r); // Rotate bullet

        // Draw bullet as a small rotated rectangle
        g2d.fillRect(-size / 2, -size / 2, size, size);
        
        // Restore previous transformation
        g2d.setTransform(oldTransform);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bullet{");
        sb.append("x=" + this.x + ", ");
        sb.append("y=" + this.y + ", ");
        sb.append("r=" + this.r + ", ");
        sb.append("damage=" + this.damage + ", ");
        sb.append("}");

        return sb.toString();
    }
}
