package topdownshooter.Weapon.Projectiles;

import java.awt.Graphics;
import java.util.ArrayList;

import topdownshooter.Core.Globals;

public class ShotgunPellets extends Projectile {

    private final int PELLET_COUNT = 9;
    private final int DISPERSION_ANGLE = 45;  // In degree
    private final double DAMAGE_RATIO = 0.5;
    private ArrayList<Bullet> pellets = null;

    public ShotgunPellets(int x, int y, double r) {
        super(0, 0, 0, 0);

        this.pellets = new ArrayList<Bullet>();

        // Calculate dispersion angle of each pellet
        double dr = Globals.degToRad(DISPERSION_ANGLE) / PELLET_COUNT;
        for(int i=0; i<PELLET_COUNT; i++) {
            this.pellets.add(new Bullet(x, y, r + (i - PELLET_COUNT/2)*dr, 0));
        }

        this.type = ProjectileType.SHOTGUN_PELLETS;
    }

    public ShotgunPellets(int x, int y, double r, int damage) {
        super(0, 0, 0, damage);

        this.pellets = new ArrayList<Bullet>();

        // Calculate dispersion angle of each pellet
        double dr = Globals.degToRad(DISPERSION_ANGLE) / PELLET_COUNT;
        for(int i=0; i<PELLET_COUNT; i++) {
            this.pellets.add(new Bullet(x, y, r + (i - PELLET_COUNT/2)*dr, (int) (damage*DAMAGE_RATIO)));
        }

        this.type = ProjectileType.SHOTGUN_PELLETS;
    }

    public ShotgunPellets(ArrayList<Bullet> pellets) {
        super(0, 0, 0, (int) (pellets.get(0).damage));

        this.pellets = pellets;

        this.type = ProjectileType.SHOTGUN_PELLETS;
    }

    public ArrayList<Bullet> getPellets() {
        return this.pellets;
    }

    @Override   
    public void move() {
        for (int i=0; i<this.pellets.size(); i++) {
            this.pellets.get(i).move();
        }
    }

    @Override
    public void draw(Graphics g) {
        for (int i=0; i<this.pellets.size(); i++) {
            this.pellets.get(i).draw(g);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ShotgunPellets{");
        sb.append("pellets=" + this.pellets);
        sb.append("}");

        return sb.toString();
    }
}
