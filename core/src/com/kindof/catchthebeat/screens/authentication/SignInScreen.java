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

public class SignInScreen extends BaseScreen {
    private TextField emailTextField, passwordTextField;
    private Label emailLabel, passwordLabel;
    private Button signInButton;

    public SignInScreen() {
        super();
    }

    @Override
    public void initialize() {
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Globals.GAME.setScreenWithTransition(Globals.AUTHENTICATION_SCREEN);
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setVisible(false);
        rootTable.setFillParent(true);

        final TextFieldStyle emailStyle = new TextFieldStyle(Color.WHITE, 22);
        final TextFieldStyle passwordStyle = new TextFieldStyle(Color.WHITE, 22);

        float
                textFieldW = Globals.WIDTH * 2.0f / 3.0f,
                textFieldX = (Globals.WIDTH - textFieldW) / 2.0f,
                labelW = textFieldX,
                verticalPad = 10 * Globals.RESOLUTION_HEIGHT_SCALE,
                itemH = Globals.HEIGHT / 10.0f,
                itemY = Globals.HEIGHT - (Globals.HEIGHT - (itemH * 2.0f + verticalPad + (itemH + verticalPad) * 1.33f)) / 2.0f - itemH;

        LabelStyle labelStyle = new LabelStyle(Color.WHITE, 22);

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

        itemY -= (itemH + verticalPad) * 1.33f;
        signInButton = new Button((event, x, y, pointer, button) -> {
            boolean correct = true;
            String email = emailTextField.getText(), password = passwordTextField.getText();
            if (!email.contains("@") || email.length() < 3) {
                emailStyle.fontColor = Color.RED;
                correct = false;
            }
            if (password.length() < 5) {
                passwordStyle.fontColor = Color.RED;
                correct = false;
            }

            if (correct) {
                Globals.GAME.getNetworkConnection().networkAction(() -> Globals.GAME.getAuth().signIn(email, password));
            }
        }, UI.SIGN_IN_BUTTON, UI.SIGN_IN_BUTTON);
        signInButton.setBounds(textFieldX, itemY, textFieldW, itemH);

        addActors(
                emailTextField,
                passwordTextField,
                emailLabel,
                passwordLabel,
                signInButton
        );
        stage.addActor(rootTable);
    }

    public TextField getEmailTextField() {
        return emailTextField;
    }
}
