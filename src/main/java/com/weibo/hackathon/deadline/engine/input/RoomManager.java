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
            //playAnimator();
        }
        // GameObject root = new GameObject();
        // Element e = new Block();
        // Size s = new Size(30, 69);
        // Location l = new Location(1, 10);
        // e.size = s;
        // e.loc = l;
        // root.element = e;
        //
        // GameObject player = new GameObject();
        // Player e1 = new Player();
        // Location l1 = new Location(1, 10);
        // e1.size = s;
        // e1.loc = l1;
        // player.element = e1;
        //
        // GameObject asc = new GameObject();
        // AscIIImage image = new AscIIImage();
        // Location l2 = new Location(4, -9);
        // image.size = s;
        // image.loc = l2;
        // asc.element = image;
        //
        //
        // root.children.add(player);
        // root.children.add(asc);
        // asc.father = root;
        // player.father = root;
        //
        // GameObject string = new GameObject();
        // GameString e2 = new GameString();
        // e2.content = "Joking";
        // e2.size = new Size(1, 9);
        // e2.loc = new Location(28, 60);
        // string.element = e2;
        //
        // root.children.add(player);
        // root.children.add(string);
        // string.father = root;
        // player.father = root;
        // double h = 0;
        // int w = 0;

        List<GameController> gcl = new ArrayList<GameController>(sessions.size());
        GameControllerImpl ctl1 = new GameControllerImpl();
        GameControllerImpl ctl2 = new GameControllerImpl();
        ctl1.init("player1");
        ctl2.init("player2");
        gcl.add(ctl1);
        gcl.add(ctl2);
        
        ctl1.connect(ctl2);

        while (true) {
            try {
                Thread.sleep(100);
                for (int i = 0; i < sessions.size(); i++) {
                    GameController controller = gcl.get(i);
                    // for (int i = 0; i < sessions.size(); i++) {
                    // sessions.get(i).getInputManager().getInputStatus();
                    // }
                    GameSession gameSession = sessions.get(i);
                    GameInput input = gameSession.getInputManager().getInputStatus();

                    if(controller.isOver()) {
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
