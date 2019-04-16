package ch.coredump.twobutton.util;

import java.util.ArrayList;
import java.util.List;

import ch.coredump.twobutton.util.Colors.Col;
import processing.core.PApplet;

public class Background {
	class Point {
		float x;
		float y;
	}

	PApplet p;
	int width;
	int height;
	int startX;
	int startY;
	List<Point> points = new ArrayList<>();
	float pointSpeed;

	public Background(PApplet p, int startX, int startY, int width, int height) {
		this.width = width;
		this.height = height;
		this.startX = startX;
		this.startY = startY;
		this.p = p;
		pointSpeed = Consts.MOVEMENT_SPEED / 2;
		init(startX, startY, width, height);
	}

	public void init(int startX, int startY, int width, int height) {
		points.clear();
		for (int i = 0; i < 50; i++) {
			Point point = new Point();
			point.x = (int) p.random(startX, width);
			point.y = (int) p.random(startY, height);
			points.add(point);
		}
	}

	public void update(long tpf) {

		for (Point point : points) {
			point.x -= pointSpeed * tpf;
			if (point.x < startX) {
				point.x = width;
			}
		}
	}

	public void draw() {
		for (Point point : points) {
			final Col c = Colors.stars();
			p.stroke(c.r, c.g, c.b, c.a);
			p.point(point.x, point.y);
		}
	}
}
