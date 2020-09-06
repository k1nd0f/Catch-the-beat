package com.kindof.catchthebeat.screens.authentication;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.ui.actors.buttons.Button;
import com.kindof.catchthebeat.ui.actors.buttons.TouchUpEventListener;
import com.kindof.catchthebeat.tools.Font;

public class SignInScreen implements Screen {
    private Stage stage;
    private Table rootTable;
    private TextField emailTextField, passwordTextField;
    private Label emailLabel, passwordLabel;
    private Button signInButton;

    public SignInScreen() {
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    emailTextField.setText("");
                    passwordTextField.setText("");
                    Res.GAME.setScreen(Res.AUTHENTICATION_SCREEN);
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setVisible(false);
        rootTable.setFillParent(true);

        init();

        rootTable.addActor(emailTextField);
        rootTable.addActor(emailLabel);
        rootTable.addActor(passwordTextField);
        rootTable.addActor(passwordLabel);
        rootTable.addActor(signInButton);
        stage.addActor(rootTable);
    }

    private void init() {
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (25 * Res.RESOLUTION_HEIGHT_SCALE));
        textFieldStyle.cursor = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.TEXT_FIELD_CURSOR)) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                super.draw(batch, x, y, Res.TEXT_FIELD_CURSOR_WIDTH, height);
            }
        };
        textFieldStyle.background = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.TEXT_FIELD_BACKGROUND));

        final TextField.TextFieldStyle emailTextFieldStyle = new TextField.TextFieldStyle(textFieldStyle);
        final TextField.TextFieldStyle passwordTextFieldStyle = new TextField.TextFieldStyle(textFieldStyle);

        float width = Res.WIDTH * 2.0f / 3.0f, height = Res.HEIGHT / 10.0f, x = (Res.WIDTH - width) / 2.0f, pad = 10 * Res.RESOLUTION_HEIGHT_SCALE, y = Res.HEIGHT - (Res.HEIGHT - (4.0f * height + 2.0f * pad)) / 2.0f - height, labelWidth = x;
        emailTextField = new TextField("", emailTextFieldStyle);
        emailTextField.setAlignment(Align.center);
        emailTextField.setBounds(x, y, width, height);
        emailTextField.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                emailTextField.getStyle().fontColor = Color.WHITE;
                return false;
            }
        });

        passwordTextField = new TextField("", passwordTextFieldStyle);
        passwordTextField.setPasswordMode(true);
        passwordTextField.setPasswordCharacter('*');
        passwordTextField.setAlignment(Align.center);
        passwordTextField.setBounds(x, y - (pad + height), width, height);
        passwordTextField.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                passwordTextField.getStyle().fontColor = Color.WHITE;
                return false;
            }
        });

        width *= 3.0f / 4.0f;
        x = (Res.WIDTH - width) / 2.0f;
        signInButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                boolean correct = true;
                String email = emailTextField.getText(), password = passwordTextField.getText();
                if (!email.contains("@") || email.length() < 3) {
                    emailTextFieldStyle.fontColor = Color.RED;
                    correct = false;
                }
                if (password.length() < 5) {
                    passwordTextFieldStyle.fontColor = Color.RED;
                    correct = false;
                }

                if (correct) {
                    Res.GAME.getAuth().signIn(email, password);
                }
            }
        }, x, y - (pad + height) * 2.0f - height, width, height * 2.0f, Res.SIGN_IN_BUTTON_UP, Res.SIGN_IN_BUTTON_PRESS);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.WHITE;
        labelStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (25 * Res.RESOLUTION_HEIGHT_SCALE));

        emailLabel = new Label("Email : ", labelStyle);
        emailLabel.setAlignment(Align.right);
        emailLabel.setBounds(0, y, labelWidth, height);

        passwordLabel = new Label("Password : ", labelStyle);
        passwordLabel.setAlignment(Align.right);
        passwordLabel.setBounds(0, y - (pad + height), labelWidth, height);
    }

    public TextField getEmailTextField() {
        return emailTextField;
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
