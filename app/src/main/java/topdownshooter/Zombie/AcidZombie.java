package topdownshooter.Zombie;

import java.awt.*;
import java.util.Random;

import topdownshooter.Core.ConfigHandler.ZombieProperties;
import topdownshooter.Weapon.Projectiles.AcidSpit;
import topdownshooter.Core.Globals;
import topdownshooter.Core.SpriteAnimation;

public class AcidZombie extends AbstractZombie {
    private final int SPIT_RANGE = 500;
    private static Random spreadRandom = new Random(); // Reuse random instance   
    private final double MAX_SPREAD_ANGLE_DEG = 15;  // In degree

    private static Random random = new Random(); // Reuse random instance

    public AcidZombie(ZombieProperties properties, int x, int y) {
        super(properties);
        this.x = x;
        this.y = y;
        this.type = ZombieType.ACID;

        this.spriteAnimation = new SpriteAnimation(Globals.ACID_ZOMBIE_MOVE);
        this.spriteAnimation.setTargetSize(WIDTH, HEIGHT);
    }
    
    public AcidZombie(int x, int y, double r, double health, int speed, int damage, int points, int range, ZombieType type) {
        super(x, y, r, health, speed, damage, points, range, type);

        this.spriteAnimation = new SpriteAnimation(Globals.ACID_ZOMBIE_MOVE);
        this.spriteAnimation.setTargetSize(WIDTH, HEIGHT);
    }

    @Override
    public void draw(Graphics g) {
        this.spriteAnimation.draw(g, this.x, this.y, this.r);
    }

    public AcidSpit rangedAttack() {
        int val = random.nextInt(Globals.Time2GameTick(1000));
        // there is a probability of 1 percent of ranged attack generated by the zombie
        if (val < Globals.Time2GameTick(10)) {
            double translatedX = this.x + WIDTH / 2 + this.spriteAnimation.getOffset().getX() * Math.cos(this.r) - this.spriteAnimation.getOffset().getY() * Math.sin(this.r);
            double translatedY = this.y + HEIGHT / 2 + this.spriteAnimation.getOffset().getX() * Math.sin(this.r) + this.spriteAnimation.getOffset().getY() * Math.cos(this.r);

            // Give some randomness for ranged attack of the acid zombie
            double spreadAngle = spreadRandom.nextDouble(MAX_SPREAD_ANGLE_DEG) - (MAX_SPREAD_ANGLE_DEG / 2.0);  // In degree
            return new AcidSpit((int) translatedX, (int) translatedY, this.r + Globals.degToRad(spreadAngle), (int) (this.damage*0.5));  // Ranged attacks only gives half damage
        }
        return null;
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

            // If distance is bigger than spit range, try to catch the player
            if (distance > SPIT_RANGE) {
                int normalizedSpeedX = 0, normalizedSpeedY = 0;
                if (distance != 0) {
                    normalizedSpeedX = (int) (this.speed * Math.abs((double) dx / distance));
                    normalizedSpeedY = (int) (this.speed * Math.abs((double) dy / distance));
                }
    
                if (this.x < playerX) this.x += normalizedSpeedX;
                if (this.x > playerX) this.x -= normalizedSpeedX;
                if (this.y < playerY) this.y += normalizedSpeedY;
                if (this.y > playerY) this.y -= normalizedSpeedY;    
            } else { }
            
            // Rotate the zombie towards player
            if (distance > 0) this.r = Math.atan2(dy, dx);

        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AcidZombie{");
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
