package topdownshooter.Core;

import topdownshooter.Weapon.WeaponType;

public class PlayerItem {
    public record Ammunation (
        WeaponType type,
        int magazineCount
    ) {}

    public record Medic (
        int headlingPoints
    ) {}
}
