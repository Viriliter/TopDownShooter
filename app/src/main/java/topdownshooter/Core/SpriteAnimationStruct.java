/*
 * @file SpriteAnimationStruct.java
 * @brief This file defines the `SpriteAnimationStruct` class.
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

import java.io.Serializable;

public class SpriteAnimationStruct implements Serializable {
    String imagePath;
    int totalFrames;
    int frameDelay;
    int rows;
    int columns;
    int xOffset;
    int yOffset;
    double rOffset;
    int defaultDelay;

    public SpriteAnimationStruct(
        String imagePath, int totalFrames, int frameDelay, 
        int rows, int columns, int xOffset, int yOffset, int defaultDelay
    ) {
        this.imagePath = imagePath;
        this.totalFrames = totalFrames;
        this.frameDelay = frameDelay;
        this.rows = rows;
        this.columns = columns;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.rOffset = 0;
        this.defaultDelay = defaultDelay;
    }
    public SpriteAnimationStruct(
        String imagePath, int totalFrames, int frameDelay, 
        int rows, int columns, int xOffset, int yOffset
    ) {
        this.imagePath = imagePath;
        this.totalFrames = totalFrames;
        this.frameDelay = frameDelay;
        this.rows = rows;
        this.columns = columns;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.rOffset = 0;
        this.defaultDelay = 0;
    }
    public SpriteAnimationStruct(
        String imagePath, int totalFrames, int frameDelay, 
        int rows, int columns, int xOffset, int yOffset, int defaultDelay, int rOffset
    ) {
        this.imagePath = imagePath;
        this.totalFrames = totalFrames;
        this.frameDelay = frameDelay;
        this.rows = rows;
        this.columns = columns;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.rOffset = Globals.degToRad(rOffset);
        this.defaultDelay = defaultDelay;
    }
}