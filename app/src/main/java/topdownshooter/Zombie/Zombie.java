package topdownshooter.Zombie;

import java.awt.*;
import java.io.Serializable;
import java.util.Map;

import topdownshooter.Core.PlayerItem;
import topdownshooter.Core.Position;

public interface Zombie extends Serializable{
    public void update(int px, int py);

    public void draw(Graphics g);

    public Rectangle getBounds();

    public int getPoints();

    public double getHealth();

    public boolean takeDamage(double damage);

    public int giveDamage();

    public ZombieType getType();

    public Map.Entry<Integer, PlayerItem> kill();

    public Position getPosition();
}
