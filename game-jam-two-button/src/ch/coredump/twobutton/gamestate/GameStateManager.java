package ch.coredump.twobutton.gamestate;

import java.util.ArrayList;
import java.util.List;

import processing.event.KeyEvent;

public class GameStateManager {
	List<BaseGameState> states = new ArrayList<>();

	public void addState(BaseGameState state) {
		states.add(state);
	}

	public void setActive(GameState... activeStates) {
		states.stream().forEach(s -> s.setActive(false));
		for (BaseGameState state : states) {
			for (GameState type : activeStates) {
				if (state.getType() == type) {
					state.activate();
				}
			}
		}
	}

	public void update() {
		for (BaseGameState state : states) {
			if (!state.isActive()) {
				continue;
			}
			state.update();
		}
	}

	public void render() {
		for (BaseGameState state : states) {
			if (!state.isActive()) {
				continue;
			}
			state.render();
		}
	}

	public void keyReleased(KeyEvent event) {
		for (BaseGameState state : states) {
			if (!state.isActive()) {
				continue;
			}
			state.keyReleased(event);
		}
	}

	public void keyPressed(KeyEvent event) {
		for (BaseGameState state : states) {
			if (!state.isActive()) {
				continue;
			}
			state.keyPressed(event);
		}

	}
}
