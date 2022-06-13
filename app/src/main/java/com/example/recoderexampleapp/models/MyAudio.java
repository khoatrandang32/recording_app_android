package com.example.recoderexampleapp.models;

import java.io.Serializable;

public class MyAudio implements Serializable {
    String title;
    String pathname;
    Long time;
    Long duration;

    public MyAudio(String title, String pathname, Long time, Long duration) {
        this.title = title;
        this.pathname = pathname;
        this.time = time;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPathname() {
        return pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
