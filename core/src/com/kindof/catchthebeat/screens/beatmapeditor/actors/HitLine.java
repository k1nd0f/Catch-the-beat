package com.kindof.catchthebeat.screens.beatmapeditor.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class HitLine {
    private float x, y, width, height;
    private Rectangle bounds;
    private ShapeRenderer shapeRenderer;

    public static final Color COLOR = Color.RED;

    public HitLine(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        bounds = new Rectangle(x, 0, width, y);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(COLOR);
    }

    public float getY() {
        return y;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void draw() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
    }
}
