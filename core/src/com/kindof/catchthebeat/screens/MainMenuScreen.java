package com.kindof.catchthebeat.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.settings.SettingsScreen;
import com.kindof.catchthebeat.screens.supportpage.SupportPageScreen;
import com.kindof.catchthebeat.ui.actors.buttons.Button;
import com.kindof.catchthebeat.ui.actors.buttons.TouchUpEventListener;

public class MainMenuScreen extends BaseScreen {
    private Button mainButton, beatmapEditorButton, settingsButton, supportPageButton, exitButton;

    public MainMenuScreen() {
        super();
    }

    private void initButtons() {
        float y, width, height;
        y = 0;
        width = Res.WIDTH / 5.0f;
        height = Res.HEIGHT;

        // Main
        mainButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Res.GAME.setScreen(Res.BEATMAP_SELECTION_MENU_SCREEN);
            }
        }, width * 2.0f, y, width, height, Res.UI_MAIN_MENU_BUTTON_UP, Res.UI_MAIN_MENU_BUTTON_PRESS);

        // Settings
        settingsButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Res.GAME.setScreen(Res.SETTINGS_SCREEN);
            }
        }, width * 4.0f, y, width, height, Res.UI_SETTINGS_BUTTON_UP, Res.UI_SETTINGS_BUTTON_PRESS);

        // Beatmap Editor
        beatmapEditorButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Res.GAME.setScreen(Res.BEATMAP_EDITOR_SCREEN);
            }
        }, width * 3.0f, y, width, height, Res.UI_BEATMAP_EDITOR_BUTTON_UP, Res.UI_BEATMAP_EDITOR_BUTTON_PRESS);

        // Support Page
        supportPageButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Res.GAME.setScreen(Res.SUPPORT_PAGE_SCREEN);
            }
        }, width, y, width, height, Res.UI_SUPPORT_PAGE_BUTTON_UP, Res.UI_SUPPORT_PAGE_BUTTON_PRESS);

        // Exit
        exitButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                exit();
            }
        }, 0, y, width, height, Res.UI_EXIT_BUTTON_UP, Res.UI_EXIT_BUTTON_PRESS);
    }

    private void exit() {
        Res.GAME.getDatabase().setUser(Res.USER);
        Gdx.app.exit();
    }

    @Override
    public void initialize() {
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    exit();
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setBackground(new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.UI_MAIN_MENU_BACKGROUND)));
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setFillParent(true);
        initButtons();
        addActors(mainButton, settingsButton, beatmapEditorButton, supportPageButton, exitButton);
        stage.addActor(rootTable);
    }

    @Override
    public void show() {
        super.show();
        mainButton.setUpFrame();
        settingsButton.setUpFrame();
        beatmapEditorButton.setUpFrame();
        supportPageButton.setUpFrame();
    }

    @Override
    public void hide() {
        super.hide();
    }
}
