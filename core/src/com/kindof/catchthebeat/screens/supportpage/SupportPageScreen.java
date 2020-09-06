package com.kindof.catchthebeat.screens.supportpage;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.BaseScreen;

/*
 *  screen with introduction
 *      / ... /
 *      / ... /
 *
 ************************************/

public class SupportPageScreen extends BaseScreen {
    public SupportPageScreen() {
        super();
    }

    @Override
    public void initialize() {
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Res.GAME.setScreen(Res.MAIN_MENU_SCREEN);
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setFillParent(true);
        stage.addActor(rootTable);
    }

    @Override
    public void hide() {
        super.hide();
    }
}
