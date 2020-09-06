package com.kindof.catchthebeat.gameobjects.fruit;

public class Overlap {
    private Type type;
    private float accuracy;

    public Overlap() {
        reset();
    }

    public void set(Type type, float accuracy) {
        this.type = type;
        this.accuracy = accuracy;
    }

    public void reset() {
        type = Overlap.Type.unknown;
        accuracy = 0;
    }

    public Type getType() {
        return type;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public enum Type {
        unknown, hit, miss
    }
}
