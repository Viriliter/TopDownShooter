/*
 * @file SniperRifle.java
 * @brief This file defines the `SniperRifle` class implements the behavior of a sniper rifle.
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
import topdownshooter.Weapon.Projectiles.ArmorPiercingBullet;

/**
 * @class SniperRifle
 * @brief Represents a sniper rifle weapon in the game.
 * 
 * The `SniperRifle` class extends the AbstractWeapon class and implements the 
 * specific behavior for a sniper rifle.
 * It handles firing mechanics and appropriate sound effects.
 * The sniper rifle fires ArmorPiercingBullet projectiles.
 * 
 * @see AbstractWeapon
 * @see ArmorPiercingBullet
 */
public class SniperRifle extends AbstractWeapon {
    /**
     * Constructor that initializes the SniperRifle with the given weapon properties.
     * 
     * @param properties The properties for the weapon.
     */
    public SniperRifle(WeaponProperties properties) {
        super(properties);

        this.type = WeaponType.SNIPERRIFLE;

        this.firingSoundFX = new SoundFX(Globals.FIRE_SNIPER_RIFLE_SOUND_FX_PATH);

        this.weaponAnimation = new SpriteAnimation(Globals.MUZZLE_ANIMATION);
        this.weaponAnimation.setTargetSize(20, 20);
        this.weaponAnimation.setRepeat(0);
    }

    @Override
    public ArmorPiercingBullet fire(int x, int y, double r) {
        if (this.ammo == 0) {
            this.emptyClickSoundFX.play(false);
            return null;
        }

        if (this.fireTick.isTimeOut() && this.ammo > 0) {
            fireTick.reset();
            this.ammo--;
            applySoundFX();
            this.weaponAnimation.setRepeat(1);  // Only repeat animation once
            return new ArmorPiercingBullet(x, y, r, this.damage);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SniperRifle{");
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
