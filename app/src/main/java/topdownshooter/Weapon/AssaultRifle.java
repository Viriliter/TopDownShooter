/*
 * @file AssaultRifle.java
 * @brief This file defines the `AssaultRifle` class implements the behavior of a pistol.
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

import java.util.Random;

import topdownshooter.Core.ConfigHandler.WeaponProperties;
import topdownshooter.Core.Globals;
import topdownshooter.Core.SoundFX;
import topdownshooter.Core.SpriteAnimation;
import topdownshooter.Weapon.Projectiles.Bullet;

/**
 * @class AssaultRifle
 * @brief Represents a assault rifle weapon in the game.
 * 
 * The `AssaultRifle` class extends the AbstractWeapon class and implements the 
 * specific behavior for a assault rifle.
 * It handles firing mechanics and appropriate sound effects.
 * The assault rifle fires Bullet projectiles.
 * 
 * @see AbstractWeapon
 * @see Bullet
 */
public class AssaultRifle extends AbstractWeapon {
    private static Random spreadRandom = new Random(); // Reuse random instance   
    private final double MAX_SPREAD_ANGLE_DEG = 30;  // In degree

    public AssaultRifle(WeaponProperties properties) {
        super(properties);

        this.type = WeaponType.ASSAULTRIFLE;

        this.firingSoundFX = new SoundFX(Globals.FIRE_RIFLE_SOUND_FX_PATH);

        this.weaponAnimation = new SpriteAnimation(Globals.MUZZLE_ANIMATION);
        this.weaponAnimation.setTargetSize(20, 20);
        this.weaponAnimation.setRepeat(0);

    }

    @Override
    public Bullet fire(int x, int y, double r) {
        if (this.fireTick.isTimeOut() && this.ammo > 0) {
            double spreadAngle = spreadRandom.nextDouble(MAX_SPREAD_ANGLE_DEG) - (MAX_SPREAD_ANGLE_DEG / 2.0);  // In degree
            fireTick.reset();
            this.ammo--;
            applySoundFX();
            this.weaponAnimation.setRepeat(1);  // Only repeat animation once
            return new Bullet(x, y, r + Globals.degToRad(spreadAngle), this.damage);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AssaultRifle{");
        sb.append("damage=" + this.damage + ", ");
        sb.append("magazineCapacity=" + this.magazineCapacity + ", ");
        sb.append("magazineCount=" + this.magazineCount + ", ");
        sb.append("fireRate=" + this.fireRate + ", ");
        sb.append("reloadDuration=" + this.reloadDuration + ", ");
        sb.append("ammo=" + this.ammo + ", ");
        sb.append("reloadTick=" + this.reloadTick + ", ");
        sb.append("fireTick=" + this.fireTick + ", ");
        sb.append("type=" + this.type);
        sb.append("}");

        return sb.toString();
    }
}
