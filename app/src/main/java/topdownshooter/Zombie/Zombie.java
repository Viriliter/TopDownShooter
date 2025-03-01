package topdownshooter.Zombie;

import java.awt.*;

public class Zombie {
    private int x, y;
    private final int SIZE = 30;

    public Zombie(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveTowards(int px, int py) {
        if (x < px) x += 1;
        if (x > px) x -= 1;
        if (y < py) y += 1;
        if (y > py) y -= 1;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x-SIZE/2, y-SIZE/2, SIZE, SIZE);
    }

    public Rectangle getBounds() {
        return new Rectangle(x-SIZE/2, y-SIZE/2, SIZE, SIZE);
    }
}
