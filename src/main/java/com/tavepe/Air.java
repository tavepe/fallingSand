package com.tavepe;

public class Air implements Pixel {
    int id = 0;
    float R = 0.78f;
    float G = 0.95f;
    float B = 1f;

    @Override
    public float[] getColor() {
        return new float[]{R, G, B};
    }

    @Override
    public int getId() {
        return id;
    }
}
