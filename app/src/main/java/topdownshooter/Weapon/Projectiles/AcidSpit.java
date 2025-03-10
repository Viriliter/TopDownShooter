package topdownshooter.Weapon.Projectiles;

public class AcidSpit extends Projectile {
    public AcidSpit (int x, int y, double r) {
        super(x, y, r);

        this.type = ProjectileType.ACID_SPIT;
    }

    public AcidSpit (int x, int y, double r, int damage) {
        super(x, y, r, damage);

        this.type = ProjectileType.ACID_SPIT;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AcidSpit{");
        sb.append("x=" + this.x + ", ");
        sb.append("y=" + this.y + ", ");
        sb.append("r=" + this.r + ", ");
        sb.append("damage=" + this.damage + ", ");
        sb.append("}");

        return sb.toString();
    }
}
