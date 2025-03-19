/*
 * @file ZombieFactory.java
 * @brief This file defines the `ZombieFactory` class which creating different types 
 * of zombies.
 * 
 * This class provides a static method to create zombies of various types 
 * based on the given configuration and position.
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

package topdownshooter.Zombie;

import topdownshooter.Core.ConfigHandler;

/**
 * @class ZombieFactory
 * @brief A factory class for creating different types of zombies.
 *
 * This class provides a static method to generate zombies of different types 
 * based on a configuration handler.
 */
public class ZombieFactory {
    /**
     * @brief Creates a new zombie based on the given type.
     *
     * This method generates a zombie using the provided configuration 
     * and assigns it the given x and y coordinates.
     *
     * @param config A reference to the ConfigHandler object that provides zombie properties.
     * @param type The type of zombie to create.
     * @param x The x-coordinate of the zombie's spawn location.
     * @param y The y-coordinate of the zombie's spawn location.
     * @return A newly created Zombie object of the specified type.
     * @throws IllegalStateException If the config parameter is null.
     * @throws IllegalArgumentException If the type parameter is null.
     */
    public static Zombie createZombie(ConfigHandler config, ZombieType type, int x, int y) {
        if (config == null) {
            throw new IllegalStateException("ConfigHandler cannot be null!");
        }
        if (type == null) {
            throw new IllegalArgumentException("Zombie type cannot be null!");
        }

        switch (type) {
            case ORDINARY:
                return new OrdinaryZombie(config.getOrdinaryZombieProperties(), x, y);
            case CRAWLER:
                return new CrawlerZombie(config.getCrawlerZombieProperties(), x, y);
            case TANK:
                return new TankZombie(config.getTankZombieProperties(), x, y);
            case ACID:
                return new AcidZombie(config.getAcidZombieProperties(), x, y);
            default:
                return null;
        }
    }
}
