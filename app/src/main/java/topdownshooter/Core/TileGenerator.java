package topdownshooter.Core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

public class TileGenerator {
    private BufferedImage tileImage;
    private int tileWidth, tileHeight;
    
    public TileGenerator(String tilePath) {
        try {
            this.tileImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(tilePath)));
            if (tileImage == null) {
                throw new IOException("Failed to load background tile image.");
            }
            this.tileWidth = tileImage.getWidth();
            this.tileHeight = tileImage.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g, int screenWidth, int screenHeight) {
        if (tileImage == null) return;

        for (int x = 0; x < screenWidth; x += tileWidth) {
            for (int y = 0; y < screenHeight; y += tileHeight) {
                g.drawImage(tileImage, x, y, null);
            }
        }
    }
}
