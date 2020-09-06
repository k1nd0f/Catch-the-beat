package com.kindof.catchthebeat.gameobjects.catcher;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kindof.catchthebeat.resources.Globals;

class Score extends Numbers {
    float y, width, height;
    double score;
    final float MAX_SCORE;
    int k;

    Score() {
        MAX_SCORE = (float) Math.pow(10, 6);
        k = 0;
        width = 25 * Globals.RESOLUTION_WIDTH_SCALE;
        height = width * 1.5f;
        y = Globals.HEIGHT - height;
    }

    void draw(SpriteBatch batch, float spacing, float scale, float top) {
        int score = (int) this.score;
        for (int i = 0; i < 7; i++) {
            int n = score % 10;
            score /= 10;

            batch.draw(numbers.get(n), Globals.WIDTH - width * scale * spacing * i - width * scale, y - top - (height * scale - height), width * scale, height * scale);
        }
    }

    double calculate(int fruitCount, float accuracy) {
        k++;
        return (f(k, fruitCount) - f(k - 1, fruitCount)) * (accuracy / 100.0);
    }

    private double f(int k, int fruitCount) {
        return (MAX_SCORE * ((Math.pow(Math.exp((19.64973 * k) / fruitCount + 10), 1.0 / 7.0) - 1) * Math.exp(-Math.pow(Math.PI, 1.0 / 7.0)) - 0.977193)) / 20.0;
    }
}