package ch.coredump.twobutton;

import ch.coredump.twobutton.gamestate.GSGame;
import ch.coredump.twobutton.gamestate.GSMenu;
import ch.coredump.twobutton.gamestate.GameState;
import ch.coredump.twobutton.gamestate.GameStateManager;
import processing.core.PApplet;
import processing.event.KeyEvent;

public class TwoButtonJam extends PApplet {

	GameStateManager manager;

	public static void main(String[] args) {
		PApplet.main(TwoButtonJam.class);
	}

	@Override
	public void settings() {
		size(600, 600);
	}

	@Override
	public void setup() {
		frameRate(30);

		manager = new GameStateManager();

		manager.addState(new GSMenu(this, manager));
		manager.addState(new GSGame(this, manager));

		manager.setActive(GameState.MENU);
	}

	@Override
	public void draw() {
		manager.update();
		manager.render();
	}

	@Override
	public void keyPressed(KeyEvent event) {
		super.keyPressed(event);

		manager.keyPressed(event);
	}

	@Override
	public void keyReleased(KeyEvent event) {
		super.keyReleased(event);

		manager.keyReleased(event);
	}
}
