package com.kindof.catchthebeat.screens.beatmapeditor.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.kindof.catchthebeat.ui.UI;

public class HitLine {
    private static Color COLOR = new Color(1f, 0.23f, 0.23f, 1f);

    private float x, y, width, height;
    private Rectangle bounds;

    public HitLine(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        bounds = new Rectangle(x, 0, width, y);
    }

    public float getY() {
        return y;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void draw(float alpha) {
        COLOR = new Color(COLOR.r, COLOR.g, COLOR.b, alpha);
        UI.SHAPE_RENDERER.setColor(COLOR);
        UI.SHAPE_RENDERER.begin(ShapeRenderer.ShapeType.Filled);
        UI.SHAPE_RENDERER.rect(x, y, width, height);
        UI.SHAPE_RENDERER.end();
    }
}
