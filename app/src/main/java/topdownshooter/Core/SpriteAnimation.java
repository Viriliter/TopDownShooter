/*
 * @file SpriteAnimation.java
 * @brief This file defines the ${fileNameNoExt} class.
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

package topdownshooter.Core;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Objects;

public class SpriteAnimation implements Serializable{
    private SpriteAnimationStruct struct = null;
    private transient BufferedImage spriteSheet = null;
    private transient BufferedImage[] subFrames = null;  // Preloaded animation frames
    private int frameWidth = 0, frameHeight = 0;  // Size of each frame in sprite sheet
    private int totalFrames; // Total number of frames in the sprite sheet
    private int currentFrame = 0; // Current animation frame
    private int frameDelay; // Delay before switching frames
    private int frameCounter = 0; // Counter to control animation speed
    private int rows;  // Number of columns in sprite sheet
    private int columns;  // Number of rows in sprite sheet
    private int repeatCount = -1;  // Animation repeat count
    private int targetWidth = 0, targetHeight = 0;  // Size of target frame which will be drawn

    private int offsetX;
    private int offsetY;
    private int defaultDelay = 0;
    private int delay = 0;

    private double rOffset = 0;  // Rotation offset in radians

    public class Offset {
        int x;
        int y;

        public Offset(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
             return this.x;
        }

        public int getY() {
            return this.y;
       }
    }

    public SpriteAnimation(SpriteAnimationStruct struct) {
        try {
            this.struct = struct;
            this.spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(this.struct.imagePath)));
            this.totalFrames = this.struct.totalFrames;
            this.frameDelay = this.struct.frameDelay;
            this.rows = this.struct.rows;
            this.columns = this.struct.columns;
            this.offsetX = this.struct.xOffset;
            this.offsetY = this.struct.yOffset;
            this.defaultDelay = this.struct.defaultDelay;
            this.delay = this.struct.defaultDelay;

            setSubFrames();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSubFrames() {
        this.frameWidth = this.spriteSheet.getWidth() / this.columns;
        this.frameHeight = this.spriteSheet.getHeight() / this.rows;
        
        // Preload all frames
        this.subFrames = new BufferedImage[totalFrames];
        for (int i = 0; i < totalFrames; i++) {
            int row = i / columns;
            int col = i % columns;
            int frameX = col * frameWidth;
            int frameY = row * frameHeight;

            // Extract and store frame
            this.subFrames[i] = spriteSheet.getSubimage(frameX, frameY, frameWidth, frameHeight);
        }
    }

    public void setRepeat(int repeatCount) {
        this.repeatCount = repeatCount;
    }
    
    public void setTargetSize(int width, int height) {
        this.targetWidth = width;
        this.targetHeight = height;
    }

    public void setRotationOffset(double rOffset) {
        this.rOffset = rOffset;
    }

    public Offset getOffset() {
        return new Offset(this.offsetX, this.offsetY);
    }

    public void update() {
        if (this.delay>0) this.delay--;

        this.frameCounter++;

        if (this.frameCounter >= this.frameDelay) {
            this.frameCounter = 0;  // Reset counter after update
    
            if (this.repeatCount < 0) {  // Infinite loop
                this.currentFrame = (this.currentFrame + 1) % this.totalFrames;
            } else if (this.repeatCount > 0) {
                if (this.currentFrame < this.totalFrames - 1) {
                    this.currentFrame++;
                } else {
                    this.repeatCount--;  // Decrement after last repeat
                    if (this.repeatCount >= 0) {
                        this.currentFrame = 0; // Restart animation
                        this.delay = this.defaultDelay;
                    }
                }
            } else {
                // Do nothing if no repeat left
            }
        }
    }

    public void draw(Graphics g, int x, int y, double rotation) {
        if (this.repeatCount == 0) return;  // If there is no repeat for the animation do not draw

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldTransform = g2d.getTransform();

        // Apply transformations (rotation + centering)
        g2d.translate(x + targetWidth / 2, y + targetHeight / 2);
        g2d.rotate(rotation+rOffset);

        if (this.subFrames != null) {
            g2d.drawImage(this.subFrames[this.currentFrame], -this.targetWidth / 2, -this.targetHeight / 2, this.targetWidth, this.targetHeight, null);
        }

        g2d.setTransform(oldTransform);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(this.struct.imagePath)));
        setSubFrames();
    }
}
