package topdownshooter.Core;

public record SpriteAnimationStruct (
    String imagePath, 
    int totalFrames, 
    int frameDelay, 
    int rows,
    int columns,
    int xOffset,
    int yOffset,
    int defaultDelay
) {
    public SpriteAnimationStruct(
        String imagePath, int totalFrames, int frameDelay, 
        int rows, int columns, int xOffset, int yOffset
    ) {
        this(imagePath, totalFrames, frameDelay, rows, columns, xOffset, yOffset, 0);
    }
}