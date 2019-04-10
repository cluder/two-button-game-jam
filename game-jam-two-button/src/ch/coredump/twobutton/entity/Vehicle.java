package ch.coredump.twobutton.entity;

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

	public Vehicle(PApplet p) {
		super(p);
		height = 50;
		length = 70;

		img = createImage();
	}

	private PGraphics createImage() {

		img = p.createGraphics(length, height);
		img.beginDraw();

		final PImage pepe = p.loadImage("resources/pepe1.png");
		pepe.resize(25, 20);
		img.image(pepe, 15, 0);

		// body
		img.rectMode(PApplet.CORNERS);
		img.fill(255, collisionAlpha);
		img.rect(10, 20, length - 10, height - 10);

		// wheels
		img.fill(200, collisionAlpha);
		img.circle(20, height - 5, 15);
		img.circle(length - 20, height - 5, 15);

		img.endDraw();

		return img;
	}

	public void reset(int width, int height) {
		x = width * .1f;
		y = height;
		xSpeed = 0;
		ySpeed = 0;
	}

	@Override
	public void update(long tpf) {
		// test values
		gravity = 1;
		maxSepeed = 5;

		// apply gravity to y speed
		ySpeed += gravity * tpf / 1000;

		// cgeck max speed (pos and neg)
		if (ySpeed >= maxSepeed) {
			ySpeed = maxSepeed;
		}
		if (ySpeed <= -maxSepeed) {
			ySpeed -= maxSepeed;
		}

		y += ySpeed;
		x += xSpeed;

	}

	@Override
	public String toString() {
		return String.format("speed: x:%s y:%s", xSpeed, ySpeed);
	}

	public void draw(PApplet p) {
		p.image(img, x, y);
	}
}
