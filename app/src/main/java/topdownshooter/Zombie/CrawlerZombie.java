package topdownshooter.Zombie;

import java.awt.*;

import topdownshooter.Core.ConfigHandler.ZombieProperties;
import topdownshooter.Core.Globals;
import topdownshooter.Core.SpriteAnimation;

public class CrawlerZombie extends AbstractZombie {
    static final int JUMP_DISTANCE = 100;
    private boolean isJumped = false;

    public CrawlerZombie(ZombieProperties properties, int x, int y) {
        super(properties);
        this.x = x;
        this.y = y;
        this.type = ZombieType.CRAWLER;

        this.spriteAnimation = new SpriteAnimation(Globals.CRAWLER_ZOMBIE_MOVE);
        this.spriteAnimation.setTargetSize(WIDTH, HEIGHT);
    }

    @Override
    public void draw(Graphics g) {
        this.spriteAnimation.draw(g, this.x, this.y, this.r);
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
        
        // Update sprite animation
        this.spriteAnimation.update();
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
