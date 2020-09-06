package com.kindof.catchthebeat.screens.beatmapeditor.actors.bottompanel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kindof.catchthebeat.screens.beatmapeditor.BeatmapEditorScreen;
import com.kindof.catchthebeat.screens.beatmapeditor.FruitEntity;
import com.kindof.catchthebeat.screens.beatmapeditor.FruitList;
import com.kindof.catchthebeat.ui.Alignment;
import com.kindof.catchthebeat.ui.UI;
import com.kindof.catchthebeat.ui.actors.button.Button;

public class BottomPanel extends Table {
    private float width, height, startX, startY, endY, pad;
    private boolean isOpen;
    private MoveToAction moveToAction;
    private Button pauseButton, resumeButton, deleteButton;
    private boolean buttonIsTouched;
    private FruitList fruitSet;
    private BeatmapEditorScreen editor;

    public BottomPanel(BeatmapEditorScreen editor, FruitList fruitSet, float startX, float startY, float width, float height, float pad) {
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
                    if (moveToAction != null) {
                        removeAction(moveToAction);
                    }

                    if (isOpen) {
                        initMoveToAction(startY);
                    } else {
                        initMoveToAction(endY);
                    }

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
                bSize = height - pad,
                bX = (width - bSize * 2.0f) / 2.0f,
                bPad = 20;

        pauseButton = new Button((event, x1, y, pointer, button) -> {
            editor.setPause();
            buttonIsTouched = true;
        }, UI.BEATMAP_EDITOR_PAUSE_BUTTON, UI.BEATMAP_EDITOR_PAUSE_BUTTON);
        pauseButton.setVisible(true);
        UI.calculateAndSetViewElementBounds(
                pauseButton,
                Alignment.center, bPad,
                bX, 0, bSize, bSize
        );

        resumeButton = new Button((event, x12, y, pointer, button) -> {
            editor.setResume();
            buttonIsTouched = true;
        }, UI.BEATMAP_EDITOR_RESUME_BUTTON, UI.BEATMAP_EDITOR_RESUME_BUTTON);
        resumeButton.setVisible(false);
        UI.calculateAndSetViewElementBounds(
                resumeButton,
                Alignment.center, bPad,
                bX, 0, bSize, bSize
        );

        deleteButton = new Button((event, x13, y, pointer, button) -> {
            buttonIsTouched = true;
            FruitEntity selectedItem = fruitSet.getSelectedItem();
            if (selectedItem != null) {
                fruitSet.delete(selectedItem);
            }
        }, UI.BEATMAP_EDITOR_DELETE_OBJECT_BUTTON, UI.BEATMAP_EDITOR_DELETE_OBJECT_BUTTON);
        UI.calculateAndSetViewElementBounds(
                deleteButton,
                Alignment.center, bPad,
                bX + bSize, 0, bSize, bSize
        );

        addActor(pauseButton);
        addActor(resumeButton);
        addActor(deleteButton);
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
