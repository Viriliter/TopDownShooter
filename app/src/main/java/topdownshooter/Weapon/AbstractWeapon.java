/*
 * @file AbstractWeapon.java
 * @brief This file defines the `AbstractWeapon` class.
 *
 * Created on Wed Mar 19 2025
 *
 * @copyright MIT License
 *
 * Copyright (c) 2025 Mert LIMONCUOGLU
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package topdownshooter.Weapon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import topdownshooter.Core.Globals;
import topdownshooter.Core.SoundFX;
import topdownshooter.Core.SpriteAnimation;
import topdownshooter.Core.SpriteAnimation.Offset;
import topdownshooter.Core.TimeTick;
import topdownshooter.Core.ConfigHandler.WeaponProperties;
import topdownshooter.Weapon.Projectiles.Projectile;

/**
 * @abstractclass AbstractWeapon
 * @brief Abstract class representing a generic Weapon in the game.
 * 
 * This class defines the common properties and behaviors of all weapon types,
 * including ammo, magazine capacity, reload behavior, and fire rate. This class also 
 * handles the firing of projectiles, reload mechanics, and sound effects for weapon actions. 
 * Subclasses should extend this class and implement the specific behavior for each weapon type.
 * 
 * @see Weapon
 * @see Projectile
 */
public abstract class AbstractWeapon implements Weapon {
    protected int damage;                               /** The damage dealt by the weapon per shot. */
    protected int magazineCapacity;                     /** The maximum capacity of the weapon's magazine. */
    protected int magazineCount;                        /** The current number of magazines available for the weapon. */
    protected int fireRate;                             /** The rate of fire for the weapon (rounds per minute). */
    protected int reloadDuration;                       /** The duration it takes to reload the weapon in seconds. */
    protected int ammo;                                 /** The current ammo count in the magazine. */
    protected TimeTick reloadTick;                      /** A time tick to manage reloading events. */
    protected TimeTick fireTick;                        /** A time tick to manage firing events and cooldowns. */
    protected WeaponType type = WeaponType.UNDEFINED;   /** The type of weapon (Default UNDEFINED). */

    protected SoundFX firingSoundFX = null;             /** Sound effect for weapon fire. */
    protected SoundFX emptyClickSoundFX = null;         /** Sound effect for trigger click of empty ammo */
    protected SpriteAnimation weaponAnimation = null;   /** Weapon animation (e.g Flash animation for muzzle) */

    /**
     * Default constructor for AbstractWeapon.
     * 
     */
    public AbstractWeapon() {}

    /**
     * Constructor that initializes the weapon with specific properties.
     * 
     * @param properties The properties for the weapon.
     */
    public AbstractWeapon(WeaponProperties properties) {
        this.damage = properties.damage();
        this.fireRate = properties.fireRate() > 0 ? properties.fireRate(): 1;  // FireRate (1/min) cannot be zero
        this.magazineCapacity = properties.magazineCapacity();
        this.magazineCount = properties.magazineCount();
        this.reloadDuration = properties.reloadDuration();  // In seconds

        this.ammo = this.magazineCapacity;  // Create the weapon fully loaded
        this.fireTick = new TimeTick(Globals.Time2GameTick(60*1000/this.fireRate));
        this.fireTick.setRepeats(-1);  // Repeates indefinetly
        this.reloadTick = new TimeTick(0/*Globals.Time2Tick(1000/this.reloadDuration)*/);
        this.reloadTick.setRepeats(-1);  // Repeates indefinetly

        this.emptyClickSoundFX = new SoundFX(Globals.EMPTY_GUN_CLICK_SOUND_FX_PATH); 
    }

    @Override
    abstract public Projectile fire(int x, int y, double r);

    @Override
    public void reload() {
        if (reloadTick.isTimeOut() && ammo < this.magazineCapacity) {
            reloadTick.reset();
            if (magazineCount == -1) {  // Infinity number of magazine count
                this.ammo = this.magazineCapacity;
            } else if (magazineCount > 0){
                this.magazineCount--;
                this.ammo = this.magazineCapacity;
            } else {
                System.out.println("Out of ammo");
            }
        }
    }

    @Override
    public void update() {
        this.fireTick.updateTick();
        this.reloadTick.updateTick();

        if (this.weaponAnimation!=null) this.weaponAnimation.update();
    }
    
    @Override
    public void draw(Graphics g, int x, int y, double r) {
        if (this.weaponAnimation == null) return;
        
        Offset offset = this.weaponAnimation.getOffset();
        double translatedX = x + offset.getX() * Math.cos(r) - offset.getY() * Math.sin(r);
        double translatedY = y + offset.getX() * Math.sin(r) + offset.getY() * Math.cos(r);

        //Graphics2D g2d = (Graphics2D) g; // Enable rotation
//
        //AffineTransform oldTransform = g2d.getTransform();
//
        //g2d.setColor(Color.WHITE);
        //g2d.translate(translatedX, translatedY);
        //g2d.rotate(r);  // Rotate to face the player
        //g2d.fillRect(-20 / 2, -20 / 2, 20, 20);
//
        //// Reset transformation
        //g2d.setTransform(oldTransform);

        if (this.weaponAnimation!=null) this.weaponAnimation.draw2(g, (int) translatedX, (int) translatedY, r);
    }

    @Override
    public void addMagazine(int magazineCount) {
        // Only if magazine count is positive, add to the magazine. Negative magazine count means that it is infinite.
        if (this.magazineCount>=0) this.magazineCount += magazineCount;
    }

    @Override
    public int getAmmo() {
        return this.ammo;
    }
   
    @Override
    public int getMagazineCapacity() {
        return this.magazineCapacity;
    }

    @Override
    public int getMagazineCount() {
        return this.magazineCount;
    }

    @Override
    public int getReloadDuration() {
        return this.reloadDuration;
    }

    @Override
    public int getFireRate() {
        return this.fireRate;
    }

    @Override
    public int getDamage() {
        return this.damage;
    }

    @Override
    public WeaponType getType() {
        return this.type;
    }

    /**
     * Plays the firing sound effect for the weapon.
     */
    protected final void applySoundFX() {
        this.firingSoundFX.play(false);
    }

    @Override
    abstract public String toString();
}
