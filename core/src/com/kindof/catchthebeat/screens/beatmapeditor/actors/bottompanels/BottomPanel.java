package com.kindof.catchthebeat.screens.beatmapeditor.actors.bottompanels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.beatmapeditor.BeatmapEdiorScreen;
import com.kindof.catchthebeat.screens.beatmapeditor.FruitEntity;
import com.kindof.catchthebeat.screens.beatmapeditor.FruitList;
import com.kindof.catchthebeat.ui.actors.buttons.Button;
import com.kindof.catchthebeat.ui.actors.buttons.TouchUpEventListener;

public class BottomPanel extends Table {
    private float width, height, startX, startY, endY, pad;
    private boolean isOpen;
    private MoveToAction moveToAction;
    private Button pauseButton, resumeButton, deleteButton;
    private boolean buttonIsTouched;
    private FruitList fruitSet;
    private BeatmapEdiorScreen editor;

    public BottomPanel(BeatmapEdiorScreen editor, FruitList fruitSet, float startX, float startY, float width, float height, float pad) {
        super(null);
        this.startX = startX;
        this.startY = startY + pad;
        this.endY = startY + height;
        this.pad = pad;
        this.width = width;
        this.height = height;
        this.fruitSet = fruitSet;
        this.editor = editor;
        isOpen = buttonIsTouched = false;

        init();
    }

    private void init() {
        setBounds(this.startX, startY, width, height);
        setBackground(new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.UI_BEATMAP_EDITOR_BOTTOM_PANEL_BACKGROUND)));
        setVisible(true);
        setTouchable(Touchable.enabled);

        initButtons();

        addListener(new InputListener() {
            private float startY = BottomPanel.this.startY;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!buttonIsTouched && (moveToAction == null || moveToAction.isComplete())) {
                    if (moveToAction != null)
                        removeAction(moveToAction);

                    if (isOpen)
                        initMoveToAction(startY);
                    else
                        initMoveToAction(endY);

                    addAction(moveToAction);
                }
                buttonIsTouched = false;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (moveToAction == null || moveToAction.isComplete()) {
                    float deltaY = Gdx.input.getDeltaY();
                    if (getY() - deltaY < endY && getY() - deltaY > startY - pad) {
                        setY(getY() - Gdx.input.getDeltaY());
                    }
                }
            }
        });
    }

    private void initButtons() {
        float
                size = height - pad,
                x = (width - size * 2.0f) / 2.0f;

        // PauseButton //
        pauseButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                editor.setPause();
                buttonIsTouched = true;
            }
        }, x, 0, size, size, Res.UI_BEATMAP_EDITOR_PAUSE_BUTTON_UP, Res.UI_BEATMAP_EDITOR_PAUSE_BUTTON_PRESS);
        pauseButton.setVisible(true);

        // ResumeButton //
        resumeButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                editor.setResume();
                buttonIsTouched = true;
            }
        }, x, 0, size, size, Res.UI_BEATMAP_EDITOR_RESUME_BUTTON_UP, Res.UI_BEATMAP_EDITOR_RESUME_BUTTON_PRESS);
        resumeButton.setVisible(false);

        // DeleteObjectButton //
        deleteButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                buttonIsTouched = true;
                FruitEntity selectedItem = fruitSet.getSelectedItem();
                if (selectedItem != null)
                    fruitSet.delete(selectedItem);
            }
        }, x + size, 0, size, size, Res.UI_BEATMAP_EDITOR_DELETE_OBJECT_BUTTON_UP, Res.UI_BEATMAP_EDITOR_DELETE_OBJECT_BUTTON_PRESS);

        addActor(pauseButton);
        addActor(resumeButton);
        addActor(deleteButton);
    }

    public float getPad() {
        return pad;
    }

    public void changeStateOfPauseResumeButton() {
        pauseButton.setVisible(!pauseButton.isVisible());
        resumeButton.setVisible(!resumeButton.isVisible());
    }

    private void initMoveToAction(float y) {
        moveToAction = new MoveToAction();
        moveToAction.setInterpolation(Interpolation.fastSlow);
        moveToAction.setDuration(0.7f * Math.abs(y - getY()) / getHeight());
        moveToAction.setPosition(getX(), y);

        isOpen = !isOpen;
    }
}
