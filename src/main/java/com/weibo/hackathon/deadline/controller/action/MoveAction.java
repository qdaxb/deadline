package com.weibo.hackathon.deadline.controller.action;

import com.weibo.hackathon.deadline.engine.Action;

public class MoveAction implements Action {

    public int forward;
    public int shift;

    public int steps;
    public int interval = 1;
    public int remain = 0;

    public void setDirection(int forward) {
        if (forward > 0) {
            this.forward = 1;
        } else if (forward < 0) {
            this.forward = -1;
        } else {
            this.forward = 0;
        }
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    /**
     * @param interval
     */
    public void setSpeed(int interval) {
        this.interval = interval;
    }

    @Override
    public void perform(int frames) {
        if (steps > 0) {
            frames += remain;
            int count = frames / interval;
            shift += count * forward;
            remain = frames % interval;
            steps -= count;
            if (steps < 0) {
                steps = 0;
            }
        }
    }

    @Override
    public boolean active() {
        return steps > 0;
    }

}
