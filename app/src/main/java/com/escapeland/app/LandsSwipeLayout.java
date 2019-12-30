package com.escapeland.app;

public class LandsSwipeLayout {

    int lands_images;
    String lands_name;
    String lands_desp;

    public String getLands_desp() {
        return lands_desp;
    }

    public void setLands_desp(String lands_desp) {
        this.lands_desp = lands_desp;
    }

    public LandsSwipeLayout(String lands_desp, String lands_name, int lands_images) {
        this.lands_images = lands_images;
        this.lands_name = lands_name;
        this.lands_desp = lands_desp;
    }

    public String getLands_name() {
        return lands_name;
    }

    public void setLands_name(String lands_name) {
        this.lands_name = lands_name;
    }

    public int getLands_images() {
        return lands_images;
    }

    public void setLands_images(int lands_images) {
        this.lands_images = lands_images;
    }
}
