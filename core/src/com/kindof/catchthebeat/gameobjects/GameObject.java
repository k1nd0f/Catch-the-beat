package com.kindof.catchthebeat.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class GameObject {
    protected TextureRegion texture;
    protected float x, y, width, height;
    protected Rectangle bounds;

    public GameObject(TextureRegion texture, float x, float y, float width, float height) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    protected void flip(boolean x, boolean y) {
        texture.flip(x, y);
    }

    public void setY(float y) {
        bounds.y = y;
        this.y = y;
    }

    public void setX(float x) {
        bounds.x = x;
        this.x = x;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public TextureRegion getTexture() {
        return texture;
    }
}
