package topdownshooter.Zombie;

import java.awt.*;
import java.util.AbstractMap;
import java.util.Map;

import topdownshooter.Core.ConfigHandler.ZombieProperties;
import topdownshooter.Core.Globals;
import topdownshooter.Core.PlayerItem;
import topdownshooter.Core.SpriteAnimation;

public abstract class AbstractZombie implements Zombie {
    protected int x = 0, y = 0;
    protected double r = 0.0; // Rotation angle in radians
    protected int WIDTH = 60;
    protected int HEIGHT = 50;
    protected double health = 0;
    protected int speed = 0;
    protected int damage = 0;
    protected int points = 0;
    protected int range = 0;
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
        this.range = properties.range();
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
        this.range = range;
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
        this.range = other.range;
        this.type = other.type;

        this.spriteAnimation = other.spriteAnimation;
        this.spriteAnimation.setTargetSize(WIDTH, HEIGHT);
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

        // If zombie cathes the survivor do not update rotation
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > 2) {
            this.r = Math.atan2(dy, dx); // Radians (used for rotation)
        }

        /*
            double length = Math.sqrt(dx * dx + dy * dy);
            if (length != 0) {
                this.x += (int) (speed * (dx / length));
                this.y += (int) (speed * (dy / length));
            }
        */
        
        // Update sprite animation
        this.spriteAnimation.update();
    }

    @Override
    abstract public void draw(Graphics g);

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x-WIDTH/2, y-HEIGHT/2, WIDTH, HEIGHT);
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
    public Map.Entry<Integer, PlayerItem> kill() {
        return new AbstractMap.SimpleEntry<>(this.points, null);
    }

    @Override
    abstract public String toString();
}
