package com.weibo.hackathon.deadline.engine;

public class GameResult {
    public static GameResult FAIL = new GameResult("gameover"), SUCCESS = new GameResult("success"), CANCELLED = new GameResult("cancelled");

    public String text() {
        return this.text;
    }

    private String text;
    private int time;


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
    public GameResult clone() {
        GameResult tmp = new GameResult(this.text);
        return tmp;
    }

    private GameResult(String text) {
        this.text = text;
    }

}
