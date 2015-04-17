package com.weibo.hackathon.deadline.engine.input;

import java.io.IOException;
import java.util.List;

import com.weibo.hackathon.deadline.engine.GameSession;
import com.weibo.hackathon.deadline.engine.model.Block;
import com.weibo.hackathon.deadline.engine.model.Element;
import com.weibo.hackathon.deadline.engine.model.GameObject;
import com.weibo.hackathon.deadline.engine.model.GameString;
import com.weibo.hackathon.deadline.engine.model.Location;
import com.weibo.hackathon.deadline.engine.model.Player;
import com.weibo.hackathon.deadline.engine.model.Size;
import com.weibo.hackathon.deadline.engine.render.TextRender;

public class RoomManager extends Thread {
    private List<GameSession> sessions;

    public RoomManager(List<GameSession> sessions) {
        this.sessions = sessions;
    }

    @Override
    public void run() {

        GameObject root = new GameObject();
        Element e = new Block();
        Size s = new Size(30, 79);
        Location l = new Location(1, 1);
        e.size = s;
        e.loc = l;
        root.element = e;

        GameObject player = new GameObject();
        Player e1 = new Player();
        Location l1 = new Location(1, 10);
        e1.size = s;
        e1.loc = l1;
        player.element = e1;

        root.children.add(player);
        player.father = root;

        GameObject string = new GameObject();
        GameString e2 = new GameString();
        e2.content = "Fuck";
        e2.size = new Size(1, 9);
        e2.loc = new Location(28, 60);
        string.element = e2;

        root.children.add(player);
        root.children.add(string);
        string.father = root;
        player.father = root;
        double h = 0;
        int w = 0;

        while (true) {
            try {
                Thread.sleep(50);
                for (int i = 0; i < sessions.size(); i++) {
                    sessions.get(i).getInputManager().getInputStatus();
                }


                h += 0.08;
                l1.height = (int) Math.abs((Math.sin(h)) * 5) + 1;
                l1.width = (w++) % 80;
                String data = new TextRender().render(root);

                for (int i = 0; i < sessions.size(); i++) {
                    sessions.get(i).getOutputDevice().output(data);
                }
            } catch (InterruptedException e4) {

            } catch (IOException e3) {
                for (GameSession session : sessions) {
                    session.close();
                }
            }
        }

    }
}
