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
import com.kindof.catchthebeat.users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpScreen implements Screen {
    private Stage stage;
    private Table rootTable;
    private TextField nicknameTextField, emailTextField, passwordTextField, confirmPasswordTextField;
    private Label nicknameLabel, emailLabel, passwordLabel, confirmPasswordLabel;
    private Button signUpButton;
    private ArrayList<String> nicknames;

    public SignUpScreen() {
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    clearAllFields();
                    Res.GAME.setScreen(Res.AUTHENTICATION_SCREEN);
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setVisible(false);
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setFillParent(true);

        init();

        rootTable.addActor(nicknameLabel);
        rootTable.addActor(nicknameTextField);
        rootTable.addActor(emailTextField);
        rootTable.addActor(emailLabel);
        rootTable.addActor(passwordTextField);
        rootTable.addActor(passwordLabel);
        rootTable.addActor(confirmPasswordTextField);
        rootTable.addActor(confirmPasswordLabel);
        rootTable.addActor(signUpButton);
        stage.addActor(rootTable);
    }

    private void init() {
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (25 * Res.RESOLUTION_HEIGHT_SCALE));
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.TEXT_FIELD_BACKGROUND));
        textFieldStyle.cursor = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.TEXT_FIELD_CURSOR)) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                super.draw(batch, x, y, Res.TEXT_FIELD_CURSOR_WIDTH, height);
            }
        };

        final TextField.TextFieldStyle passwordStyle = new TextField.TextFieldStyle(textFieldStyle);
        final TextField.TextFieldStyle emailStyle = new TextField.TextFieldStyle(textFieldStyle);
        final TextField.TextFieldStyle nicknameStyle = new TextField.TextFieldStyle(textFieldStyle);

        float width = Res.WIDTH * 2.0f / 3.0f, height = Res.HEIGHT / 10.0f, x = (Res.WIDTH - width) / 2.0f, pad = 10 * Res.RESOLUTION_HEIGHT_SCALE, y = Res.HEIGHT - (Res.HEIGHT - (6.0f * height + 4.0f * pad)) / 2.0f - height, labelWidth = x;
        nicknameTextField = new TextField("", nicknameStyle);
        nicknameTextField.setBounds(x, y, width, height);
        nicknameTextField.setAlignment(Align.center);
        nicknameTextField.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                nicknameTextField.getStyle().fontColor = Color.WHITE;
                return false;
            }
        });

        emailTextField = new TextField("", emailStyle);
        emailTextField.setAlignment(Align.center);
        emailTextField.setBounds(x, y - pad - height, width, height);
        emailTextField.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                emailTextField.getStyle().fontColor = Color.WHITE;
                return false;
            }
        });

        passwordTextField = new TextField("", passwordStyle);
        passwordTextField.setPasswordMode(true);
        passwordTextField.setPasswordCharacter('*');
        passwordTextField.setAlignment(Align.center);
        passwordTextField.setBounds(x, y - (pad + height) * 2.0f, width, height);
        passwordTextField.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                passwordTextField.getStyle().fontColor = Color.WHITE;
                return false;
            }
        });

        confirmPasswordTextField = new TextField("", passwordStyle);
        confirmPasswordTextField.setPasswordMode(true);
        confirmPasswordTextField.setPasswordCharacter('*');
        confirmPasswordTextField.setAlignment(Align.center);
        confirmPasswordTextField.setBounds(x, y - (pad + height) * 3.0f, width, height);

        width *= 3.0f / 4.0f;
        x = (Res.WIDTH - width) / 2.0f;
        signUpButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                String password = passwordTextField.getText(), confirmPassword = confirmPasswordTextField.getText();
                String nickname = nicknameTextField.getText();
                boolean correct = true;
                if (!password.equals(confirmPassword) || password.length() < 5) {
                    passwordStyle.fontColor = Color.RED;
                    correct = false;
                }
                if (!emailTextField.getText().contains("@")) {
                    emailStyle.fontColor = Color.RED;
                    correct = false;
                }
                if (nickname.length() < 2 || nickname.length() > 32 || nicknames.contains(nickname.toLowerCase())) {
                    nicknameStyle.fontColor = Color.RED;
                    correct = false;
                }

                if (correct) {
                    Res.GAME.getAuth().signUp(emailTextField.getText(), password, nicknameTextField.getText());
                }
            }
        }, x, y - (pad + height) * 3.0f - height * 2.0f - pad, width, height * 2.0f, Res.SIGN_UP_BUTTON_UP, Res.SIGN_UP_BUTTON_PRESS);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (25 * Res.RESOLUTION_HEIGHT_SCALE));
        labelStyle.fontColor = Color.WHITE;

        nicknameLabel = new Label("Nickname : ", labelStyle);
        nicknameLabel.setAlignment(Align.right);
        nicknameLabel.setBounds(0, y, labelWidth, height);

        emailLabel = new Label("Email : ", labelStyle);
        emailLabel.setAlignment(Align.right);
        emailLabel.setBounds(0, y - (pad + height), labelWidth, height);

        passwordLabel = new Label("Password : ", labelStyle);
        passwordLabel.setAlignment(Align.right);
        passwordLabel.setBounds(0, y - (pad + height) * 2.0f, labelWidth, height);

        confirmPasswordLabel = new Label("Password : ", labelStyle);
        confirmPasswordLabel.setAlignment(Align.right);
        confirmPasswordLabel.setBounds(0, y - (pad + height) * 3.0f, labelWidth, height);
        initNicknamesArray();
    }

    public String getEmail() {
        return emailTextField.getText();
    }

    private void initNicknamesArray() {
        HashMap<String, User> users = Res.GAME.getDatabase().getUsers();
        nicknames = new ArrayList<>(users.size());
        for (Map.Entry<String, User> entry : users.entrySet()) {
            nicknames.add(entry.getValue().getNickname().toLowerCase());
        }
    }

    public void clearAllFields() {
        nicknameTextField.setText("");
        emailTextField.setText("");
        passwordTextField.setText("");
        confirmPasswordTextField.setText("");
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
