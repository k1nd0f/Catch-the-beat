package com.kindof.catchthebeat.gameobjects.catcher;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kindof.catchthebeat.resources.Globals;

class Hud {
    private float padding; // = [0; 0.425]  : recommended range of values
    private float spacing; // = (0; 2.5]    : recommended range of values
    private float alpha;   // = [0; 1]      : recommended range of values
    private float scale;   // = (0; 1.7]    : recommended range of values

    Hud() {
        this(0f, 0.9f, 1f, 1f);
    }

    Hud(float padding, float spacing, float scale, float alpha) {
        this.padding = padding * Globals.HEIGHT;
        this.spacing = spacing;
        this.scale = scale;
        this.alpha = alpha;
    }

    void draw(SpriteBatch batch, Score score, com.kindof.catchthebeat.gameobjects.catcher.Health health, com.kindof.catchthebeat.gameobjects.catcher.Combo combo, com.kindof.catchthebeat.gameobjects.catcher.Accuracy accuracy) {
        Color c = batch.getColor();
        float r = c.r, g = c.g, b = c.b, a = c.a;

        batch.setColor(1, 1, 1, alpha);
        score.draw(batch, spacing, scale, padding);
        health.draw(batch, scale, padding);
        combo.draw(batch, spacing, scale, padding);
        accuracy.draw(batch, spacing, scale, padding);
        batch.setColor(r, g, b, a);
    }
}