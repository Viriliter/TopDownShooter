package topdownshooter.Zombie;

import java.util.List;

import java.awt.*;
import java.io.Serializable;

import topdownshooter.Core.Position;
import topdownshooter.Player.Loot;
import topdownshooter.Weapon.WeaponType;

public interface Zombie extends Serializable{
    public void update(Rectangle playerBound);

    public void draw(Graphics g);

    public Rectangle getBounds();

    public int getPoints();

    public double getHealth();

    public boolean takeDamage(double damage);

    public int giveDamage();

    public ZombieType getType();

    public Loot kill(List<WeaponType> weaponList);

    public Position getPosition();

    public int getX();
    
    public int getY();

}
