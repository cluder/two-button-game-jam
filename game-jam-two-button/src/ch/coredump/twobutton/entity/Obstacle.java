package ch.coredump.twobutton.entity;

import ch.coredump.twobutton.util.Colors;
import ch.coredump.twobutton.util.Colors.Col;
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

	public Obstacle(PApplet p, float x, long spawnTime, int height, Type t) {
		super(p);
		this.spawnTime = spawnTime;
		this.height = height;
		this.x = x;
		this.y = calcY();
		this.type = t;
		// constant speed right to left
		this.xSpeed = -Consts.MOVEMENT_SPEED;
		createImage();
	}

	private void createImage() {
		img = p.createGraphics(width, height);
		img.beginDraw();

		Col c = Colors.obstacle();
		img.fill(c.r, c.g, c.b, c.a);
		img.rectMode(PApplet.CORNER);
		img.rect(0, 0, width, height, 15);

		img.endDraw();
	}

	@Override
	public void draw(PApplet p) {
		createImage();
		if (dead) {
			return;
		}
		switch (type) {
		case ELLIPSE:
			Col c = Colors.obstacle();
			p.fill(c.r, c.g, c.b, c.a);
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
		y = calcY();
	}

	private float calcY() {
		return (p.height * Consts.FLOOR_HEIGHT) - height;
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
	}

	private boolean checkPixels(int i, int j) {
		int oi = i - (int) x;
		int oj = j - (int) y;

		if (oi < 0 || oi > width) {
			return false;
		}
		if (oj < 0 || oj > height) {
			return false;
		}
		final int pixel = img.get(oi, oj);
//		int alpha = (pixel >> 24) & 255;
		int red = (pixel >> 16) & 255;
//		int green = (pixel >> 8) & 255;
//		int blue = pixel & 255;
		if (red > 250) {
			return true;
		}
		return false;
	}
}
