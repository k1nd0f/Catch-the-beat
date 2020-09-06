package com.kindof.catchthebeat.gameobjects.catchers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.kindof.catchthebeat.beatmaps.Beatmap;
import com.kindof.catchthebeat.gameobjects.fruits.Overlap;
import com.kindof.catchthebeat.gameobjects.fruits.Overlaps;
import com.kindof.catchthebeat.gameobjects.GameObject;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.game.FailPauseMenuScreen;
import com.kindof.catchthebeat.screens.game.State;

public class Catcher extends GameObject implements Disposable {
    private Health health;
    private Score score;
    private Accuracy accuracy;
    private Combo combo;
    private float misses;
    private int n;
    private boolean moveRight, moveLeft;
    private Hud headUpDisplay;
    private Beatmap beatmap;
    private FailPauseMenuScreen failPauseMenuScreen;
    private Sound hitSound, breakSound;

    public Catcher(Beatmap beatmap, TextureRegion region, float x, float y, float width, float height) {
        super(region, x, y, width, height);
        initialize();
        this.beatmap = beatmap;
        Res.ON_COMPLETION_SCREEN.setCatcher(this);
        Res.ON_COMPLETION_SCREEN.setBeatmap(beatmap);
        failPauseMenuScreen = new FailPauseMenuScreen();
        hitSound = Res.ASSET_MANAGER.get(Res.INTERNAL_PATH_TO_SKINS_DIRECTORY + Res.SKIN_NAME + "/" + Res.HIT_SOUND, Sound.class);
        breakSound = Res.ASSET_MANAGER.get(Res.INTERNAL_PATH_TO_SKINS_DIRECTORY + Res.SKIN_NAME + "/" + Res.BREAK_SOUND, Sound.class);
    }

    private void initialize() {
        bounds = new Rectangle(x, y + height, width, 0);
        misses = 0;
        moveRight = true;
        moveLeft = false;

        health = new Health();
        score = new Score();
        accuracy = new Accuracy();
        combo = new Combo();
        headUpDisplay = new Hud();
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        headUpDisplay.draw(batch, score, health, combo, accuracy);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setVector(boolean moveRight, boolean moveLeft) {
        if (this.moveRight != moveRight && this.moveLeft != moveLeft) {
            flip(true, false);
        }

        this.moveRight = moveRight;
        this.moveLeft = moveLeft;
    }

    public void motionEvent(boolean type) {
        float x = Gdx.input.getX(), deltaX;
        if (Res.GAME_SCREEN.getState() == State.run && Gdx.input.isTouched()) {
            if (type) {
                deltaX = Gdx.input.getDeltaX();
                setX(getX() + deltaX);
            } else {
                deltaX = (float) (x - (getX() + getWidth() / 2.0));
                setX(x - getWidth() / 2f);
            }

            if (deltaX > 0) {
                setVector(true, false);
            } else if (deltaX < 0) {
                setVector(false, true);
            }

            if (getX() < 0) {
                setX(0);
            } else if (getX() + getWidth() > Res.WIDTH) {
                setX(Res.WIDTH - getWidth());
            }
        }
    }

    public void update(int fruitCount, float healthRate, Overlap overlap) {
        n++;
        if (overlap.getType() == Overlaps.hit) {
            hitSound.play(Res.SOUND_VOLUME);
            combo.combo++;
            if (combo.combo > combo.maxCombo) {
                combo.maxCombo = combo.combo;
            }
            misses = 0;
            health.health += health.calculate(healthRate - healthRate * Math.min(combo.combo, healthRate) / healthRate, healthRate);
            if (health.health > 1)
                health.health = 1;
        } else {
            if (combo.combo != 0) {
                breakSound.play(Res.SOUND_VOLUME);
            }
            combo.combo = 0;
            misses++;
            health.health -= health.calculate(healthRate - healthRate * Math.min(misses, healthRate) / healthRate, healthRate);
            if (health.health <= 0) {
                health.health = 0;
                Res.GAME_SCREEN.setPause();
                Res.USER.incPlayCount();
                Res.STATISTIC_SCREEN.reInitLabels();
                Res.GAME.getDatabase().setUser(Res.USER);
                Res.GAME.setScreen(failPauseMenuScreen);
            }
        }

        score.score += score.calculate(fruitCount, overlap.getAccuracy());
        accuracy.sum += overlap.getAccuracy();
        accuracy.accuracy = accuracy.sum / n;

        if (n == beatmap.getFruitCount() && health.health > 0) {
            setVector(true, false);
            Res.GAME_SCREEN.setPause();
            Res.USER.incCompletedBeatmapCount();
            updateUserData();
            Res.GAME.getDatabase().setUser(Res.USER);
            Res.STATISTIC_SCREEN.reInitLabels();
            Res.GAME.setScreen(Res.ON_COMPLETION_SCREEN);
        }
    }

    private void updateUserData() {
        Res.USER.incTotalScore((int) score.score);
        Res.USER.incTotalAccuracy(accuracy.accuracy);
        Res.USER.incTotalDifficulty(beatmap.getDifficulty());
        Res.USER.incPlayCount();
        Res.USER.updateData();
    }

    public float getAccuracy() {
        return accuracy.accuracy;
    }

    public int getMaxCombo() {
        return combo.maxCombo;
    }

    public int getScore() {
        return (int) score.score;
    }

    @Override
    public void dispose() {
        hitSound.dispose();
        breakSound.dispose();
    }
}
