package com.weibo.hackathon.deadline.controller.action;


public class MoveAction {

    public int forward;
    public int shift;

    public int steps = 0;
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

    public boolean active() {
        return steps > 0;
    }

}
