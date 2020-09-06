package com.kindof.catchthebeat.ui.actors.slidevaluechanger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kindof.catchthebeat.tools.Util;
import com.kindof.catchthebeat.ui.styles.LabelStyle;

public class SlideValueChanger extends Table {
    private Label label;
    private float width, height;
    private float startValue, endValue, value, ratio; // ratio in % / 100
    private String textBefore, textAfter;
    private int textAlignment; // com.badlogic.gdx.utils.Align
    private OnValueChangedListener onValueChangedListener;

    public SlideValueChanger(float width, float height, float startValue, float endValue, String textBefore, String textAfter, int textAlignment, int fontSize, OnValueChangedListener onValueChangedListener) {
        this.width = width;
        this.height = height;
        this.startValue = startValue;
        this.endValue = endValue;
        this.ratio = (endValue - startValue) / width;
        this.textBefore = textBefore;
        this.textAfter = textAfter;
        this.textAlignment = textAlignment;
        this.onValueChangedListener = onValueChangedListener;
        initialize(fontSize);
    }

    private void initialize(int fontSize) {
        initLabel(fontSize);
        setTouchable(Touchable.enabled);
        addActor(label);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                float deltaX = Gdx.input.getDeltaX() * ratio;
                setValue(value + deltaX);
            }
        });
    }

    private void initLabel(int fontSize) {
        LabelStyle labelStyle = new LabelStyle(Color.WHITE, fontSize);
        label = new Label("", labelStyle);
        label.setAlignment(textAlignment);
        label.setTouchable(Touchable.disabled);
        label.setBounds(0, 0, width, height);
    }

    public void setValue(float value) {
        silentSetValue(value);
        onValueChangedListener.onValueChanged();
    }

    public void silentSetValue(float value) {
        if (value >= startValue && value <= endValue) {
            this.value = value;
        } else if (value < startValue) {
            this.value = startValue;
        } else /* if (value > endValue) */ {
            this.value = endValue;
        }
        label.setText(textBefore + Util.round(this.value, 2) + textAfter);
    }

    public float getValue() {
        return value;
    }
}
