package com.weibo.hackathon.deadline.engine.output;

import java.io.IOException;

/**
 * Created by axb on 15/4/17.
 */
public interface OutputDevice<T> {
    void output(String data) throws IOException;
    
    void outputRaw(String data) throws IOException;
}
