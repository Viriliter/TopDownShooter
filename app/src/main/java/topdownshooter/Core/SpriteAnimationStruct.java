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
        this.defaultDelay = 0;
    }
}