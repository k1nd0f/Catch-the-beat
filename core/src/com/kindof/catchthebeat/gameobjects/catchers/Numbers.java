package com.kindof.catchthebeat.gameobjects.catchers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kindof.catchthebeat.resources.Res;

import java.util.ArrayList;

class Numbers {
    private static int SIZE = 10;
    protected static ArrayList<TextureRegion> numbers;

    static {
        numbers = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            numbers.add(Res.SKIN_ATLAS.findRegion("score-" + i));
        }
    }
}
