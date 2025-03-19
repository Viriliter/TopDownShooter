/*
 * @file TankZombie.java
 * @brief This file defines the `TankZombie` class.
 * 
 * The `TankZombie` class extends `AbstractZombie`.
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
import topdownshooter.Core.SpriteAnimation;

/**
 * @class TankZombie
 * @brief Represents a Tank Zombie in the game.
 *
 * The `TankZombie` class is a type of zombie with higher health and damage points but a 
 * slower speed compared to other zombies. This class handles drawing the zombie and
 * providing information about its state.
 */
public class TankZombie extends AbstractZombie {
    protected int WIDTH = 120;
    protected int HEIGHT = 95;

    public TankZombie(ZombieProperties properties, int x, int y) {
        super(properties);
        this.x = x;
        this.y = y;
        this.type = ZombieType.TANK;

        this.spriteAnimation = new SpriteAnimation(Globals.TANK_ZOMBIE_MOVE);
        this.spriteAnimation.setTargetSize(WIDTH, HEIGHT);
    }

    @Override
    public void draw(Graphics g) {
        this.spriteAnimation.draw(g, this.x, this.y, this.r);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TankZombie{");
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
