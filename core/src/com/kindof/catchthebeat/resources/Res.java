package com.kindof.catchthebeat.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.kindof.catchthebeat.CatchTheBeatGame;
import com.kindof.catchthebeat.screens.authentication.AuthenticationScreen;
import com.kindof.catchthebeat.screens.beatmapeditor.BeatmapEdiorScreen;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.BeatmapSelectionMenuScreen;
import com.kindof.catchthebeat.screens.chat.ChatScreen;
import com.kindof.catchthebeat.screens.game.GameScreen;
import com.kindof.catchthebeat.screens.game.OnCompletionScreen;
import com.kindof.catchthebeat.screens.MainMenuScreen;
import com.kindof.catchthebeat.screens.settings.SettingsScreen;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.StatisticScreen;
import com.kindof.catchthebeat.screens.supportpage.SupportPageScreen;
import com.kindof.catchthebeat.users.User;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

public class Res {
    // Screen values
    public static final float DEFAULT_WIDTH = 800;
    public static final float DEFAULT_HEIGHT = 480;
    public static final float WIDTH = Gdx.graphics.getWidth();
    public static final float HEIGHT = Gdx.graphics.getHeight();
    public static final float RESOLUTION_WIDTH_SCALE = WIDTH / DEFAULT_WIDTH;
    public static final float RESOLUTION_HEIGHT_SCALE = HEIGHT / DEFAULT_HEIGHT;

    // Catcher values
    public static final float DEFAULT_CATCHER_WIDTH = 100;
    private static final float DEFAULT_CATCHER_HEIGHT = DEFAULT_CATCHER_WIDTH * 1.33333333f;
    public static final float CATCHER_WIDTH = DEFAULT_CATCHER_WIDTH * RESOLUTION_WIDTH_SCALE;
    public static final float CATCHER_HEIGHT = DEFAULT_CATCHER_HEIGHT * RESOLUTION_HEIGHT_SCALE;
    public static final float CATCHER_DELTA_DOWN_SCALE = 2.2f;

    public static final float FRUIT_DIAMETER = CATCHER_WIDTH / 2.0f;

    // Osu values
    public static final float OSU_WIDTH = 512;
    public static final float OSU_HEIGHT = 384;

    private static StringWriter stringWriter = new StringWriter();
    private static XmlWriter xmlWriter = new XmlWriter(stringWriter);

    // Settings values
    public static float MUSIC_VOLUME = 0.25f;
    public static float SOUND_VOLUME = 0.1f;
    public static float BEATMAP_BACKGROUND_BRIGHTNESS = 0.35f;
    public static boolean FULLSCREEN = true;
    public static String SKIN_NAME = "Sujak";
    public static String FONT_NAME = "Raleway-Black.ttf";
    public static final String FONT_CHARACTERS;
    public static Long CURRENT_BEATMAP_ID = null;

    public static AssetManager ASSET_MANAGER = new AssetManager();;
    public static TextureAtlas SKIN_ATLAS;

    // Screens
    public static CatchTheBeatGame GAME = null;
    public static GameScreen GAME_SCREEN = null;
    public static BeatmapSelectionMenuScreen BEATMAP_SELECTION_MENU_SCREEN = null;
    public static StatisticScreen STATISTIC_SCREEN = null;
    public static MainMenuScreen MAIN_MENU_SCREEN = null;
    public static BeatmapEdiorScreen BEATMAP_EDITOR_SCREEN = null;
    public static SettingsScreen SETTINGS_SCREEN = null;
    public static SupportPageScreen SUPPORT_PAGE_SCREEN = null;
    public static AuthenticationScreen AUTHENTICATION_SCREEN = null;
    public static OnCompletionScreen ON_COMPLETION_SCREEN = null;

    public static FileType CURRENT_FILE_TYPE = FileType.unknown;
    public static ChatScreen CURRENT_CHAT_SCREEN = null;
    public static HashMap<Integer, ChatScreen> CHAT_SCREENS = new HashMap<>();

    public static User USER;

    public static final float TEXT_FIELD_CURSOR_WIDTH = 4;

