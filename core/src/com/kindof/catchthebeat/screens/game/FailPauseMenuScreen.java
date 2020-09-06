package com.kindof.catchthebeat.screens.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.ui.actors.buttons.Button;
import com.kindof.catchthebeat.ui.actors.buttons.TouchUpEventListener;

public class FailPauseMenuScreen extends BaseScreen {
    private Button retryButton, quitButton;
    private Music failSound;

    public FailPauseMenuScreen() {
        super();
    }

    @Override
    public void initialize() {
        failSound = Res.ASSET_MANAGER.get(Res.INTERNAL_PATH_TO_SKINS_DIRECTORY + Res.SKIN_NAME + "/" + Res.FAIL_SOUND, Music.class);
        failSound.setVolume(Res.SOUND_VOLUME);

        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Res.GAME_SCREEN.quitFromBeatmap();
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.background(new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.FAIL_BACKGROUND)));

        float x = 0, width = Res.WIDTH, height = Res.HEIGHT / 3.0f;

        retryButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Res.GAME_SCREEN.retry();
            }
        }, x, Res.HEIGHT / 2.0f, width, height, Res.PAUSE_RETRY_BUTTON_UP, Res.PAUSE_RETRY_BUTTON_PRESS);

        quitButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Res.GAME_SCREEN.quitFromBeatmap();
            }
        }, x, Res.HEIGHT / 2.0f - height, width, height, Res.PAUSE_QUIT_BUTTON_UP, Res.PAUSE_QUIT_BUTTON_PRESS);

        rootTable.addActor(retryButton);
        rootTable.addActor(quitButton);
        stage.addActor(rootTable);
    }

    @Override
    public void show() {
        super.show();
        failSound.play();
    }

    @Override
    public void hide() {
        super.hide();
        failSound.stop();
    }
}
