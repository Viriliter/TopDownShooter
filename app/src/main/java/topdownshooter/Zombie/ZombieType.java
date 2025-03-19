/*
 * @file ZombieType.java
 * @brief This file defines the `ZombieType` enums which defines different types of zombies.
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

/**
 * @enum ZombieType
 * @brief Represents different types of zombies in the game.
 *
 */
public enum ZombieType {
    //UNDEFINED,
    /**
     * @brief A standard zombie with balanced attributes.
     */
    ORDINARY,

    /**
     * @brief A fast-moving zombie with lower health.
     */
    CRAWLER,

    /**
     * @brief A heavily armored zombie with high health but slow speed.
     */
    TANK,
    
    /**
     * @brief A zombie that has ranged attack with acid spit.
     */
    ACID
}
