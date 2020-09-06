package com.kindof.catchthebeat.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kindof.catchthebeat.beatmaps.Beatmap;
import com.kindof.catchthebeat.gameobjects.catchers.Catcher;
import com.kindof.catchthebeat.resources.Res;

public class GameScreen implements Screen, InputProcessor {
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
        catcher = new Catcher(beatmap, Res.SKIN_ATLAS.findRegion(Res.CATCHER_IDLE), Res.WIDTH / 2f - Res.CATCHER_WIDTH / 2f, -Res.CATCHER_HEIGHT / Res.CATCHER_DELTA_DOWN_SCALE, Res.CATCHER_WIDTH, Res.CATCHER_HEIGHT);
    }

    public void start() {
        setGameState(State.run);
        beatmap.start();
    }

    public State getState() {
        return state;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    private void update(float delta) {
        if (isRunning()) {
            catcher.motionEvent(false);
            beatmap.update(delta, catcher);
        }
    }

    public void setPause() {
        setGameState(State.pause);
        beatmap.pause();
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

    private boolean isRunning() {
        return state == State.run;
    }

    public void setGameState(State state) {
        this.state = state;
    }

    public void resumeGame() {
        Res.GAME.setScreen(this);
        beatmap.resume();
        setGameState(State.run);
    }

    public void quitFromBeatmap() {
        catcher.setVector(true, false);
        dispose();
        Res.GAME_SCREEN = null;
        Res.GAME.setScreen(Res.BEATMAP_SELECTION_MENU_SCREEN);
    }

    public void retry() {
        catcher.setVector(true, false);
        dispose();
        Res.GAME_SCREEN = new GameScreen(new Beatmap(beatmap.getId()));
        Res.GAME.setScreen(Res.GAME_SCREEN);
        Res.GAME_SCREEN.start();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        setPause();
        Res.GAME.setScreen(pauseMenuScreen);
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            setPause();
            Res.GAME.setScreen(pauseMenuScreen);
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void dispose() {
        beatmap.dispose();
        batch.dispose();
        pauseMenuScreen.dispose();

        Res.GAME_SCREEN = null;
    }
}
