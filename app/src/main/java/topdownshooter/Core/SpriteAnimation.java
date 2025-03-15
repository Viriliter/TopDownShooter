package topdownshooter.Core;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class SpriteAnimation {
    private BufferedImage spriteSheet;
    private BufferedImage[] subFrames;  // Preloaded animation frames
    private int frameWidth, frameHeight;  // Size of each frame in sprite sheet
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
            setSprite(struct);
    }

    public void setSprite(SpriteAnimationStruct struct) {
        try {
            this.spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(struct.imagePath())));
            this.totalFrames = struct.totalFrames();
            this.frameDelay = struct.frameDelay();
            this.rows = struct.rows();
            this.columns = struct.columns();
            this.offsetX = struct.xOffset();
            this.offsetY = struct.yOffset();
            
            this.frameWidth = spriteSheet.getWidth() / columns;
            this.frameHeight = spriteSheet.getHeight() / this.rows;
            this.targetWidth = this.frameWidth;
            this.targetHeight = this.frameHeight;
            
            // Preload all frames
            this.subFrames = new BufferedImage[totalFrames];
            for (int i = 0; i < totalFrames; i++) {
                int row = i / columns;
                int col = i % columns;
                int frameX = col * frameWidth;
                int frameY = row * frameHeight;

                // Extract and store frame
                this.subFrames[i] = spriteSheet.getSubimage(frameX, frameY, frameWidth, frameHeight);

                //Graphics g = this.subFrames[i].createGraphics();
                //g.drawImage(spriteSheet, 0, 0, frameWidth, frameHeight, frameX, frameY, frameX + frameWidth, frameY + frameHeight, null);
                //g.dispose();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                    if (this.repeatCount > 0) {
                        this.currentFrame = 0; // Restart animation
                    }
                }
            } else {
                // Do nothing if no repeat left
            }
        }
    }

    public void draw(Graphics g, int x, int y, double rotation) {
        System.out.println("this.repeatCount:" + this.repeatCount);
        if (this.repeatCount == 0) return;  // If there is no repeat for the animation do not draw

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldTransform = g2d.getTransform();

        // Apply transformations (rotation + centering)
        g2d.translate(x + targetWidth / 2, y + targetHeight / 2);
        g2d.rotate(rotation);

        if (this.subFrames != null) {
            g2d.drawImage(this.subFrames[this.currentFrame], -this.targetWidth / 2, -this.targetHeight / 2, this.targetWidth, this.targetHeight, null);
        }

        g2d.setTransform(oldTransform);
    }
}
