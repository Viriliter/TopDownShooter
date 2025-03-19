/*
 * @file OrdinaryZombie.java
 * @brief This file defines the  `OrdinaryZombie` class.
 * 
 * The `OrdinaryZombie` class extends `AbstractZombie`.
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

/**
 * @class OrdinaryZombie
 * @brief Represents a Ordinary Zombie in the game.
 *
 * The `OrdinaryZombie` class is a type of zombie with balanced attributes. This class
 * handles drawing the zombie and providing information about its state.
 */
public class OrdinaryZombie extends AbstractZombie {
    public OrdinaryZombie(ZombieProperties properties, int x, int y) {
        super(properties);
        this.x = x;
        this.y = y;
        this.type = ZombieType.ORDINARY;
    }

    @Override
    public void draw(Graphics g) {
        /*
        Graphics2D g2d = (Graphics2D) g; // Enable rotation

        AffineTransform oldTransform = g2d.getTransform();

        g2d.setColor(Color.GREEN);
        g2d.translate(x + WIDTH / 2, y + HEIGHT / 2);
        g2d.rotate(r);  // Rotate to face the player
        g2d.fillRect(-WIDTH / 2, -HEIGHT / 2, WIDTH, WIDTH);

        // Reset transformation
        g2d.setTransform(oldTransform);
        */
        this.spriteAnimation.draw(g, this.x, this.y, this.r);
        /*
        AffineTransform oldTransform2 = g2d.getTransform();

        g2d.setColor(Color.ORANGE);
        g2d.translate(this.x + WIDTH / 2, this.y + HEIGHT / 2);
        g2d.rotate(this.r); 
        g2d.fillRect(0, 0, 2, 2);
        
        g2d.setTransform(oldTransform2);
        */
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OrdinaryZombie{");
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
