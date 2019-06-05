package com.example.moviesappstage2nano.modellingclasses;

import java.io.Serializable;

public class VideoDetails implements Serializable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String name;

    public VideoDetails(String name, String key) {
        this.name = name;
        this.key = key;
    }

    private String key;
}


