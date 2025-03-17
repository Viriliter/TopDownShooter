package topdownshooter.Player;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import topdownshooter.Weapon.WeaponType;

public abstract class PlayerItem implements Serializable{
    private static Random random = new Random(); // Reuse random instance

    public enum ItemType {
        UNDEFINED,
        AMMUNITION,
        SMALL_MEDIC_PACK,
        LARGE_MEDIC_PACK
    }

    protected ItemType lootType = ItemType.UNDEFINED;

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

    public ItemType getItemType() {
        return this.lootType;
    }

    abstract public String toString();
}
