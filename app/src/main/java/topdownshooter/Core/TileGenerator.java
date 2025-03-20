/*
 * @file TileGenerator.java
 * @brief This file defines the `TileGenerator` class.
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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Objects;

import javax.imageio.ImageIO;

public class TileGenerator implements Serializable{
    private String tilePath;
    private BufferedImage tileImage;
    private int tileWidth, tileHeight;
    
    public TileGenerator(String tilePath) {
        try {
            this.tilePath = tilePath;
            this.tileImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(this.tilePath)));
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
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.tileImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(this.tilePath)));
        if (tileImage == null) {
            throw new IOException("Failed to load background tile image.");
        }
        this.tileWidth = tileImage.getWidth();
        this.tileHeight = tileImage.getHeight();

    }
}
