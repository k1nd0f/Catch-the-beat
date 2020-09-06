package com.kindof.catchthebeat.gameobjects.catcher;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kindof.catchthebeat.ui.UI;

import java.util.ArrayList;

class Numbers {
    private static final int SIZE = 10;
    protected static final ArrayList<TextureRegion> numbers;

    static {
        numbers = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            numbers.add(UI.SKIN_ATLAS.findRegion("score-" + i));
        }
    }
}
