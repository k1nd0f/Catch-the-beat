package com.kindof.catchthebeat.screens.beatmapeditor.settings;

public class Velocity {
    public float plus, minus, accelerate;

    public Velocity() {
        this(0, 0, 0);
    }

    public Velocity(float plus, float minus, float accelerate) {
        this.plus = plus;
        this.minus = minus;
        this.accelerate = accelerate;
    }
}
