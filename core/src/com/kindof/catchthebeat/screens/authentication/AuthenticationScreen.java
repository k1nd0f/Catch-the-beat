package com.kindof.catchthebeat.screens.authentication;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.ui.actors.buttons.Button;
import com.kindof.catchthebeat.ui.actors.buttons.TouchUpEventListener;

public class AuthenticationScreen implements Screen {
    private Stage stage;
    private Table rootTable;
    private Button goToSignIn, goToSignUp;
    private SignUpScreen signUpScreen;
    private SignInScreen signInScreen;

    public AuthenticationScreen() {
        signUpScreen = new SignUpScreen();
        signInScreen = new SignInScreen();

        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Gdx.app.exit();
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setVisible(false);
        rootTable.setFillParent(true);
        rootTable.setTouchable(Touchable.childrenOnly);

        float height = Res.HEIGHT / 5.0f, width = height * 6, x = (Res.WIDTH - width) / 2.0f, pad = 10 * Res.RESOLUTION_HEIGHT_SCALE, y = (Res.HEIGHT - height * 2 + pad) / 2.0f;
        goToSignIn = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Res.GAME.setScreen(signInScreen);
            }
        }, x, y + pad + height, width, height, Res.SIGN_IN_BUTTON_UP, Res.SIGN_IN_BUTTON_PRESS);
        goToSignUp = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Res.GAME.setScreen(signUpScreen);
            }
        }, x, y, width, height, Res.SIGN_UP_BUTTON_UP, Res.SIGN_UP_BUTTON_PRESS);

        rootTable.addActor(goToSignIn);
        rootTable.addActor(goToSignUp);
        stage.addActor(rootTable);
    }

    public SignInScreen getSignInScreen() {
        return signInScreen;
    }

    public SignUpScreen getSignUpScreen() {
        return signUpScreen;
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
        signUpScreen.dispose();
        signInScreen.dispose();
        stage.dispose();
    }
}
