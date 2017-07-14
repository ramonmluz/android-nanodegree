package com.android.nanodegree.udacity.myappportifolio.model.vo.movie;

import com.google.gson.annotations.Expose;

/**
 * Created by ramon on 25/06/17.
 */

public class Trailer {

    @Expose
    public static final String TRAILER_URL = "http://www.youtube.com/watch?v=";

    private String id;
    private String key;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
