/*
 * @file RocketLauncher.java
 * @brief This file defines the `RocketLauncher` class implements the behavior 
 * of a rocket launcher.
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
import topdownshooter.Weapon.Projectiles.Rocket;

/**
 * @class RocketLauncher
 * @brief Represents a rocket launcher weapon in the game.
 * 
 * The `RocketLauncher` class extends the AbstractWeapon class and implements the 
 * specific behavior for a rocket launcher.
 * It handles firing mechanics and appropriate sound effects.
 * The rocket launcher fires Rocket projectiles.
 * 
 * @see AbstractWeapon
 * @see Rocket
 */
public class RocketLauncher extends AbstractWeapon {
    /**
     * Constructor that initializes the RocketLauncher with the given weapon properties.
     * 
     * @param properties The properties for the weapon.
     */
    public RocketLauncher(WeaponProperties properties) {
        super(properties);

        this.type = WeaponType.ROCKETLAUNCHER;
        this.firingSoundFX = new SoundFX(Globals.FIRE_ROCKET_LAUNCHER_SOUND_FX_PATH);
    }

    @Override
    public Rocket fire(int x, int y, double r) {
        if (this.ammo == 0) {
            this.emptyClickSoundFX.play(false);
            return null;
        }

        if (this.fireTick.isTimeOut() && this.ammo > 0) {
            fireTick.reset();
            this.ammo--;
            applySoundFX();
            return new Rocket(x, y, r, this.damage);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RocketLauncher{");
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
