package ch.coredump.twobutton.gamestate;

import java.text.DecimalFormat;
import java.util.HashMap;

import ch.coredump.twobutton.LevelManager;
import ch.coredump.twobutton.TwoButtonJam;
import ch.coredump.twobutton.entity.Vehicle;
import processing.core.PApplet;
import processing.event.KeyEvent;

/**
 * 
 */
public class GSGame extends BaseGameState {
	// maps of pressed keys and pressed time in ms
	HashMap<Character, Long> pressedKeys = new HashMap<>();
	final int timeToSwitchToMenu = 1000;

	long score;
	long maxScore;
	int scoreHeight = 50;

	final float floorHeight = p.height * 0.6f;

	Vehicle vehicle;
	boolean vehicleOnFloor = false;

	LevelManager levelManager;

	public GSGame(PApplet p, GameStateManager manager) {
		super(p, manager, GameState.GAME);

		vehicle = new Vehicle(p);
		levelManager = new LevelManager(p, floorHeight);
	}

	@Override
	public void init() {

	}

	@Override
	public void onActivate() {
		reset();
	}

	public long getMaxScore() {
		return maxScore;
	}

	private void reset() {
		pressedKeys.clear();
		score = 0;
		vehicle.reset(p.width, p.height);

		levelManager.init();
	}

	@Override
	protected void doUpdate(long tpf) {
		updateMaxScore();

		updateKeyTime(tpf, TwoButtonJam.KEY_1);
		updateKeyTime(tpf, TwoButtonJam.KEY_2);

		// press left & right key to return to menu / restart
		if (isPressed(TwoButtonJam.KEY_1, timeToSwitchToMenu) && isPressed(TwoButtonJam.KEY_2, timeToSwitchToMenu)) {
			manager.setActive(GameState.MENU);
		}

		// jump
		if (isPressed(TwoButtonJam.KEY_1, 1)) {
			// only jump, if vehicle is on the floor
			if (vehicleOnFloor) {
				vehicle.ySpeed = -1.5f;
			}
		}

		if (isPressed(TwoButtonJam.KEY_2, 1)) {
			levelManager.levelStarted = true;
		}

		// update entities
		levelManager.update(tpf);

		if (!levelManager.levelFailed) {
			vehicle.update(tpf);
		}

		// update score
		score = levelManager.levelTime / 100;

		checkCollision();

//		debugPrint();
	}

	private void checkCollision() {

		// check collision vs obstacles
		levelManager.checkCollision(vehicle);

		// floor check
		if (vehicle.y + vehicle.height > floorHeight) {
			vehicle.y = floorHeight - 1 - vehicle.height;
			vehicleOnFloor = true;
		} else {
			vehicleOnFloor = false;
		}
	}

	@SuppressWarnings("unused")
	private void debugPrint() {

	}

	/**
	 * Draw scene.
	 */
	@Override
	protected void doRender() {
		p.background(0);

		drawOverlay();
		drawVehicle();
		drawFloor();
		drawScore();
		levelManager.draw(p);
	}

	private void drawVehicle() {
		vehicle.draw(p);
	}

	private void drawFloor() {
		p.rectMode(PApplet.CORNERS);
		p.fill(155);
		p.rect(30, floorHeight, p.width - 30, floorHeight + 50);
	}

	private void drawOverlay() {
		// frame surrounding the playfield
		p.rectMode(PApplet.CORNERS);
		p.noFill();
		p.strokeJoin(PApplet.ROUND);
		p.strokeWeight(1);
		p.stroke(255, 255, 0, 150);
		p.rect(20, scoreHeight + 5, p.width - 20, p.height - 20);

		// info message to start level
		if (!levelManager.levelStarted) {
			p.textSize(15);
			p.textAlign(PApplet.LEFT);
			p.text("press '" + TwoButtonJam.KEY_2 + "' to start", p.width * 0.1f, p.height * 0.45f);
		}

		if (levelManager.levelFinished || levelManager.levelFailed) {
			String info = "Level finished!";
			if (levelManager.levelFailed) {
				info = "Level failed!";
			}
			p.textSize(15);
			p.textAlign(PApplet.LEFT);
			p.text(info, p.width * 0.5f, p.height * 0.45f);
			p.text("press '" + TwoButtonJam.KEY_1 + "' and '" + TwoButtonJam.KEY_2 + "' (" + timeToSwitchToMenu / 1000
					+ "sec) to restart", p.width * 0.5f, p.height * 0.48f);
		}

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
		p.text("Distance (best:" + maxScore + ")", padding, padding);
		p.textAlign(PApplet.RIGHT, PApplet.CENTER);
		p.text(df.format(score), p.width - padding, padding);
	}

	private void updateMaxScore() {
		if (score > maxScore) {
			maxScore = score;
		}
	}

	/**
	 * checks if the given key is pressed.
	 */
	private boolean isPressed(char key, int ms) {
		final Long keyTime = pressedKeys.get(key);
		if (keyTime == null) {
			return false;
		}
		return keyTime > ms;
	}

	/**
	 * record how long a key is pressed
	 */
	private void updateKeyTime(long tpf, char key) {
		Long leftKey = pressedKeys.get(key);
		if (leftKey != null) {
			pressedKeys.put(key, leftKey += tpf);
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		final char key = event.getKey();
		if (pressedKeys.containsKey(key) == false) {
			pressedKeys.put(key, 0L);
		}

	}

	@Override
	public void keyReleased(KeyEvent event) {
		final char key = event.getKey();
		pressedKeys.remove(key);
	}

}
