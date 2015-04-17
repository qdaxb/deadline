package com.weibo.hackathon.deadline.controller.action;


public class MoveAction {

    public int shift;

    public int steps = 0;
    public int interval = 1;
    public float speed;
    public int remain = 0;

    public void setSteps(int steps) {
        this.steps = steps;
    }

    /**
     * @param speed
     */
    public void setSpeed(float speed) {
        this.speed = speed;
        if (speed != 0) {
            this.interval = (int) Math.abs(1 / speed);
        } else {
            this.interval = Integer.MAX_VALUE;
        }
    }

    public void perform() {
        if (speed == 0) {
            return;
        }
        if (steps > 0) {
            remain++;
            if (remain >= interval) {
                if(steps != Integer.MAX_VALUE) {
                    steps--;
                }
                shift += Math.round(this.speed * remain);
                remain = 0;
            }
            if (steps < 0) {
                steps = 0;
            }
        }
    }

    public boolean active() {
        return steps > 0;
    }

}
