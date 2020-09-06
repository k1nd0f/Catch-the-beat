package com.kindof.catchthebeat.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class BaseScreen implements Screen {
    protected Stage stage;
    protected Table rootTable;

    public BaseScreen() {
        initialize();
    }

    public abstract void initialize();

    public void addActors(Actor... actors) {
        addActorsToTable(rootTable, actors);
    }

    public static void addActorsToTable(Table table, Actor... actors) {
        for (Actor actor : actors) {
            table.addActor(actor);
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        rootTable.setVisible(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
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
    public void hide() {
        rootTable.setVisible(false);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
