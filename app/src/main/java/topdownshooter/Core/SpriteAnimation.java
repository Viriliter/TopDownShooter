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
    private boolean isRepeated = true;  // Flag to repeat animation
    private int targetWidth = 0, targetHeight = 0;  // Size of target frame which will be drawn

    private Offset offset = null;  // It is offset of character's interest of point (e.g location of gun) from central point

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
            this.offset = new Offset(struct.xOffset(), struct.yOffset());

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

    public void setRepeat(boolean repeat) {
        this.isRepeated = repeat;
    }
    
    public void setTargetSize(int width, int height) {
        this.targetWidth = width;
        this.targetHeight = height;
    }

    public Offset getOffset() {
        return this.offset;
    }

    public void update() {
        frameCounter++;

        if (frameCounter >= frameDelay) {
            // Move to the next frame
            if (isRepeated) {
                currentFrame = (currentFrame + 1) % totalFrames; // Loop through frames
            } else {
                // If animation shouldn't repeat, stop at the last frame
                if (currentFrame < totalFrames - 1) {
                    currentFrame++;
                }
            }
            frameCounter = 0;
        }
    }

    public void draw(Graphics g, int x, int y, double rotation) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldTransform = g2d.getTransform();

        // Apply transformations (rotation + centering)
        g2d.translate(x + targetWidth / 2, y + targetHeight / 2);
        g2d.rotate(rotation);

        if (this.subFrames != null) {
            g2d.drawImage(this.subFrames[currentFrame], -targetWidth / 2, -targetHeight / 2, targetWidth, targetHeight, null);
        }

        g2d.setTransform(oldTransform);
    }
}
