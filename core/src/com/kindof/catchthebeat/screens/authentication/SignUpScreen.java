package com.kindof.catchthebeat.screens.authentication;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.ui.UI;
import com.kindof.catchthebeat.ui.styles.LabelStyle;
import com.kindof.catchthebeat.ui.styles.TextFieldStyle;
import com.kindof.catchthebeat.ui.actors.button.Button;

public class SignUpScreen extends BaseScreen {
    private TextField nicknameTextField, emailTextField, passwordTextField, confirmPasswordTextField;
    private Label nicknameLabel, emailLabel, passwordLabel, confirmPasswordLabel;
    private Button signUpButton;

    public SignUpScreen() {
        super();
    }

    @Override
    public void initialize() {
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    clearAllFields();
                    Globals.GAME.setScreenWithTransition(Globals.AUTHENTICATION_SCREEN);
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setVisible(false);
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setFillParent(true);

        final TextFieldStyle passwordStyle = new TextFieldStyle(Color.WHITE, 22);
        final TextFieldStyle emailStyle = new TextFieldStyle(Color.WHITE, 22);
        final TextFieldStyle nicknameStyle = new TextFieldStyle(Color.WHITE, 22);

        float
                textFieldW = Globals.WIDTH * 2.0f / 3.0f,
                textFieldX = (Globals.WIDTH - textFieldW) / 2.0f,
                labelW = textFieldX,
                verticalPad = 10 * Globals.RESOLUTION_HEIGHT_SCALE,
                itemH = Globals.HEIGHT / 10.0f,
                itemY = Globals.HEIGHT - (Globals.HEIGHT - (itemH * 4.0f + verticalPad * 3.0f + (itemH + verticalPad) * 1.33f)) / 2.0f - itemH;

        LabelStyle labelStyle = new LabelStyle(Color.WHITE, 22);

        nicknameLabel = new Label("Nickname : ", labelStyle);
        nicknameLabel.setAlignment(Align.right);
        nicknameLabel.setBounds(0, itemY, labelW, itemH);

        nicknameTextField = new TextField("", nicknameStyle);
        nicknameTextField.setAlignment(Align.center);
        nicknameTextField.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                nicknameStyle.fontColor = Color.WHITE;
                return false;
            }
        });
        nicknameTextField.setBounds(textFieldX, itemY, textFieldW, itemH);

        itemY -= (itemH + verticalPad);
        emailLabel = new Label("Email : ", labelStyle);
        emailLabel.setAlignment(Align.right);
        emailLabel.setBounds(0, itemY, labelW, itemH);

        emailTextField = new TextField("", emailStyle);
        emailTextField.setAlignment(Align.center);
        emailTextField.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                emailStyle.fontColor = Color.WHITE;
                return false;
            }
        });
        emailTextField.setBounds(textFieldX, itemY, textFieldW, itemH);

        itemY -= (itemH + verticalPad);
        passwordLabel = new Label("Password : ", labelStyle);
        passwordLabel.setAlignment(Align.right);
        passwordLabel.setBounds(0, itemY, labelW, itemH);

        passwordTextField = new TextField("", passwordStyle);
        passwordTextField.setPasswordMode(true);
        passwordTextField.setPasswordCharacter('-');
        passwordTextField.setAlignment(Align.center);
        passwordTextField.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                passwordStyle.fontColor = Color.WHITE;
                return false;
            }
        });
        passwordTextField.setBounds(textFieldX, itemY, textFieldW, itemH);

        itemY -= (itemH + verticalPad);
        confirmPasswordLabel = new Label("Password : ", labelStyle);
        confirmPasswordLabel.setAlignment(Align.right);
        confirmPasswordLabel.setBounds(0, itemY, labelW, itemH);

        confirmPasswordTextField = new TextField("", passwordStyle);
        confirmPasswordTextField.setPasswordMode(true);
        confirmPasswordTextField.setPasswordCharacter('-');
        confirmPasswordTextField.setAlignment(Align.center);
        confirmPasswordTextField.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                passwordStyle.fontColor = Color.WHITE;
                return false;
            }
        });
        confirmPasswordTextField.setBounds(textFieldX, itemY, textFieldW, itemH);

        itemY -= (itemH + verticalPad) * 1.33f;
        signUpButton = new Button((event, x, y, pointer, button) -> {
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

            if (nickname.length() < 2 || nickname.length() > 32) {
                nicknameStyle.fontColor = Color.RED;
                correct = false;
            }

            if (correct) {
                Globals.GAME.getNetworkConnection().networkAction(() -> Globals.GAME.getAuth().signUp(emailTextField.getText(), password, nicknameTextField.getText()));
            }
        }, UI.SIGN_UP_BUTTON, UI.SIGN_UP_BUTTON);
        signUpButton.setBounds(textFieldX, itemY, textFieldW, itemH);

        addActors(
                nicknameTextField,
                emailTextField,
                passwordTextField,
                confirmPasswordTextField,
                nicknameLabel,
                emailLabel,
                passwordLabel,
                confirmPasswordLabel,
                signUpButton
        );
        stage.addActor(rootTable);
    }

    public String getEmail() {
        return emailTextField.getText();
    }

    public void clearAllFields() {
        nicknameTextField.setText("");
        emailTextField.setText("");
        passwordTextField.setText("");
        confirmPasswordTextField.setText("");
    }
}
