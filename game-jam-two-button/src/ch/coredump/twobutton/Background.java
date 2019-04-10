package ch.coredump.twobutton;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

public class Background {
	class Point {
		int x;
		int y;
	}

	PApplet p;
	int width;
	int height;
	int startX;
	int startY;
	List<Point> points = new ArrayList<>();
	float pointSpeed;

	public Background(PApplet p, int startX, int startY, int width, int height, float speed) {
		this.width = width;
		this.height = height;
		this.startX = startX;
		this.startY = startY;
		this.p = p;
		pointSpeed = speed;
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
			point.x -= pointSpeed;
			if (point.x < startX) {
				point.x = width;
			}
		}
	}

	public void draw() {
		for (Point point : points) {
			p.point(point.x, point.y);
		}
	}
}
