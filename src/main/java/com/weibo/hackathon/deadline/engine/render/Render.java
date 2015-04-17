package com.weibo.hackathon.deadline.engine.render;

import com.weibo.hackathon.deadline.engine.model.GameObject;

/**
 * Created by axb on 15/4/17.
 */
public interface Render<T> {
    public T render(GameObject root);
}
