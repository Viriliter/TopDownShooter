package topdownshooter.Core;

import java.io.Serializable;

public class TimeTick implements Serializable {
    private int tick = 0;
    private int defaultTick = 0;
    private int repeatCount = -1;
    private Runnable action = null;

    public TimeTick(int defaultTick) {
        this.defaultTick = defaultTick;
        this.reset();
    }

    public TimeTick(int defaultTick, Runnable action) {
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

    public void setAction(Runnable action) {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GameTick{");
        sb.append("tick=" + this.tick + ", ");
        sb.append("defaultTick=" + this.defaultTick + ", ");
        sb.append("repeatCount=" + this.repeatCount + ", ");
        sb.append("}");

        return sb.toString();
    }
}
