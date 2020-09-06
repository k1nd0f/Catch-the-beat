package com.kindof.catchthebeat.gameobjects.catchers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kindof.catchthebeat.resources.Res;

class Combo extends Numbers {
    float y, width, height;
    int combo, maxCombo;
    private TextureRegion x;

    Combo() {
        width = 20 * Res.RESOLUTION_WIDTH_SCALE;
        height = width * 1.5f;
        y = 0;
        combo = 0;

        x = Res.SKIN_ATLAS.findRegion(Res.SCORE_X);
    }

    void draw(SpriteBatch batch, float spacing, float scale, float down) {
        int length = (combo + "").length();
        int combo = this.combo;
        for (int i = length - 1; i >= 0; i--) {
            int n = combo % 10;
            combo /= 10;
            batch.draw(numbers.get(n), width * scale * spacing * i, y + down, width * scale, height * scale);
        }

        batch.draw(x, width * scale * spacing * length, y + down, width * scale, height * scale);
    }
}
