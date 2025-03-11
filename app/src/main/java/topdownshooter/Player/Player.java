package topdownshooter.Player;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

import topdownshooter.Weapon.Weapon;
import topdownshooter.Core.ConfigHandler;
import topdownshooter.Core.Globals;
import topdownshooter.Core.ConfigHandler.PlayerProperties;
import topdownshooter.Core.PlayerItem;
import topdownshooter.Core.SpriteAnimation;
import topdownshooter.Weapon.WeaponFactory;
import topdownshooter.Weapon.WeaponType;
import topdownshooter.Weapon.Projectiles.Projectile;


public class Player extends JPanel {
    private int score = 0;
    private double health = 0.0;
    private int x, y, dx, dy;
    private double r;
    private int speed;
    private final int WIDTH = 60;
    private final int HEIGHT = 51;
    private ArrayList<Weapon> inventory;
    private int currentWeaponIndex = 0;

    private SpriteAnimation spriteAnimation = null;

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

        this.spriteAnimation = new SpriteAnimation(Globals.HUNTER_PISTOL_IDLE);
        this.spriteAnimation.setTargetSize(WIDTH, HEIGHT);

        this.inventory = new ArrayList<>();
        // Every player starts with a pistol
        this.inventory.add(WeaponFactory.createWeapon(config, WeaponType.PISTOL));
    }

    public void rotate(double rRad) {
        this.r = rRad;
    }

    public void update(final int maxWidth, final int maxHeight) {
        // Update location
        this.x = this.x + this.dx > maxWidth-WIDTH ? maxWidth-WIDTH : this.x + this.dx;
        this.x = this.x + this.dx < 0 ? 0 : this.x + this.dx;
        this.y = this.y + this.dy > maxHeight-HEIGHT ? maxHeight-HEIGHT : this.y + this.dy;
        this.y = this.y + this.dy < 0 ? 0 : this.y + this.dy;

        // Update weapons
        for (Weapon w : inventory) {
            w.update();
        }

        // Update sprite animation
        this.spriteAnimation.update();
    }

    public void draw(Graphics g) {
        /*
        Graphics2D g2d = (Graphics2D) g;

        // Save the current transformation matrix
        AffineTransform oldTransform = g2d.getTransform();

        g2d.setColor(Color.BLUE);
        g2d.translate(this.x + WIDTH / 2, this.y + HEIGHT / 2);
        g2d.rotate(this.r); 
        g2d.fillRect(-WIDTH / 2, -HEIGHT / 2, WIDTH, HEIGHT);
        
        g2d.setTransform(oldTransform);
        */
        this.spriteAnimation.draw(g, this.x, this.y, this.r);
    }

    public void decrementDx() { this.dx = -this.speed; }
    
    public void incrementDx() { this.dx = this.speed; }
    
    public void decrementDy() { this.dy = -this.speed; }
    
    public void incrementDy() { this.dy = +this.speed; }

    public void setDx(int dx) { this.dx = dx; }

    public void setDy(int dy) { this.dy = dy; }
    
    public int getX() { return this.x + WIDTH / 2 ; }
    
    public int getY() { return this.y + HEIGHT / 2 ; }
    
    public double getR() { return this.r; }

    public void moveX() { this.x += this.dx; }

    public void moveY() { this.y += this.dy; }

    public void heal(int healPoints) {this.health = this.health+healPoints > 100 ? 100: this.health+healPoints;}

    public double getHealth() {return this.health;}

    public void addScore(int points) {
        this.score += points<0 ? 0 : points;
    }

    public void addPlayerItem(PlayerItem item) {
        if (item == null) return;

    }

    public int getScore() {return this.score;}
    
    public void takeDamage(double damage) {this.health = this.health-damage <= 0 ? 0.0: this.health-damage;}
    
    public void switchWeapon() {this.currentWeaponIndex = (this.currentWeaponIndex + 1) % this.inventory.size();}
    
    public Weapon getCurrentWeapon() {return this.inventory.get(currentWeaponIndex);}

    public Projectile fire() {
        double translatedX = this.x + WIDTH / 2 + this.spriteAnimation.getOffset().getX() * Math.cos(this.r) - this.spriteAnimation.getOffset().getY() * Math.sin(this.r);
        double translatedY = this.y + HEIGHT / 2 + this.spriteAnimation.getOffset().getX() * Math.sin(this.r) + this.spriteAnimation.getOffset().getY() * Math.cos(this.r);
        return this.getCurrentWeapon().fire((int) translatedX, (int) translatedY, this.r);
    }

    public void addNewWeapon(ConfigHandler config, WeaponType type) {
        this.inventory.add(WeaponFactory.createWeapon(config, type));
    }

    public InventoryInfo getInventoryInfo() {
        InventoryInfo inventoryInfo = new InventoryInfo();
        
        inventoryInfo.selectedWeaponID = this.currentWeaponIndex;
        for (Weapon weapon: this.inventory) {
            switch (weapon.getType()) {
                case UNDEFINED:
                    break;
                case PISTOL:
                    inventoryInfo.pistolAmmo = weapon.getAmmo();
                    inventoryInfo.pistolMagazine = weapon.getMagazineCount();
                    break;
                case ASSAULTRIFLE:
                    inventoryInfo.assultRifleAmmo = weapon.getAmmo();
                    inventoryInfo.assultRifleMagazine = weapon.getMagazineCount();
                    break;
                case SHOTGUN:
                    inventoryInfo.shotgunAmmo = weapon.getAmmo();
                    inventoryInfo.shotgunMagazine = weapon.getMagazineCount();
                    break;
                case SNIPERRIFLE:
                    inventoryInfo.sniperRifleAmmo = weapon.getAmmo();
                    inventoryInfo.sniperRifleMagazine = weapon.getMagazineCount();
                    break;
                case ROCKETLAUNCHER:
                    inventoryInfo.rocketLauncherAmmo = weapon.getAmmo();
                    inventoryInfo.rocketLauncherMagazine = weapon.getMagazineCount();
                    break;
                default:
                    break;
            }
        }

        return inventoryInfo;
    }


    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, this.WIDTH, this.HEIGHT);
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
