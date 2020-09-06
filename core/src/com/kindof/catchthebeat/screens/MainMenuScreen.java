package com.kindof.catchthebeat.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.ui.Alignment;
import com.kindof.catchthebeat.ui.UI;
import com.kindof.catchthebeat.ui.actors.button.Button;

public class MainMenuScreen extends BaseScreen {
    private Button exitButton, supportPageButton, mainButton, beatmapEditorButton, settingsButton;

    public MainMenuScreen() {
        super();
    }

    @Override
    public void initialize() {
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Globals.GAME.exit();
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setFillParent(true);
        initButtons();
        addActors(
                mainButton,
                settingsButton,
                beatmapEditorButton,
                supportPageButton,
                exitButton
        );
        stage.addActor(rootTable);
    }

    private void initButtons() {
        float buttonW = Globals.WIDTH / 5.0f;

        exitButton = new Button((event, x1, y1, pointer, button) -> Globals.GAME.exit(), UI.CLOSE_BUTTON, UI.CLOSE_BUTTON);
        UI.calculateAndSetViewElementBounds(
                exitButton,
                Alignment.center, 0,
                0, 0, buttonW, Globals.HEIGHT
        );

        supportPageButton = new Button((event, x1, y1, pointer, button) -> Globals.GAME.setScreenWithTransition(Globals.SUPPORT_PAGE_SCREEN), UI.HEART_BUTTON, UI.HEART_BUTTON);
        UI.calculateAndSetViewElementBounds(
                supportPageButton,
                Alignment.center, 0,
                buttonW, 0, buttonW, Globals.HEIGHT
        );

        mainButton = new Button((event, x1, y1, pointer, button) -> Globals.GAME.setScreenWithTransition(Globals.BEATMAP_SELECTION_MENU_SCREEN), UI.MAIN_BUTTON, UI.MAIN_BUTTON);
        UI.calculateAndSetViewElementBounds(
                mainButton,
                Alignment.center, 0,
                buttonW * 2.0f, 0, buttonW, Globals.HEIGHT
        );

        beatmapEditorButton = new Button((event, x1, y1, pointer, button) -> Globals.GAME.setScreenWithTransition(Globals.BEATMAP_EDITOR_SCREEN), UI.EDIT_BUTTON, UI.EDIT_BUTTON);
        UI.calculateAndSetViewElementBounds(
                beatmapEditorButton,
                Alignment.center, 0,
                buttonW * 3.0f, 0, buttonW, Globals.HEIGHT
        );

        settingsButton = new Button((event, x1, y1, pointer, button) -> Globals.GAME.setScreenWithTransition(Globals.SETTINGS_SCREEN), UI.SETTINGS_BUTTON, UI.SETTINGS_BUTTON);
        UI.calculateAndSetViewElementBounds(
                settingsButton,
                Alignment.center, 0,
                buttonW * 4.0f, 0, buttonW, Globals.HEIGHT
        );
    }

    @Override
    public void hide() {
        super.hide();
        exitButton.finish();
        mainButton.finish();
        settingsButton.finish();
        beatmapEditorButton.finish();
        supportPageButton.finish();
    }
}
