package com.tavepe;

public class Sand implements Pixel {
    int id = 1;
    float R = 1f;
    float G = 1f;
    float B = 0f;

    @Override
    public float[] getColor() {
        return new float[]{R, G, B};
    }

    @Override
    public int getId() {
        return id;
    }

}
