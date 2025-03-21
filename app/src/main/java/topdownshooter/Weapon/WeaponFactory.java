/*
 * @file WeaponFactory.java
 * @brief This file defines the `WeaponFactory` class which creating different types 
 * of weapons.
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

import topdownshooter.Core.ConfigHandler;

/**
 * @class WeaponFactory
 * @brief A factory class for creating different types of weapons.
 *
 * This class provides a static method to generate weapons of different types 
 * based on a configuration handler.
 */
public class WeaponFactory {
    /**
     * Creates a new weapon based on the given type.
     *
     * This method generates a weapon using the provided configuration 
     *
     * @param config A reference to the ConfigHandler object that provides weapon properties.
     * @param type The type of weapon to create.
     * @return A newly created Weapon object of the specified type.
     * @throws IllegalStateException If the config parameter is null.
     * @throws IllegalArgumentException If the type parameter is null.
     */
    public static Weapon createWeapon(ConfigHandler config, WeaponType type) {
        if (config == null) {
            throw new IllegalStateException("ConfigHandler cannot be null!");
        }
        if (type == null) {
            throw new IllegalArgumentException("Weapon type cannot be null!");
        }

        switch (type) {
            case PISTOL:
                return new Pistol(config.getPistolProperties());
            case ASSAULTRIFLE:
                return new AssaultRifle(config.getAssaultRifleProperties());
            case SHOTGUN:
                return new Shotgun(config.getShotgunProperties());
            case SNIPERRIFLE:
                return new SniperRifle(config.getSniperRifleProperties());
            case ROCKETLAUNCHER:
                return new RocketLauncher(config.getRocketLauncherProperties());
            default:
                return null;
        }
    }
}
