package ch.coredump.twobutton;

import ch.coredump.twobutton.entity.SoundManager;
import ch.coredump.twobutton.gamestate.GSGame;
import ch.coredump.twobutton.gamestate.GSMenu;
import ch.coredump.twobutton.gamestate.GameState;
import ch.coredump.twobutton.gamestate.GameStateManager;
import ch.coredump.twobutton.util.Consts;
import processing.core.PApplet;
import processing.core.PFont;
import processing.event.KeyEvent;

public class TwoButtonJam extends PApplet {

	GameStateManager manager;

	public static void main(String[] args) {
		args = append(args, TwoButtonJam.class.getName());
		PApplet.main(args);
	}

	@Override
	public void settings() {
		size(800, 600);
	}

	@Override
	public void setup() {
		SoundManager.init(this);

		frameRate(Consts.FPS_TARGET);

		manager = new GameStateManager();

		manager.addState(new GSMenu(this, manager));
		manager.addState(new GSGame(this, manager));

		manager.setActive(GameState.MENU);
		final PFont font = createFont("resources/prstartk.ttf", 10);
		textFont(font);
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
