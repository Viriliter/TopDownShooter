/*
 * @file CrawlerZombie.java
 * @brief This file defines the `CrawlerZombie` class.
 * 
 * The `CrawlerZombie` class extends `AbstractZombie`.
 * It implements the required behavior for drawing and representing itself
 * in the game world.
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

package topdownshooter.Zombie;

import java.awt.*;

import topdownshooter.Core.ConfigHandler.ZombieProperties;
import topdownshooter.Core.Globals;
import topdownshooter.Core.RectangleBound;
import topdownshooter.Core.SpriteAnimation;
import topdownshooter.Core.TimeTick;

/**
 * @class CrawlerZombie
 * @brief Represents a Crawler Zombie in the game.
 *
 * The `CrawlerZombie` class is a type of zombie that moves fast but it has lower health. 
 * It can jump the player if it is within the certain distance from the player. This class
 * handles drawing the zombie and providing information about its state.
 */
public class CrawlerZombie extends AbstractZombie {
    static final int JUMP_DISTANCE = 250;           /**< Jump distance of the zombie. */
    private TimeTick jumpTick = null;               /**< Timer for crawler zombie to jump again. */
    private static final int MAX_JUMP_DELAY = 30;    /**< Delay to jump again in Game ticks. */

    public CrawlerZombie(ZombieProperties properties, int x, int y) {
        super(properties);
        this.x = x;
        this.y = y;
        this.type = ZombieType.CRAWLER;

        this.jumpTick = new TimeTick(MAX_JUMP_DELAY);
        this.spriteAnimation = new SpriteAnimation(Globals.CRAWLER_ZOMBIE_MOVE);
        this.spriteAnimation.setTargetSize(WIDTH, HEIGHT);
    }

    @Override
    public void draw(Graphics g) {
        this.spriteAnimation.draw(g, this.x, this.y, this.r);
    }

    @Override
    public void update(RectangleBound playerBounds) {
        // Try to catch the player
        int playerX = (int) playerBounds.getX();
        int playerY = (int) playerBounds.getY();
        int playerMinSize = Math.min((int) playerBounds.getWidth(), (int) playerBounds.getHeight());

        this.jumpTick.updateTick();  // Update jump timer

        // If objects collided, which means zombie catched the player, do not update position of the zombie
        int dx = playerX - this.x;
        int dy = playerY - this.y;

        // Need to normalize speed according to the speed vector
        double distance = Math.sqrt(dx * dx + dy * dy);
        int normalizedSpeedX = 0, normalizedSpeedY = 0;
        if (distance > playerMinSize * 0.5) {  // Do not coincide to the player           
            normalizedSpeedX = (int) (this.speed * Math.abs((double) dx / distance));
            normalizedSpeedY = (int) (this.speed * Math.abs((double) dy / distance));

            if (this.x < playerX) this.x += normalizedSpeedX;
            if (this.x > playerX) this.x -= normalizedSpeedX;
            if (this.y < playerY) this.y += normalizedSpeedY;
            if (this.y > playerY) this.y -= normalizedSpeedY;    

            // Rotate the zombie towards player
            this.r = Math.atan2(dy, dx);

            // Check zombie is at the jump range to the player
            // If it has already jumped, do not jump again. Wait the timer to finish
            if (distance <= JUMP_DISTANCE && this.jumpTick.isTimeOut()) {
                this.x = playerX + WIDTH / 2;
                this.y = playerY + HEIGHT / 2;
                this.jumpTick.reset();  // When it is jumped reset it
            }
        }

        // Update sprite animation
        this.spriteAnimation.update();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CrawlerZombie{");
        sb.append("x=" + this.x + ", ");
        sb.append("y=" + this.y + ", ");
        sb.append("r=" + this.r + ", ");
        sb.append("health=" + this.health + ", ");
        sb.append("speed=" + this.speed + ", ");
        sb.append("damage=" + this.damage + ", ");
        sb.append("points=" + this.points + ", ");
        sb.append("type=" + this.type);
        sb.append("}");

        return sb.toString();
    }
}
