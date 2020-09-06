package com.kindof.catchthebeat.screens;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.kindof.catchthebeat.resources.Globals;

public class Transition {
    public static final float DURATION = 0.20f;

    private BaseScreen currentScreen;
    private BaseScreen nextScreen;

    public Transition() {

    }

    public void set(BaseScreen currentScreen, BaseScreen nextScreen) {
        this.currentScreen = currentScreen;
        this.nextScreen = nextScreen;
    }

    public void transition(TransitionType type) {
        transition(type, DURATION);
    }

    public void transition(TransitionType type, float duration) {
        currentScreen.stage.getRoot().clearActions();
        nextScreen.stage.getRoot().clearActions();

        if (type == TransitionType.fade) {
            currentScreen.stage.addAction(Actions.sequence(
                    Actions.fadeOut(duration),
                    Actions.run(() -> Globals.GAME.setScreen(nextScreen)),
                    Actions.alpha(1f)
            ));
            nextScreen.stage.addAction(Actions.sequence(
                    Actions.alpha(0f),
                    Actions.fadeIn(duration)
            ));
        }
    }

    public enum TransitionType {
        fade
    }
}
