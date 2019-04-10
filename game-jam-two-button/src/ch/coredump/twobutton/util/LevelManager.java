package ch.coredump.twobutton.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ch.coredump.twobutton.entity.Obstacle;
import ch.coredump.twobutton.entity.Projectile;
import ch.coredump.twobutton.entity.SoundManager;
import ch.coredump.twobutton.entity.Vehicle;
import processing.core.PApplet;

/**
 * Manages drawing / updating of obstacles.
 */
public class LevelManager {
	PApplet p;

	public long levelTime = 0;
	public boolean levelStarted = false;
	public boolean levelFailed = false;
	public boolean levelFinished = false;
	List<Obstacle> obstacles = new ArrayList<>();

	float floorHeight;

	public LevelManager(PApplet p, float floorHeight) {
		this.p = p;
		this.floorHeight = floorHeight;
	}

	public void init(float speed) {
		levelStarted = false;
		levelFinished = false;
		levelFailed = false;
		levelTime = 0;

		// todo add some kind of level mechanism
		obstacles.clear();

		// starting x/y coords
		float obstacleX = p.width;
		float obstacleY = floorHeight;

		{
			final Obstacle o = new Obstacle(p, obstacleX, obstacleY, 3000, speed);
			add(o);
		}
		{
			final Obstacle o = new Obstacle(p, obstacleX, obstacleY, 8000, speed);
			add(o);
		}
	}

	private void add(Obstacle o) {
		obstacles.add(o);
	}

	public void start() {
		levelStarted = true;
	}

	public void update(long tpf) {
		if (!levelStarted) {
			// level has not yet started
			return;
		}

		if (levelFailed) {
			// we failed, stop updating positions
			return;
		}

		if (levelStarted && levelFinished == false && levelFailed == false) {
			levelTime += tpf;
		}

		// all obstacles move at the same speed towards the player
		for (Obstacle o : obstacles) {
			if (levelTime > o.spawnTime) {
				o.update(tpf);
				if (o.x < 50) {
					o.dead = true;
				}
			}
		}

		final Optional<Obstacle> result = obstacles.stream().filter(p -> p.dead == false).findAny();
		if (result.isPresent() == false) {
			levelFinished = true;
		}
	}

	public void draw(PApplet p) {
		for (Obstacle o : obstacles) {
			if (levelTime > o.spawnTime) {
				o.draw(p);
			}
		}
	}

	/**
	 * Check if player and projectiles collides with obstacles.
	 */
	public boolean checkCollision(Vehicle v) {
		for (int i = 0; i < v.img.width; i++) {
			for (int j = 0; j < v.img.height; j++) {

				final int pixel = v.img.get(i, j);
				int a = (pixel >> 24) & 255;
				if (a != v.collisionAlpha) {
					// skip collision check for this pixel
					continue;
				}

				for (Obstacle o : obstacles) {
					if (o.dead) {
						continue;
					}
					// player vs obstacle
					if (o.collidesWith((int) (i + v.x), (int) (j + v.y))) {
						levelFailed = true;
						return true;
					}

					// projectole vs obstacle
					for (Projectile pr : v.projectiles) {
						if (pr.dead) {
							continue;
						}
						if (o.collidesWith((int) pr.x, (int) pr.y)) {
							o.dead = true;
							pr.dead = true;
							SoundManager.get().play(SoundManager.Effect.IMPACT);
						}
					}
				}
			}
		}
		return false;
	}

	public boolean isRunning() {
		return levelStarted && levelFinished == false && levelFailed == false;
	}
}