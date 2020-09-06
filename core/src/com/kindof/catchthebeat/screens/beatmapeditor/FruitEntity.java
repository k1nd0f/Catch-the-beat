package com.kindof.catchthebeat.screens.beatmapeditor;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kindof.catchthebeat.gameobjects.fruit.Overlap;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.resources.Settings;
import com.kindof.catchthebeat.screens.beatmapeditor.actors.HitLine;
import com.kindof.catchthebeat.ui.UI;

public class FruitEntity implements Comparable<FruitEntity> {
    private Image actor;
    private Rectangle bounds;
    private float spawnTime; // in seconds
    private float kx; // x coordinate in percent / 100
    private float tableWidth;
    private String type;
    private Sound hitSound;
    private boolean hitSoundWasPlayed;
    private Overlap overlap;
    private Integer count;

    public FruitEntity(String type, float x, float y, float size, float tableWidth, float spawnTime) {
        this.spawnTime = spawnTime;
        this.tableWidth = tableWidth;
        this.type = type;
        actor = new Image(UI.SKIN_ATLAS.findRegion("fruit-" + type));
        actor.setBounds(x, y, size, size);
        hitSound = UI.ASSET_MANAGER.get(Globals.INTERNAL_PATH_TO_SKINS_DIRECTORY + UI.SKIN_NAME + "/hit-sound.wav", Sound.class);
        hitSoundWasPlayed = true;
        overlap = new Overlap();
        count = -1;
        bounds = new Rectangle();
        calculateBounds();
        calculateKx();
    }

    public void update(float scrollSpeed, float delta) {
        float deltaY = scrollSpeed * delta;
        actor.setY(actor.getY() - deltaY);
        bounds.y = bounds.y - deltaY;
        if (actor.getY() + actor.getHeight() < 0) {
            actor.setVisible(false);
        } else if (!actor.isVisible()) {
            actor.setVisible(true);
        }
        soundCheck();
    }

    public void onPositionChanged(float deltaY) {
        setSpawnTime(getSpawnTime() - deltaY / (Globals.BEATMAP_EDITOR_SCREEN.getSettings().getScrollSpeed() * Globals.RESOLUTION_HEIGHT_SCALE));
    }

    public void soundCheck() {
        HitLine hitLine = Globals.BEATMAP_EDITOR_SCREEN.getHitLine();
        overlap = boundsOverlap(hitLine.getBounds());
        if (overlap.getType() == Overlap.Type.hit) {
            if (!hitSoundWasPlayed) {
                hitSound.play(Settings.SOUND_VOLUME);
                hitSoundWasPlayed = true;
            }
            resetOverlap();
        } else {
            hitSoundWasPlayed = false;
        }
        resetCount();
    }

    private Overlap boundsOverlap(Rectangle bounds) {
        if (this.bounds.y <= bounds.y + bounds.height) {
            count++;
        }

        if (count == 0 && this.bounds.x > bounds.x && this.bounds.x < bounds.x + bounds.width) {
            overlap.set(Overlap.Type.hit, 100);
        } else if (count > 0 && this.bounds.overlaps(bounds)) {
            overlap.set(Overlap.Type.hit, (this.bounds.height - (bounds.y - this.bounds.y)) / this.bounds.height * 100);
        } else if (this.bounds.y + this.bounds.height < 0) {
            overlap.set(Overlap.Type.miss, 0);
        }

        return overlap;
    }

    public void calculateBounds() {
        float
                x = actor.getX(),
                y = actor.getY(),
                w = actor.getWidth(),
                h = actor.getHeight();

        bounds.set(x + w * 0.5f, y + h * 0.25f, 0, h * 0.5f);
    }

    public void calculateKx() {
        kx = actor.getX() / (tableWidth - actor.getWidth());
    }

    public void setSpawnTime(float spawnTime) {
        this.spawnTime = spawnTime;
    }

    public float getSpawnTime() {
        return spawnTime;
    }

    public Image getActor() {
        return actor;
    }

    public String getType() {
        return type;
    }

    public float getKx() {
        return kx;
    }

    private void resetCount() {
        count = -1;
    }

    private  void resetOverlap() {
        overlap.reset();
    }

    @Override
    public int compareTo(FruitEntity fruitEntity) {
        return Float.compare(actor.getY(), fruitEntity.getActor().getY());
    }
}
