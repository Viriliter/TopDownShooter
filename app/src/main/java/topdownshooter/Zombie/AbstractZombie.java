package topdownshooter.Zombie;

import java.awt.*;
import java.util.List;

import topdownshooter.Core.ConfigHandler.ZombieProperties;
import topdownshooter.Player.Loot;
import topdownshooter.Player.PlayerItem;
import topdownshooter.Weapon.WeaponType;
import topdownshooter.Core.Globals;
import topdownshooter.Core.Position;
import topdownshooter.Core.SpriteAnimation;

public abstract class AbstractZombie implements Zombie {
    protected int x = 0, y = 0;
    protected double r = 0.0; // Rotation angle in radians
    protected int WIDTH = 80;
    protected int HEIGHT = 67;
    protected double health = 0;
    protected int speed = 0;
    protected int damage = 0;
    protected int points = 0;
    protected ZombieType type;

    protected SpriteAnimation spriteAnimation = null;

    public AbstractZombie() {}

    public AbstractZombie(ZombieProperties properties) {
        this.x = 0;
        this.y = 0;
        this.r = 0;
        this.health = properties.health();
        this.speed = properties.speed();
        this.damage = properties.damage();
        this.points = properties.points();
        
        this.spriteAnimation = new SpriteAnimation(Globals.ORDINARY_ZOMBIE_MOVE);
        this.spriteAnimation.setTargetSize(WIDTH, HEIGHT);
    }
    
    public AbstractZombie(int x, int y, double r, double health, int speed, int damage, int points, int range, ZombieType type) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.health = health;
        this.speed = speed;
        this.damage = damage;
        this.points = points;
        this.type = type;

        this.spriteAnimation = new SpriteAnimation(Globals.ORDINARY_ZOMBIE_MOVE);
        this.spriteAnimation.setTargetSize(WIDTH, HEIGHT);
    }

    public AbstractZombie(AbstractZombie other) {
        this.x = other.x;
        this.y = other.y;
        this.r = other.r;
        this.health = other.health;
        this.speed = other.speed;
        this.damage = other.damage;
        this.points = other.points;
        this.type = other.type;

        this.spriteAnimation = other.spriteAnimation;
        this.spriteAnimation.setTargetSize(WIDTH, HEIGHT);
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
        }
        
        // Update sprite animation
        this.spriteAnimation.update();
    }

    @Override
    abstract public void draw(Graphics g);

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    @Override
    public int getPoints() {
        return this.points;
    }

    @Override
    public double getHealth() {
        return this.health;
    }

    @Override
    public boolean takeDamage(double damage) {
        this.health -= damage;
        
        if (this.health <= 0.0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int giveDamage() {
        return this.damage;
    }

    @Override
    public ZombieType getType() {
        return this.type;
    }

    @Override
    public Loot kill(List<WeaponType> weaponList) {
        PlayerItem item = PlayerItem.generatePlayerItem(this.points, weaponList);
        return new Loot(this.x, this.y, this.points, item);
    }
    
    @Override
    public Position getPosition() {
        return new Position(this.x + WIDTH / 2, this.y + HEIGHT / 2);
    }
    
    @Override
    public int getX() {
        return this.x + WIDTH / 2;
    }
    
    @Override
    public int getY() {
        return this.y + HEIGHT / 2;
    }

    @Override
    abstract public String toString();
}
