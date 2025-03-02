package topdownshooter.Zombie;

import java.awt.*;
import java.util.Map;

import topdownshooter.Core.PlayerItem;

public interface Zombie {
    public void update(int px, int py);

    public void draw(Graphics g);

    public Rectangle getBounds();

    public int getPoints();

    public int getHealth();

    public boolean takeDamage(int damage);

    public int giveDamage();

    public ZombieType getType();

    public Map.Entry<Integer, PlayerItem> kill();
}
