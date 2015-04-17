package com.weibo.hackathon.deadline.controller;

import com.weibo.hackathon.deadline.engine.model.Element;

public interface ElementGenerator {

    Element nextElement();

    boolean nextAvailable();

    boolean finished();

}
