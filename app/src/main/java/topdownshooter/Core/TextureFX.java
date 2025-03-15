package topdownshooter.Core;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.imageio.ImageIO;

public class TextureFX {
    private BufferedImage texture = null;
    private int targetWidth; 
    private int targetHeight;
    private int offsetX; 
    private int offsetY;

    public TextureFX (TextureFXStruct struct) {
        try {
            this.texture = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(struct.path())));
            if (this.texture != null) {
                this.targetWidth = this.texture.getWidth();
                this.targetHeight = this.texture.getHeight();
                this.offsetX = struct.offsetX();
                this.offsetY = struct.offsetY();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTargetSize(int width, int height) {
        this.targetWidth = width;
        this.targetHeight = height;
    }

    public void draw(Graphics g, int x, int y, double rotation) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldTransform = g2d.getTransform();

        // Apply transformations (rotation + centering)
        g2d.translate(x, y);
        g2d.rotate(rotation);
        g2d.translate(this.offsetX, this.offsetY);

        if (this.texture!=null) g2d.drawImage(this.texture, -this.targetWidth / 2, -this.targetHeight / 2, this.targetWidth, this.targetHeight, null);

        g2d.setTransform(oldTransform);

    }
}
