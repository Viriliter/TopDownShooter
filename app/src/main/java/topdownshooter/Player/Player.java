package topdownshooter.Player;

import javax.swing.*;

import topdownshooter.Weapon.Weapon;
import topdownshooter.Core.ConfigHandler;
import topdownshooter.Core.ConfigHandler.PlayerProperties;
import topdownshooter.Weapon.WeaponFactory;
import topdownshooter.Weapon.WeaponType;
import topdownshooter.Weapon.Bullet;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Player extends JPanel{
    private double score = 0;
    private double health = 0;
    private int x, y, dx, dy;
    private double r;
    private int speed;
    private final int SIZE = 30;
    private ArrayList<Weapon> inventory;
    private int currentWeaponIndex = 0;

    public Player(ConfigHandler config) {
        PlayerProperties playerProperties = config.getPlayerProperties();
        this.x = playerProperties.startingX();
        this.y = playerProperties.startingY();
        this.r = 0;
        this.dx = 0;
        this.dy = 0;
        this.health = playerProperties.startingHealth();
        this.speed = playerProperties.speed();
        this.score = 0;

        inventory = new ArrayList<>();
        // Every player starts with a pistol
        inventory.add(WeaponFactory.createWeapon(config, WeaponType.PISTOL));
    }

    public void rotate(double rRad) {
        this.r = rRad;
    }

    public void update() {
        // Update location
        this.x += this.dx;
        this.y += this.dy;

        // Update weapons
        for (Weapon w : inventory) {
            w.update();
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Save the current transformation matrix
        AffineTransform oldTransform = g2d.getTransform();

        g2d.setColor(Color.BLUE);
        g2d.translate(this.x + SIZE / 2, this.y + SIZE / 2);
        g2d.rotate(this.r); 
        g2d.fillRect(-SIZE / 2, -SIZE / 2, SIZE, SIZE);
        
        g2d.setTransform(oldTransform);
    }

    public void decrementDx() { this.dx = -this.speed; }
    
    public void incrementDx() { this.dx = this.speed; }
    
    public void decrementDy() { this.dy = -this.speed; }
    
    public void incrementDy() { this.dy = +this.speed; }

    public void setDx(int dx) { this.dx = dx; }

    public void setDy(int dy) { this.dy = dy; }
    
    public int getX() { return this.x + SIZE / 2 ; }
    
    public int getY() { return this.y + SIZE / 2 ; }
    
    public double getR() { return this.r; }

    public void moveX() { this.x += this.dx; }

    public void moveY() { this.y += this.dy; }

    public double getHealth() {return this.health;}

    public double getScore() {return this.score;}
    
    public void takeDamage(double damage) {this.health -= damage;}
    
    public void switchWeapon() {this.currentWeaponIndex = (this.currentWeaponIndex + 1) % this.inventory.size();}
    
    public Weapon getCurrentWeapon() {return this.inventory.get(currentWeaponIndex);}

    public Bullet fire() {
        return this.getCurrentWeapon().fire(this.x + SIZE / 2, this.y + SIZE / 2, this.r);
    }

    public void addNewWeapon(ConfigHandler config, WeaponType type) {
        this.inventory.add(WeaponFactory.createWeapon(config, type));
    }
}
