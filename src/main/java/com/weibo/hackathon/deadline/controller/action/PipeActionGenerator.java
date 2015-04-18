package com.weibo.hackathon.deadline.controller.action;

import java.util.LinkedList;
import java.util.Queue;

import com.weibo.hackathon.deadline.controller.ActionGenerator;
import com.weibo.hackathon.deadline.controller.GameScene;
import com.weibo.hackathon.deadline.controller.GameSceneImpl;
import com.weibo.hackathon.deadline.controller.RandomActionGenerator;
import com.weibo.hackathon.deadline.engine.Action;
import com.weibo.hackathon.deadline.engine.GameResult;

public class PipeActionGenerator implements ActionGenerator {

    public class ReportResultAction implements Action {

        private final GameScene gs;
        GameResult result;

        public ReportResultAction(GameResult result,GameScene gs) {
            super();
            this.result = result;
            this.gs = gs;
        }

        @Override
        public void perform() {
            // TODO Auto-generated method stub
            if (result == GameResult.CANCELLED) {
                gs.cancel();
            } else if (result == GameResult.SUCCESS) {
                gs.fail();
                System.out.println(gs.getName() + "fail");
            } else if (result == GameResult.FAIL) {
                System.out.println(gs.getName()+"succ");
                gs.success();
            }
            if (another != null) {
                another.another = null;
            }
            another = null;
        }

    }

    PipeActionGenerator another;
    public GameScene gs;
    RandomActionGenerator rag;
    public Queue<Action> actions = new LinkedList<Action>();

    public void setGameScene(GameSceneImpl gs) {
        this.gs = gs;
        rag = new RandomActionGenerator(gs);
        rag.randX = true;
    }

    @Override
    public Action nextAction() {
        return actions.poll();
    }

    @Override
    public boolean isOver() {
        return another == null;
    }

    @Override
    public boolean isNextAvailable() {
        return !actions.isEmpty();
    }

    public void reportResult(GameResult result) {
        another.actions.add(new ReportResultAction(result,another.gs));
    }

    private void makeBlockAction() {
        Action act = null;
        if (rag != null && rag.isNextAvailable()) {
            act = rag.nextAction();
            actions.add(act);
        }
    }

    public void makeBlock() {
        if (another != null) {
            another.makeBlockAction();
        }
    }

    public void pipeWith(PipeActionGenerator pag2) {
        another = pag2;
        pag2.another = this;
    }
}
