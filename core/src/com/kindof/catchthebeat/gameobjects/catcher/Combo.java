package com.kindof.catchthebeat.gameobjects.catcher;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.ui.Alignment;
import com.kindof.catchthebeat.ui.UI;

class Combo extends Numbers {
    float y, width, height;
    int combo, maxCombo;
    private TextureRegion x;

    Combo() {
        width = 20 * Globals.RESOLUTION_WIDTH_SCALE;
        height = width * 1.5f;
        y = 0;
        combo = 0;

        x = UI.SKIN_ATLAS.findRegion(UI.SCORE_X);
    }

    void draw(SpriteBatch batch, float spacing, float scale, float down) {
        int
                length = (this.combo + "").length(),
                combo = this.combo;

        float
                nW = width * scale,
                nH = height * scale,
                nX,
                nY = y + down;

        Rectangle viewBounds;

        for (int i = length - 1; i >= 0; i--) {
            int n = combo % 10;
            combo /= 10;
            nX = width * scale * spacing * i;

            TextureRegion nRegion = numbers.get(n);
            viewBounds = UI.calculateViewElementBounds(Alignment.center, 0, nX, nY, nW, nH, nRegion.getRegionWidth(), nRegion.getRegionHeight());

            batch.draw(nRegion, viewBounds.x, viewBounds.y, viewBounds.width, viewBounds.height);
        }

        nX = width * scale * spacing * length;
        viewBounds = UI.calculateViewElementBounds(Alignment.center, 0, nX, nY, nW, nH, x.getRegionWidth(), x.getRegionHeight());
        batch.draw(x, viewBounds.x, viewBounds.y, viewBounds.width, viewBounds.height);
    }
}
