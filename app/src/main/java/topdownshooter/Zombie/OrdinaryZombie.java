package topdownshooter.Zombie;

import java.awt.*;
import java.awt.geom.AffineTransform;

import topdownshooter.Core.ConfigHandler.ZombieProperties;

public class OrdinaryZombie extends AbstractZombie {
    public OrdinaryZombie(ZombieProperties properties, int x, int y) {
        super(properties);
        this.x = x;
        this.y = y;
        this.type = ZombieType.ORDINARY;
    }
    
    public OrdinaryZombie(int x, int y, double r, double health, int speed, int damage, int points, int range, ZombieType type) {
        super(x, y, r, health, speed, damage, points, range, type);
    }

    @Override
    public void draw(Graphics g) {
        /*
        Graphics2D g2d = (Graphics2D) g; // Enable rotation

        AffineTransform oldTransform = g2d.getTransform();

        g2d.setColor(Color.GREEN);
        g2d.translate(x + WIDTH / 2, y + HEIGHT / 2);
        g2d.rotate(r);  // Rotate to face the player
        g2d.fillRect(-WIDTH / 2, -HEIGHT / 2, WIDTH, WIDTH);

        // Reset transformation
        g2d.setTransform(oldTransform);
        */
        this.spriteAnimation.draw(g, this.x, this.y, this.r);
        /*
        AffineTransform oldTransform2 = g2d.getTransform();

        g2d.setColor(Color.ORANGE);
        g2d.translate(this.x + WIDTH / 2, this.y + HEIGHT / 2);
        g2d.rotate(this.r); 
        g2d.fillRect(0, 0, 2, 2);
        
        g2d.setTransform(oldTransform2);
        */
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OrdinaryZombie{");
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
