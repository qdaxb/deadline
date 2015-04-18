package com.weibo.hackathon.deadline.engine;

public enum GameResult {
    FAIL("gameover"), SUCCESS("gameover"), CANCELLED("cancelled");

    public String text() {
        // TODO Auto-generated method stub
        return this.text;
    }

    private String text;

    private GameResult(String text) {
        this.text = text;
    }

}
