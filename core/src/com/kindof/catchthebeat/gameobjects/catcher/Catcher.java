package com.kindof.catchthebeat.gameobjects.catcher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.kindof.catchthebeat.resources.Settings;
import com.kindof.catchthebeat.beatmaps.Beatmap;
import com.kindof.catchthebeat.gameobjects.fruit.Overlap;
import com.kindof.catchthebeat.gameobjects.GameObject;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.screens.game.PauseMenuScreen;
import com.kindof.catchthebeat.screens.game.State;
import com.kindof.catchthebeat.ui.UI;

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
    private Sound hitSound, breakSound;

    public Catcher(Beatmap beatmap, TextureRegion region, float x, float y, float width, float height) {
        super(region, x, y, width, height);
        initialize();
        this.beatmap = beatmap;
        Globals.ON_COMPLETION_SCREEN.setCatcher(this);
        Globals.ON_COMPLETION_SCREEN.setBeatmap(beatmap);
        hitSound = UI.ASSET_MANAGER.get(Globals.INTERNAL_PATH_TO_SKINS_DIRECTORY + UI.SKIN_NAME + "/" + Globals.HIT_SOUND, Sound.class);
        breakSound = UI.ASSET_MANAGER.get(Globals.INTERNAL_PATH_TO_SKINS_DIRECTORY + UI.SKIN_NAME + "/" + Globals.BREAK_SOUND, Sound.class);
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
        if (Globals.GAME_SCREEN.getState() == State.run && Gdx.input.isTouched()) {
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
            } else if (getX() + getWidth() > Globals.WIDTH) {
                setX(Globals.WIDTH - getWidth());
            }
        }
    }

    public void update(int fruitCount, float healthRate, Overlap overlap) {
        n++;
        if (overlap.getType() == Overlap.Type.hit) {
            hitSound.play(Settings.SOUND_VOLUME);
            combo.combo++;
            if (combo.combo > combo.maxCombo) {
                combo.maxCombo = combo.combo;
            }
            misses = 0;
            health.health += health.calculate(healthRate - healthRate * Math.min(combo.combo, healthRate) / healthRate, healthRate);
            if (health.health > 1) {
                health.health = 1;
            }
        } else {
            if (combo.combo != 0) {
                breakSound.play(Settings.SOUND_VOLUME);
            }
            combo.combo = 0;
            misses++;
            health.health -= health.calculate(healthRate - healthRate * Math.min(misses, healthRate) / healthRate, healthRate);
            if (health.health <= 0) {
                health.health = 0;
                Globals.GAME_SCREEN.setPause();
                Globals.USER.incPlayCount();
                Globals.STATISTIC_SCREEN.reInitLabels();
                Globals.GAME.getDatabase().setUser(Globals.USER);
                PauseMenuScreen pauseMenuScreen = Globals.GAME_SCREEN.getPauseMenuScreen();
                pauseMenuScreen.set(PauseMenuScreen.PauseType.fail);
                Globals.GAME.setScreen(pauseMenuScreen);
            }
        }

        score.score += score.calculate(fruitCount, overlap.getAccuracy());
        accuracy.sum += overlap.getAccuracy();
        accuracy.accuracy = accuracy.sum / n;

        if (n == beatmap.getFruitCount() && health.health > 0) {
            setVector(true, false);
            Globals.GAME_SCREEN.setPause();
            Globals.USER.incCompletedBeatmapCount();
            updateUserData();
            Globals.GAME.getDatabase().setUser(Globals.USER);
            Globals.STATISTIC_SCREEN.reInitLabels();
            Globals.GAME.setScreen(Globals.ON_COMPLETION_SCREEN);

        }
    }

    private void updateUserData() {
        Globals.USER.incTotalScore((int) score.score);
        Globals.USER.incTotalAccuracy(accuracy.accuracy);
        Globals.USER.incTotalDifficulty(beatmap.getDifficulty());
        Globals.USER.incPlayCount();
        Globals.USER.updateData();
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
