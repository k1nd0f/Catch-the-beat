package com.kindof.catchthebeat.screens.beatmapeditor.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.beatmapeditor.FruitList;
import com.kindof.catchthebeat.screens.beatmapeditor.settings.Settings;

public class MyScrollPane extends ScrollPane {
    private boolean hasScrolled, touchable;
    private FruitList fruitSet;
    private Settings settings;

    public MyScrollPane(Actor widget, FruitList fruitSet) {
        super(widget);
        this.fruitSet = fruitSet;
        this.touchable = true;
    }

    @Override
    protected void scrollY(float pixelsY) {
        if (touchable) {
            hasScrolled = true;
            float
                    scrollSpeed = settings.getScrollSpeed() * Res.RESOLUTION_HEIGHT_SCALE,
                    deltaY = Gdx.input.getDeltaY(),
                    deltaTime = deltaY / scrollSpeed;

            if (Res.BEATMAP_EDITOR_SCREEN.getMusic() != null) {
                float finalMusicPos = Res.BEATMAP_EDITOR_SCREEN.getSongPos() + deltaTime;
                Res.BEATMAP_EDITOR_SCREEN.setMusicPosition(finalMusicPos);
            }
            fruitSet.checkPosition(false);
            fruitSet.update(scrollSpeed, deltaTime);
        }
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void setHasScrolled(boolean hasScrolled) {
        this.hasScrolled = hasScrolled;
    }

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    public boolean isTouchable() {
        return touchable;
    }

    public boolean hasScrolled() {
        return hasScrolled;
    }
}
