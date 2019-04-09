package ch.coredump.twobutton.entity;

import processing.core.PApplet;

public class Obstacle extends BaseEntity {
	// number of ms after level start where this obstacle spawns
	public long spawnTime;
	public boolean dead = false;
	public float height = 30;
	final int width = 20;

	public Obstacle(PApplet p, float x, float y, long spawnTime) {
		super(p);
		this.spawnTime = spawnTime;
		this.x = x;
		this.y = y - height; // place on floor

		// constant speed right to left
		this.xSpeed = -3;

	}

	@Override
	public void draw(PApplet p) {
		if (dead) {
			return;
		}

		p.fill(255, 255, 0);
		p.rectMode(PApplet.CORNER);
		p.rect(x, y, width, height);
	}

	public void update(long tpf) {
		x += xSpeed;
	}

	@Override
	public String toString() {
		return String.format("x:%s, y%s spawnTime:%s", x, y, spawnTime);
	}

	/**
	 * Checks if the pixel i,j coordinate is inside this obstacle.
	 */
	public boolean collidesWith(int i, int j) {
		if (i < x || i > x + width) {
			return false;
		}

		if (j < y || j > y + height) {
			return false;
		}

		return true;
	}
}
