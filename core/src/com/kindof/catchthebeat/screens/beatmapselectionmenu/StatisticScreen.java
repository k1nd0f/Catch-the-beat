package com.kindof.catchthebeat.screens.beatmapselectionmenu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.tools.Util;
import com.kindof.catchthebeat.ui.Alignment;
import com.kindof.catchthebeat.ui.UI;
import com.kindof.catchthebeat.ui.styles.LabelStyle;

public class StatisticScreen extends BaseScreen {
    private Image userIcon, userIconBackground;
    private Label avgAccLabel, avgDifLabel, playCountLabel, totalScoreLabel;
    private float iconX, iconY, iconSize, iconPad, iconBgPad;

    public StatisticScreen() {
        super();
    }

    @Override
    public void initialize() {
        iconSize = Globals.HEIGHT / 2.0f;
        iconX = 0;
        iconY = Globals.HEIGHT - iconSize;
        iconPad = 25 * Globals.RESOLUTION_HEIGHT_SCALE;
        iconBgPad = 7 * Globals.RESOLUTION_HEIGHT_SCALE;

        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Globals.GAME.setScreenWithTransition(Globals.BEATMAP_SELECTION_MENU_SCREEN);
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setVisible(false);
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setFillParent(true);
        // "UI_STATISTICS_SCREEN_BACKGROUND" doesnt exist in shadeofpink skin
        // rootTable.setBackground(new TextureRegionDrawable(UI.SKIN_ATLAS.findRegion(Globals.UI_STATISTICS_SCREEN_BACKGROUND)));

        initLabels();
        initUserIconBackground();
        addActors(
                avgAccLabel,
                avgDifLabel,
                playCountLabel,
                totalScoreLabel
        );
        stage.addActor(rootTable);
    }

    private void initLabels() {
        float
                leftRightPad = 5 * Globals.RESOLUTION_WIDTH_SCALE,
                labelW = Globals.WIDTH - iconSize - 2.0f * leftRightPad,
                labelH = Globals.HEIGHT / 10.0f,
                topPad = (Globals.HEIGHT / 2.0f - 4.0f * labelH) / 2.0f,
                labelX = iconSize + leftRightPad;

        LabelStyle labelStyle = new LabelStyle(Color.WHITE, 30);

        avgAccLabel = new Label("", labelStyle);
        avgAccLabel.setAlignment(Align.left);
        avgAccLabel.setBounds(labelX, Globals.HEIGHT - labelH - topPad, labelW, labelH);

        avgDifLabel = new Label("", labelStyle);
        avgDifLabel.setAlignment(Align.left);
        avgDifLabel.setBounds(labelX, Globals.HEIGHT - labelH * 2.0f - topPad, labelW, labelH);

        playCountLabel = new Label("", labelStyle);
        playCountLabel.setAlignment(Align.left);
        playCountLabel.setBounds(labelX, Globals.HEIGHT - labelH * 3.0f - topPad, labelW, labelH);

        totalScoreLabel = new Label("", labelStyle);
        totalScoreLabel.setAlignment(Align.left);
        totalScoreLabel.setBounds(labelX, Globals.HEIGHT - labelH * 4.0f - topPad, labelW, labelH);
    }

    public void reInitLabels() {
        avgAccLabel.setText("# Avg. accuracy : " + Util.round(Globals.USER.getAvgAccuracy(), 2) + " %");
        avgDifLabel.setText("# Avg. difficulty : " + Util.round(Globals.USER.getAvgDifficulty(), 2));
        playCountLabel.setText("# Play count : " + Globals.USER.getPlayCount());
        totalScoreLabel.setText("# Total score : " + Globals.USER.getTotalScore());
    }

    public void initUserIcon(Image icon) {
        if (userIcon != null) {
            rootTable.removeActor(userIcon);
        }

        userIcon = icon;
        UI.calculateAndSetViewElementBounds(
                userIcon,
                null, iconPad + iconBgPad,
                iconX, iconY, iconSize, iconSize
        );
        rootTable.addActor(userIcon);
    }

    private void initUserIconBackground() {
        userIconBackground = new Image(UI.SKIN_ATLAS.findRegion(UI.USER_ICON_BG));
        UI.calculateAndSetViewElementBounds(
                userIconBackground,
                null, iconPad,
                iconX, iconY, iconSize, iconSize
        );
        rootTable.addActor(userIconBackground);
    }
}
