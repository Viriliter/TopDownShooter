/*
 * @file TimeTick.java
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

import java.io.Serializable;

public class TimeTick implements Serializable {
    private int tick = 0;
    private int defaultTick = 0;
    private int repeatCount = -1;
    private SerializableRunnable action = null;

    public TimeTick(int defaultTick) {
        this.defaultTick = defaultTick;
        this.reset();
    }

    public TimeTick(int defaultTick, SerializableRunnable action) {
        this.defaultTick = defaultTick;
        this.action = action;
        this.reset();
    }

    public TimeTick(int tick, int defaultTick, int repeatCount) {
        this.tick = tick;
        this.defaultTick = defaultTick;
        this.repeatCount = repeatCount;
    }

    public void setRepeats(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public int getTick() {
        return this.tick;
    }

    public void setAction(SerializableRunnable action) {
        this.action = action;
    }

    public void updateTick() {
        if (this.tick > 0) this.tick--;

        if (this.tick == 0 && action != null) {
            action.run();
        }

        // If repeats are available, reset and decrement the repeat count
        if (this.repeatCount > 0 && this.tick == 0) {
            this.repeatCount--;
        }

        // If no repeats are set (repeatCount is -1), keep ticking
        if (this.repeatCount == -1 && this.tick == 0) {
        }
    }

    public void reset() {
        this.tick = this.defaultTick;
    }

    public boolean isTimeOut() {
        return this.tick == 0; 
    }

}
