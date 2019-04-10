package ch.coredump.twobutton.entity;

import processing.core.PApplet;
import processing.sound.SoundFile;

public class SoundManager {
	public enum Effect {
		SHOT, IMPACT;
	}

	private static SoundManager instance;

	PApplet p;
	SoundFile menuMusic;
	SoundFile gameMusic;
	SoundFile shot;
	SoundFile impact;

	float amp = 0.5f;
	float rate = 1;

	public static SoundManager get() {
		return instance;
	}

	public static void init(PApplet p) {
		instance = new SoundManager(p);
	}

	private SoundManager(PApplet p) {
		this.p = p;
		loadSounds();
	}

	private void loadSounds() {
		menuMusic = new SoundFile(p, "resources/menu.mp3");
		gameMusic = new SoundFile(p, "resources/running.mp3");
		shot = new SoundFile(p, "resources/zapsplat_multimedia_laser_weapon_fire_001_25877.mp3");
		impact = new SoundFile(p, "resources/impact.mp3");
	}

	public void playMenuMusic() {
		amp = 0.5f;
		stop();
		menuMusic.loop(rate, amp);
	}

	public void playGameMusic() {
		amp = 0.5f;
		stop();
		gameMusic.play(rate, amp);
	}

	public void stop() {
		menuMusic.stop();
		gameMusic.stop();
	}

	public void play(Effect effect) {
		switch (effect) {
		case IMPACT:
			impact.play(rate, amp);
			break;
		case SHOT:
			shot.play(rate, amp);
			break;
		default:
			break;
		}
	}
}
