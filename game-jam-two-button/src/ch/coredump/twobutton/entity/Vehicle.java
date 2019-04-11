package ch.coredump.twobutton.entity;

import java.util.ArrayList;
import java.util.List;

import ch.coredump.twobutton.entity.SoundManager.Effect;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class Vehicle extends BaseEntity {
	// height of the vehicle
	public int height;
	public int length;

	// parts with this alpha value, will be checked for collision
	public final int collisionAlpha = 250;
	public PGraphics img;

	long fireCoolDown = 0;

	public List<Projectile> projectiles = new ArrayList<>();
	private float jumpForce = 0;

	public Vehicle(PApplet p) {
		super(p);
		height = 50;
		length = 70;

		gravity = 0.0012f;
		jumpForce = 1.2f;

		img = createImage();
	}

	private PGraphics createImage() {
		img = p.createGraphics(length, height);
		img.beginDraw();

		final PImage pepe = p.loadImage("resources/pepe1.png");
		pepe.resize(25, 20);
		img.image(pepe, 18, 0);

		// body
		img.rectMode(PApplet.CORNERS);

		img.fill(255, collisionAlpha);
		img.rect(8, 20, length - 8, height - 7, 12);

		// wheels
		img.fill(200, collisionAlpha);
		img.circle(20, height - 8, 16);
		img.circle(length - 20, height - 6, 12);

		img.endDraw();

		return img;
	}

	public void reset(int width, int height) {
		x = width * .1f;
		y = height;
		xSpeed = 0;
		ySpeed = 0;
		fireCoolDown = 0;
		projectiles.clear();
	}

	@Override
	public void update(long tpf) {
		fireCoolDown -= tpf;

		maxSpeed = 5;

		// apply gravity to y speed
		ySpeed += gravity * tpf;

		maxSpeed = 1.2f;
		// check max speed (pos and neg)
		if (ySpeed >= maxSpeed) {
			ySpeed = maxSpeed;
		}
		if (ySpeed <= -maxSpeed) {
			ySpeed = maxSpeed;
		}

		y += ySpeed;
//		x += xSpeed;

		for (Projectile pr : projectiles) {
			pr.update(tpf);
		}
	}

	@Override
	public String toString() {
		return String.format("speed: x:%s y:%s", xSpeed, ySpeed);
	}

	public void draw(PApplet p) {
		p.image(img, x, y);
		for (Projectile pr : projectiles) {
			pr.draw(p);
		}
	}

	public void fire() {
		System.out.println(fireCoolDown);
		if (fireCoolDown > 0) {
			return;
		}
		fireCoolDown = 2000;
		SoundManager.get().play(Effect.SHOT);

		projectiles.add(new Projectile(p, x + length, y + height / 2));
		projectiles.removeIf(x -> x.dead);
	}

	public void jump() {
		ySpeed = -jumpForce;
		System.out.println("jump");
	}
}
