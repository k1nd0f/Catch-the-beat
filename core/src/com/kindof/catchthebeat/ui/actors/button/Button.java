package com.kindof.catchthebeat.ui.actors.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kindof.catchthebeat.ui.UI;
import com.kindof.catchthebeat.ui.animation.MyAnimation;

public class Button extends com.badlogic.gdx.scenes.scene2d.ui.Button {
    private TouchUpEventListener touchUpListener;
    private TouchHoldEventListener touchHoldListener;
    private ButtonStyle style;
    private ButtonPressAnimation buttonPressAnimation;
    private Drawable upFrame;

    private Button(String upFrameName, String pressFrameName) {
        style = new ButtonStyle();
        TextureRegion regionUpFrame = UI.SKIN_ATLAS.findRegion(upFrameName);
        style.up = upFrame = new TextureRegionDrawable(regionUpFrame);
        setStyle(style);
        setSize(regionUpFrame.getRegionWidth(), regionUpFrame.getRegionHeight());

        buttonPressAnimation = new ButtonPressAnimation(pressFrameName);
        addListener(new MyClickListener());
    }

    public Button(TouchUpEventListener touchUpListener, String upFrameName, String pressFrameName) {
        this(upFrameName, pressFrameName);
        this.touchUpListener = touchUpListener;
        this.touchHoldListener = () -> {

        };
    }

    public Button(TouchUpEventListener touchUpListener, TouchHoldEventListener touchHoldListener, String upFrameName, String pressFrameName) {
        this(upFrameName, pressFrameName);
        this.touchUpListener = touchUpListener;
        this.touchHoldListener = touchHoldListener;
    }

    public Button(TouchHoldEventListener touchHoldListener, String upFrameName, String pressFrameName) {
        this(upFrameName, pressFrameName);
        this.touchHoldListener = touchHoldListener;
        this.touchUpListener = (event, x, y, pointer, button) -> {

        };
    }

    public void setUpFrame() {
        style.up = upFrame;
    }

    public void finish() {
        buttonPressAnimation.finish();
        setUpFrame();
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
        //private InputData inputData;

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
                finish();
            }
        }

        private void finish() {
            hasStarted = false;
            stateTime = 0;
            //touchUpListener.touchUp(inputData.event, inputData.x, inputData.y, inputData.pointer, inputData.button);
        }

        private void start(InputEvent event, float x, float y, int pointer, int button) {
            hasStarted = true;
            stateTime = 0;
            //inputData = new InputData(event, x, y, pointer, button);
            touchUpListener.touchUp(event, x, y, pointer, button);
        }

        private boolean isFinished() {
            return stateTime >= framesCount * drawableAnimation.getFrameDuration();
        }

        /* private class InputData {
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
        } */
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
            if (!buttonPressAnimation.hasStarted) {
                buttonPressAnimation.start(event, x, y, pointer, button);
            }

            return false;
        }
    }
}
