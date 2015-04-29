package com.weibo.hackathon.deadline.engine.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.weibo.hackathon.deadline.engine.GameSession;
import com.weibo.hackathon.deadline.engine.model.GameObject;
import com.weibo.hackathon.deadline.engine.render.TextRender;
import com.weibo.hackathon.deadline.game.Game;
import com.weibo.hackathon.deadline.game.Ground;

public class RoomManager extends Thread {
	private List<GameSession> sessions;

	public RoomManager(List<GameSession> sessions) {
		this.sessions = sessions;
	}

	@Override
	public void run() {
		if (sessions.size() == 1) {
			// playAnimator();
		}

		// List<GameRoom> gcl = createGameControllers();

		Game game = new Game();
		game.setPlayerNumber(sessions.size());
		List<Ground> plays = new ArrayList<Ground>(sessions.size());
		for (String id : new TreeSet<String>(game.playerIds())) {
			plays.add(game.getPlaying(id));
		}

		outer: while (true) {
			try {
				Thread.sleep(30);
				for (int i = 0; i < sessions.size(); i++) {
					// GameRoom controller = gcl.get(i);
					// for (int i = 0; i < sessions.size(); i++) {
					// sessions.get(i).getInputManager().getInputStatus();
					// }
					GameSession gameSession = sessions.get(i);
					GameInput input = gameSession.getInputManager()
							.getInputStatus();

					if (game.isOver()) {
						if (input == GameInput.RESTART && sessions.size() == 1) {
							game = new Game();
							game.setPlayerNumber(sessions.size());
							plays = new ArrayList<Ground>(sessions.size());
							for (String id : new TreeSet<String>(
									game.playerIds())) {
								plays.add(game.getPlaying(id));
							}
							continue outer;
						}
						continue;
					}
					Ground play = plays.get(i);
					if (input == GameInput.UP) {
						play.playerUp();
					} else if (input == GameInput.DOWN) {
						play.playerDown();
					}

					// Scene scene = controller.getScene();
					// GameObject translate = ModelAdapter.translate(scene);
					//
					// // h += 0.08;
					// // l1.height = (int) Math.abs((Math.sin(h)) * 5) + 1;
					// // l1.width = (w++) % 80;
					// String data = new TextRender().render(translate);
					//
					// gameSession.getOutputDevice().output(data);
				}
				game.oneStep();
				for (int i = 0; i < sessions.size(); i++) {
					GameObject go = game.getUpdate(plays.get(i).getId());
					if (go != null) {
						String data = new TextRender().render(go);
						sessions.get(i).getOutputDevice().output(data);
					}
				}
			} catch (InterruptedException e4) {

			} catch (IOException e3) {
				for (GameSession session : sessions) {
					session.close();
				}
			}
		}

	}

	private void playAnimator() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					getClass().getClassLoader()
							.getResourceAsStream("movie.ani")));
			String tempString = null;
			int line = 0;
			while ((tempString = reader.readLine()) != null) {
				line++;
				for (int i = 0; i < sessions.size(); i++) {
					sessions.get(i).getOutputDevice().outputRaw(tempString);
					if (line % 34 != 0) {
						sessions.get(i).getOutputDevice().outputRaw("\r\n");
					}
				}
				if (line % 34 == 0) {
					if (line / 34 < 3 || line / 34 > 39) {
						Thread.sleep(1500);
					} else {
						Thread.sleep(100);
					}

				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
