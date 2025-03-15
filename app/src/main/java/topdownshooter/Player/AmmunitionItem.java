package topdownshooter.Player;

import topdownshooter.Weapon.WeaponType;

public class AmmunitionItem extends PlayerItem {
    public WeaponType type;
    public int magazineCount;

    public AmmunitionItem(WeaponType type, int magazineCount) {
        this.type = type; 
        this.magazineCount = magazineCount;
        this.lootType = ItemType.AMMUNITION;
    }

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
