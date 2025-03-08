package topdownshooter.Player;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import topdownshooter.Weapon.Weapon;
import topdownshooter.Core.ConfigHandler;
import topdownshooter.Core.ConfigHandler.PlayerProperties;
import topdownshooter.Core.PlayerItem;
import topdownshooter.Weapon.WeaponFactory;
import topdownshooter.Weapon.WeaponType;
import topdownshooter.Weapon.Bullet;


public class Player extends JPanel {
    private int score = 0;
    private int health = 0;
    private int x, y, dx, dy;
    private double r;
    private int speed;
    private final int SIZE = 30;
    private ArrayList<Weapon> inventory;
    private int currentWeaponIndex = 0;

    public Player() {}

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

    public void update(final int maxWidth, final int maxHeight) {
        // Update location
        this.x = this.x + this.dx > maxWidth-SIZE ? maxWidth-SIZE : this.x + this.dx;
        this.x = this.x + this.dx < 0 ? 0 : this.x + this.dx;
        this.y = this.y + this.dy > maxHeight-SIZE ? maxHeight-SIZE : this.y + this.dy;
        this.y = this.y + this.dy < 0 ? 0 : this.y + this.dy;

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

    public void heal(int healPoints) {this.health = this.health+healPoints > 100 ? 100: this.health+healPoints;}

    public int getHealth() {return this.health;}

    public void addScore(int points) {
        this.score += points<0 ? 0 : points;
    }

    public void addPlayerItem(PlayerItem item) {
        if (item == null) return;

    }

    public int getScore() {return this.score;}
    
    public void takeDamage(int damage) {this.health = this.health-damage <= 0 ? 0: this.health-damage;}
    
    public void switchWeapon() {this.currentWeaponIndex = (this.currentWeaponIndex + 1) % this.inventory.size();}
    
    public Weapon getCurrentWeapon() {return this.inventory.get(currentWeaponIndex);}

    public Bullet fire() {
        return this.getCurrentWeapon().fire(this.x + SIZE / 2, this.y + SIZE / 2, this.r);
    }

    public void addNewWeapon(ConfigHandler config, WeaponType type) {
        this.inventory.add(WeaponFactory.createWeapon(config, type));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player{");
        sb.append("score=" + this.score + ", ");
        sb.append("health=" + this.health + ", ");
        sb.append("x=" + this.x + ", ");
        sb.append("y=" + this.y + ", ");
        sb.append("dx=" + this.dx + ", ");
        sb.append("dy=" + this.dy + ", ");
        sb.append("r=" + this.r + ", ");
        sb.append("speed=" + this.speed + ", ");
        sb.append("inventory=" + this.inventory + ", ");
        sb.append("currentWeaponIndex=" + this.currentWeaponIndex);
        sb.append("}");

        return sb.toString();
    }
}
