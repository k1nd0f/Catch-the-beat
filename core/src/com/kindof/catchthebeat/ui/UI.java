package com.kindof.catchthebeat.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kindof.catchthebeat.screens.Transition;

/*
 * bg - background
 * fg - foreground
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class UI {

    public static AssetManager ASSET_MANAGER = new AssetManager();
    public static TextureAtlas SKIN_ATLAS;
    public static ShapeRenderer SHAPE_RENDERER = new ShapeRenderer();

    public static String SKIN_NAME = "shadeofpink";
    public static String FONT_NAME = "Raleway-SemiBold.ttf";
    public static final String FONT_CHARS;

    public static final String UP_POSTFIX = "-up";
    public static final String PRESS_POSTFIX = "";

    static {
        String s = "";
        for (int i = 0x20; i < 0x7B; i++) {
            s += (char) i;
        }
        for (int i = 0x401; i < 0x452; i++) {
            s += (char) i;
        }
        FONT_CHARS = s;
    }

    public static final Transition TRANSITION = new Transition();

    /* Assets */

    // User
    public static final String USER_ICON_BG = "ui-user-icon-bg";
    public static final String USER_DEFAULT_ICON = "ui-user-default-icon";

    // Fruits
    public static final String FRUIT_APPLE = "fruit-apple";
    public static final String FRUIT_ORANGE = "fruit-orange";
    public static final String FRUIT_PEAR = "fruit-pear";
    public static final String FRUIT_GRAPE = "fruit-grape";

    // Catcher
    public static final String CATCHER = "catcher";

    // Ranks
    public static final String RANK_SS = "rank-SS";
    public static final String RANK_S = "rank-S";
    public static final String RANK_A = "rank-A";
    public static final String RANK_B = "rank-B";
    public static final String RANK_C = "rank-C";
    public static final String RANK_D = "rank-D";

    // Score
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

    public static final String SCORE_PERCENT = "score-percent";
    public static final String SCORE_COMMA = "score-comma";
    public static final String SCORE_DOT = "score-dot";
    public static final String SCORE_X = "score-x";

    // Authentication Screen
    public static final String SIGN_IN_BUTTON = "ui-sign-in-button";
    public static final String SIGN_UP_BUTTON = "ui-sign-up-button";

    // Result Tab
    public static final String RESULT_STATS_BG = "ui-result-stats-bg";

    // Pause Menu
    public static final String PAUSE_BG = "ui-pause-bg";
    public static final String FAIL_BG = "ui-fail-bg";
    public static final String PAUSE_RESUME = "ui-pause-continue";
    public static final String PAUSE_RETRY = "ui-pause-retry";
    public static final String PAUSE_QUIT = "ui-pause-back";

    // Beatmap Selection Menu
    public static final String BEATMAP_SELECTION_BG = "ui-beatmap-selection-bg";
    public static final String BEATMAP_BUTTON_FG = "ui-selection-beatmap-overlay-button";
    public static final String STATISTICS_BUTTON = "ui-statistics-button";
    public static final String SIDE_BAR_BG = "ui-side-bar-bg";

    // Main Menu
    public static final String MAIN_BUTTON = "ui-main-button";
    public static final String HEART_BUTTON = "ui-heart-button";
    public static final String CLOSE_BUTTON = "ui-close-button";
    public static final String EDIT_BUTTON = "ui-edit-button";
    public static final String SETTINGS_BUTTON = "ui-settings-button";

    // Beatmap Editor
    public static final String BEATMAP_EDITOR_SETTINGS_BUTTON = "ui-beatmap-editor-settings-button";
    public static final String BEATMAP_EDITOR_RESUME_BUTTON = "ui-beatmap-editor-resume-button";
    public static final String BEATMAP_EDITOR_PAUSE_BUTTON = "ui-beatmap-editor-pause-button";
    public static final String BEATMAP_EDITOR_DELETE_OBJECT_BUTTON = "ui-beatmap-editor-delete-object-button";

    // HUD
    public static final String HEALTH_BAR_BG = "hud-health-bar-bg";
    public static final String HEALTH_BAR_FG = "hud-health-bar-fg";

    // Buttons
    public static final String GALLERY_BUTTON = "ui-gallery-button";
    public static final String UPLOAD_BUTTON = "ui-upload-button";
    public static final String DOWNLOAD_BUTTON = "ui-download-button";
    public static final String FRIENDS_BUTTON = "ui-friends-button";
    public static final String SEND_BUTTON = "ui-send-button";
    public static final String OSZ_PARSE_BUTTON = "ui-osz-parse-button";
    public static final String MUSIC_BUTTON = "ui-music-button";
    public static final String SAVE_BUTTON = "ui-save-button";

    // CheckBox
    public static final String CHECKBOX_OFF = "ui-checkbox-off";
    public static final String CHECKBOX_ON = "ui-checkbox-on";

    // Dropdown
    public static final String DROPDOWN_BG = "ui-dropdown-bg";
    public static final String DROPDOWN_LIST_BG = "ui-dropdown-list-bg";
    public static final String DROPDOWN_LIST_SELECTION = "ui-dropdown-list-selection";

    // Text Field
    public static final String TEXT_FIELD_BG = "ui-text-field-bg";
    public static final String TEXT_AREA_BG = "ui-text-area-bg";
    public static final String TEXT_FIELD_CURSOR = "ui-text-field-cursor";

    public static Rectangle calculateViewElementBounds(Alignment alignment, float pad, float x, float y, float width, float height, float viewWidth, float viewHeight) {
        width -= pad;
        height -= pad;
        float
                viewX, viewY,
                viewAspectRatio = viewWidth / viewHeight,
                deltaWidth = width - viewWidth,
                deltaHeight = height - viewHeight;

        if (alignment != null) {
            if (viewWidth > width || deltaHeight >= deltaWidth) {
                viewWidth = width;
                viewHeight = viewWidth / viewAspectRatio;
            } else if (viewHeight > height || deltaHeight < deltaWidth) {
                viewHeight = height;
                viewWidth = viewHeight * viewAspectRatio;
            }

            if (alignment == Alignment.center) {
                viewX = x + (width - viewWidth) / 2.0f;
                viewY = y + (height - viewHeight) / 2.0f;
            } else if (alignment == Alignment.top) {
                viewX = x + (width - viewWidth) / 2.0f;
                viewY = y + height - viewHeight;
            } else if (alignment == Alignment.bottom) {
                viewX = x + (width - viewWidth) / 2.0f;
                viewY = y;
            } else if (alignment == Alignment.left) {
                viewX = x;
                viewY = y + (height - viewHeight) / 2.0f;
            } else if (alignment == Alignment.right) {
                viewX = x + width - viewWidth;
                viewY = y + (height - viewHeight) / 2.0f;
            } else if (alignment == Alignment.topLeft) {
                viewX = x;
                viewY = y + height - viewHeight;
            } else if (alignment == Alignment.topRight) {
                viewX = x + width - viewWidth;
                viewY = y + height - viewHeight;
            } else if (alignment == Alignment.bottomLeft) {
                viewX = x;
                viewY = y;
            } else /* if (alignment == Alignment.bottomRight) */ {
                viewX = x + width - viewWidth;
                viewY = y;
            }
        } else {
            viewWidth = width;
            viewHeight = height;
            viewX = x;
            viewY = y;
        }

        viewX += pad / 2.0f;
        viewY += pad / 2.0f;

        return new Rectangle(viewX, viewY, viewWidth, viewHeight);
    }

    public static void calculateAndSetViewElementBounds(Actor viewElement, Alignment alignment, float pad, float x, float y, float width, float height) {
        Rectangle rect = UI.calculateViewElementBounds(alignment, pad, x, y, width, height, viewElement.getWidth(), viewElement.getHeight());
        viewElement.setBounds(rect.x, rect.y, rect.width, rect.height);
    }

    public static void dispose() {
        ASSET_MANAGER.dispose();
    }
}
