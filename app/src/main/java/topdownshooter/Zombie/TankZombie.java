package topdownshooter.Zombie;

import java.awt.*;

import topdownshooter.Core.ConfigHandler.ZombieProperties;
import topdownshooter.Core.Globals;
import topdownshooter.Core.SpriteAnimation;

public class TankZombie extends AbstractZombie {
    public TankZombie(ZombieProperties properties, int x, int y) {
        super(properties);
        this.x = x;
        this.y = y;
        this.type = ZombieType.TANK;

        this.spriteAnimation = new SpriteAnimation(Globals.TANK_ZOMBIE_MOVE);
        this.spriteAnimation.setTargetSize(WIDTH, HEIGHT);
    }

    @Override
    public void draw(Graphics g) {
        this.spriteAnimation.draw(g, this.x, this.y, this.r);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TankZombie{");
        sb.append("x=" + this.x + ", ");
        sb.append("y=" + this.y + ", ");
        sb.append("r=" + this.r + ", ");
        sb.append("health=" + this.health + ", ");
        sb.append("speed=" + this.speed + ", ");
        sb.append("damage=" + this.damage + ", ");
        sb.append("points=" + this.points + ", ");
        sb.append("type=" + this.type);
        sb.append("}");

        return sb.toString();
    }
}
