/*
 * @file InventoryInfo.java
 * @brief This file defines the `InventoryInfo` class.
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

package topdownshooter.Player;

import topdownshooter.Weapon.WeaponType;

/**
 * The `InventoryInfo` class holds the player's inventory details, including the 
 * currently selected weapon and ammunition counts for different weapon types.
 * It contains the ammo and magazine counts for the player's weapons, such as 
 * pistols, assault rifles, shotguns, sniper rifles, and rocket launchers.
 */
public class InventoryInfo {
    public WeaponType selectedWeaponType;       /* The type of weapon currently selected by the player. */
    public int pistolAmmo = -1;                 /* The amount of ammunition for the pistol. (Default: -1, indicating uninitialized)*/
    public int pistolMagazine = -1;             /* The amount of magazine for the pistol. (Default: -1, indicating uninitialized)*/
    public int assaultRifleAmmo = -1;           /* The amount of ammunition for the assult rifle. (Default: -1, indicating uninitialized)*/
    public int assaultRifleMagazine = -1;       /* The amount of magazine for the assult rifle. (Default: -1, indicating uninitialized)*/
    public int shotgunAmmo = -1;                /* The amount of ammunition for the shotgun. (Default: -1, indicating uninitialized)*/
    public int shotgunMagazine = -1;            /* The amount of magazine for the shotgun. (Default: -1, indicating uninitialized)*/
    public int sniperRifleAmmo = -1;            /* The amount of ammunition for the sniper rifle. (Default: -1, indicating uninitialized)*/
    public int sniperRifleMagazine = -1;        /* The amount of magazine for the sniper rifle. (Default: -1, indicating uninitialized)*/
    public int rocketLauncherAmmo = -1;         /* The amount of ammunition for the rocket launcher. (Default: -1, indicating uninitialized)*/
    public int rocketLauncherMagazine = -1;     /* The amount of magazine for the rocket launcher. (Default: -1, indicating uninitialized)*/
}
