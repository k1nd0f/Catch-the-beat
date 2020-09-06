package com.kindof.catchthebeat.screens.authentication;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.ui.Alignment;
import com.kindof.catchthebeat.ui.UI;
import com.kindof.catchthebeat.ui.actors.button.Button;

public class AuthenticationScreen extends BaseScreen {
    private SignUpScreen signUpScreen;
    private SignInScreen signInScreen;

    public AuthenticationScreen() {
        super();
    }

    @Override
    public void initialize() {
        signInScreen = new SignInScreen();
        signUpScreen = new SignUpScreen();

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

        float
                buttonH = Globals.HEIGHT / 5.0f,
                buttonW = Globals.WIDTH / (2.0f / 3.0f),
                buttonPad = 15 * Globals.RESOLUTION_HEIGHT_SCALE,
                iButtonW = Globals.WIDTH,
                iButtonH = Globals.HEIGHT / 2.0f;

        Button goToSignInButton = new Button((event, x, y, pointer, button) -> Globals.GAME.setScreenWithTransition(signInScreen), UI.SIGN_IN_BUTTON, UI.SIGN_IN_BUTTON);
        goToSignInButton.setSize(buttonW, buttonH);
        UI.calculateAndSetViewElementBounds(
                goToSignInButton,
                Alignment.bottom, buttonPad,
                0, iButtonH, iButtonW, iButtonH
        );

        Button goToSignUpButton = new Button((event, x, y, pointer, button) -> Globals.GAME.setScreenWithTransition(signUpScreen), UI.SIGN_UP_BUTTON, UI.SIGN_UP_BUTTON);
        goToSignUpButton.setSize(buttonW, buttonH);
        UI.calculateAndSetViewElementBounds(
                goToSignUpButton,
                Alignment.top, buttonPad,
                0, 0, iButtonW, iButtonH
        );

        addActors(goToSignInButton, goToSignUpButton);
        stage.addActor(rootTable);
    }

    public SignInScreen getSignInScreen() {
        return signInScreen;
    }

    public SignUpScreen getSignUpScreen() {
        return signUpScreen;
    }

    @Override
    public void dispose() {
        signUpScreen.dispose();
        signInScreen.dispose();
        super.dispose();
    }
}
