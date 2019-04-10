package ch.coredump.twobutton.gamestate;

import ch.coredump.twobutton.entity.SoundManager;
import ch.coredump.twobutton.util.Consts;
import processing.core.PApplet;

public class GSMenu extends BaseGameState {
	long menuTime = 0;
	String title = "TWO BUTTON JAM";
	String title1 = "";
	String info1 = "Press '1' to jump, '2' to fire.";
	String info2 = "Press '2' to start";
	String credits = "Contains music ©2019 Joshua McLean (mrjoshuamclean.com)\r\n"
			+ "Licensed under Creative Commons Attribution-ShareAlike 4.0 International\r\n\n"
			+ "Contains free to use soundeffects from https://www.zapsplat.com\n" + "Font 'Press start' by codeman38 ";

	int waitTime = 1000;

	public GSMenu(PApplet p, GameStateManager manager) {
		super(p, manager, GameState.MENU);
	}

	@Override
	public void init() {
		super.init();
	}

	public void onActivate() {
		// when coming from the game, we wait 5 seconds until we allow switching to the
		// game again
		waitTime = 1000;
		menuTime = 0;
		SoundManager.get().playMenuMusic();
	}

	@Override
	protected void doRender() {
		p.background(0);
		p.fill(255);
		p.textAlign(PApplet.CENTER, PApplet.CENTER);

		float y = p.height * .25f;
		// title
		p.textSize(Consts.DEFAULT_FONT_SIZE - 2);
		p.text("A game prototype for the ", p.width / 2, y);

		y = p.height * .32f;
		p.textSize(Consts.DEFAULT_FONT_SIZE + 7);
		p.text("8 Bits to Infinity ", p.width / 2, y);

		y = p.height * .4f;
		p.textSize(Consts.DEFAULT_FONT_SIZE + 25);
		p.text(title, p.width / 2, y);

		// info
		y = p.height * 0.60f;
		p.textSize(Consts.DEFAULT_FONT_SIZE);
		p.text(info1, p.width / 2, y);
		if (switchAllowed()) {
			y += p.height * .05f;
			p.text(info2, p.width / 2, y);
		}

		// credits
		y = p.height * 0.9f;
		p.textSize(Consts.DEFAULT_FONT_SIZE - 7);
		p.text(credits, p.width / 2, y);

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
