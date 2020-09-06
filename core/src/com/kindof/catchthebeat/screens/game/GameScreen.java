package com.kindof.catchthebeat.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kindof.catchthebeat.beatmaps.Beatmap;
import com.kindof.catchthebeat.gameobjects.catcher.Catcher;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.ui.UI;

public class GameScreen extends InputAdapter implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private State state;
    private Catcher catcher;
    private Beatmap beatmap;
    private PauseMenuScreen pauseMenuScreen;

    public GameScreen(Beatmap beatmap) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        pauseMenuScreen = new PauseMenuScreen();

        this.beatmap = beatmap;
        catcher = new Catcher(beatmap, UI.SKIN_ATLAS.findRegion(UI.CATCHER), Globals.WIDTH / 2f - Globals.CATCHER_WIDTH / 2f, -Globals.CATCHER_HEIGHT / Globals.CATCHER_DELTA_DOWN_SCALE, Globals.CATCHER_WIDTH, Globals.CATCHER_HEIGHT);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
        batch.begin();
        beatmap.draw(batch);
        catcher.draw(batch);
        batch.end();
    }

    private void update(float delta) {
        if (isRunning()) {
            catcher.motionEvent(false);
            beatmap.update(delta, catcher);
        }
    }

    public void start() {
        this.state = State.run;
        beatmap.start();
    }

    public State getState() {
        return state;
    }

    private boolean isRunning() {
        return state == State.run;
    }

    public void setPause() {
        this.state = State.pause;
        beatmap.pause();
        pauseMenuScreen.set(PauseMenuScreen.PauseType.pause);
        Globals.GAME.setScreen(pauseMenuScreen);
    }

    public void resumeGame() {
        Globals.GAME.setScreen(this);
        beatmap.resume();
        this.state = State.run;
    }

    public void quitFromBeatmap() {
        catcher.setVector(true, false);
        dispose();
        Globals.GAME_SCREEN = null;
        Globals.GAME.setScreen(Globals.BEATMAP_SELECTION_MENU_SCREEN);
    }

    public void retry() {
        catcher.setVector(true, false);
        dispose();
        Globals.GAME_SCREEN = new GameScreen(new Beatmap(beatmap.getId()));
        Globals.GAME.setScreen(Globals.GAME_SCREEN);
        Globals.GAME_SCREEN.start();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            setPause();
        }

        return false;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    public PauseMenuScreen getPauseMenuScreen() {
        return pauseMenuScreen;
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
        setPause();
    }

    @Override
    public void resume() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
        beatmap.dispose();
        batch.dispose();
        pauseMenuScreen.dispose();

        Globals.GAME_SCREEN = null;
    }
}
