package topdownshooter.Player;

public class SmallMedicPackItem extends PlayerItem {
    public final int headlingPoints=15;
      
    public SmallMedicPackItem() {
        this.lootType = ItemType.SMALL_MEDIC_PACK;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SmallMedicPackItem{");
        sb.append("}");

        return sb.toString();
    }
}
