/*
 * @file PlayerItem.java
 * @brief This file defines the `PlayerItem` class.
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

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import topdownshooter.Weapon.WeaponType;

/**
 * @class PlayerItem
 * @brief Represents an item that can be obtained by the player during the game.
 * 
 * This class is the base class for all player items, including ammunition, 
 * small and large medic packs. These items can be spawned during the game 
 * and picked up by the player. The class provides functionality to generate 
 * player items based on a given chance factor.
 * 
 * @see AmmunitionItem
 * @see SmallMedicPackItem
 * @see LargeMedicPackItem
 */
public abstract class PlayerItem implements Serializable{
    private static Random random = new Random(); // Reuse random instance

    /**
     * @enum ItemType
     * @brief Enumeration for defining the types of player items.
     * 
     * The available item types are:
     * - UNDEFINED: Represents an undefined item type.
     * - AMMUNITION: Represents ammunition items.
     * - SMALL_MEDIC_PACK: Represents small medic pack items.
     * - LARGE_MEDIC_PACK: Represents large medic pack items.
     */
    public enum ItemType {
        UNDEFINED,
        AMMUNITION,
        SMALL_MEDIC_PACK,
        LARGE_MEDIC_PACK
    }

    protected ItemType lootType = ItemType.UNDEFINED;   // The loot type of the item (default is UNDEFINED)

    /**
     * Generates a random player item based on the given chance factor and list of weapon types.
     * This method determines the probability of an item spawning and selects the item type 
     * based on the spawn chance and available weapon types.
     * 
     * @param chanceFactor The factor affecting the spawn chance (higher value increases chances).
     * @param weaponLists A list of available weapon types for ammunition item generation.
     * @return A randomly generated player item (null if no item is spawned).
     */
    public static PlayerItem generatePlayerItem(int chanceFactor, List<WeaponType> weaponLists) {
        int spawnChance = random.nextInt(100);
        if (spawnChance * chanceFactor > 90) {
            int itemType = random.nextInt(10);  // 0-5: Ammo, 6-8: SmallMedicPack, 9: LargeMedicPack
            if (itemType>=9) return new LargeMedicPackItem();
            else if (itemType>=6 && itemType<9) return new SmallMedicPackItem();
            else {
                int ammoTypeIndex = random.nextInt(weaponLists.size());
                return new AmmunitionItem(weaponLists.get(ammoTypeIndex), 1);
            }
        } else {
            return null;
        }
    }

    /**
     * Gets the item type of the player item.
     * 
     * @return The type of the item.
     */
    public ItemType getItemType() {
        return this.lootType;
    }

    /**
     * Provides a string representation of the PlayerItem object.
     * 
     * @return A string representation of the PlayerItem object.
     */
    abstract public String toString();
}
