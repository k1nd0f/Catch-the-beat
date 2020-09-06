package com.kindof.catchthebeat.gameobjects.catcher;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.ui.Alignment;
import com.kindof.catchthebeat.ui.UI;

class Accuracy extends Numbers {
    float y, width, height;
    float accuracy, sum;
    private TextureRegion comma;
    private TextureRegion percent;

    Accuracy() {
        width = 15 * Globals.RESOLUTION_WIDTH_SCALE;
        height = width * 1.5f;
        y = 0;

        comma = UI.SKIN_ATLAS.findRegion(UI.SCORE_COMMA);
        percent = UI.SKIN_ATLAS.findRegion(UI.SCORE_PERCENT);
    }

    void draw(SpriteBatch batch, float spacing, float scale, float down) {
        int 
                first = (int) accuracy,
                last = (int) ((accuracy - (int) accuracy) * 100),
                length = first == 100 ? 7 : 6,
                i = 0;
        
        float
                nW = width * scale,
                nH = height * scale,
                nX = Globals.WIDTH - width * scale,
                nY = y + down;

        TextureRegion nRegion;
        Rectangle viewBounds = UI.calculateViewElementBounds(Alignment.center, 0, nX, nY, nW, nH, percent.getRegionWidth(), percent.getRegionHeight());
        
        batch.draw(percent, viewBounds.x, viewBounds.y, viewBounds.width, viewBounds.height);
        for (i++; i < 3; i++) {
            int n = last % 10;
            last /= 10;
            nX = Globals.WIDTH - width * scale * spacing * i - width * scale;
            
            nRegion = numbers.get(n);
            viewBounds = UI.calculateViewElementBounds(Alignment.center, 0, nX, nY, nW, nH, nRegion.getRegionWidth(), nRegion.getRegionHeight());
            
            batch.draw(nRegion, viewBounds.x, viewBounds.y, viewBounds.width, viewBounds.height);
        }

        batch.draw(comma, Globals.WIDTH - width * scale * spacing * i - width * scale, nY, nW, nH);
        for (i++; i < length; i++) {
            int n = first % 10;
            first /= 10;
            nX = Globals.WIDTH - width * scale * spacing * i - width * scale;

            nRegion = numbers.get(n);
            viewBounds = UI.calculateViewElementBounds(Alignment.center, 0, nX, nY, nW, nH, nRegion.getRegionWidth(), nRegion.getRegionHeight());

            batch.draw(nRegion, viewBounds.x, viewBounds.y, viewBounds.width, viewBounds.height);
        }
    }
}