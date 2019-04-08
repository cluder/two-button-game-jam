package ch.coredump.twobutton.gamestate;

import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

/**
 * Base class for all GameStates.<br>
 * Contains basic logic used in all parts of the game.
 */
public abstract class BaseGameState {
	PApplet p;

	long lastFrameTime = System.currentTimeMillis();
	long tpf = 0;

	GameStateManager manager;
	GameState type;

	boolean active;

	public BaseGameState(PApplet p, GameStateManager manager, GameState type) {
		this.p = p;
		this.manager = manager;
		this.type = type;

		init();
	}

	/**
	 * Called once when creating the game state.
	 */
	public void init() {

	}

	abstract public void onActivate();

	/**
	 * Called when this game state is added to the active gamestate list.
	 */
	public void activate() {
		setActive(true);
		updateFrameTime();

		onActivate();
	}

	public void render() {
		doRender();
	}

	protected abstract void doRender();

	public void update() {
		updateFrameTime();

		doUpdate(tpf);
	}

	private void updateFrameTime() {
		final long currentTime = System.currentTimeMillis();
		tpf = currentTime - lastFrameTime;
		lastFrameTime = currentTime;
	}

	protected abstract void doUpdate(long tpf);

	public void mousePressed(MouseEvent event) {

	}

	public void keyPressed(KeyEvent event) {

	}

	public void keyReleased(KeyEvent event) {

	}

	public PApplet getProcessing() {
		return p;
	}

	public GameState getType() {
		return type;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}
}