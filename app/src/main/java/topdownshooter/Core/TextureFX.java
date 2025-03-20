/*
 * @file TextureFX.java
 * @brief This file defines the `TextureFX` class.
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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Objects;

import javax.imageio.ImageIO;

public class TextureFX implements Serializable{
    private transient BufferedImage texture = null;
    private TextureFXStruct struct = null;
    private int targetWidth; 
    private int targetHeight;
    private int offsetX;
    private int offsetY;

    private int defaultDelay = 0;
    private int delay = 0;

    public TextureFX (TextureFXStruct struct) {
        try {
            this.struct = struct;
            this.texture = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(struct.path)));
            if (this.texture != null) {
                this.targetWidth = this.texture.getWidth();
                this.targetHeight = this.texture.getHeight();
                this.offsetX = struct.offsetX;
                this.offsetY = struct.offsetY;
                this.defaultDelay = struct.defaultDelay;
                this.delay = this.defaultDelay;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTargetSize(int width, int height) {
        this.targetWidth = width;
        this.targetHeight = height;
    }

    public void update() {
        this.delay = this.delay> 0 ? this.delay-1 : 0; 
    }

    public void draw(Graphics g, int x, int y, double rotation) {
        //if (this.delay>0) System.out.println(this.delay);
        if (this.delay>0) return;

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldTransform = g2d.getTransform();

        // Apply transformations (rotation + centering)
        g2d.translate(x, y);
        g2d.rotate(rotation);
        g2d.translate(this.offsetX, this.offsetY);

        if (this.texture!=null) g2d.drawImage(this.texture, -this.targetWidth / 2, -this.targetHeight / 2, this.targetWidth, this.targetHeight, null);

        g2d.setTransform(oldTransform);

    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.texture = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(this.struct.path)));
    }
}
