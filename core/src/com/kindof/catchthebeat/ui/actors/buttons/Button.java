package com.kindof.catchthebeat.ui.actors.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.ui.animation.MyAnimation;

public class Button extends com.badlogic.gdx.scenes.scene2d.ui.Button {
    private MyClickListener clickListener;
    private TouchUpEventListener touchUpListener;
    private TouchHoldEventListener touchHoldListener;
    private ButtonStyle style;
    private ButtonPressAnimation buttonPressAnimation;
    private Drawable upFrame;

    private Button(float x, float y, float width, float height, String upFrameName, String pressFrameName) {
        style = new ButtonStyle();
        style.up = upFrame = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(upFrameName));
        setStyle(style);
        setBounds(x, y, width, height);

        buttonPressAnimation = new ButtonPressAnimation(pressFrameName);
        clickListener = new MyClickListener();
        addListener(clickListener);
    }

    public Button(TouchUpEventListener touchUpListener, float x, float y, float width, float height, String upFrameName, String pressFrameName) {
        this(x, y, width, height, upFrameName, pressFrameName);
        this.touchUpListener = touchUpListener;
        this.touchHoldListener = new TouchHoldEventListener() {
            @Override
            public void touchHold() {

            }
        };
    }

    public Button(TouchUpEventListener touchUpListener, TouchHoldEventListener touchHoldListener, float x, float y, float width, float height, String upFrameName, String pressFrameName) {
        this(x, y, width, height, upFrameName, pressFrameName);
        this.touchUpListener = touchUpListener;
        this.touchHoldListener = touchHoldListener;
    }

    public Button(TouchHoldEventListener touchHoldListener, float x, float y, float width, float height, String upFrameName, String pressFrameName) {
        this(x, y, width, height, upFrameName, pressFrameName);
        this.touchHoldListener = touchHoldListener;
        this.touchUpListener = new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        };
    }

    public void setUpFrame() {
        style.up = upFrame;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        update();
        super.draw(batch, parentAlpha);
    }

    private void update() {
        buttonPressAnimation.nextFrame();
        if (isOver()) {
            touchHoldListener.touchHold();
        }
    }

    private class ButtonPressAnimation extends MyAnimation {
        private boolean hasStarted;
        private InputData inputData;

        private ButtonPressAnimation(String framesName) {
            super(framesName, Animation.PlayMode.NORMAL);
        }

        @Override
        public void nextFrame() {
            if (hasStarted) {
                stateTime += Gdx.graphics.getDeltaTime();
                style.up = drawableAnimation.getKeyFrame(stateTime);
            }

            if (isFinished()) {
                hasStarted = false;
                stateTime = 0;
                touchUpListener.touchUp(inputData.event, inputData.x, inputData.y, inputData.pointer, inputData.button);
            }
        }

        private void start(InputEvent event, float x, float y, int pointer, int button) {
            hasStarted = true;
            stateTime = 0;
            inputData = new InputData(event, x, y, pointer, button);
        }

        private boolean isFinished() {
            return stateTime >= framesCount * drawableAnimation.getFrameDuration();
        }

        private class InputData {
            private InputEvent event;
            private float x, y;
            private int pointer, button;

            private InputData(InputEvent event, float x, float y, int pointer, int button) {
                this.event = event;
                this.x = x;
                this.y = y;
                this.pointer = pointer;
                this.button = button;
            }
        }
    }

    /*private class ButtonOverAnimation extends ButtonAnimation {
        private boolean hasStopped;

        private ButtonOverAnimation(String framesName) {
            super(framesName);
        }


        @Override
        protected void nextFrame() {
            if (!hasStopped) {
                if (isOver()) {
                    stateTime += Gdx.graphics.getDeltaTime();
                    if (stateTime > framesCount * animation.getFrameDuration()) {
                        stateTime = framesCount * animation.getFrameDuration();
                    }

                    style.over = animation.getKeyFrame(stateTime);
                } else {
                    if (stateTime > 0) {
                        stateTime -= Gdx.graphics.getDeltaTime();
                        if (stateTime < 0) {
                            stateTime = 0;
                        }

                        style.up = animation.getKeyFrame(stateTime);
                    } else {
                        style.up = upFrame;
                    }
                }
            }
        }

        private void stop() {
            hasStopped = true;
        }
    }*/

    private class MyClickListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (!buttonPressAnimation.hasStarted)
                buttonPressAnimation.start(event, x, y, pointer, button);

            return false;
        }
    }
}
