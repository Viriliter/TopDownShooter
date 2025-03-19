/*
 * @file AmmunitionItem.java
 * @brief This file defines the `AmmunitionItem` class.
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
 * @class AmmunitionItem
 * @brief Represents an ammunition item that can be used by the player.
 * 
 * This class models an ammunition item, which includes the type of weapon it corresponds 
 * and the number of magazines available. 
 * Ammunition items are considered loot, which can be picked up by the player.
 * 
 * @see WeaponType
 * @see PlayerItem
 */
public class AmmunitionItem extends PlayerItem {
    public WeaponType type;     // The type of weapon that this ammunition corresponds to
    public int magazineCount;   // The number of magazines available in this ammunition item

    /**
     * Constructor to initialize the AmmunitionItem with a weapon type and magazine count.
     * 
     * @param type The type of weapon for which this ammunition is intended.
     * @param magazineCount The number of magazines available in the item.
     */
    public AmmunitionItem(WeaponType type, int magazineCount) {
        this.type = type; 
        this.magazineCount = magazineCount;
        this.lootType = ItemType.AMMUNITION;
    }

    /**
     * Provides a string representation of the AmmunitionItem object, which includes 
     * the weapon type and the magazine count for debugging or logging purposes.
     * 
     * @return A string representation of the AmmunitionItem object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AmmunitionItem{");
        sb.append("type=" + this.type + ", ");
        sb.append("magazineCount=" + this.magazineCount + ", ");
        sb.append("}");

        return sb.toString();
    }
}
