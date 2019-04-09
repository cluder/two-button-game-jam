package ch.coredump.twobutton;

import ch.coredump.twobutton.gamestate.GSGame;
import ch.coredump.twobutton.gamestate.GSMenu;
import ch.coredump.twobutton.gamestate.GameState;
import ch.coredump.twobutton.gamestate.GameStateManager;
import processing.core.PApplet;
import processing.event.KeyEvent;

public class TwoButtonJam extends PApplet {
	// constants
	public static final char KEY_1 = '1';
	public static final char KEY_2 = '2';

	GameStateManager manager;

	public static void main(String[] args) {
		String[] newArgs = append(args, "--location=-1900,400");
		newArgs = append(newArgs, TwoButtonJam.class.getName());
		PApplet.main(newArgs);
	}

	@Override
	public void settings() {
		size(800, 600);
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
