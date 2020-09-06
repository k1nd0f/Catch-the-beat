package com.kindof.catchthebeat.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.ui.actors.buttons.Button;
import com.kindof.catchthebeat.ui.actors.buttons.TouchUpEventListener;

public class PauseMenuScreen implements Screen {
    private Stage stage;
    private Table rootTable;
    private Button resumeButton, retryButton, quitButton;

    public PauseMenuScreen() {
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
        rootTable.background(new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.PAUSE_BACKGROUND)));

        float x = 0, width = Res.WIDTH, height = Res.HEIGHT / 3.0f;
        resumeButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Res.GAME_SCREEN.resumeGame();
            }
        }, x, height * 2.0f, width, height, Res.PAUSE_RESUME_BUTTON_UP, Res.PAUSE_RESUME_BUTTON_PRESS);

        retryButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Res.GAME_SCREEN.retry();
            }
        }, x, height, width, height, Res.PAUSE_RETRY_BUTTON_UP, Res.PAUSE_RETRY_BUTTON_PRESS);

        quitButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Res.GAME_SCREEN.quitFromBeatmap();
            }
        }, x, 0, width, height, Res.PAUSE_QUIT_BUTTON_UP, Res.PAUSE_QUIT_BUTTON_PRESS);

        rootTable.addActor(resumeButton);
        rootTable.addActor(retryButton);
        rootTable.addActor(quitButton);
        stage.addActor(rootTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        rootTable.setVisible(true);
    }

    @Override
    public void hide() {
        rootTable.setVisible(false);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
