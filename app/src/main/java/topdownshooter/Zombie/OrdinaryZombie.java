package topdownshooter.Zombie;

import java.awt.*;

import topdownshooter.Core.ConfigHandler.ZombieProperties;

public class OrdinaryZombie extends AbstractZombie {
    public OrdinaryZombie(ZombieProperties properties, int x, int y) {
        super(properties);
        this.x = x;
        this.y = x;
        this.type = ZombieType.ORDINARY;
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
}