    // Asset names
    public static final String ADD_DELETE_FRIEND_BUTTON_PRESS = "add-delete-friend-button";
    public static final String ADD_DELETE_FRIEND_BUTTON_UP = "add-delete-friend-button";
    public static final String BUTTON_MINUS_PRESS = "button-minus";
    public static final String BUTTON_MINUS_UP = "button-minus";
    public static final String BUTTON_PLUS_PRESS = "button-plus";
    public static final String BUTTON_PLUS_UP = "button-plus";
    public static final String BEATMAP_DOWNLOAD_BUTTON_PRESS = "beatmap-download-button";
    public static final String BEATMAP_DOWNLOAD_BUTTON_UP = "beatmap-download-button";
    public static final String CATCHER_FAIL = "catcher-fail";
    public static final String CATCHER_IDLE = "catcher-idle";
    public static final String CHECKBOX_OFF = "checkbox-off";
    public static final String CHECKBOX_ON = "checkbox-on";
    public static final String DEFAULT_PERCENT = "default-percent";
    public static final String DROPDOWN_BACKGROUND = "dropdown-background";
    public static final String DROPDOWN_LIST_BACKGROUND = "dropdown-list-background";
    public static final String DROPDOWN_LIST_SELECTION = "dropdown-list-selection";
    public static final String FAIL_BACKGROUND = "fail-background";
    public static final String FRUIT_APPLE = "fruit-apple";
    public static final String FRUIT_BANANAS = "fruit-bananas";
    public static final String FRUIT_GRAPES = "fruit-grapes";
    public static final String FRUIT_ORANGE = "fruit-orange";
    public static final String FRUIT_PEAR = "fruit-pear";
    public static final String HEALTH_BAR_BACKGROUND = "health-bar-background";
    public static final String HEALTH_BAR_FOREGROUND = "health-bar-foreground";
    public static final String OSZ_PARSE_BUTTON_PRESS = "osz-parse-button";
    public static final String OSZ_PARSE_BUTTON_UP = "osz-parse-button";
    public static final String PAUSE_BACKGROUND = "pause-background";
    public static final String PAUSE_QUIT_BUTTON_PRESS = "pause-quit";
    public static final String PAUSE_QUIT_BUTTON_UP = "pause-quit";
    public static final String PAUSE_RESUME_BUTTON_PRESS = "pause-resume";
    public static final String PAUSE_RESUME_BUTTON_UP = "pause-resume";
    public static final String PAUSE_RETRY_BUTTON_PRESS = "pause-retry";
    public static final String PAUSE_RETRY_BUTTON_UP = "pause-retry";
    public static final String RANK_A = "rank-A";
    public static final String RANK_B = "rank-B";
    public static final String RANK_C = "rank-C";
    public static final String RANK_D = "rank-D";
    public static final String RANK_S = "rank-S";
    public static final String RANK_SS = "rank-SS";
    public static final String SCORE_0 = "score-0";
    public static final String SCORE_1 = "score-1";
    public static final String SCORE_2 = "score-2";
    public static final String SCORE_3 = "score-3";
    public static final String SCORE_4 = "score-4";
    public static final String SCORE_5 = "score-5";
    public static final String SCORE_6 = "score-6";
    public static final String SCORE_7 = "score-7";
    public static final String SCORE_8 = "score-8";
    public static final String SCORE_9 = "score-9";
    public static final String SCORE_COMMA = "score-comma";
    public static final String SCORE_DOT = "score-dot";
    public static final String SCORE_PERCENT = "score-percent";
    public static final String SCORE_X = "score-x";
    public static final String SELECT_IMAGE_ICON_BUTTON_PRESS = "select-image-icon-button";
    public static final String SELECT_IMAGE_ICON_BUTTON_UP = "select-image-icon-button";
    public static final String SIGN_IN_BUTTON_PRESS = "sign-in-button";
    public static final String SIGN_IN_BUTTON_UP = "sign-in-button";
    public static final String SIGN_UP_BUTTON_PRESS = "sign-up-button";
    public static final String SIGN_UP_BUTTON_UP = "sign-up-button";
    public static final String TEXT_FIELD_BACKGROUND = "text-field-background";
    public static final String TEXT_AREA_BACKGROUND = "text-area-background";
    public static final String TEXT_FIELD_CURSOR = "text-field-cursor";
    public static final String UI_ADD_DELETE_FRIEND_SCREEN_BACKGROUND = "ui-add-delete-friend-screen-background";
    public static final String UI_BEATMAP_EDITOR_BOTTOM_PANEL_BACKGROUND = "ui-beatmap-editor-bottom-panel-background";
    public static final String UI_BEATMAP_EDITOR_BUTTON_PRESS = "ui-beatmap-editor-button-press";
    public static final String UI_BEATMAP_EDITOR_BUTTON_UP = "ui-beatmap-editor-button-up";
    public static final String UI_BEATMAP_EDITOR_DELETE_OBJECT_BUTTON_PRESS = "ui-beatmap-editor-delete-object-button";
    public static final String UI_BEATMAP_EDITOR_DELETE_OBJECT_BUTTON_UP = "ui-beatmap-editor-delete-object-button";
    public static final String UI_BEATMAP_EDITOR_PAUSE_BUTTON_PRESS = "ui-beatmap-editor-pause-button";
    public static final String UI_BEATMAP_EDITOR_PAUSE_BUTTON_UP = "ui-beatmap-editor-pause-button";
    public static final String UI_BEATMAP_EDITOR_RESUME_BUTTON_PRESS = "ui-beatmap-editor-resume-button";
    public static final String UI_BEATMAP_EDITOR_RESUME_BUTTON_UP = "ui-beatmap-editor-resume-button";
    public static final String UI_BEATMAP_EDITOR_SETTINGS_BACKGROUND_BUTTON_PRESS = "ui-beatmap-editor-settings-background-button";
    public static final String UI_BEATMAP_EDITOR_SETTINGS_BACKGROUND_BUTTON_UP = "ui-beatmap-editor-settings-background-button";
    public static final String UI_BEATMAP_EDITOR_SETTINGS_BUTTON_PRESS = "ui-beatmap-editor-settings-button";
    public static final String UI_BEATMAP_EDITOR_SETTINGS_BUTTON_UP = "ui-beatmap-editor-settings-button";
    public static final String UI_BEATMAP_EDITOR_SETTINGS_MUSIC_BUTTON_PRESS = "ui-beatmap-editor-settings-music-button";
    public static final String UI_BEATMAP_EDITOR_SETTINGS_MUSIC_BUTTON_UP = "ui-beatmap-editor-settings-music-button";
    public static final String UI_BEATMAP_EDITOR_SETTINGS_SAVE_BEATMAP_BUTTON_PRESS = "ui-beatmap-editor-settings-save-beatmap-button";
    public static final String UI_BEATMAP_EDITOR_SETTINGS_SAVE_BEATMAP_BUTTON_UP = "ui-beatmap-editor-settings-save-beatmap-button";
    public static final String UI_BEATMAP_EDITOR_SETTINGS_UPLOAD_BEATMAP_BUTTON_PRESS = "ui-beatmap-editor-settings-upload-beatmap-button";
    public static final String UI_BEATMAP_EDITOR_SETTINGS_UPLOAD_BEATMAP_BUTTON_UP = "ui-beatmap-editor-settings-upload-beatmap-button";
    public static final String UI_BEATMAP_SELECTION_ITEM_BACKGROUND = "ui-beatmap-selection-item-background";
    public static final String UI_EXIT_BUTTON_PRESS = "ui-exit-button";
    public static final String UI_EXIT_BUTTON_UP = "ui-exit-button";
    public static final String UI_MAIN_MENU_BACKGROUND = "ui-main-menu-background";
    public static final String UI_MAIN_MENU_BUTTON_PRESS = "ui-main-menu-button";
    public static final String UI_MAIN_MENU_BUTTON_UP = "ui-main-menu-button";
    public static final String UI_RESULT_TAB_ACCURACY_BACKGROUND = "ui-result-tab-accuracy-background";
    public static final String UI_RESULT_TAB_BACKGROUND = "ui-result-tab-background";
    public static final String UI_RESULT_TAB_BPM_BACKGROUND = "ui-result-tab-BPM-background";
    public static final String UI_RESULT_TAB_COMBO_BACKGROUND = "ui-result-tab-combo-background";
    public static final String UI_RESULT_TAB_DIFFICULTY_BACKGROUND = "ui-result-tab-difficulty-background";
    public static final String UI_RESULT_TAB_HEALTH_RATE_BACKGROUND = "ui-result-tab-health-rate-background";
    public static final String UI_RESULT_TAB_SCORE_BACKGROUND = "ui-result-tab-score-background";
    public static final String UI_SETTINGS_BUTTON_PRESS = "ui-settings-button-press";
    public static final String UI_SETTINGS_BUTTON_UP = "ui-settings-button-up";
    public static final String UI_SIDE_BAR_BACKGROUND = "ui-side-bar-background";
    public static final String UI_SIDE_BAR_CURRENT_USER_BACKGROUND = "ui-side-bar-current-user-background";
    public static final String UI_STATISTICS_SCREEN_BACKGROUND = "ui-statistic-screen-background";
    public static final String UI_STATISTICS_BUTTON_PRESS = "ui-statistics-button-press";
    public static final String UI_STATISTICS_BUTTON_UP = "ui-statistics-button-up";
    public static final String UI_SUPPORT_PAGE_BUTTON_PRESS = "ui-support-page-button-press";
    public static final String UI_SUPPORT_PAGE_BUTTON_UP = "ui-support-page-button-up";
    public static final String USER_DEFAULT_ICON = "user-default-icon";
    public static final String USER_ICON_BACKGROUND = "user-icon-background";
    public static final String SEND_MESSAGE_BUTTON_UP = "send-message-button";
    public static final String SEND_MESSAGE_BUTTON_PRESS = "send-message-button";
    public static final String HIT_SOUND = "hit-sound.wav";
    public static final String BREAK_SOUND = "break-sound.wav";
    public static final String COMPLETION_SOUND = "completion-sound.mp3";
    public static final String FAIL_SOUND = "fail-sound.mp3";

