package com.kindof.catchthebeat.screens.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kindof.catchthebeat.resources.Settings;
import com.kindof.catchthebeat.beatmaps.Beatmap;
import com.kindof.catchthebeat.gameobjects.catcher.Catcher;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.tools.Util;
import com.kindof.catchthebeat.ui.Alignment;
import com.kindof.catchthebeat.ui.UI;
import com.kindof.catchthebeat.ui.styles.LabelStyle;

public class OnCompletionScreen extends BaseScreen {
    private Label comboLabel, accuracyLabel, scoreLabel, difficultyLabel, healthRateLabel, bpmLabel;
    private Image rankImage;
    private Catcher catcher;
    private Beatmap beatmap;
    private Music completionSound;

    public OnCompletionScreen() {
        super();
    }

    @Override
    public void initialize() {
        completionSound = UI.ASSET_MANAGER.get(Globals.INTERNAL_PATH_TO_SKINS_DIRECTORY + UI.SKIN_NAME + "/" + Globals.COMPLETION_SOUND, Music.class);
        completionSound.setVolume(Settings.SOUND_VOLUME);

        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Globals.GAME_SCREEN.dispose();
                    Globals.GAME_SCREEN = null;
                    Globals.GAME.setScreenWithTransition(Globals.BEATMAP_SELECTION_MENU_SCREEN);
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setFillParent(true);
        rootTable.setVisible(false);

        LabelStyle comboLabelStyle = new LabelStyle(Color.WHITE, 30);
        comboLabel = new Label("", comboLabelStyle);
        comboLabel.setTouchable(Touchable.disabled);
        comboLabel.setAlignment(Align.center);
        comboLabel.setBounds(0, Globals.HEIGHT / 4.0f + Globals.HEIGHT / 8.0f, Globals.WIDTH / 2.0f, Globals.HEIGHT / 8.0f);

        LabelStyle accuracyLabelStyle = new LabelStyle(Color.WHITE, 20);
        accuracyLabel = new Label("", accuracyLabelStyle);
        accuracyLabel.setTouchable(Touchable.disabled);
        accuracyLabel.setAlignment(Align.right);
        accuracyLabel.setBounds(0, Globals.HEIGHT / 4.0f, Globals.WIDTH / 2.0f, Globals.HEIGHT / 8.0f);

        LabelStyle scoreLabelStyle = new LabelStyle(Color.GOLDENROD, 70);
        scoreLabel = new Label("", scoreLabelStyle);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setBounds(0, 0, Globals.WIDTH / 2.0f, Globals.HEIGHT / 4.0f);

        float pad = (Globals.HEIGHT - Globals.HEIGHT / 5.0f * 3.0f) / 3.0f;
        LabelStyle difficultyLabelStyle = new LabelStyle(Color.WHITE, 25, UI.RESULT_STATS_BG);
        difficultyLabel = new Label("", difficultyLabelStyle);
        difficultyLabel.setAlignment(Align.center);
        difficultyLabel.setBounds(Globals.WIDTH / 2.0f, Globals.HEIGHT - pad / 2.0f - Globals.HEIGHT / 5.0f, Globals.WIDTH / 2.0f, Globals.HEIGHT / 5.0f);

        LabelStyle healthRateLabelStyle = new LabelStyle(Color.WHITE, 25, UI.RESULT_STATS_BG);
        healthRateLabel = new Label("", healthRateLabelStyle);
        healthRateLabel.setAlignment(Align.center);
        healthRateLabel.setBounds(Globals.WIDTH / 2.0f, Globals.HEIGHT - pad * 1.5f - Globals.HEIGHT / 5.0f * 2.0f, Globals.WIDTH / 2.0f, Globals.HEIGHT / 5.0f);

        LabelStyle bpmLabelStyle = new LabelStyle(Color.WHITE, 25, UI.RESULT_STATS_BG);
        bpmLabel = new Label("", bpmLabelStyle);
        bpmLabel.setAlignment(Align.center);
        bpmLabel.setBounds(Globals.WIDTH / 2.0f, Globals.HEIGHT - pad * 2.5f - Globals.HEIGHT / 5.0f * 3.0f, Globals.WIDTH / 2.0f, Globals.HEIGHT / 5.0f);

        addActors(/*background,*/ comboLabel, accuracyLabel, scoreLabel, difficultyLabel, healthRateLabel, bpmLabel);
        stage.addActor(rootTable);
    }

    public void setCatcher(Catcher catcher) {
        this.catcher = catcher;
    }

    public void setBeatmap(Beatmap beatmap) {
        this.beatmap = beatmap;
        TextureRegionDrawable bg = new TextureRegionDrawable(beatmap.getBackground().getTexture());
        rootTable.setBackground(bg.tint(new Color(0.2f, 0.2f, 0.2f, 1)));
    }

    private void reInitResult() {
        float acc = catcher.getAccuracy();
        String rank = "";
        if (acc >= 0 && acc < 70) {
            rank = UI.RANK_D;
        } else if (acc >= 70 && acc < 80) {
            rank = UI.RANK_C;
        } else if (acc >= 80 && acc < 90) {
            rank = UI.RANK_B;
        } else if (acc >= 90 && acc < 95) {
            rank = UI.RANK_A;
        } else if (acc >= 95 && acc < 100) {
            rank = UI.RANK_S;
        } else if (acc >= 100) {
            rank = UI.RANK_SS;
        }
        initRankImage(rank);

        comboLabel.setText(catcher.getMaxCombo() + " / " + beatmap.getFruitCount() + "x");
        accuracyLabel.setText(Util.round(acc, 2) + " / 100.0 %");
        scoreLabel.setText(catcher.getScore() + "");
        difficultyLabel.setText("Difficulty : " + Util.round(beatmap.getDifficulty(), 1));
        healthRateLabel.setText("HealthRate : " + Util.round(beatmap.getHealthRate(), 1));
        bpmLabel.setText("BPM : " + Util.round(beatmap.getBPM(), 1));
    }

    private void initRankImage(String rank) {
        if (rankImage != null) {
            rootTable.removeActor(rankImage);
        }

        float
                rankImageW = Globals.WIDTH / 2.0f,
                rankImageH = Globals.HEIGHT / 2.0f;

        rankImage = new Image(new TextureRegionDrawable(UI.SKIN_ATLAS.findRegion(rank)));
        UI.calculateAndSetViewElementBounds(
                rankImage,
                Alignment.center, 0,
                0, Globals.HEIGHT - rankImageH, rankImageW, rankImageH
        );
        rootTable.addActor(rankImage);
    }

    @Override
    public void show() {
        super.show();
        completionSound.play();
        reInitResult();
    }

    @Override
    public void hide() {
        super.hide();
        completionSound.stop();
    }

    @Override
    public void dispose() {
        super.dispose();
        completionSound.dispose();
    }
}
