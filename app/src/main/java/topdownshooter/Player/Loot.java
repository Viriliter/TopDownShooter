package topdownshooter.Player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import topdownshooter.Core.Globals;
import topdownshooter.Core.SpriteAnimation;
import topdownshooter.Player.PlayerItem.ItemType;

public class Loot extends JPanel {
    private int x, y;
    private final int WIDTH = 50;
    private final int HEIGHT = 50;

    private int score;
    private PlayerItem item = null;

    private SpriteAnimation spriteAnimationAmmo = null;
    private SpriteAnimation spriteAnimationSmallMedic = null;
    private SpriteAnimation spriteAnimationLargeMedic = null;

    public Loot(int x, int y, int score, PlayerItem item) {
        this.x = x;
        this.y = y;
        this.score = score;
        this.item = item;

        // No loot item may exists
        if (this.item == null) return;

        if (this.item.lootType == ItemType.AMMUNITION) {
            this.spriteAnimationAmmo = new SpriteAnimation(Globals.AMMO_ANIMATION);
            this.spriteAnimationAmmo.setTargetSize(WIDTH, HEIGHT); 
        } else if (this.item.lootType == ItemType.SMALL_MEDIC_PACK) {
            this.spriteAnimationSmallMedic = new SpriteAnimation(Globals.SMALL_MEDIC_ANIMATION);
            this.spriteAnimationSmallMedic.setTargetSize(WIDTH, HEIGHT); 
        } else if (this.item.lootType == ItemType.LARGE_MEDIC_PACK) {
            this.spriteAnimationLargeMedic = new SpriteAnimation(Globals.LARGE_MEDIC_ANIMATION);
            this.spriteAnimationLargeMedic.setTargetSize(WIDTH, HEIGHT); 
        } else {}
    }

    public void draw(Graphics g) {
        if (this.item == null) return;

        if (this.item.lootType == ItemType.AMMUNITION) {
            this.spriteAnimationAmmo.draw(g, this.x, this.y, 0.0);
        } else if (this.item.lootType == ItemType.SMALL_MEDIC_PACK) {
            this.spriteAnimationSmallMedic.draw(g, this.x, this.y, 0.0);
        } else if (this.item.lootType == ItemType.LARGE_MEDIC_PACK) {
            this.spriteAnimationLargeMedic.draw(g, this.x, this.y, 0.0);
        } else {

        }
    }

    public void update() {
        if (this.item == null) return;

        if (this.item.lootType == ItemType.AMMUNITION) {
            this.spriteAnimationAmmo.update();
        } else if (this.item.lootType == ItemType.SMALL_MEDIC_PACK) {
            this.spriteAnimationSmallMedic.update();
        } else if (this.item.lootType == ItemType.LARGE_MEDIC_PACK) {
            this.spriteAnimationLargeMedic.update();
        }
    } 

    public int getScore() {
        return this.score;
    }

    public PlayerItem getItem() {
        return this.item;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, WIDTH, HEIGHT);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Loot{");
        sb.append("x=" + this.x + ", ");
        sb.append("y=" + this.y + ", ");
        sb.append("score=" + this.score + ", ");
        sb.append("item=" + this.item + ", ");
        sb.append("}");

        return sb.toString();
    }
}
