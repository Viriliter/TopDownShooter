package topdownshooter.Core;

public record TextureFXStruct(
    String path,
    int offsetX,
    int offsetY,
    int defaultDelay
) {
    public TextureFXStruct(String path, int offsetX, int offsetY) {
        this(path, offsetX, offsetY, 0);
    }
}