    public static final String LOCAL_PATH_TO_PACKED_SKINS_DIRECTORY = "Packed skins/";
    public static final String LOCAL_PATH_TO_SKINS_DIRECTORY = "Skins/";
    public static final String LOCAL_PATH_TO_FONTS_DIRECTORY = "Fonts/";
    public static final String INTERNAL_PATH_TO_PACKED_SKINS_DIRECTORY = "Packed skins/";
    public static final String INTERNAL_PATH_TO_SKINS_DIRECTORY = "Skins/";
    public static final String INTERNAL_PATH_TO_FONTS_DIRECTORY = "Fonts/";
    public static final String BEATMAP_EDITOR_DIRECTORY_NAME = "BeatmapEditor";
    public static final String BEATMAP_EDITOR_TMP_DIRECTORY_NAME = "Tmp";
    public static final String LOCAL_PATH_TO_BEATMAP_EDITOR_TMP_DIRECTORY = BEATMAP_EDITOR_DIRECTORY_NAME + "/" + BEATMAP_EDITOR_TMP_DIRECTORY_NAME + "/";
    public static final String LOCAL_PATH_TO_USERS_DIRECTORY = "Users/";
    public static final String LOCAL_PATH_TO_SONGS_DIRECTORY = "Songs/";
    public static final String LOCAL_PATH_TO_BEATMAP_EDITOR_DIRECTORY = "BeatmapEditor/";
    public static final String USER_CONFIG_NAME = "config.xml";

