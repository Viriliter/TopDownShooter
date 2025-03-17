package topdownshooter.Core;

import java.io.Serializable;

public class TextureFXStruct implements Serializable {
    String path;
    int offsetX;
    int offsetY;
    int defaultDelay;

    public TextureFXStruct(String path, int offsetX, int offsetY, int defaultDelay) {
        this.path = path;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.defaultDelay = defaultDelay;
    }

    public TextureFXStruct(String path, int offsetX, int offsetY) {
        this.path = path;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.defaultDelay = 0;
    }
}
