package com.kindof.catchthebeat.ui.animation;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;

public class AlphaAnimation {
    private AlphaAction alphaAction;
    private float startAlpha, endAlpha;
    private float alpha;
    private float duration;

    public AlphaAnimation(float duration, float startAlpha, float endAlpha) {
        this.startAlpha = startAlpha;
        this.endAlpha = endAlpha;
        alpha = startAlpha;
        this.duration = duration;
    }

    public AlphaAction nextAlphaAction() {
        init();
        return alphaAction;
    }

    private void init() {
        alpha = alpha == startAlpha ? endAlpha : startAlpha;

        alphaAction = new AlphaAction();
        alphaAction.setInterpolation(Interpolation.fastSlow);
        alphaAction.setDuration(duration);
        alphaAction.setAlpha(alpha);
    }

    public float getDuration() {
        return duration;
    }
}
