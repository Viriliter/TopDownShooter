package topdownshooter.Player;

public class LargeMedicPackItem extends PlayerItem {
    public final int headlingPoints=50;
    
    public LargeMedicPackItem() {
        this.lootType = ItemType.LARGE_MEDIC_PACK;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LargeMedicPackItem{");
        sb.append("}");

        return sb.toString();
    }
}
