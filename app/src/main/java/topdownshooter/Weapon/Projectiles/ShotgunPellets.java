/*
 * @file ShotgunPellets.java
 * @brief This file defines the `ShotgunPellets` class.
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
import java.util.ArrayList;

import topdownshooter.Core.Globals;

/**
 * @class ShotgunPellets
 * @brief Represents a group of pellets fired by a shotgun. The shotgun fires multiple pellets in a spread pattern.
 *        Each pellet is represented by a Bullet object with a specific direction and damage.
 * 
 * This class simulates the spread of the pellets by calculating a dispersion angle and creating multiple
 * Bullet objects accordingly. The damage of each pellet is reduced by a certain ratio compared to the original 
 * damage of the shotgun.
 * 
 * @see Bullet
 * @see Projectile
 */
public class ShotgunPellets extends Projectile {
    private final int PELLET_COUNT = 9;         // Number of pellets fired in one shot
    private final int DISPERSION_ANGLE = 45;    // Angle of spread for the pellets (in degrees)
    private final double DAMAGE_RATIO = 0.8;    // Damage ratio for each individual pellet
    private ArrayList<Bullet> pellets = null;   // List that hols all pellets fired by the shotgun

    /**
     * Constructor to initialize shotgun pellets.
     * Creates multiple Bullet objects spread across the given dispersion angle.
     * 
     * @param x The initial x-coordinate of the shotgun shot.
     * @param y The initial y-coordinate of the shotgun shot.
     * @param r The direction of fire (in radians).
     * @param damage The base damage of the shotgun.
     */
    public ShotgunPellets(int x, int y, double r, int damage) {
        super(0, 0, 0, damage);

        this.pellets = new ArrayList<Bullet>();

        // Calculate dispersion angle of each pellet
        double dr = Globals.degToRad(DISPERSION_ANGLE) / PELLET_COUNT;
        for(int i=0; i<PELLET_COUNT; i++) {
            this.pellets.add(new Bullet(x, y, r + (i - PELLET_COUNT/2)*dr, (int) (damage*DAMAGE_RATIO)));
        }

        this.type = ProjectileType.SHOTGUN_PELLETS;
    }

    /**
     * Constructor to initialize shotgun pellets from an existing list of Bullet objects.
     * 
     * @param pellets The list of Bullet objects representing individual pellets.
     */
    public ShotgunPellets(ArrayList<Bullet> pellets) {
        super(0, 0, 0, (int) (pellets.get(0).damage));

        this.pellets = pellets;

        this.type = ProjectileType.SHOTGUN_PELLETS;
    }

    /**
     * Returns the list of pellets fired by the shotgun.
     * 
     * @return An ArrayList of Bullet objects representing the shotgun pellets.
     */
    public ArrayList<Bullet> getPellets() {
        return this.pellets;
    }

    @Override   
    public void move() {
        for (int i=0; i<this.pellets.size(); i++) {
            this.pellets.get(i).move();
        }
    }

    @Override
    public void draw(Graphics g) {
        for (int i=0; i<this.pellets.size(); i++) {
            this.pellets.get(i).draw(g);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ShotgunPellets{");
        sb.append("pellets=" + this.pellets);
        sb.append("}");

        return sb.toString();
    }
}
