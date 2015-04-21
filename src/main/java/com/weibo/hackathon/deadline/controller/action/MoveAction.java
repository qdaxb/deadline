package com.weibo.hackathon.deadline.controller.action;


public class MoveAction {

    public int shift;
    public int steps = 0;
    public int speed;

    public void setSteps(int steps) {
        this.steps = steps;
    }

    /**
     * @param mov
     */
    public void setMovement(int mov) {
        this.speed = mov ;
    }

    public void perform() {
        if (speed == 0) {
            return;
        }
        if (steps > 0) {
            shift += speed;
            if (steps != Integer.MAX_VALUE) {
                steps--;
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
