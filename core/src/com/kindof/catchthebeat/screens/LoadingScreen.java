package com.kindof.catchthebeat.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.kindof.catchthebeat.resources.FileType;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.authentication.AuthenticationScreen;
import com.kindof.catchthebeat.authentication.IAuthentication;
import com.kindof.catchthebeat.screens.beatmapeditor.BeatmapEdiorScreen;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebars.SideBar;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.BeatmapSelectionMenuScreen;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.StatisticScreen;
import com.kindof.catchthebeat.screens.game.OnCompletionScreen;
import com.kindof.catchthebeat.screens.settings.SettingsScreen;
import com.kindof.catchthebeat.screens.supportpage.SupportPageScreen;

public class LoadingScreen implements Screen {
    private float progress;

    public LoadingScreen() {
        progress = 0;
        loadAssets();
    }

    private void loadAssets() {
        Res.ASSET_MANAGER.load(Res.INTERNAL_PATH_TO_PACKED_SKINS_DIRECTORY + Res.SKIN_NAME + "/" + Res.SKIN_NAME + ".atlas", TextureAtlas.class);
        String internalPathToSkin = Res.INTERNAL_PATH_TO_SKINS_DIRECTORY + Res.SKIN_NAME;
        Res.ASSET_MANAGER.load(internalPathToSkin + "/" + Res.HIT_SOUND, Sound.class);
        Res.ASSET_MANAGER.load(internalPathToSkin + "/" + Res.BREAK_SOUND, Sound.class);
        Res.ASSET_MANAGER.load(internalPathToSkin + "/" + Res.COMPLETION_SOUND, Music.class);
        Res.ASSET_MANAGER.load(internalPathToSkin + "/" + Res.FAIL_SOUND, Music.class);
    }

    private void update() {
        progress = MathUtils.lerp(progress, Res.ASSET_MANAGER.getProgress(), 0.1f);
        if (Res.ASSET_MANAGER.update() && progress >= Res.ASSET_MANAGER.getProgress() - 0.01) {
            Res.SKIN_ATLAS = Res.ASSET_MANAGER.get(Res.INTERNAL_PATH_TO_PACKED_SKINS_DIRECTORY + Res.SKIN_NAME + "/" + Res.SKIN_NAME + ".atlas", TextureAtlas.class);
            Res.MAIN_MENU_SCREEN = new MainMenuScreen();
            Res.AUTHENTICATION_SCREEN = new AuthenticationScreen();
            Res.ON_COMPLETION_SCREEN = new OnCompletionScreen();
            Res.STATISTIC_SCREEN = new StatisticScreen();
            Res.BEATMAP_SELECTION_MENU_SCREEN = new BeatmapSelectionMenuScreen();
            Res.BEATMAP_EDITOR_SCREEN = new BeatmapEdiorScreen();
            Res.SUPPORT_PAGE_SCREEN = new SupportPageScreen();
            Res.SETTINGS_SCREEN = new SettingsScreen();

            IAuthentication auth = Res.GAME.getAuth();
            if (auth.currentUserNotExist()) {
                Res.GAME.setScreen(Res.AUTHENTICATION_SCREEN);
            } else {
                Res.USER = Res.GAME.getDatabase().getUser(auth.getCurrentUserUid());
                Res.STATISTIC_SCREEN.reInitLabels();
                SideBar sideBar = Res.BEATMAP_SELECTION_MENU_SCREEN.getSideBar();
                sideBar.initCurrentUserNicknameLabel();
                sideBar.initFriendScrollPane();
                sideBar.getCurrentUserScreen().getAddDeleteFriendScreen().initUserScrollPaneItems();
                String pathToUserDirectory = Res.LOCAL_PATH_TO_USERS_DIRECTORY + Res.USER.getUid() + "/";
                if (!Gdx.files.local(pathToUserDirectory).exists()) {
                    Gdx.files.local(pathToUserDirectory).file().mkdir();
                }
                if (!Gdx.files.local(pathToUserDirectory + "icon").exists()) {
                    Res.CURRENT_FILE_TYPE = FileType.userIcon;
                    Res.GAME.getStorage().getFile(pathToUserDirectory + "icon", Gdx.files.local(pathToUserDirectory + "icon").file());
                } else {
                    Res.USER.initIcon();
                }
                Res.GAME.setScreen(Res.MAIN_MENU_SCREEN);
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(progress, progress, progress, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update();
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
