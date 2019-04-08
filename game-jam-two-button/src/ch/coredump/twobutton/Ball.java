package ch.coredump.twobutton;

public class Ball {

	public float x;
	public float y;

	public float ySpeed = 0;
	public float xSpeed = 0;

	float maxSepeed = 10;
	float gravity = 3f;

	public void reset(int width, int height) {
		x = width - 50;
		y = height - 50;
		xSpeed = 0;
		ySpeed = 0;
	}

	public void setxSpeed(float xSpeed) {
		this.xSpeed = xSpeed;
	}

	public void setySpeed(float ySpeed) {
		this.ySpeed = ySpeed;
	}

	public float getxSpeed() {
		return xSpeed;
	}

	public float getySpeed() {
		return ySpeed;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void update(long tpf) {

		gravity = 0;
		maxSepeed = 5;

		ySpeed += gravity * tpf / 1000;
		if (ySpeed >= maxSepeed) {
			ySpeed = maxSepeed;
		}

		y += ySpeed;
		x += xSpeed;
	}
}
