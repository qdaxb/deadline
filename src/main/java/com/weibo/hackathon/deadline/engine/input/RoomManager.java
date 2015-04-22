package com.weibo.hackathon.deadline.engine.input;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.weibo.hackathon.deadline.controller.GameControllerImpl;
import com.weibo.hackathon.deadline.engine.GameController;
import com.weibo.hackathon.deadline.engine.GameSession;
import com.weibo.hackathon.deadline.engine.model.GameObject;
import com.weibo.hackathon.deadline.engine.model.ModelAdapter;
import com.weibo.hackathon.deadline.engine.model.Scene;
import com.weibo.hackathon.deadline.engine.render.TextRender;

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

        List<GameController> gcl = createGameControllers();


        outer: while (true) {
            try {
                Thread.sleep(150);
                for (int i = 0; i < sessions.size(); i++) {
                    GameController controller = gcl.get(i);
                    // for (int i = 0; i < sessions.size(); i++) {
                    // sessions.get(i).getInputManager().getInputStatus();
                    // }
                    GameSession gameSession = sessions.get(i);
                    GameInput input = gameSession.getInputManager().getInputStatus();

                    if (controller.isOver()) {
                        if (input == GameInput.RESTART && sessions.size() == 1) {
                            gcl = createGameControllers();
                            continue outer;
                        }
                        continue;
                    }
                    controller.input(input);

                    Scene scene = controller.getScene();
                    GameObject translate = ModelAdapter.translate(scene);



                    // h += 0.08;
                    // l1.height = (int) Math.abs((Math.sin(h)) * 5) + 1;
                    // l1.width = (w++) % 80;
                    String data = new TextRender().render(translate);

                    gameSession.getOutputDevice().output(data);
                }
            } catch (InterruptedException e4) {

            } catch (IOException e3) {
                for (GameSession session : sessions) {
                    session.close();
                }
            }
        }

    }

    private List<GameController> createGameControllers() {
        List<GameController> gcl = new ArrayList<GameController>(sessions.size());
        for (int i = 0; i < 2; i++) {
            GameControllerImpl ctl1 = new GameControllerImpl();
            ctl1.init(sessions.get(i > sessions.size() - 1 ? sessions.size() - 1 : i).getPlayerName());
            gcl.add(ctl1);
        }
        if (gcl.size() > 1) {
            gcl.get(0).connect(gcl.get(1));
        }
        return gcl;
    }

    private void playAnimator() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("movie.ani")));
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
