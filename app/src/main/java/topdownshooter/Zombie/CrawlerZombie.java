package topdownshooter.Zombie;

import java.awt.*;

import topdownshooter.Core.ConfigHandler.ZombieProperties;

public class CrawlerZombie extends AbstractZombie {
    static final int JUMP_DISTANCE = 30;

    public CrawlerZombie(ZombieProperties properties, int x, int y) {
        super(properties);
        this.x = x;
        this.y = x;
        this.type = ZombieType.CRAWLER;
    }
    
    public CrawlerZombie(int x, int y, double r, int health, int speed, int damage, int points, int range, ZombieType type) {
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
    public void update(int px, int py) {
        if (this.x < px) this.x += 1;
        if (this.x > px) this.x -= 1;
        if (this.y < py) this.y += 1;
        if (this.y > py) this.y -= 1;
    
        int dx = px - this.x;
        int dy = py - this.y;

        this.r = Math.atan2(dy, dx); // Radians (used for rotation)
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance <= JUMP_DISTANCE) {
            this.x = px - SIZE / 2;
            this.y = py - SIZE / 2;
        }
        /*
            
            if (length != 0) {
                this.x += (int) (speed * (dx / length));
                this.y += (int) (speed * (dy / length));
            }
        */
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CrawlerZombie{");
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
