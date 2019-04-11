package ch.coredump.twobutton.entity;

import processing.core.PApplet;

public class Projectile extends BaseEntity {
	public float length;
	float lifetime = 1500;
	public boolean dead = false;

	public Projectile(PApplet p, float x, float y) {
		super(p);
		this.x = x;
		this.y = y;
		length = 10;
		xSpeed = 0.2f;
	}

	@Override
	public void draw(PApplet p) {
		if (dead) {
			return;
		}

		p.stroke(255);
		p.fill(255);
		p.rect(x, y, x + length, y + 1);
	}

	@Override
	public void update(long tpf) {
		if (dead) {
			return;
		}
		lifetime -= tpf;
		x += xSpeed * tpf;
		if (lifetime <= 0) {
			dead = true;
		}
	}

}
