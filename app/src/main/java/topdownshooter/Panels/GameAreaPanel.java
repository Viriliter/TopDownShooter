/*
 * @file GameAreaPanel.java
 * @brief This file defines the `GameAreaPanel` class.
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

package topdownshooter.Panels;

import javax.swing.*;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.awt.*;
import java.awt.event.*;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import topdownshooter.Core.Globals;
import topdownshooter.Core.SoundFX;
import topdownshooter.Core.TileGenerator;
import topdownshooter.Core.ConfigHandler;
import topdownshooter.Core.BlastFX;
import topdownshooter.Core.GameLevel;
import topdownshooter.Core.TimeTick;
import topdownshooter.Core.BlastFX.BlastType;
import topdownshooter.Player.Loot;
import topdownshooter.Player.Player;
import topdownshooter.Weapon.WeaponType;
import topdownshooter.Weapon.Projectiles.AcidSpit;
import topdownshooter.Weapon.Projectiles.ArmorPiercingBullet;
import topdownshooter.Weapon.Projectiles.Bullet;
import topdownshooter.Weapon.Projectiles.Projectile;
import topdownshooter.Weapon.Projectiles.ProjectileType;
import topdownshooter.Weapon.Projectiles.Rocket;
import topdownshooter.Weapon.Projectiles.ShotgunPellets;
import topdownshooter.Zombie.AcidZombie;
import topdownshooter.Zombie.Zombie;
import topdownshooter.Zombie.ZombieType;

/**
 * @class GameAreaPanel
 * @brief Panel that handles the main gameplay area, including game objects, interactions, and rendering.
 * 
 * This class is responsible for managing and rendering the game area. It interacts with the player, zombies, 
 * projectiles, loot, blast effects, and the game level. It handles the game loop, collision detection, and state 
 * updates (e.g., pausing, resuming, saving, loading, etc.). The panel also responds to key presses and action events 
 * to control the gameplay.
 */
public class GameAreaPanel extends JPanel implements ActionListener, KeyListener{
    private GamePanel parentPanel = null;                   /*< Reference to the parent game panel. */
    private ConfigHandler config;                           /*< Configuration handler for game settings. */

    private GameLevel gameLevel = null;                     /*< The current game level. */
    private Player player = null;                           /*< The player object in the game. */
    private ArrayList<Zombie> zombies = null;               /*< List of zombies in the game. */
    private ArrayList<Projectile> projectiles = null;       /*< List of projectiles fired by the player. */
    private ArrayList<Loot> loots = null;                   /*< List of loot items in the game. */
    private ArrayList<BlastFX> blastFXs = null;             /*< List of blast effects in the game. */
    
    private static boolean isGamePaused = false;            /*< Flag indicating whether the game is paused. */
    private Timer gameTimer;                                /*< Timer for handling periodic updates. */
    private TimeTick fireRateTick = null;                   /*< Time tick for handling fire rate control. */
    
    private TileGenerator playgroundTileGenerator = null;   /*< Tile generator for creating the game terrain. */

    private SoundFX backgroundSoundFX = null;               /*< Background sound effects. */

