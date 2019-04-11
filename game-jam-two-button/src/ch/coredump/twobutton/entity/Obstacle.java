package ch.coredump.twobutton.entity;

import ch.coredump.twobutton.util.Consts;
import processing.core.PApplet;
import processing.core.PGraphics;

public class Obstacle extends BaseEntity {
	public enum Type {
		RECT, ELLIPSE;
	}

	// number of ms after level start where this obstacle spawns
	public long spawnTime;
	public boolean dead = false;
	public int height = 30;
	final int width = 20;
	Type type = Type.RECT;
	PGraphics img;

	public Obstacle(PApplet p, float x, float y, long spawnTime, int height, Type t) {
		super(p);
		this.spawnTime = spawnTime;
		this.height = height;
		this.x = x;
		this.y = y - height; // place on floor
		this.type = t;
		// constant speed right to left
		this.xSpeed = -Consts.MOVEMENT_SPEED;
		createImage();
	}

	private void createImage() {
		img = p.createGraphics(width, height);
		img.beginDraw();

		img.fill(251, 255, 0);
		img.rectMode(PApplet.CORNER);
		img.rect(0, 0, width, height, 15);

		img.endDraw();
	}

	@Override
	public void draw(PApplet p) {
		if (dead) {
			return;
		}
		switch (type) {
		case ELLIPSE:
			p.fill(255, 255, 0);
//			p.rectMode(PApplet.CORNER);
			p.ellipse(x, y, width, height);
			break;
		case RECT:
		default:
			p.image(img, x, y);
			break;
		}
	}

	public void update(long tpf) {
		x += xSpeed * tpf;
	}

	@Override
	public String toString() {
		return String.format("x:%s, y%s spawnTime:%s", x, y, spawnTime);
	}

	/**
	 * Checks if the pixel i,j coordinate is inside this obstacle.
	 */
	public boolean collidesWith(int i, int j) {
		return rectangleCollision(i, j);
	}

	private boolean rectangleCollision(int i, int j) {
		if (i < x || i > x + width) {
			return false;
		}

		if (j < y || j > y + height) {
			return false;
		}

		return checkPixels(i, j);
//		return true;
	}

	private boolean checkPixels(int i, int j) {
		int oi = i - (int) x;
		int oj = j - (int) y;

		if (oi < 0 || oi > width) {
			System.out.println(oi + " oi not possible");
			return false;
		}
		if (oj < 0 || oj > height) {
			System.out.println(oj + " oj not possible");
			return false;
		}
		final int pixel = img.get(oi, oj);
//		int alpha = (pixel >> 24) & 255;
		int red = (pixel >> 16) & 255;
//		int green = (pixel >> 8) & 255;
//		int blue = pixel & 255;
//		Debug.get().active = true;
//		Debug.get().x = (int) (x + oi);
//		Debug.get().y = (int) (y + oj);
		if (red > 250) {
//			System.out.println("oi:" + oi + " oj" + oj + " hit");
			return true;
		}
//		System.out.println("oi:" + oi + " oj" + oj + " no hit");
		return false;
	}
}
