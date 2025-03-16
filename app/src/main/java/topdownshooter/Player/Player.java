package topdownshooter.Player;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import topdownshooter.Weapon.Weapon;
import topdownshooter.Core.ConfigHandler;
import topdownshooter.Core.Globals;
import topdownshooter.Core.ConfigHandler.PlayerProperties;
import topdownshooter.Core.SequencialSoundFX;
import topdownshooter.Core.SpriteAnimation;
import topdownshooter.Core.SpriteAnimation.Offset;
import topdownshooter.Weapon.WeaponFactory;
import topdownshooter.Weapon.WeaponType;
import topdownshooter.Weapon.Projectiles.Projectile;


public class Player extends JPanel {
    private enum PlayerState {
        IDLE,
        MOVE,
        SHOOT,
    }

    private int score = 0;
    private double health = 0.0;
    private int x, y, dx, dy;
    private double r;
    private int speed;
    private final int WIDTH = 80;
    private final int HEIGHT = 68;
    private LinkedHashMap<WeaponType, Weapon> inventory;
    private WeaponType currentWeaponType = WeaponType.UNDEFINED;

    private Map<WeaponType, Map<PlayerState, SpriteAnimation>> spriteAnimations = null;

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

        this.spriteAnimations = new HashMap<>();

        Map<PlayerState, SpriteAnimation> pistolAnimations = new HashMap<>();
        pistolAnimations.put(PlayerState.IDLE, new SpriteAnimation(Globals.HUNTER_PISTOL_IDLE));
        pistolAnimations.put(PlayerState.MOVE, new SpriteAnimation(Globals.HUNTER_PISTOL_MOVE));
        pistolAnimations.put(PlayerState.SHOOT, new SpriteAnimation(Globals.HUNTER_PISTOL_SHOOT));
        this.spriteAnimations.put(WeaponType.PISTOL, pistolAnimations);

        Map<PlayerState, SpriteAnimation> assultRifleAnimations = new HashMap<>();
        assultRifleAnimations.put(PlayerState.IDLE, new SpriteAnimation(Globals.HUNTER_RIFLE_IDLE));
        assultRifleAnimations.put(PlayerState.MOVE, new SpriteAnimation(Globals.HUNTER_RIFLE_MOVE));
        assultRifleAnimations.put(PlayerState.SHOOT, new SpriteAnimation(Globals.HUNTER_RIFLE_SHOOT));
        this.spriteAnimations.put(WeaponType.ASSAULTRIFLE, assultRifleAnimations);

        Map<PlayerState, SpriteAnimation> shotgunAnimations = new HashMap<>();
        shotgunAnimations.put(PlayerState.IDLE, new SpriteAnimation(Globals.HUNTER_SHOTGUN_IDLE));
        shotgunAnimations.put(PlayerState.MOVE, new SpriteAnimation(Globals.HUNTER_SHOTGUN_MOVE));
        shotgunAnimations.put(PlayerState.SHOOT, new SpriteAnimation(Globals.HUNTER_SHOTGUN_SHOOT));
        this.spriteAnimations.put(WeaponType.SHOTGUN, shotgunAnimations);

        Map<PlayerState, SpriteAnimation> sniperRifleAnimations = new HashMap<>();
        sniperRifleAnimations.put(PlayerState.IDLE, new SpriteAnimation(Globals.HUNTER_RIFLE_IDLE));
        sniperRifleAnimations.put(PlayerState.MOVE, new SpriteAnimation(Globals.HUNTER_RIFLE_MOVE));
        sniperRifleAnimations.put(PlayerState.SHOOT, new SpriteAnimation(Globals.HUNTER_RIFLE_SHOOT));
        this.spriteAnimations.put(WeaponType.SNIPERRIFLE, sniperRifleAnimations);
        
        Map<PlayerState, SpriteAnimation> rocketLauncherAnimations = new HashMap<>();
        rocketLauncherAnimations.put(PlayerState.IDLE, new SpriteAnimation(Globals.HUNTER_ROCKET_LAUNCHER_MOVE));
        rocketLauncherAnimations.put(PlayerState.MOVE, new SpriteAnimation(Globals.HUNTER_ROCKET_LAUNCHER_IDLE));
        rocketLauncherAnimations.put(PlayerState.SHOOT, new SpriteAnimation(Globals.HUNTER_ROCKET_LAUNCHER_SHOOT));
        this.spriteAnimations.put(WeaponType.ROCKETLAUNCHER, rocketLauncherAnimations);

