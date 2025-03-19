/*
 * @file SmallMedicPackItem.java
 * @brief This file defines the `SmallMedicPackItem` class.
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

/**
 * @class SmallMedicPackItem
 * @brief Represents a small medic pack item that can be used by the player for healing.
 * 
 * It extends the {@link PlayerItem} class and provides healing functionality.
 * 
 * @see PlayerItem
 */
public class SmallMedicPackItem extends PlayerItem {
    public final int headlingPoints = 15;  // The number of healing points provided by the small medic pack
    
    /**
     * Constructor to initialize the SmallMedicPackItem. This sets the loot type 
     * to SMALL_MEDIC_PACK.
     */
    public SmallMedicPackItem() {
        this.lootType = ItemType.SMALL_MEDIC_PACK;
    }
    
    /**
     * Provides a string representation of the SmallMedicPackItem object.
     * This is useful for debugging or logging purposes, providing 
     * a summary of the item.
     * 
     * @return A string representation of the SmallMedicPackItem object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SmallMedicPackItem{");
        sb.append("}");

        return sb.toString();
    }
}
