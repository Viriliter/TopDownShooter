package topdownshooter.Zombie;

import java.awt.*;

import topdownshooter.Core.ConfigHandler.ZombieProperties;

public class TankZombie extends AbstractZombie {
    public TankZombie(ZombieProperties properties, int x, int y) {
        super(properties);
        this.x = x;
        this.y = y;
        this.type = ZombieType.TANK;
    }

    public TankZombie(int x, int y, double r, double health, int speed, int damage, int points, int range, ZombieType type) {
        super(x, y, r, health, speed, damage, points, range, type);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g; // Enable rotation
        g2d.setColor(Color.GREEN);

        // Apply rotation around the zombie's center
        g2d.translate(x, y);
        g2d.rotate(r);  // Rotate to face the player

        // Draw rotated rectangle as zombie
        g2d.fillRect(-SIZE / 2, -SIZE / 2, SIZE, SIZE);

        // Reset transformation
        g2d.rotate(-r);
        g2d.translate(-x, -y);

        // Outline for better visibility
        //g2d.setColor(Color.BLACK);
        //g2d.drawRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TankZombie{");
        sb.append("x=" + this.x + ", ");
        sb.append("y=" + this.y + ", ");
        sb.append("r=" + this.r + ", ");
        sb.append("health=" + this.health + ", ");
        sb.append("speed=" + this.speed + ", ");
        sb.append("damage=" + this.damage + ", ");
        sb.append("points=" + this.points + ", ");
        sb.append("range=" + this.range + ", ");
        sb.append("type=" + this.type);
        sb.append("}");

        return sb.toString();
    }
}