        for (Map<PlayerState, SpriteAnimation> weaponAnimation : spriteAnimations.values()) {
            for (SpriteAnimation stateAnimation : weaponAnimation.values()) {
                stateAnimation.setTargetSize(WIDTH, HEIGHT);
            }
        }

        this.inventory = new LinkedHashMap<WeaponType, Weapon>();
        // Every player starts with a pistol
        addNewWeapon(config, WeaponType.PISTOL);
        //this.inventory.add(WeaponFactory.createWeapon(config, WeaponType.ASSAULTRIFLE));
        //this.inventory.add(WeaponFactory.createWeapon(config, WeaponType.SHOTGUN));
        //this.inventory.add(WeaponFactory.createWeapon(config, WeaponType.SNIPERRIFLE));
        //this.inventory.add(WeaponFactory.createWeapon(config, WeaponType.ROCKETLAUNCHER));

        this.walkSoundFX = new SequencialSoundFX(Globals.HUNTER_SOUND_FX_PATH);
    }

    public Player(int score, double health, int x, int y, int dx, int dy, double r, int speed, LinkedHashMap<WeaponType, Weapon> inventory, WeaponType currentWeaponIndex) {
        this.score = score;
        this.health = health;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.r = r;
        this.speed = speed;
        this.inventory = inventory;
        this.currentWeaponType = currentWeaponIndex;

        this.spriteAnimations = new HashMap<>();

        Map<PlayerState, SpriteAnimation> pistolAnimations = new HashMap<>();
        pistolAnimations.put(PlayerState.IDLE, new SpriteAnimation(Globals.HUNTER_PISTOL_IDLE));
        pistolAnimations.put(PlayerState.MOVE, new SpriteAnimation(Globals.HUNTER_PISTOL_MOVE));
        pistolAnimations.put(PlayerState.SHOOT, new SpriteAnimation(Globals.HUNTER_PISTOL_SHOOT));
        this.spriteAnimations.put(WeaponType.PISTOL, pistolAnimations);

        Map<PlayerState, SpriteAnimation> assultRifleAnimations = new HashMap<>();
        assultRifleAnimations.put(PlayerState.IDLE, new SpriteAnimation(Globals.HUNTER_RIFLE_IDLE));
        assultRifleAnimations.put(PlayerState.MOVE, new SpriteAnimation(Globals.HUNTER_RIFLE_MOVE));
        assultRifleAnimations.put(PlayerState.SHOOT, new SpriteAnimation(Globals.HUNTER_RIFLE_SHOOT));
        this.spriteAnimations.put(WeaponType.ASSAULTRIFLE, assultRifleAnimations);

        Map<PlayerState, SpriteAnimation> shotgunAnimations = new HashMap<>();
        shotgunAnimations.put(PlayerState.IDLE, new SpriteAnimation(Globals.HUNTER_SHOTGUN_IDLE));
        shotgunAnimations.put(PlayerState.MOVE, new SpriteAnimation(Globals.HUNTER_SHOTGUN_MOVE));
        shotgunAnimations.put(PlayerState.SHOOT, new SpriteAnimation(Globals.HUNTER_SHOTGUN_SHOOT));
        this.spriteAnimations.put(WeaponType.SHOTGUN, shotgunAnimations);

        Map<PlayerState, SpriteAnimation> sniperRifleAnimations = new HashMap<>();
        sniperRifleAnimations.put(PlayerState.IDLE, new SpriteAnimation(Globals.HUNTER_RIFLE_IDLE));
        sniperRifleAnimations.put(PlayerState.MOVE, new SpriteAnimation(Globals.HUNTER_RIFLE_MOVE));
        sniperRifleAnimations.put(PlayerState.SHOOT, new SpriteAnimation(Globals.HUNTER_RIFLE_SHOOT));
        this.spriteAnimations.put(WeaponType.SNIPERRIFLE, sniperRifleAnimations);
        
        Map<PlayerState, SpriteAnimation> rocketLauncherAnimations = new HashMap<>();
        rocketLauncherAnimations.put(PlayerState.IDLE, new SpriteAnimation(Globals.HUNTER_ROCKET_LAUNCHER_MOVE));
        rocketLauncherAnimations.put(PlayerState.MOVE, new SpriteAnimation(Globals.HUNTER_ROCKET_LAUNCHER_IDLE));
        rocketLauncherAnimations.put(PlayerState.SHOOT, new SpriteAnimation(Globals.HUNTER_ROCKET_LAUNCHER_SHOOT));
        this.spriteAnimations.put(WeaponType.ROCKETLAUNCHER, rocketLauncherAnimations);

        for (Map<PlayerState, SpriteAnimation> weaponAnimation : spriteAnimations.values()) {
            for (SpriteAnimation stateAnimation : weaponAnimation.values()) {
                stateAnimation.setTargetSize(WIDTH, HEIGHT);
            }
        }

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
        for (Weapon w : this.inventory.values()) {
            w.update();
        }

        // Update sprite animation
        WeaponType currentWeaponType = this.inventory.get(this.currentWeaponType).getType();
        if (currentWeaponType != WeaponType.UNDEFINED) this.spriteAnimations.get(currentWeaponType).get(PlayerState.IDLE).update();
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g; // Enable rotation

        AffineTransform oldTransform = g2d.getTransform();

        g2d.setColor(Color.GREEN);
        g2d.translate(x + WIDTH / 2, y + HEIGHT / 2);
        g2d.rotate(r);  // Rotate to face the player
        g2d.fillRect(-WIDTH / 2, -HEIGHT / 2, WIDTH, WIDTH);

        // Reset transformation
        g2d.setTransform(oldTransform);

        // Draw sprite animations of player according to weapon type
        PlayerState playerState = PlayerState.IDLE;
        WeaponType currentWeaponType = this.inventory.get(this.currentWeaponType).getType();

        if (currentWeaponType == WeaponType.UNDEFINED) return;

        this.spriteAnimations.get(currentWeaponType).get(playerState).draw(g, this.x, this.y, this.r);

        // Draw weapon animation
        Offset offset = this.spriteAnimations.get(currentWeaponType).get(playerState).getOffset();
        this.inventory.get(this.currentWeaponType).draw(g, this.x + offset.getX(), this.y + offset.getY(), this.r);


        AffineTransform oldTransform2 = g2d.getTransform();

        g2d.setColor(Color.ORANGE);
        g2d.translate(this.x + WIDTH / 2, this.y + HEIGHT / 2);
        g2d.rotate(this.r); 
        g2d.fillRect(0, 0, 2, 2);
        
        g2d.setTransform(oldTransform2);
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
        for (Weapon w: this.inventory.values()) {
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
    
    public void switchWeapon() {
        List<WeaponType> availableWeapons = new ArrayList<>(this.inventory.keySet());
        int currentWeaponIndex = availableWeapons.indexOf(this.currentWeaponType);
        int nextWeaponIndex = (currentWeaponIndex + 1) % availableWeapons.size();

        this.currentWeaponType = availableWeapons.get(nextWeaponIndex);
    }
    
    public Weapon getCurrentWeapon() {return this.inventory.get(currentWeaponType);}

    public Projectile fire() {
        PlayerState playerState = PlayerState.IDLE;
        WeaponType currentWeaponType = this.inventory.get(this.currentWeaponType).getType();
        Offset offset = this.spriteAnimations.get(currentWeaponType).get(playerState).getOffset();

        double translatedX = this.x + WIDTH / 2 + offset.getX() * Math.cos(this.r) - offset.getY() * Math.sin(this.r);
        double translatedY = this.y + HEIGHT / 2 + offset.getX() * Math.sin(this.r) + offset.getY() * Math.cos(this.r);
        return this.getCurrentWeapon().fire((int) translatedX, (int) translatedY, this.r);
    }

    public boolean addNewWeapon(ConfigHandler config, WeaponType type) {
        // Only if that type is not available in the inventory, add the weapon 
        for (Weapon weaponItem: this.inventory.values()) {
            if (weaponItem.getType() == type)
                return false;
        }

        Weapon weapon = WeaponFactory.createWeapon(config, type);
        if (this.inventory.isEmpty() && weapon!=null) this.currentWeaponType = weapon.getType();
        if (weapon!=null) this.inventory.put(weapon.getType(), weapon);

        return true;
    }

    public InventoryInfo getInventoryInfo() {
        InventoryInfo inventoryInfo = new InventoryInfo();
        
        inventoryInfo.selectedWeaponType = this.currentWeaponType;


        for (Weapon weapon: this.inventory.values()) {
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

        for(Weapon weapon: this.inventory.values()) {
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
        sb.append("currentWeaponType=" + this.currentWeaponType);
        sb.append("}");

        return sb.toString();
    }
}
