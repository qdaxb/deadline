package com.weibo.hackathon.deadline.controller;

import java.util.LinkedList;
import java.util.List;

import com.weibo.hackathon.deadline.engine.Action;

public class AggregateActionGenerator implements ActionGenerator {

    List<ActionGenerator> generators = new LinkedList<ActionGenerator>();

    @Override
    public Action nextAction() {
        for (ActionGenerator ag : generators) {
            Action act = ag.nextAction();
            if (act != null) {
                return act;
            }
        }
        return null;
    }

    @Override
    public boolean isOver() {
        for (ActionGenerator ag : generators) {
            if (!ag.isOver()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isNextAvailable() {
        for (ActionGenerator ag : generators) {
            if (ag.isNextAvailable()) {
                return true;
            }
        }
        return false;
    }

}
