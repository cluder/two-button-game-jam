package ch.coredump.twobutton.util;

import processing.core.PApplet;

public class Debug {
	private static Debug instance = null;

	public static Debug get() {
		if (instance == null) {
			instance = new Debug();
		}
		return instance;
	}

	public int x = 0;
	public int y = 0;

	public boolean active = false;

	public void draw(PApplet p) {
		if (!active) {
			return;
		}
		p.stroke(1, 1, 255);
		final int len = 5;
		p.line(x - len, y, x + len, y);
		p.line(x, y - len, x, y + len);
	}
}