    static {
        String s = "";
        for (int i = 0x20; i < 0x7B; i++) {
            s += (char) i;
        }
        for (int i = 0x401; i < 0x452; i++) {
            s += (char) i;
        }
        FONT_CHARACTERS = s;

        if (!Gdx.files.local(LOCAL_PATH_TO_SONGS_DIRECTORY).exists()) {
            createLocalSongsDirectory();
        }
        if (!Gdx.files.local(LOCAL_PATH_TO_FONTS_DIRECTORY).exists()) {
            copyDefaultFontsToLocalDirectory();
        }
        if (!Gdx.files.local(LOCAL_PATH_TO_SKINS_DIRECTORY).exists()) {
            copyDefaultSkinsToLocalDirectory();
        }
        if (!Gdx.files.local(LOCAL_PATH_TO_PACKED_SKINS_DIRECTORY).exists()) {
            copyDefaultPackedSkinsToLocalDirectory();
        }
        if (!Gdx.files.local(LOCAL_PATH_TO_USERS_DIRECTORY).exists()) {
            createUsersDirectory();
        }
        if (!Gdx.files.local(LOCAL_PATH_TO_BEATMAP_EDITOR_DIRECTORY).exists()) {
            createDirectoryForBeatmapEditor();
        }
        if (!Gdx.files.local(USER_CONFIG_NAME).exists()) {
            createLocalConfigFile();
            saveLocalConfigFile();
        }
        initSettingsFromLocalConfig();
    }

