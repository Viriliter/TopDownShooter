/*
 * @file WeaponType.java
 * @brief This file defines the `WeaponType` enums which defines different types of 
 * weapons available by player.
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

/**
 * @enum WeaponType
 * @brief Represents different types of weapons in the game
 * 
 * The types listed as follow:
 * - UNDEFINED: Invalid weapon type.
 * - PISTOL: A basic firearm (low rate of fire and damage).
 * - ASSAULTRIFLE: A assault firearm (high rate of fire and moderate damage).
 * - SHOTGUN: A close-range firearm (multiple damage).
 * - SNIPERRIFLE: A sniper firearm (dealing high damage).
 * - ROCKETLAUNCHER: A rocket launcher that contains explosive projectiles (area damage).
 *
 *
 */
public enum WeaponType {
    UNDEFINED,
    PISTOL,
    ASSAULTRIFLE,
    SHOTGUN,
    SNIPERRIFLE,
    ROCKETLAUNCHER,
}
