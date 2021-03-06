package ch.coredump.twobutton.entity;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class BaseEntity {
	PImage img;

	public float x;
	public float y;

	public float xSpeed;
	public float ySpeed;

	public float gravity = 0f;
	public float maxSpeed = 0;
	PApplet p;

	public BaseEntity(PApplet p) {
		this.p = p;
	}

	abstract public void draw(PApplet p);

	abstract public void update(long tpf);
}
