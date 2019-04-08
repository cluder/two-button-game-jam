package ch.coredump.twobutton.gamestate;

import static processing.core.PConstants.LEFT;
import static processing.core.PConstants.RIGHT;

import java.text.DecimalFormat;
import java.util.HashMap;

import ch.coredump.twobutton.Ball;
import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;

/**
 * 
 */
public class GSGame extends BaseGameState {
	HashMap<Integer, Long> pressedKeys = new HashMap<>();

	long score;
	long maxScore;
	int scoreHeight = 50;

	Ball ball = new Ball();
	PImage tableInfo;

	public GSGame(PApplet p, GameStateManager manager) {
		super(p, manager, GameState.GAME);
	}

	@Override
	public void init() {
		// load assets
		tableInfo = p.loadImage("resources/table.png");

	}

	@Override
	public void onActivate() {
		reset();
		tableInfo = p.loadImage("resources/table.png");
	}

	public long getMaxScore() {
		return maxScore;
	}

	private void reset() {
		pressedKeys.clear();
		score = 0;
		ball.reset(p.width, p.height);
	}

	@Override
	protected void doUpdate(long tpf) {
		updateMaxScore();

		updateKeyTime(tpf, LEFT);
		updateKeyTime(tpf, RIGHT);

		// press left & right key to return to menu / restart
		final int timeToSwitchToMenu = 1000;
		if (isPressed(LEFT, timeToSwitchToMenu) && isPressed(RIGHT, timeToSwitchToMenu)) {
			manager.setActive(GameState.MENU);
		}

		// update ball
		ball.update(tpf);

		checkCollision();

//		debugPrint();
	}

	private void checkCollision() {

		int x = (int) ball.getX();
		int y = (int) ball.getY();

		// testing
//		x = p.mouseX;
//		y = p.mouseY;

		final int pInfo = tableInfo.get(x, y);

//		int a = (pInfo >> 24) & 255;
//		int r = (pInfo >> 16) & 255;
//		int g = (pInfo >> 8) & 255;

		// blue value
		int b = pInfo & 255;

		if (b == 255) {
			return;
		}
		// convert grey value to degrees (0-360)
		final float degree = 360.0f / 255.0f * b;
		// convert to sin/cos
		final float rad = PApplet.radians(degree);
		final float sin = PApplet.sin(rad);
		final float cos = PApplet.cos(rad);

		ball.xSpeed += sin * 0.3f;
		ball.ySpeed += cos * 0.3f;
		System.out.println(String.format("grey:%s, degree:%s, sin:%s, cos:%s", b, degree, sin, cos));

		if (ball.getY() >= p.height - 20) {
			ball.setY(p.height - 30);
//			ball.setySpeed(ball.getySpeed() * -1.1f);
			ball.ySpeed = 0;
		}
		if (ball.getY() <= 30) {
			ball.setY(50);
			ball.ySpeed = 0;
		}
		if (ball.getX() <= 30) {
			ball.setX(50);
			ball.xSpeed = 0;
		}
		if (ball.getX() >= p.width - 20) {
			ball.setX(p.width - 30);
			ball.xSpeed = 0;
		}

//		System.out.println(ball.ySpeed);
	}

	@Override
	protected void doRender() {
		p.background(0);

		drawTable();

		drawBall();

		drawPaddles();

		drawScore();
	}

	private void drawBall() {
		p.noStroke();
		p.fill(155);

		p.circle(ball.getX(), ball.getY(), 20);
	}

	private void drawPaddles() {

	}

	private void drawTable() {
		p.image(tableInfo, 0, 0);

		p.rectMode(PApplet.CORNERS);
		p.noFill();
		p.strokeJoin(PApplet.ROUND);
		p.strokeWeight(1);
		p.stroke(255, 255, 0, 150);
		p.rect(20, scoreHeight + 5, p.width - 20, p.height - 20);

	}

	private void drawScore() {
		int padding = 20;

		DecimalFormat df = new DecimalFormat("#0");
		df.setGroupingSize(3);
		df.setGroupingUsed(true);

		p.strokeWeight(1);
		p.stroke(255, 255, 0);
		p.line(padding, 50, p.width - padding, 50);

		p.textSize(30);
		p.fill(255, 200, 0, 255);

		p.textAlign(PApplet.LEFT, PApplet.CENTER);
		p.text("SCORE", padding, padding);
		p.textAlign(PApplet.RIGHT, PApplet.CENTER);
		p.text(df.format(score), p.width - padding, padding);
	}

	private void updateMaxScore() {
		if (score > maxScore) {
			maxScore = score;
		}
	}

	private void debugPrint() {
		final Long left = pressedKeys.get(LEFT);
		System.out.println("left:" + left);

		final Long right = pressedKeys.get(RIGHT);
		System.out.println("right:" + right);
	}

	private boolean isPressed(int key, int ms) {
		final Long keyTime = pressedKeys.get(key);
		if (keyTime == null) {
			return false;
		}
		return keyTime > ms;
	}

	private void updateKeyTime(long tpf, int key) {
		Long leftKey = pressedKeys.get(key);
		if (leftKey != null) {
			pressedKeys.put(key, leftKey += tpf);
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		final int keyCode = event.getKeyCode();
		if (pressedKeys.containsKey(keyCode) == false) {
			pressedKeys.put(keyCode, 0L);
		}

	}

	@Override
	public void keyReleased(KeyEvent event) {
		final int keyCode = event.getKeyCode();
		pressedKeys.remove(keyCode);
	}
}
