package com.kindof.catchthebeat.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.kindof.catchthebeat.CatchTheBeatGame;
import com.kindof.catchthebeat.screens.authentication.AuthenticationScreen;
import com.kindof.catchthebeat.screens.beatmapeditor.BeatmapEditorScreen;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.BeatmapSelectionMenuScreen;
import com.kindof.catchthebeat.screens.chat.ChatScreen;
import com.kindof.catchthebeat.screens.game.GameScreen;
import com.kindof.catchthebeat.screens.game.OnCompletionScreen;
import com.kindof.catchthebeat.screens.MainMenuScreen;
import com.kindof.catchthebeat.screens.settings.SettingsScreen;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.StatisticScreen;
import com.kindof.catchthebeat.screens.supportpage.SupportPageScreen;
import com.kindof.catchthebeat.ui.UI;
import com.kindof.catchthebeat.user.User;

import java.io.IOException;
import java.util.HashMap;

public class Globals {
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

    public static Long CURRENT_BEATMAP_ID = null;

    // Screens
    public static CatchTheBeatGame GAME = null;
    public static GameScreen GAME_SCREEN = null;
    public static BeatmapSelectionMenuScreen BEATMAP_SELECTION_MENU_SCREEN = null;
    public static StatisticScreen STATISTIC_SCREEN = null;
    public static MainMenuScreen MAIN_MENU_SCREEN = null;
    public static BeatmapEditorScreen BEATMAP_EDITOR_SCREEN = null;
    public static SettingsScreen SETTINGS_SCREEN = null;
    public static SupportPageScreen SUPPORT_PAGE_SCREEN = null;
    public static AuthenticationScreen AUTHENTICATION_SCREEN = null;
    public static OnCompletionScreen ON_COMPLETION_SCREEN = null;

    public static FileType CURRENT_FILE_TYPE = FileType.unknown;
    public static ChatScreen CURRENT_CHAT_SCREEN = null;
    public static HashMap<Long, ChatScreen> CHAT_SCREENS = new HashMap<>();

    public static User USER;

    // Sounds
    public static final String HIT_SOUND = "hit-sound.wav";
    public static final String BREAK_SOUND = "break-sound.wav";
    public static final String COMPLETION_SOUND = "completion-sound.mp3";
    public static final String FAIL_SOUND = "fail-sound.mp3";

    // Paths
    public static final String INTERNAL_PATH_TO_PACKED_SKINS_DIRECTORY = "Packed skins/";
    public static final String INTERNAL_PATH_TO_SKINS_DIRECTORY = "Skins/";
    public static final String INTERNAL_PATH_TO_FONTS_DIRECTORY = "Fonts/";
    public static final String LOCAL_PATH_TO_USERS_DIRECTORY = "Users/";
    public static final String LOCAL_PATH_TO_SONGS_DIRECTORY = "Songs/";
    public static final String LOCAL_PATH_TO_BEATMAP_EDITOR_DIRECTORY = "BeatmapEditor/";
    public static final String LOCAL_PATH_TO_BEATMAP_EDITOR_TMP_DIRECTORY = LOCAL_PATH_TO_BEATMAP_EDITOR_DIRECTORY + "Tmp/";

    static {
        createLocalDirectory(LOCAL_PATH_TO_SONGS_DIRECTORY);
        createLocalDirectory(LOCAL_PATH_TO_USERS_DIRECTORY);
        createLocalDirectory(LOCAL_PATH_TO_BEATMAP_EDITOR_TMP_DIRECTORY);
    }

    public static void createLocalDirectory(String localPath) {
        Gdx.files.local(localPath).mkdirs();
    }

    public static boolean createLocalFile(String localPath) {
        try {
            return Gdx.files.local(localPath).file().createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean deleteLocalFile(String localPath) {
        return Gdx.files.local(localPath).file().delete();
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
    }
}
