package topdownshooter.Player;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.ArrayList;

import topdownshooter.Weapon.Weapon;
import topdownshooter.Core.ConfigHandler;
import topdownshooter.Core.Globals;
import topdownshooter.Core.ConfigHandler.PlayerProperties;
import topdownshooter.Core.SequencialSoundFX;
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
    private final int WIDTH = 80;
    private final int HEIGHT = 68;
    private ArrayList<Weapon> inventory;
    private int currentWeaponIndex = 0;

    private SpriteAnimation spriteAnimationPistolIdle = null;
    private SpriteAnimation spriteAnimationRifleIdle = null;
    private SpriteAnimation spriteAnimationShotgunIdle = null;
    private SpriteAnimation spriteAnimationRocketLauncherIdle = null;

    private SequencialSoundFX walkSoundFX = null;
    
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

        this.spriteAnimationPistolIdle = new SpriteAnimation(Globals.HUNTER_PISTOL_IDLE);
        this.spriteAnimationPistolIdle.setTargetSize(WIDTH, HEIGHT);

        this.spriteAnimationRifleIdle = new SpriteAnimation(Globals.HUNTER_RIFLE_IDLE);
        this.spriteAnimationRifleIdle.setTargetSize(WIDTH, HEIGHT);

        this.spriteAnimationShotgunIdle = new SpriteAnimation(Globals.HUNTER_SHOTGUN_IDLE);
        this.spriteAnimationShotgunIdle.setTargetSize(WIDTH, HEIGHT);

        this.spriteAnimationRocketLauncherIdle = new SpriteAnimation(Globals.HUNTER_ROCKET_LAUNCHER_IDLE);
        this.spriteAnimationRocketLauncherIdle.setTargetSize(WIDTH, HEIGHT);

        this.inventory = new ArrayList<>();
        // Every player starts with a pistol
        this.inventory.add(WeaponFactory.createWeapon(config, WeaponType.PISTOL));

        this.walkSoundFX = new SequencialSoundFX(Globals.HUNTER_SOUND_FX_PATH);
    }

    public Player(int score, double health, int x, int y, int dx, int dy, double r, int speed, ArrayList<Weapon> inventory, int currentWeaponIndex) {
        this.score = score;
        this.health = health;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.r = r;
        this.speed = speed;
        this.inventory = inventory;
        this.currentWeaponIndex = currentWeaponIndex;

        this.spriteAnimationPistolIdle = new SpriteAnimation(Globals.HUNTER_PISTOL_IDLE);
        this.spriteAnimationPistolIdle.setTargetSize(WIDTH, HEIGHT);

        this.spriteAnimationRifleIdle = new SpriteAnimation(Globals.HUNTER_RIFLE_IDLE);
        this.spriteAnimationRifleIdle.setTargetSize(WIDTH, HEIGHT);

        this.spriteAnimationShotgunIdle = new SpriteAnimation(Globals.HUNTER_SHOTGUN_IDLE);
        this.spriteAnimationShotgunIdle.setTargetSize(WIDTH, HEIGHT);

        this.spriteAnimationRocketLauncherIdle = new SpriteAnimation(Globals.HUNTER_ROCKET_LAUNCHER_IDLE);
        this.spriteAnimationRocketLauncherIdle.setTargetSize(WIDTH, HEIGHT);

        this.walkSoundFX = new SequencialSoundFX(Globals.HUNTER_SOUND_FX_PATH);
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
        for (Weapon w : this.inventory) {
            w.update();
        }

        // Update sprite animation
        if (this.inventory.get(this.currentWeaponIndex).getType() == WeaponType.PISTOL)
            this.spriteAnimationPistolIdle.update();
    
        if (this.inventory.get(this.currentWeaponIndex).getType() == WeaponType.ASSAULTRIFLE)
            this.spriteAnimationRifleIdle.update();
        
        if (this.inventory.get(this.currentWeaponIndex).getType() == WeaponType.SHOTGUN)
            this.spriteAnimationShotgunIdle.update();
        
        if (this.inventory.get(this.currentWeaponIndex).getType() == WeaponType.SNIPERRIFLE)
            this.spriteAnimationRifleIdle.update();
        
        if (this.inventory.get(this.currentWeaponIndex).getType() == WeaponType.ROCKETLAUNCHER)
            this.spriteAnimationRocketLauncherIdle.update();

    }

    public void draw(Graphics g) {
        
        if (this.inventory.get(this.currentWeaponIndex).getType() == WeaponType.PISTOL)
            this.spriteAnimationPistolIdle.draw(g, this.x, this.y, this.r);
        
        if (this.inventory.get(this.currentWeaponIndex).getType() == WeaponType.ASSAULTRIFLE)
            this.spriteAnimationRifleIdle.draw(g, this.x, this.y, this.r);
        
        if (this.inventory.get(this.currentWeaponIndex).getType() == WeaponType.SHOTGUN)
            this.spriteAnimationShotgunIdle.draw(g, this.x, this.y, this.r);
        
        if (this.inventory.get(this.currentWeaponIndex).getType() == WeaponType.SNIPERRIFLE)
            this.spriteAnimationRifleIdle.draw(g, this.x, this.y, this.r);
        
        if (this.inventory.get(this.currentWeaponIndex).getType() == WeaponType.ROCKETLAUNCHER)
            this.spriteAnimationRocketLauncherIdle.draw(g, this.x, this.y, this.r);   
    }

    public void decrementDx() { this.dx = -this.speed; this.walkSoundFX.update();}
    
    public void incrementDx() { this.dx = this.speed; this.walkSoundFX.update();}
    
    public void decrementDy() { this.dy = -this.speed; this.walkSoundFX.update();}
    
    public void incrementDy() { this.dy = +this.speed; this.walkSoundFX.update();}

    public void setDx(int dx) { this.dx = dx; }

    public void setDy(int dy) { this.dy = dy; }
    
    public int getX() { return this.x + WIDTH / 2; }
    
    public int getY() { return this.y + HEIGHT / 2; }
    
    public double getR() { return this.r; }

    public void moveX() { this.x += this.dx;}

    public void moveY() { this.y += this.dy;}

    public void heal(int healPoints) {this.health = this.health+healPoints > 100 ? 100: this.health+healPoints;}

    public double getHealth() {return this.health;}

    public void addScore(int points) {
        this.score += points<0 ? 0 : points;
    }

    public void addAmmo(WeaponType type, int magazineCount) {
        for (Weapon w: this.inventory) {
            if (w.getType() == type) {
                w.addMagazine(magazineCount);
            }
        }
    }

    public void addLoot(Loot loot) {
        if (loot == null) return;

        PlayerItem item = loot.getItem();

        if (item == null) return;

        switch(item.getItemType()) {
            case AMMUNITION:
                AmmunitionItem ammo = (AmmunitionItem) item;
                addAmmo(ammo.type, ammo.magazineCount);
                break;
            case SMALL_MEDIC_PACK:
                SmallMedicPackItem smallMedicPack = (SmallMedicPackItem) item;
                heal(smallMedicPack.headlingPoints);
                break;
            case LARGE_MEDIC_PACK:
                LargeMedicPackItem largeMedicPack = (LargeMedicPackItem) item;
                heal(largeMedicPack.headlingPoints);
                break;
            default:
                return;
        }

    }

    public int getScore() {return this.score;}
    
    public void takeDamage(double damage) {this.health = this.health-damage <= 0 ? 0.0: this.health-damage;}
    
    public void switchWeapon() {this.currentWeaponIndex = (this.currentWeaponIndex + 1) % this.inventory.size();}
    
    public Weapon getCurrentWeapon() {return this.inventory.get(currentWeaponIndex);}

    public Projectile fire() {
        double translatedX = this.x + WIDTH / 2 + this.spriteAnimationPistolIdle.getOffset().getX() * Math.cos(this.r) - this.spriteAnimationPistolIdle.getOffset().getY() * Math.sin(this.r);
        double translatedY = this.y + HEIGHT / 2 + this.spriteAnimationPistolIdle.getOffset().getX() * Math.sin(this.r) + this.spriteAnimationPistolIdle.getOffset().getY() * Math.cos(this.r);
        return this.getCurrentWeapon().fire((int) translatedX, (int) translatedY, this.r);
    }

    public boolean addNewWeapon(ConfigHandler config, WeaponType type) {
        // Only if that type is not available in the inventory, add the weapon 
        for (Weapon weaponItem: this.inventory) {
            if (weaponItem.getType() == type)
                return false;
        }

        Weapon weapon = WeaponFactory.createWeapon(config, type);
        if (weapon!=null) this.inventory.add(weapon);

        return true;
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
                    inventoryInfo.assaultRifleAmmo = weapon.getAmmo();
                    inventoryInfo.assaultRifleMagazine = weapon.getMagazineCount();
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

    public List<WeaponType> getAvailableWeapons() {
        List<WeaponType> weaponTypes = new ArrayList<>();

        for(Weapon weapon: this.inventory) {
            weaponTypes.add(weapon.getType());
        }
        return weaponTypes;
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
