package topdownshooter.Zombie;

import java.awt.*;

import topdownshooter.Core.ConfigHandler.ZombieProperties;
import topdownshooter.Core.Globals;

public class CrawlerZombie extends AbstractZombie {
    static final int JUMP_DISTANCE = 100;
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
    public void update(Rectangle playerBounds) {
        // Try to catch the player
        int playerX = (int) playerBounds.getX();
        int playerY = (int) playerBounds.getY();
        
        // If objects collided, which means zombie catched the player, do not update position of the zombie
        if (!Globals.isObjectsCollided(this.getBounds(), playerBounds)) {
            int dx = playerX - this.x;
            int dy = playerY - this.y;
            
            // Need to normalize speed according to the speed vector
            double distance = Math.sqrt(dx * dx + dy * dy);
            int normalizedSpeedX = 0, normalizedSpeedY = 0;
            if (distance != 0) {
                normalizedSpeedX = (int) (this.speed * Math.abs((double) dx / distance));
                normalizedSpeedY = (int) (this.speed * Math.abs((double) dy / distance));
            }

            if (this.x < playerX) this.x += normalizedSpeedX;
            if (this.x > playerX) this.x -= normalizedSpeedX;
            if (this.y < playerY) this.y += normalizedSpeedY;
            if (this.y > playerY) this.y -= normalizedSpeedY;    

            // Rotate the zombie towards player
            this.r = Math.atan2(dy, dx);

            // Check zombie is at the jump range to the player
            // If it has already jumped, do not jump again.
            if (distance <= JUMP_DISTANCE && !this.isJumped) {
                this.x = playerX + WIDTH / 2;
                this.y = playerY + WIDTH / 2;
                this.isJumped = true;
            } else {
                this.isJumped = false;
            }
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
        sb.append("type=" + this.type);
        sb.append("}");

        return sb.toString();
    }
}
