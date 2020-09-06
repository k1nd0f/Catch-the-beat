package com.kindof.catchthebeat.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.kindof.catchthebeat.resources.FileType;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.screens.authentication.AuthenticationScreen;
import com.kindof.catchthebeat.authentication.IAuthentication;
import com.kindof.catchthebeat.screens.beatmapeditor.BeatmapEditorScreen;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar.SideBar;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.BeatmapSelectionMenuScreen;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.StatisticScreen;
import com.kindof.catchthebeat.screens.game.OnCompletionScreen;
import com.kindof.catchthebeat.screens.settings.SettingsScreen;
import com.kindof.catchthebeat.screens.supportpage.SupportPageScreen;
import com.kindof.catchthebeat.ui.UI;

public class LoadingScreen implements Screen {
    private float progress;

    public LoadingScreen() {
        progress = 0;
        loadAssets();
    }

    private void loadAssets() {
        UI.ASSET_MANAGER.load(Globals.INTERNAL_PATH_TO_PACKED_SKINS_DIRECTORY + UI.SKIN_NAME + "/" + UI.SKIN_NAME + ".atlas", TextureAtlas.class);
        String internalPathToSkin = Globals.INTERNAL_PATH_TO_SKINS_DIRECTORY + UI.SKIN_NAME;
        UI.ASSET_MANAGER.load(internalPathToSkin + "/" + Globals.HIT_SOUND, Sound.class);
        UI.ASSET_MANAGER.load(internalPathToSkin + "/" + Globals.BREAK_SOUND, Sound.class);
        UI.ASSET_MANAGER.load(internalPathToSkin + "/" + Globals.COMPLETION_SOUND, Music.class);
        UI.ASSET_MANAGER.load(internalPathToSkin + "/" + Globals.FAIL_SOUND, Music.class);
    }

    private void update() {
        progress = MathUtils.lerp(progress, UI.ASSET_MANAGER.getProgress(), 0.1f);
        if (UI.ASSET_MANAGER.update() && progress >= UI.ASSET_MANAGER.getProgress() - 0.01) {
            UI.SKIN_ATLAS = UI.ASSET_MANAGER.get(Globals.INTERNAL_PATH_TO_PACKED_SKINS_DIRECTORY + UI.SKIN_NAME + "/" + UI.SKIN_NAME + ".atlas", TextureAtlas.class);
            Globals.MAIN_MENU_SCREEN = new MainMenuScreen();
            Globals.AUTHENTICATION_SCREEN = new AuthenticationScreen();
            Globals.ON_COMPLETION_SCREEN = new OnCompletionScreen();
            Globals.STATISTIC_SCREEN = new StatisticScreen();
            Globals.BEATMAP_SELECTION_MENU_SCREEN = new BeatmapSelectionMenuScreen();
            Globals.BEATMAP_EDITOR_SCREEN = new BeatmapEditorScreen();
            Globals.SUPPORT_PAGE_SCREEN = new SupportPageScreen();
            Globals.SETTINGS_SCREEN = new SettingsScreen();

            IAuthentication auth = Globals.GAME.getAuth();
            if (auth.currentUserNotExist()) {
                Globals.GAME.setScreen(Globals.AUTHENTICATION_SCREEN);
            } else {
                Globals.USER = Globals.GAME.getDatabase().getUser(auth.getCurrentUserUid());
                Globals.STATISTIC_SCREEN.reInitLabels();
                SideBar sideBar = Globals.BEATMAP_SELECTION_MENU_SCREEN.getSideBar();
                sideBar.initCurrentUserNicknameLabel();
                sideBar.initFriendScrollPane();
                sideBar.getCurrentUserScreen().getAddDeleteFriendScreen().initUserScrollPaneItems();
                String pathToUserDirectory = Globals.LOCAL_PATH_TO_USERS_DIRECTORY + Globals.USER.getUid() + "/";
                if (!Gdx.files.local(pathToUserDirectory).exists()) {
                    Gdx.files.local(pathToUserDirectory).file().mkdir();
                }
                if (!Gdx.files.local(pathToUserDirectory + "icon").exists()) {
                    Globals.CURRENT_FILE_TYPE = FileType.userIcon;
                    Globals.GAME.getStorage().getFile(pathToUserDirectory + "icon", Gdx.files.local(pathToUserDirectory + "icon").file());
                } else {
                    Globals.USER.initIcon();
                }
                Globals.GAME.setScreen(Globals.MAIN_MENU_SCREEN);
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(progress, progress, progress, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Globals.GAME.getNetworkConnection().networkAction(this::update);
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
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
