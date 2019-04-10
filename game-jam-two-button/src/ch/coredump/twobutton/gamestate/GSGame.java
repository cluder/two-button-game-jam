package ch.coredump.twobutton.gamestate;

import java.text.DecimalFormat;
import java.util.HashMap;

import ch.coredump.twobutton.entity.SoundManager;
import ch.coredump.twobutton.entity.Vehicle;
import ch.coredump.twobutton.util.Background;
import ch.coredump.twobutton.util.Consts;
import ch.coredump.twobutton.util.LevelManager;
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

	Background bg;
	float movementSpeed = 3;
	Vehicle vehicle;
	boolean vehicleOnFloor = false;

	LevelManager levelManager;

	public GSGame(PApplet p, GameStateManager manager) {
		super(p, manager, GameState.GAME);

		// TODO correct padding (50)
		bg = new Background(p, 50, 70, p.width - 50, (int) floorHeight - 70, movementSpeed);
		vehicle = new Vehicle(p);
		levelManager = new LevelManager(p, floorHeight);
	}

	@Override
	public void init() {

	}

	@Override
	public void onActivate() {
		reset();
		SoundManager.get().playGameMusic();
	}

	public long getMaxScore() {
		return maxScore;
	}

	private void reset() {
		pressedKeys.clear();
		score = 0;
		vehicle.reset(p.width, (int) floorHeight);

		levelManager.init(movementSpeed);
		bg.init(50, 100, p.width - 50, (int) floorHeight - 70);
	}

	@Override
	protected void doUpdate(long tpf) {
		updateMaxScore();

		updateKeyTime(tpf, Consts.KEY_1);
		updateKeyTime(tpf, Consts.KEY_2);

		// press left & right key to return to menu / restart
		if (isPressed(Consts.KEY_1, timeToSwitchToMenu) && isPressed(Consts.KEY_2, timeToSwitchToMenu)) {
			manager.setActive(GameState.MENU);
		}

		// jump
		if (isPressed(Consts.KEY_1, 1)) {
			// only jump, if vehicle is on the floor
			if (vehicleOnFloor) {
				vehicle.ySpeed = -1.5f;
			}
		}

		if (isPressed(Consts.KEY_2, 1)) {
			levelManager.levelStarted = true;
		}

		// update entities
		levelManager.update(tpf);

		if (levelManager.isRunning()) {
			vehicle.update(tpf);
			bg.update(tpf);
		}

		// update score
		score = levelManager.levelTime / 100;

		checkCollision();

//		debugPrint();
	}

	private void checkCollision() {

		// check collision vs obstacles
		boolean collision = levelManager.checkCollision(vehicle);
		if (collision) {
			SoundManager.get().stop();
		}

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
		bg.draw();

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
			p.textAlign(PApplet.CENTER);
			p.textSize(Consts.DEFAULT_FONT_SIZE);
			p.noFill();
			p.stroke(255, 255, 0, 255);
			p.text("press '" + Consts.KEY_2 + "' to start level", p.width * .5f, p.height * 0.35f);
		}

		if (levelManager.levelFinished || levelManager.levelFailed) {
			String info = "Level finished!";
			if (levelManager.levelFailed) {
				info = "Level failed!";
			}
			p.textAlign(PApplet.CENTER);
			p.textSize(Consts.DEFAULT_FONT_SIZE);
			p.text(info, p.width * 0.5f, p.height * 0.2f);
			p.text("Hold '" + Consts.KEY_1 + "' and '" + Consts.KEY_2 + "' to restart", p.width * 0.5f,
					p.height * 0.25f);
		}

		// help text
		p.textSize(Consts.DEFAULT_FONT_SIZE);
		p.textAlign(PApplet.LEFT);

		float yPos = p.height * 0.73f;
		p.text("Controls:", p.width * 0.05f, yPos);
		p.text("Jump: '" + Consts.KEY_1 + "'", p.width * 0.25f, yPos);
		yPos += 20;
		p.text("Fire: '" + Consts.KEY_2 + "'", p.width * 0.25f, yPos);

	}

	private void drawScore() {
		int padding = 20;

		DecimalFormat df = new DecimalFormat("#0");
		df.setGroupingSize(3);
		df.setGroupingUsed(true);

		p.strokeWeight(1);
		p.stroke(255, 255, 0);
		p.line(padding, 50, p.width - padding, 50);

		p.textSize(Consts.SCORE_FONT_SIZE);
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

		if (event.getKey() == Consts.KEY_2 && levelManager.isRunning()) {
			vehicle.fire();
		}

	}

	@Override
	public void keyReleased(KeyEvent event) {
		final char key = event.getKey();
		pressedKeys.remove(key);
	}

}
