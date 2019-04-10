package ch.coredump.twobutton.gamestate;

import processing.core.PApplet;
import processing.sound.SoundFile;

public class GSMenu extends BaseGameState {
	long menuTime = 0;
	String title = "TWO BUTTON JAM";
	String info1 = "Press '1' to jump, '2' to fire.";
	String info2 = "Press '2' to start";

	SoundFile music;
	int waitTime = 1000;

	public GSMenu(PApplet p, GameStateManager manager) {
		super(p, manager, GameState.MENU);
	}

	@Override
	public void init() {
		super.init();

//		music = new SoundFile(p, "resources/two-buttons.mp3");
	}

	public void onActivate() {
		// when coming from the game, we wait 5 seconds until we allow switching to the
		// game again
		waitTime = 1000;
		menuTime = 0;
	}

	@Override
	protected void doRender() {
		p.background(0);

		p.fill(255);
		// title
		p.textSize(45);
		p.textAlign(PApplet.CENTER, PApplet.CENTER);
		p.text(title, p.width / 2, p.height * .2f);

		// info
		p.textSize(20);
		p.textAlign(PApplet.CENTER, PApplet.CENTER);
		p.text(info1, p.width / 2, p.height * .5f);
		if (switchAllowed()) {
			p.text(info2, p.width / 2, p.height * .6f);
		}
	}

	private boolean switchAllowed() {
		return menuTime > waitTime;
	}

	@Override
	protected void doUpdate(long tpf) {
		menuTime += tpf;

		if (switchAllowed() && p.keyPressed && p.key == '2') {
			manager.setActive(GameState.GAME);
			System.out.println("switching to game with menuTime:" + menuTime);
		}
	}

}
