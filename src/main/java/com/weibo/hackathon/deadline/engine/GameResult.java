package com.weibo.hackathon.deadline.engine;

public enum GameResult {
    FAIL("gameover"), SUCCESS("success"), CANCELLED("cancelled");

    public String text() {
        return this.text;
    }

    private String text;

    private GameResult(String text) {
        this.text = text;
    }

}
