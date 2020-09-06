package com.kindof.catchthebeat.gameobjects.fruits;

public class Overlap {
    private Overlaps type;
    private float accuracy;

    public Overlap() {
        reset();
    }

    public void set(Overlaps type, float accuracy) {
        this.type = type;
        this.accuracy = accuracy;
    }

    public void reset() {
        type = Overlaps.unknown;
        accuracy = 0;
    }

    public Overlaps getType() {
        return type;
    }

    public float getAccuracy() {
        return accuracy;
    }
}
