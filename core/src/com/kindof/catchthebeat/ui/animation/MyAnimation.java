package com.kindof.catchthebeat.ui.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.kindof.catchthebeat.ui.UI;

public class MyAnimation {
    protected Animation<TextureRegionDrawable> drawableAnimation;
    //protected Animation<TextureAtlas.AtlasRegion> regionAnimation;
    protected TextureRegionDrawable drawableFrame;
    //protected TextureRegion regionFrame;
    protected float stateTime;
    protected int framesCount;

    private static final float TIME_PER_FRAME = 1 / 60f;

    public MyAnimation(String framesName, Animation.PlayMode playMode) {
        initAnimation(framesName);
        drawableAnimation.setPlayMode(playMode);
        //regionAnimation.setPlayMode(playMode);
    }

    public void initAnimation(String framesName) {
        Array<TextureAtlas.AtlasRegion> regionFrames = UI.SKIN_ATLAS.findRegions(framesName);
        Array<TextureRegionDrawable> drawableFrames = new Array<>();
        for (int i = 0; i < regionFrames.size; i++) {
            TextureRegion frame = regionFrames.get(i);
            drawableFrames.add(new TextureRegionDrawable(frame));
        }

        drawableAnimation = new Animation<>(TIME_PER_FRAME, drawableFrames);
        //regionAnimation = new Animation<>(TIME_PER_FRAME, regionFrames);
        framesCount = regionFrames.size;
    }

    public void nextFrame() {
        stateTime += Gdx.graphics.getDeltaTime();
        drawableFrame = drawableAnimation.getKeyFrame(stateTime);
        //regionFrame = regionAnimation.getKeyFrame(stateTime);
    }

    public TextureRegionDrawable getDrawableFrame() {
        return drawableFrame;
    }

    /*
    public TextureRegion getRegionFrame() {
        return regionFrame;
    } */
}
