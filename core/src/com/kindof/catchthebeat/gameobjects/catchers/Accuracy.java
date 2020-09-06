package com.kindof.catchthebeat.gameobjects.catchers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kindof.catchthebeat.resources.Res;

class Accuracy extends Numbers {
    float y, width, height;
    float accuracy, sum;
    private TextureRegion comma;
    private TextureRegion percent;

    Accuracy() {
        width = 15 * Res.RESOLUTION_WIDTH_SCALE;
        height = width * 1.5f;
        y = 0;

        comma = Res.SKIN_ATLAS.findRegion(Res.SCORE_COMMA);
        percent = Res.SKIN_ATLAS.findRegion(Res.SCORE_PERCENT);
    }

    void draw(SpriteBatch batch, float spacing, float scale, float down) {
        int first = (int) accuracy;
        int last = (int) ((accuracy - (int) accuracy) * 100);
        int length = first == 100 ? 7 : 6;
        int i = 0;

        batch.draw(percent, Res.WIDTH - width * scale, y + down, width * scale, height * scale);
        for (i++; i < 3; i++) {
            int n = last % 10;
            last /= 10;

            batch.draw(numbers.get(n), Res.WIDTH - width * scale * spacing * i - width * scale, y + down, width * scale, height * scale);
        }

        batch.draw(comma, Res.WIDTH - width * scale * spacing * i - width * scale, y + down, width * scale, height * scale);
        for (i++; i < length; i++) {
            int n = first % 10;
            first /= 10;

            batch.draw(numbers.get(n), Res.WIDTH - width * scale * spacing * i - width * scale, y + down, width * scale, height * scale);
        }
    }
}