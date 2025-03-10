package topdownshooter.Core;

public record SpriteAnimationStruct (
    String imagePath, 
    int totalFrames, 
    int frameDelay, 
    int rows,
    int columns,
    int xOffset,
    int yOffset
) {}