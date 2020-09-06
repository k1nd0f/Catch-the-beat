package com.kindof.catchthebeat.gameobjects.catchers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.kindof.catchthebeat.resources.Res;

class Health {
    float y, width, height;
    float health, prevHealth;
    final float MAX_HEALTH;
    private TextureRegion background;
    private TextureRegion foreground;
    private TextureRegion healthRegion;

    Health() {
        MAX_HEALTH = 1;
        width = Res.WIDTH * 0.4f;
        height = Res.HEIGHT * 0.07f;
        y = Res.HEIGHT - height;
        health = prevHealth = MAX_HEALTH;

        background = Res.SKIN_ATLAS.findRegion(Res.HEALTH_BAR_BACKGROUND);
        foreground = Res.SKIN_ATLAS.findRegion(Res.HEALTH_BAR_FOREGROUND);
        healthRegion = new TextureRegion();
    }

    void draw(SpriteBatch batch, float scale, float top) {
        prevHealth = MathUtils.lerp(prevHealth, health, 0.1f);
        healthRegion.setRegion(foreground, 0, 0, (int) (foreground.getRegionWidth() * prevHealth), foreground.getRegionHeight());

        batch.draw(background, 0, y - top - (height * scale - height), width * scale, height * scale);
        batch.draw(healthRegion, 0, y - top - (height * scale - height), width * prevHealth * scale, height * scale);
    }

    double calculate(float x, float healthRate) {
        return Math.cos(x / healthRate * 1.570796308);
    }
}
