package com.kindof.catchthebeat.screens.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.resources.Settings;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.ui.Alignment;
import com.kindof.catchthebeat.ui.UI;
import com.kindof.catchthebeat.ui.actors.button.Button;

public class PauseMenuScreen extends BaseScreen {
    private Button resumeButton, retryButton, quitButton;
    private TextureRegionDrawable pauseBG, failBG;
    private Music failSound;
    private PauseType pauseType;

    public PauseMenuScreen() {
        super();
    }

    @Override
    public void initialize() {
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Globals.GAME_SCREEN.quitFromBeatmap();
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setFillParent(true);

        failSound = UI.ASSET_MANAGER.get(Globals.INTERNAL_PATH_TO_SKINS_DIRECTORY + UI.SKIN_NAME + "/" + Globals.FAIL_SOUND, Music.class);
        failSound.setVolume(Settings.SOUND_VOLUME);

        pauseBG = new TextureRegionDrawable(UI.SKIN_ATLAS.findRegion(UI.PAUSE_BG));
        failBG = new TextureRegionDrawable(UI.SKIN_ATLAS.findRegion(UI.FAIL_BG));

        float
                buttonW = Globals.WIDTH,
                buttonH = Globals.HEIGHT / 3.0f,
                buttonPad = 100 * Globals.RESOLUTION_HEIGHT_SCALE;

        resumeButton = new Button((event, x, y, pointer, button) -> Globals.GAME_SCREEN.resumeGame(), UI.PAUSE_RESUME, UI.PAUSE_RESUME);
        UI.calculateAndSetViewElementBounds(
                resumeButton,
                Alignment.bottom, buttonPad,
                0, buttonH * 2.0f, buttonW, buttonH
        );

        retryButton = new Button((event, x, y, pointer, button) -> Globals.GAME_SCREEN.retry(), UI.PAUSE_RETRY, UI.PAUSE_RETRY);
        UI.calculateAndSetViewElementBounds(
                retryButton,
                Alignment.center, buttonPad,
                0, buttonH, buttonW, buttonH
        );

        quitButton = new Button((event, x, y, pointer, button) -> Globals.GAME_SCREEN.quitFromBeatmap(), UI.PAUSE_QUIT, UI.PAUSE_QUIT);
        UI.calculateAndSetViewElementBounds(
                quitButton,
                Alignment.top, buttonPad,
                0, 0, buttonW, buttonH
        );


        addActors(resumeButton, retryButton, quitButton);
        stage.addActor(rootTable);
    }

    public void set(PauseType pauseType) {
        this.pauseType = pauseType;
        if (pauseType == PauseType.pause) {
            resumeButton.setVisible(true);
            retryButton.setVisible(true);
            quitButton.setVisible(true);
            rootTable.setBackground(pauseBG);
        } else /* if (pauseType == PauseType.fail) */ {
            resumeButton.setVisible(false);
            retryButton.setVisible(true);
            quitButton.setVisible(true);
            rootTable.setBackground(failBG);
        }
    }

    @Override
    public void show() {
        super.show();
        if (pauseType == PauseType.fail) {
            failSound.play();
        }
    }

    @Override
    public void hide() {
        super.hide();
        failSound.stop();
    }

    public enum PauseType {
        pause, fail
    }
}