    private static void initSettingsFromLocalConfig() {
        XmlReader xmlReader = new XmlReader();
        XmlReader.Element element = xmlReader.parse(Gdx.files.local("config.xml"));
        Res.SKIN_NAME = element.get("Skin");
        Res.FONT_NAME = element.get("Font");
        Res.FULLSCREEN = element.getBoolean("Fullscreen");
        Res.BEATMAP_BACKGROUND_BRIGHTNESS = element.getFloat("BeatmapBackgroundBrightness");
        Res.MUSIC_VOLUME = element.getFloat("MusicVolume");
        Res.SOUND_VOLUME = element.getFloat("SoundVolume");
    }

    private static void createLocalSongsDirectory() {
        Gdx.files.local(LOCAL_PATH_TO_SONGS_DIRECTORY).mkdirs();
    }

    private static void copyDefaultFontsToLocalDirectory() {
        Gdx.files.internal(INTERNAL_PATH_TO_FONTS_DIRECTORY).copyTo(Gdx.files.local(""));
    }

    private static void copyDefaultSkinsToLocalDirectory() {
        Gdx.files.internal(INTERNAL_PATH_TO_SKINS_DIRECTORY).copyTo(Gdx.files.local(""));
    }

    private static void copyDefaultPackedSkinsToLocalDirectory() {
        Gdx.files.internal(INTERNAL_PATH_TO_PACKED_SKINS_DIRECTORY).copyTo(Gdx.files.local(""));
    }

    private static void createLocalConfigFile() {
        try {
            Gdx.files.local(USER_CONFIG_NAME).file().createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createUsersDirectory() {
        Gdx.files.local(LOCAL_PATH_TO_USERS_DIRECTORY).file().mkdir();
    }

    private static void createDirectoryForBeatmapEditor() {
        Gdx.files.local(LOCAL_PATH_TO_BEATMAP_EDITOR_TMP_DIRECTORY).mkdirs();
    }

    public static void saveLocalConfigFile() {
        try {
            xmlWriter.element("Config")
                        .element("Skin").text(Res.SKIN_NAME).pop()
                        .element("Font").text(Res.FONT_NAME).pop()
                        .element("BeatmapBackgroundBrightness").text(Res.BEATMAP_BACKGROUND_BRIGHTNESS).pop()
                        .element("Fullscreen").text(Res.FULLSCREEN).pop()
                        .element("MusicVolume").text(Res.MUSIC_VOLUME).pop()
                        .element("SoundVolume").text(Res.SOUND_VOLUME).pop();
            xmlWriter.close();
            Gdx.files.local(USER_CONFIG_NAME).writeString(stringWriter.toString(), false);
            stringWriter.getBuffer().setLength(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void dispose() {
        if (GAME_SCREEN != null) {
            GAME_SCREEN.dispose();
        }
        if (BEATMAP_SELECTION_MENU_SCREEN != null) {
            BEATMAP_SELECTION_MENU_SCREEN.dispose();
        }
        if (STATISTIC_SCREEN != null) {
            STATISTIC_SCREEN.dispose();
        }
        if (MAIN_MENU_SCREEN != null) {
            MAIN_MENU_SCREEN.dispose();
        }
        if (BEATMAP_EDITOR_SCREEN != null) {
            BEATMAP_EDITOR_SCREEN.dispose();
        }
        if (SETTINGS_SCREEN != null) {
            SETTINGS_SCREEN.dispose();
        }
        if (SUPPORT_PAGE_SCREEN != null) {
            SUPPORT_PAGE_SCREEN.dispose();
        }
        if (ON_COMPLETION_SCREEN != null) {
            ON_COMPLETION_SCREEN.dispose();
        }
        if (AUTHENTICATION_SCREEN != null) {
            AUTHENTICATION_SCREEN.dispose();
        }

        ASSET_MANAGER.dispose();
    }
}