    /**
     * Constructs the GameAreaPanel with the provided configuration handler.
     * 
     * Initializes the panel with the necessary configuration and sets up the initial state.
     * 
     * @param config The configuration handler that contains the game settings.
     */
    public GameAreaPanel(ConfigHandler config) {
        setFocusable(true);
        requestFocusInWindow();
        setBackground(Color.BLACK);
        addKeyListener(this);

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (GameAreaPanel.isGamePaused) return;  // Do not move player when game is paused

                double rRad = Math.atan2(e.getY() - player.getY(), e.getX() - player.getX());
                player.rotate(rRad);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (GameAreaPanel.isGamePaused) return;  // Do not move player when game is paused

                double rRad = Math.atan2(e.getY() - player.getY(), e.getX() - player.getX());
                player.rotate(rRad);
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startFireTimer();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                stopFire();
            }
        });
        
        this.config = config;

        // Create game objects
        gameLevel = new GameLevel(this.config);

        player = new Player(this.config);
        zombies = new ArrayList<>();
        projectiles = new ArrayList<>();
        loots = new ArrayList<>();

        gameTimer = new Timer(Globals.GAME_TICK_MS, e -> this.update());
        gameTimer.setRepeats(true);

        this.playgroundTileGenerator = new TileGenerator(Globals.PLAYGROUND_TILE_PATH);
        this.blastFXs = new ArrayList<>();

        this.backgroundSoundFX = new SoundFX(Globals.BACKGROUND_SOUND_FX_PATH);
    }

    /**
     * Called when the panel is added to the container.
     * 
     * This method is overridden to set this panel for focus which is required for keyboard events.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    /**
     * Custom paint method for rendering the game area.
     * 
     * This method is responsible for drawing all game objects (player, zombies, projectiles, etc.) on the screen.
     * 
     * @param g The Graphics object used to render the game area.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
     
        this.playgroundTileGenerator.draw(g, getWidth(), getHeight());
        this.player.draw(g);

        for (Zombie z : this.zombies) {
            z.draw(g);
        }

        for (Projectile projectile : this.projectiles) {
            if (projectile == null) continue;
            projectile.draw(g);
        }

        for (Loot loot : this.loots) {
            if (loot == null) continue;
            loot.draw(g);
        }

        for (BlastFX bFX : this.blastFXs) {
            bFX.draw(g);
        }
    }

    /**
     * Handles action events for the game.
     * 
     * Not implemented.
     * 
     * @param e The action event that occurred.
     */
    @Override
    public void actionPerformed(ActionEvent e) {}

    /**
     * Sets the parent panel for this game area.
     * 
     * This method links the current game area panel to the parent panel.
     * 
     * @param panel The parent GamePanel to link.
     */
    public void setParentPanel(GamePanel panel) {
        this.parentPanel = panel;
    }

    /**
     * Starts a new wave of zombies in the game.
     * 
     * This method triggers the spawning of a new wave of zombies.
     */
    private void startWave() {
        if (this.gameLevel.getWaveStatus()== GameLevel.GameLevelStatus.UNDEFINED ||
            this.gameLevel.getWaveStatus()== GameLevel.GameLevelStatus.ENDED)  {

            this.backgroundSoundFX.stop();

            this.player.addScore(this.gameLevel.calculateLevelBonus());  // Add level bonus into player's score

            WeaponType weaponPrize = this.gameLevel.startWave();                

            if (weaponPrize!=null && weaponPrize!=WeaponType.UNDEFINED) {
                this.player.addNewWeapon(config, weaponPrize);
            }
            
            String message = "Wave " + this.gameLevel.getLevel() + " Approaching!";
            this.parentPanel.getNotificationPanel().show(message, Globals.WAVE_SUSPEND_DURATION_MS);
            this.backgroundSoundFX.delayedPlay(true, Globals.WAVE_SUSPEND_DURATION_MS);
        }
    }

    /**
     * Updates all game objects, and draw them into the screen accordingly.
     * 
     * This method is called periodically to update all the game objects and check for collisions.
     */
    public void update() {
        updatePlayer();

        updateGameLevel();

        updateZombies();

        updateProjectiles();

        updateLoots();

        checkCollisions();

        if (this.fireRateTick!=null) this.fireRateTick.updateTick();
        
        updateGameInfo();

        updateBlasts();

        repaint();
    }

    /**
     * Updates the player state, such as position and status.
     */
    private void updatePlayer() {
        if (this.player==null) return;

        this.player.update(getWidth(), getHeight());

        if (this.player.getHealth() <= 0) {
            this.endGame();
        }
    }

    /**
     * Updates the game level status, including checking level progression.
     */
    private void updateGameLevel() {
        if (this.gameLevel==null || this.player == null || this.zombies == null) return;

        if (this.zombies.size() == 0) {
            startWave();
        }

        Zombie zombie = this.gameLevel.update(getWidth(), getHeight());
        if (zombie != null) zombies.add(zombie);
    }

    /**
     * Updates the zombies, including their movement and interactions.
     */
    private void updateZombies() {
        if (this.player==null ||this.zombies==null) return;

        // Move zombies towards player
        for (Zombie z : zombies) {
            z.update(player.getTargetBounds());

            // Acid zombies have special ranged attack
            if (z.getType() == ZombieType.ACID) {
                AcidZombie acidZombie = (AcidZombie) z;
                Projectile projectile = (Projectile) acidZombie.rangedAttack();
                if (projectile != null) this.projectiles.add(projectile);
            }
        }
    }

    /**
     * Updates the projectiles, including their movement and interactions.
     */
    private void updateProjectiles() {
        if (this.projectiles==null) return;

        Iterator<Projectile> iterator = this.projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();

            // If projectile is out of bounds, remove it
            if (projectile.isOutOfBounds(getWidth(), getHeight())) {
                iterator.remove();
            } else {
                projectile.move();
            }
        }
    }

    /**
     * Updates the loot, including their position and the state.
     */
    private void updateLoots() {
        if (this.player==null || this.loots==null) return;

        for (Loot loot : this.loots) {
            loot.update();
        }

        // Remove old loots
        ListIterator<Loot> lootIterator = this.loots.listIterator();
        while (lootIterator.hasNext()) {
            Loot loot = lootIterator.next();
            if (Globals.GameTick2Time(loot.getLootAge()) > Globals.MAX_LOOT_DURATION) {
                lootIterator.remove();
            }
        }
    }

    /**
     * Updates the blast effects in the game area.
     */
    private void updateBlasts() {
        if (this.blastFXs==null) return;

        Iterator<BlastFX> blastFXIterator = this.blastFXs.listIterator();
        while (blastFXIterator.hasNext()) {
            BlastFX bFX = blastFXIterator.next();
            boolean isUpdated = bFX.update();
            if (!isUpdated) blastFXIterator.remove();  // If blast animation has finished, it returns false so remove it.
        }
    }

    /**
     * Checks for collisions between projectiles and other game objects (e.g. Zombie and Player).
     */
    private void checkProjectileCollisions() {
        if (this.player==null ||this.zombies==null || this.projectiles==null) return;

        ListIterator<Projectile> projectileIterator = this.projectiles.listIterator();
        
        while (projectileIterator.hasNext()) {
            Projectile projectile = projectileIterator.next();

            if (projectile.getType() == ProjectileType.BULLET) {
                Bullet bullet = (Bullet) projectile;

                ListIterator<Zombie> zombieIterator = this.zombies.listIterator();
                while (zombieIterator.hasNext()) {
                    Zombie zombie = zombieIterator.next();                   
                    if (Globals.isObjectsCollided(bullet.getBounds(), zombie.getBounds())) {
                        zombie.takeDamage(bullet.getDamage());
                        projectileIterator.remove();  // After damaging zombie, remove it.
                        break;
                    }
                }
            } else if (projectile.getType() == ProjectileType.ARMOR_PIERCING_BULLET) {
                ArmorPiercingBullet bullet = (ArmorPiercingBullet) projectile;

                ListIterator<Zombie> zombieIterator = this.zombies.listIterator();
                while (zombieIterator.hasNext()) {
                    Zombie zombie = zombieIterator.next();
                    if (Globals.isObjectsCollided(bullet.getBounds(), zombie.getBounds())) {
                        zombie.takeDamage(bullet.getDamage());
                        // Do not remove armor piercing bullets on collision with zombie.
                    }
                }
            } else if (projectile.getType() == ProjectileType.ROCKET) {
                Rocket rocket = (Rocket) projectile;
                
                Boolean isProjectileDetonated = false;
                ListIterator<Zombie> zombieIterator = this.zombies.listIterator();
                while (zombieIterator.hasNext()) {
                    Zombie zombie = zombieIterator.next();
                    if (Globals.isObjectsCollided(rocket.getBounds(), zombie.getBounds())) {
                        // Rocket damages its surrounded area
                        damageZombies(rocket.getX(), rocket.getY(), rocket.getDamage(), rocket.getEffectiveRange());
                        isProjectileDetonated = true;
                        break;  // No need to continue since explosion applies for all zombies in range
                    }
                }
                if (isProjectileDetonated) {
                    // Create blast effect and add into the blast list
                    this.blastFXs.add(new BlastFX(BlastType.EXPLOSIVE_BLAST, rocket.getX(), rocket.getY(), rocket.getEffectiveRange()));

                    projectileIterator.remove();  // After detonation, remove it.
                }

            } else if (projectile.getType() == ProjectileType.SHOTGUN_PELLETS) {
                ShotgunPellets pellets = (ShotgunPellets) projectile;
                
                // If all pellets are destroyed remove the projectile
                if (pellets.getPellets().size() == 0) {
                    projectileIterator.remove();
                    continue;
                }

                ListIterator<Zombie> zombieIterator = this.zombies.listIterator();
                while (zombieIterator.hasNext()) {
                    Zombie zombie = zombieIterator.next();

                    ListIterator<Bullet> pelletIterator = pellets.getPellets().listIterator();
                    while (pelletIterator.hasNext()) {
                        Bullet bullet = pelletIterator.next();
                        if (Globals.isObjectsCollided(bullet.getBounds(), zombie.getBounds())) {
                            zombie.takeDamage(bullet.getDamage());
                            pelletIterator.remove();  // After damaging zombie, remove it.
                            break;
                        }
                    }
                }
            } else if (projectile.getType() == ProjectileType.ACID_SPIT) {
                AcidSpit acidSpit = (AcidSpit) projectile;

                if (Globals.isObjectsCollided(acidSpit.getBounds(), this.player.getTargetBounds())) {
                    this.player.takeDamage(acidSpit.getDamage());
                    projectileIterator.remove();  // After contact with player, remove it.
                }

            }
        }
    
        ListIterator<Zombie> zombieIterator = this.zombies.listIterator();
        while (zombieIterator.hasNext()) {
            Zombie zombie = zombieIterator.next();

            // If health of zombie is non positive, it is killed
            if (zombie.getHealth() < 0) {              
                ZombieType zombieType = zombie.getType();
                int zombieX = zombie.getX();
                int zombieY = zombie.getY();
                int zombieDamage = zombie.giveDamage();

                // Retrieve loot and score when zombie killed.
                Loot loot = zombie.kill(this.player.getAvailableWeapons());  // To generate loot, available weapon types are needed.
                
                if (loot != null) {
                    this.player.addScore(loot.getScore());
                    // If loot does not have an item, then do not add to the list
                    if (loot.getItem() != null) this.loots.add(loot);
                }

                zombieIterator.remove();

                // Acid zombies damages their surrounds when they killed (reduce original damage of the zombie when it explodes.)
                if (zombieType == ZombieType.ACID) {
                    damageZombies(zombieX, zombieY, (int) ((double) zombieDamage * 0.3), AcidZombie.EFFECTIVE_RANGE);
                    damagePlayer(zombieX, zombieY, (int) ((double) zombieDamage * 0.3), AcidZombie.EFFECTIVE_RANGE);

                    // Create blast effect and add into the blast list
                    this.blastFXs.add(new BlastFX(BlastType.TOXIC_BLAST, zombieX, zombieY, AcidZombie.EFFECTIVE_RANGE));
                }
            }
        }
    }

    /**
     * Checks for collisions between zombies and the player.
     * 
     * This method updates zombie health if a collision occurs.
     */
    private void checkZombieCollisions() {
        if (this.player==null ||this.zombies==null) return;

        Iterator<Zombie> zombieIterator = this.zombies.iterator();
        while (zombieIterator.hasNext()) {
            Zombie zombie = zombieIterator.next();
            // If zombie collides with the player, it gives damage by attacking
            boolean isColliding = Globals.isObjectsCollided(zombie.getBounds(), player.getTargetBounds());

            if (isColliding) {
                // Normalize damage according to game tick (Full damage is taken by player in 500ms)
                double damagePerTick = (double) zombie.giveDamage() * ((double) Globals.GAME_TICK_MS / Globals.FULL_DAMAGE_PERIOD);
                this.player.takeDamage(damagePerTick);
            }
        }
    }

    /**
     * Checks for collisions between loot and the player.
     */
    private void checkLootCollisions() {
        ListIterator<Loot> lootIterator = this.loots.listIterator();

        while (lootIterator.hasNext()) {
            Loot loot = lootIterator.next();
            if (Globals.isObjectsCollided(loot.getBounds(), this.player.getTargetBounds())) {
                this.player.addLoot(loot);
                lootIterator.remove();  // Remove the loot after player takes it
            }
        }
    }

    /**
     * Checks for any collisions between the various game objects (player, zombies, projectiles, loot).
     */
    private void checkCollisions() {
        // Projectile collisions
        checkProjectileCollisions();

        // Zombie collisions to the survivor (aka zombie attack)
        checkZombieCollisions();

        // Player collision to loot objects
        checkLootCollisions();
    }

    /**
     * Calculates the squared distance between two points to avoid using the costly square root operation.
     * 
     * @param x1 The x-coordinate of the first point.
     * @param y1 The y-coordinate of the first point.
     * @param x2 The x-coordinate of the second point.
     * @param y2 The y-coordinate of the second point.
     * @return The squared distance between the two points.
     */
    private double getDistanceSquared(int x1, int y1, int x2, int y2) {
        int dx = x1 - x2;
        int dy = y1 - y2;
        return dx * dx + dy * dy;
    }
    
    /**
     * Calculates the damage dealt to an entity based on the distance from the origin.
     * 
     * The damage decreases based on the distance squared from the origin compared to the effective range.
     * 
     * @param originDamage The damage of the attack at the origin point.
     * @param distanceSquared The squared distance from the attack's origin to the target.
     * @param effectiveRangeSquared The squared effective range of the attack.
     * @param effectiveRange The maximum effective range of the attack. (No damage if the distance is more than that)
     * @return The calculated damage to be applied.
     */
    private double calculateDamage(int originDamage, double distanceSquared, double effectiveRangeSquared, int effectiveRange) {
        if (distanceSquared == 0) {
            return originDamage;
        } else if (distanceSquared < effectiveRangeSquared && effectiveRange > 0) {
            return (double) originDamage * (1 - (Math.sqrt(distanceSquared) / (4 * effectiveRange)));
        } else if (distanceSquared == effectiveRangeSquared) {
            return (double) originDamage * 0.25;
        }
        return 0;
    }

    /**
     * Damages zombies within range of a specific attack.
     * 
     * This method applies damage to zombies based on their distance from the origin of the attack.
     * 
     * @param originX The x-coordinate of the attack's origin.
     * @param originY The y-coordinate of the attack's origin.
     * @param originDamage The damage of the attack at the origin point.
     * @param effectiveRange The maximum effective range of the attack. (No damage if the distance is more than that)
     */
    private void damageZombies(int originX, int originY, int originDamage, int effectiveRange) {
        double effectiveRangeSquared = effectiveRange * effectiveRange;

        ListIterator<Zombie> zombieIterator = zombies.listIterator();
        while (zombieIterator.hasNext()) {
            Zombie zombie = zombieIterator.next();
            topdownshooter.Core.Position position = zombie.getPosition();
            double distanceSquared = getDistanceSquared(position.x(), position.y(), originX, originY);  // Calculate distance from origin point of detonation

            // Calculate detonation damage with the function of the distance from origin
            double damage = calculateDamage(originDamage, distanceSquared, effectiveRangeSquared, effectiveRange);

            if (damage>0) {
                zombie.takeDamage(damage);
            }
        }
    }

    /**
     * Damages the player if they are within range of an attack.
     * 
     * This method applies damage to the player based on their distance from the attack's origin.
     * 
     * @param originX The x-coordinate of the attack's origin.
     * @param originY The y-coordinate of the attack's origin.
     * @param originDamage The damage of the attack at the origin point.
     * @param effectiveRange The maximum effective range of the attack. (No damage if the distance is more than that)
     */
    private void damagePlayer(int originX, int originY, int originDamage, int effectiveRange) {
        double effectiveRangeSquared = effectiveRange * effectiveRange;

        double distanceSquared = getDistanceSquared(this.player.getX(), this.player.getY(), originX, originY);  // Calculate distance from origin point of detonation

        // Calculate detonation damage with the function of the distance from origin
        double damage = calculateDamage(originDamage, distanceSquared, effectiveRangeSquared, effectiveRange);

        if (damage>0) {
            this.player.takeDamage(damage);
        }
    }

    /**
     * Updates the game information (score, level, etc.) based on the current game state.
     */
    private void updateGameInfo() {
        GameInfoPanel gameInfoPanel = this.parentPanel.getGameInfoPanel();
        gameInfoPanel.updatePlayerHealth(this.player==null ? 0 : this.player.getHealth());
        gameInfoPanel.updatePlayerScore(this.player==null ? 0 : this.player.getScore());
        gameInfoPanel.updateGameLevel(this.gameLevel==null ? 0 : this.gameLevel.getLevel());
        gameInfoPanel.updateGameRemainingTime(this.gameLevel==null ? 0 : this.gameLevel.getRemainingTime());
        gameInfoPanel.updateRemainingZombieCount(this.gameLevel==null ? 0 : this.gameLevel.getRemainingZombies());
        gameInfoPanel.updatePlayerInventory(this.player.getInventoryInfo());
    }

    /**
     * Starts the game, initializing all necessary game objects and state.
     */
    public void startGame() {
        GameAreaPanel.isGamePaused = false;
        gameTimer.start();
    }

    /** 
     * This method pauses the game, freezing all game activity by stoping game timer.
     */
    public void pauseGame() {
        GameAreaPanel.isGamePaused = true;
        gameTimer.stop();
        this.backgroundSoundFX.stop();
    }

    /**
     * Resumes the game from a paused state.
     * 
     * This method resumes all game activity after being paused.
     */
    public void resumeGame() {
        requestFocus();

        gameTimer.start();
        GameAreaPanel.isGamePaused = false;
        this.backgroundSoundFX.play(true);
    }

    /**
     * Shows the game over dialog when the game ends.
     * 
     * This method is called to display the game over screen and any related information.
     */
    private void showGameOverDialog() {
        GameOverPanel gameOverPanel = this.parentPanel.getGameOverPanel();
        gameOverPanel.setPlayerScore(this.player.getScore());
        gameOverPanel.setGameLevel(this.gameLevel.getLevel());
        gameOverPanel.fadeIn();  // Animated pop up 
    }

    /**
     * Resets the game to its initial state.
     * 
     * This method is called when restarting the game to clear all game objects and reset settings.
     */
    private void resetGame() {
        this.loots.clear();
        this.projectiles.clear();
        this.zombies.clear();

        this.player = null;
        this.gameLevel = null;
    }

    /**
     * Serializes the current game objects to an output stream.
     * 
     * @param os The output stream to write the serialized game objects to.
     * @throws IOException If an I/O error occurs during serialization.
     */
    private void serializeGameObjects(ObjectOutputStream os) throws IOException{
        // Serialize player object
        os.writeObject(this.player);
        
        // Serialize game level
        os.writeObject(this.gameLevel);

        // Serialize zombie objects
        os.writeObject(this.zombies);

        // Serialize projectiles
        os.writeObject(this.projectiles);

        // Serialize loots
        os.writeObject(this.loots);

        // Serialize blast effects
        os.writeObject(this.blastFXs);
    }

    /**
     * Creates game objects from a serialized input stream.
     * 
     * @param os The input stream to read the serialized game objects from.
     * @throws IOException If an I/O error occurs during deserialization.
     * @throws ClassNotFoundException If the class definition of a game object is not found.
     */
    @SuppressWarnings("unchecked")
    private void createGameObjects(ObjectInputStream os) throws IOException, ClassNotFoundException{
        try {
            this.player = (Player) os.readObject();
        
            this.gameLevel = (GameLevel) os.readObject();
            this.gameLevel.setConfig(this.config);
        
            this.zombies = (ArrayList<Zombie>) os.readObject();
            
            this.projectiles = (ArrayList<Projectile>) os.readObject();
            
            this.loots = (ArrayList<Loot>) os.readObject();

            this.blastFXs = (ArrayList<BlastFX>) os.readObject();

            /*
            do {
                Object object = os.readObject();
                if (object instanceof GameLevel) {
                    this.gameLevel = (GameLevel) object;
                    this.gameLevel.setConfig(this.config);
                } else if (object instanceof Player) {
                    this.player = (Player) object;
                } else if (object instanceof ArrayList<?>) {
                    ArrayList<?> list = (ArrayList<?>) object;
                    // Get first element of the list to deduce its type 
                    if (!list.isEmpty()) {
                        Object firstElement = list.get(0);
                        
                        if (firstElement instanceof Zombie) {
                            this.zombies = (ArrayList<Zombie>) list;
                        } else if (firstElement instanceof Projectile) {
                            this.projectiles = (ArrayList<Projectile>) list;
                        } else if (firstElement instanceof Loot) {
                            this.loots = (ArrayList<Loot>) list;
                        } else {
                            System.err.println("Array list is illegal to be serialized:" + firstElement.getClass().getName());
                        }
                    } else {
                        System.err.println("Trying to serialize empty array list");
                    }
                } else {
                    System.err.println("Illegal class to be serialized:" + object.getClass().getName());
                }
            } while(true);
            */
        } catch (EOFException e) {
            // Reached to end of file
        }
    }

    /**
     * Loads a saved game from a file.
     * 
     * @param inputStream The input stream from which the saved game data is loaded.
     * @throws IOException If an I/O error occurs during loading.
     * @throws ClassNotFoundException If the class definitions for the saved game objects are not found.
     */
    public void loadGame(FileInputStream inputStream) throws IOException, ClassNotFoundException{
        if (inputStream==null) return;

        ObjectInputStream os = new ObjectInputStream(inputStream);
        
        resetGame();

        createGameObjects(os);
    
        os.close();
    }

    /**
     * Opens a dialog screen to save the current game state to a file.
     */
    public void saveGame() {
        SwingUtilities.invokeLater(() -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save File"); // Set dialog title

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
            String formattedDatetime = LocalDateTime.now().format(formatter);
            String defaultFilename = formattedDatetime + ".dat";
    
            fileChooser.setSelectedFile(new File(defaultFilename));

            int userSelection = fileChooser.showSaveDialog(null); // Open "Save File" dialog

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile(); // Get selected file

                // Save content to the file
                try {
                    FileOutputStream savedFile = new FileOutputStream(fileToSave);
                    try {
                        ObjectOutputStream os = new ObjectOutputStream(savedFile);

                        serializeGameObjects(os);
                    
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
        
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Starts the fire rate timer to control how frequently the player can fire.
     * 
     * It is triggered by mouse events.
     * 
     */
    public void startFireTimer() {
        if (this.fireRateTick == null || this.fireRateTick.isTimeOut()) {
            this.fireRateTick = new TimeTick(Globals.Time2GameTick(Globals.GAME_TICK_MS), () -> triggerFireFunction()); // Calls function every 15ms
            this.fireRateTick.setRepeats(0);  // Repeat only once
        }
    }

    /**
     * Stops firing and cancels the fire rate timer.
     * 
     * It is triggered by mouse events.
     * 
     */
    public void stopFire() {
        this.fireRateTick = null;
    }

    /**
     * Adds a projectile to the projectile list when the player shoots.
     */
    public void triggerFireFunction() {
        Projectile projectile = player.fire();
        if (projectile != null)
            this.projectiles.add(projectile);
    }

    /**
     * Ends the game and transitions to the game over state.
     */
    public void endGame() {
        if (this.gameTimer!=null) this.gameTimer.stop();
        if (this.fireRateTick != null) this.fireRateTick.reset();
        
        GameAreaPanel.isGamePaused = true;  // Set to true to prevent player rotation on mouse movement

        if (this.backgroundSoundFX!=null) this.backgroundSoundFX.stop();

        showGameOverDialog();
    }

    public void exit() {
        if (this.gameTimer!=null) this.gameTimer.stop();
        if (this.fireRateTick != null) this.fireRateTick.reset();
        
        GameAreaPanel.isGamePaused = true;  // Set to true to prevent player rotation on mouse movement
        if (this.backgroundSoundFX!=null) this.backgroundSoundFX.stop();
    }

    /**
     * Exits the game, closing all resources and returning to the main menu.
     */
    private void openInGameMenu() {
        pauseGame();

        InGameMenuPanel inGameMenuPanel = this.parentPanel.getInGameMenuPanel();
        inGameMenuPanel.fadeIn();
    }

    /**
     * Handles key press events.
     * This method is called when a key is pressed.
     *
     * @param e The KeyEvent that contains the details of the key press.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player.decrementDx();
            case KeyEvent.VK_D -> player.incrementDx();
            case KeyEvent.VK_W -> player.decrementDy();
            case KeyEvent.VK_S -> player.incrementDy();
            case KeyEvent.VK_R -> player.getCurrentWeapon().reload();
            case KeyEvent.VK_Q -> player.switchWeapon();
            case KeyEvent.VK_ESCAPE -> openInGameMenu();
        }
    }

    /**
     * Handles key release events.
     * This method is called when a key is released.
     *
     * @param e The KeyEvent that contains the details of the key release.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) player.setDx(0);
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S) player.setDy(0);
    }

/**
     * Handles key typing events.
     * 
     * Not implemented
     *
     * @param e The KeyEvent that contains the details of the key typed.
     */
    @Override
    public void keyTyped(KeyEvent e) { }
}
