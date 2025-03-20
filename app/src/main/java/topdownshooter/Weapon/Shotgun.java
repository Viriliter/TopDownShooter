/*
 * @file Shotgun.java
 * @brief This file defines the `Shotgun` class implements the behavior of a shotgun.
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

import topdownshooter.Core.ConfigHandler.WeaponProperties;
import topdownshooter.Core.Globals;
import topdownshooter.Core.SoundFX;
import topdownshooter.Core.SpriteAnimation;
import topdownshooter.Weapon.Projectiles.ShotgunPellets;

/**
 * @class Shotgun
 * @brief Represents a shotgun weapon in the game.
 * 
 * The `Shotgun` class extends the AbstractWeapon class and implements the 
 * specific behavior for a shotgun.
 * It handles firing mechanics and appropriate sound effects.
 * The shotgun fires ShotgunPellets projectiles.
 * 
 * @see AbstractWeapon
 * @see ShotgunPellets
 */
public class Shotgun extends AbstractWeapon {
    /**
     * Constructor that initializes the Shotgun with the given weapon properties.
     * 
     * @param properties The properties for the weapon.
     */
    public Shotgun(WeaponProperties properties) {
        super(properties);

        this.type = WeaponType.SHOTGUN;

        this.firingSoundFX = new SoundFX(Globals.FIRE_SHOTGUN_SOUND_FX_PATH);

        this.weaponAnimation = new SpriteAnimation(Globals.MUZZLE_ANIMATION);
        this.weaponAnimation.setTargetSize(20, 20);
        this.weaponAnimation.setRepeat(0);
    }

    @Override
    public ShotgunPellets fire(int x, int y, double r) {
        if (this.ammo == 0) {
            this.emptyClickSoundFX.play(false);
            return null;
        }

        if (this.fireTick.isTimeOut() && this.ammo > 0) {
            fireTick.reset();
            this.ammo--;
            applySoundFX();
            this.weaponAnimation.setRepeat(1);  // Only repeat animation once
            return new ShotgunPellets(x, y, r, this.damage);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Shotgun{");
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
