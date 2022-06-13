package com.example.recoderexampleapp.models;

import java.io.Serializable;

public class MyAudio implements Serializable {
    String name;
    String path;
    Long createAt;
    Long duration;

    public MyAudio(String title, String pathname, Long time, Long duration) {
        this.name = title;
        this.path = pathname;
        this.createAt = time;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
