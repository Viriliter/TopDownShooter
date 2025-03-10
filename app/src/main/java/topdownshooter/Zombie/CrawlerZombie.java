package topdownshooter.Zombie;

import java.awt.*;

import topdownshooter.Core.ConfigHandler.ZombieProperties;

public class CrawlerZombie extends AbstractZombie {
    static final int JUMP_DISTANCE = 30;
    private boolean isJumped = false;

    public CrawlerZombie(ZombieProperties properties, int x, int y) {
        super(properties);
        this.x = x;
        this.y = y;
        this.type = ZombieType.CRAWLER;
    }
    
    public CrawlerZombie(int x, int y, double r, double health, int speed, int damage, int points, int range, ZombieType type) {
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
        g2d.fillRect(-WIDTH / 2, -WIDTH / 2, WIDTH, WIDTH);

        // Reset transformation
        g2d.rotate(-r);
        g2d.translate(-x, -y);

        // Outline for better visibility
        //g2d.setColor(Color.BLACK);
        //g2d.drawRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
    }

    @Override
    public void update(int px, int py) {
        // Try to catch the player
        if (this.x < px) this.x += this.speed;
        if (this.x > px) this.x -= this.speed;
        if (this.y < py) this.y += this.speed;
        if (this.y > py) this.y -= this.speed;
    
        // Rotate the zombie towards player
        int dx = px - this.x;
        int dy = py - this.y;

        this.r = Math.atan2(dy, dx); // Radians (used for rotation)

        // Calculate distance between the zombie and the player
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Check zombie is at the jump range to the player
        // If it has already jumped, do not jump again.
        if (distance <= JUMP_DISTANCE && !isJumped) {
            this.x = px - WIDTH / 2;
            this.y = py - WIDTH / 2;
            isJumped = true;
        } else {
            isJumped = false;
        }
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
