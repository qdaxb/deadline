package com.weibo.hackathon.deadline.controller;

final class Frame100TimeController implements TimeController {
    int frame = 10;
    private long last = System.currentTimeMillis();
    private long pause = -1;

    @Override
    public int timeInterval() {
        if (isPaused()) {
            return 0;
        } else {
            long now = System.currentTimeMillis();
            int itv = (int) ((now - last) / frame);
            if (itv > 0) {
                last = now;
            }
            return itv;
        }
    }

    @Override
    public void resume() {
        if (isPaused()) {
            long now = System.currentTimeMillis();
            last += (now - pause);
            pause = -1;
        }
    }

    @Override
    public void pause() {
        pause = System.currentTimeMillis();
    }

    @Override
    public boolean isPaused() {
        return pause > 0;
    }
}