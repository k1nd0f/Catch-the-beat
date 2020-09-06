package com.kindof.catchthebeat.screens.beatmapselectionmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.tools.Font;

public class StatisticScreen extends BaseScreen {
    private Image userIcon, userIconBackground;
    private Label avgAccLabel, avgDifLabel, playCountLabel, totalScoreLabel, firstPlaces;
    private float iconSize, iconPad, iconX, iconY, absoluteIconPad, absoluteIconSize;

    public StatisticScreen() {
        super();
    }

    @Override
    public void initialize() {
        absoluteIconSize = Res.HEIGHT / 2.0f;
        iconPad = 7 * Res.RESOLUTION_HEIGHT_SCALE;
        absoluteIconPad = 25 * Res.RESOLUTION_HEIGHT_SCALE;
        iconX = absoluteIconPad;
        iconY = Res.HEIGHT - absoluteIconSize + absoluteIconPad;
        iconSize = absoluteIconSize - absoluteIconPad * 2.0f;

        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Res.GAME.setScreen(Res.BEATMAP_SELECTION_MENU_SCREEN);
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setVisible(false);
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setFillParent(true);
        rootTable.setBackground(new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.UI_STATISTICS_SCREEN_BACKGROUND)));

        initLabels();
        initUserIconBackground();

        rootTable.addActor(avgAccLabel);
        rootTable.addActor(avgDifLabel);
        rootTable.addActor(playCountLabel);
        rootTable.addActor(totalScoreLabel);
        stage.addActor(rootTable);
    }

    private void initLabels() {
        float leftRightPad = 5 * Res.RESOLUTION_WIDTH_SCALE, width = Res.WIDTH - absoluteIconSize - 2.0f * leftRightPad, height = Res.HEIGHT / 10.0f, x = absoluteIconSize, topPad = (Res.HEIGHT / 2.0f - 4.0f * height) / 2.0f;
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (30 * Res.RESOLUTION_HEIGHT_SCALE));
        labelStyle.fontColor = Color.WHITE;

        avgAccLabel = new Label("", labelStyle);
        avgAccLabel.setAlignment(Align.left);
        avgAccLabel.setBounds(x + leftRightPad, Res.HEIGHT - height - topPad, width, height);

        avgDifLabel = new Label("", labelStyle);
        avgDifLabel.setAlignment(Align.left);
        avgDifLabel.setBounds(x + leftRightPad, Res.HEIGHT - height * 2.0f - topPad, width, height);

        playCountLabel = new Label("", labelStyle);
        playCountLabel.setAlignment(Align.left);
        playCountLabel.setBounds(x + leftRightPad, Res.HEIGHT - height * 3.0f - topPad, width, height);

        totalScoreLabel = new Label("", labelStyle);
        totalScoreLabel.setAlignment(Align.left);
        totalScoreLabel.setBounds(x + leftRightPad, Res.HEIGHT - height * 4.0f - topPad, width, height);
    }

    public void reInitLabels() {
        avgAccLabel.setText("# Avg. accuracy : " + (((int) (Res.USER.getAvgAccuracy() * 100.0f)) / 100f) + "%");
        avgDifLabel.setText("# Avg. difficulty : " + (((int) (Res.USER.getAvgDifficulty() * 100.0f)) / 100f));
        playCountLabel.setText("# Play count : " + Res.USER.getPlayCount());
        totalScoreLabel.setText("# Total score : " + Res.USER.getTotalScore());
    }

    public void initUserIcon(Image icon) {
        if (userIcon != null)
            rootTable.removeActor(userIcon);

        userIcon = icon;
        userIcon.setBounds(iconX + iconPad, iconY + iconPad, iconSize - iconPad * 2.0f, iconSize - iconPad * 2.0f);
        rootTable.addActor(userIcon);
    }

    private void initUserIconBackground() {
        userIconBackground = new Image(Res.SKIN_ATLAS.findRegion(Res.USER_ICON_BACKGROUND));
        userIconBackground.setBounds(iconX, iconY, iconSize, iconSize);
        rootTable.addActor(userIconBackground);
    }
}
