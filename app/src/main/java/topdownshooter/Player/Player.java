package topdownshooter.Player;

import javax.swing.*;

import topdownshooter.Weapon.Weapon;
import topdownshooter.Weapon.Pistol;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Player extends JPanel{
    private double score = 0;
    private double health = 0;
    private int x, y, dx, dy;
    private double r;
    private final int SIZE = 30;
    private ArrayList<Weapon> inventory;
    private int currentWeaponIndex = 0;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.r = 0;
        this.dx = 0;
        this.dy = 0;
        this.health = 100;
        this.score = 0;

        inventory = new ArrayList<>();
        inventory.add(new Pistol());
    }

    public void rotate(double rRad) {
        this.r = rRad;
    }

    public void move() {
        this.x += this.dx;
        this.y += this.dy;
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

    public void setDx(int dx) { this.dx = dx; }
    public void setDy(int dy) { this.dy = dy; }
    
    public int getX() { return this.x + SIZE / 2 ; }
    public int getY() { return this.y + SIZE / 2 ; }
    public double getR() { return this.r; }

    public double getHealth() {return this.health;}
    public double getScore() {return this.score;}
    public void takeDamage(double damage) {this.health -= damage;}
    public void switchWeapon() {this.currentWeaponIndex = (this.currentWeaponIndex + 1) % this.inventory.size();}
    public Weapon getCurrentWeapon() {return this.inventory.get(currentWeaponIndex);}
}
