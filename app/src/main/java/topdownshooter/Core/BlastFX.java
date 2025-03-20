package topdownshooter.Core;

import java.awt.Graphics;
import java.util.Random;

public class BlastFX {

    public enum BlastType {
        UNDEFINED,
        EXPLOSIVE_BLAST,
        TOXIC_BLAST
    }

    private BlastType type = BlastType.UNDEFINED;
    private int originX = 0, originY = 0;
    private double r = 0.0;
    private int perimeter = 0;

    private SpriteAnimation blastAnimation = null;
    private static Random random = new Random();


    public BlastFX(BlastType type, int x, int y, int perimeter) {
        this.type = type;
        this.perimeter = perimeter;

        if (this.type == BlastType.TOXIC_BLAST) {
            this.blastAnimation = new SpriteAnimation(Globals.TOXIC_BLAST_ANIMATION);
        } else {
            this.blastAnimation = new SpriteAnimation(Globals.EXPLOSIVE_BLAST_ANIMATION);
        }

        this.originX = x - this.perimeter / 2;
        this.originY = y - this.perimeter / 2;
        this.r = random.nextDouble(Globals.degToRad(360));  // Randomize rotation of the animation to make explosion unique

        this.blastAnimation.setTargetSize(this.perimeter, this.perimeter);
        this.blastAnimation.setRepeat(1);  // Explosions does not repeat
    }

    public boolean update() {
        if(this.blastAnimation!=null) return this.blastAnimation.update();
        return false;        
    }

    public void draw(Graphics g) {
        if(this.blastAnimation!=null) this.blastAnimation.draw(g, this.originX, this.originY, this.r);        
    }
}
