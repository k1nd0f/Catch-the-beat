package com.kindof.catchthebeat.screens.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kindof.catchthebeat.beatmaps.Beatmap;
import com.kindof.catchthebeat.gameobjects.catchers.Catcher;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.tools.Font;

public class OnCompletionScreen extends BaseScreen {
    private Label comboLabel, accuracyLabel, scoreLabel, difficultyLabel, healthRateLabel, bpmLabel;
    private Label.LabelStyle comboLabelStyle, accuracyLabelStyle, scoreLabelStyle, difficultyLabelStyle, healthRateLabelStyle, bpmLabelStyle;
    private Image rank, background;
    private Catcher catcher;
    private Beatmap beatmap;
    private Music completionSound;

    public OnCompletionScreen() {
        super();
    }

    @Override
    public void initialize() {
        completionSound = Res.ASSET_MANAGER.get(Res.INTERNAL_PATH_TO_SKINS_DIRECTORY + Res.SKIN_NAME + "/" + Res.COMPLETION_SOUND, Music.class);
        completionSound.setVolume(Res.SOUND_VOLUME);

        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Res.GAME_SCREEN.dispose();
                    Res.GAME_SCREEN = null;
                    Res.GAME.setScreen(Res.BEATMAP_SELECTION_MENU_SCREEN);
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table() {
            @Override
            protected void drawBackground(Batch batch, float parentAlpha, float x, float y) {
                super.drawBackground(batch, 0.3f, x, y);
            }
        };
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setFillParent(true);
        rootTable.setVisible(false);

        background = new Image(Res.SKIN_ATLAS.findRegion(Res.UI_RESULT_TAB_BACKGROUND));
        background.setBounds(0, 0, Res.WIDTH, Res.HEIGHT);

        comboLabelStyle = new Label.LabelStyle();
        comboLabelStyle.background = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.UI_RESULT_TAB_COMBO_BACKGROUND));
        comboLabelStyle.fontColor = Color.WHITE;
        comboLabelStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (30 * Res.RESOLUTION_HEIGHT_SCALE));
        comboLabel = new Label("", comboLabelStyle); // catcher.getMaxCombo() + " / " + beatmap.getFruitCount() + "x"
        comboLabel.setTouchable(Touchable.disabled);
        comboLabel.setAlignment(Align.center);
        comboLabel.setBounds(0, Res.HEIGHT / 4.0f + Res.HEIGHT / 8.0f, Res.WIDTH / 2.0f, Res.HEIGHT / 8.0f);

        accuracyLabelStyle = new Label.LabelStyle();
        accuracyLabelStyle.background = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.UI_RESULT_TAB_ACCURACY_BACKGROUND));
        accuracyLabelStyle.fontColor = Color.WHITE;
        accuracyLabelStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (20 * Res.RESOLUTION_HEIGHT_SCALE));
        accuracyLabel = new Label("", accuracyLabelStyle); // MathUtils.round(acc * 100) / 100.0 + " / 100.0%"
        accuracyLabel.setTouchable(Touchable.disabled);
        accuracyLabel.setAlignment(Align.right);
        accuracyLabel.setBounds(0, Res.HEIGHT / 4.0f, Res.WIDTH / 2.0f, Res.HEIGHT / 8.0f);

        scoreLabelStyle = new Label.LabelStyle();
        scoreLabelStyle.background = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.UI_RESULT_TAB_SCORE_BACKGROUND));
        scoreLabelStyle.fontColor = Color.GOLDENROD;
        scoreLabelStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (70 * Res.RESOLUTION_HEIGHT_SCALE));
        scoreLabel = new Label("", scoreLabelStyle); // catcher.getScore() + ""
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setBounds(0, 0, Res.WIDTH / 2.0f, Res.HEIGHT / 4.0f);

        float pad = (Res.HEIGHT - Res.HEIGHT / 5.0f * 3.0f) / 3.0f;
        difficultyLabelStyle = new Label.LabelStyle();
        difficultyLabelStyle.background = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.UI_RESULT_TAB_DIFFICULTY_BACKGROUND));
        difficultyLabelStyle.fontColor = Color.WHITE;
        difficultyLabelStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (25 * Res.RESOLUTION_HEIGHT_SCALE));
        difficultyLabel = new Label("", difficultyLabelStyle);
        difficultyLabel.setAlignment(Align.center);
        difficultyLabel.setBounds(Res.WIDTH / 2.0f, Res.HEIGHT - pad / 2.0f - Res.HEIGHT / 5.0f, Res.WIDTH / 2.0f, Res.HEIGHT / 5.0f);

        healthRateLabelStyle = new Label.LabelStyle();
        healthRateLabelStyle.background = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.UI_RESULT_TAB_HEALTH_RATE_BACKGROUND));
        healthRateLabelStyle.fontColor = Color.WHITE;
        healthRateLabelStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (25 * Res.RESOLUTION_HEIGHT_SCALE));
        healthRateLabel = new Label("", healthRateLabelStyle);
        healthRateLabel.setAlignment(Align.center);
        healthRateLabel.setBounds(Res.WIDTH / 2.0f, Res.HEIGHT - pad * 1.5f - Res.HEIGHT / 5.0f * 2.0f, Res.WIDTH / 2.0f, Res.HEIGHT / 5.0f);

        bpmLabelStyle = new Label.LabelStyle();
        bpmLabelStyle.background = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.UI_RESULT_TAB_BPM_BACKGROUND));
        bpmLabelStyle.fontColor = Color.WHITE;
        bpmLabelStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (25 * Res.RESOLUTION_HEIGHT_SCALE));
        bpmLabel = new Label("", bpmLabelStyle);
        bpmLabel.setAlignment(Align.center);
        bpmLabel.setBounds(Res.WIDTH / 2.0f, Res.HEIGHT - pad * 2.5f - Res.HEIGHT / 5.0f * 3.0f, Res.WIDTH / 2.0f, Res.HEIGHT / 5.0f);

        addActors(background, comboLabel, accuracyLabel, scoreLabel, difficultyLabel, healthRateLabel, bpmLabel);
        stage.addActor(rootTable);
    }

    public void setCatcher(Catcher catcher) {
        this.catcher = catcher;
    }

    public void setBeatmap(Beatmap beatmap) {
        this.beatmap = beatmap;
        rootTable.setBackground(new TextureRegionDrawable(beatmap.getBackground().getTexture()));
    }

    @Override
    public void show() {
        super.show();
        completionSound.play();
        rootTable.removeActor(this.rank);

        float acc = catcher.getAccuracy();
        String rank = "";
        if (acc >= 0 && acc < 70) {
            rank = Res.RANK_D;
        } else if (acc >= 70 && acc < 80) {
            rank = Res.RANK_C;
        } else if (acc >= 80 && acc < 90) {
            rank = Res.RANK_B;
        } else if (acc >= 90 && acc < 95) {
            rank = Res.RANK_A;
        } else if (acc >= 95 && acc < 100) {
            rank = Res.RANK_S;
        } else if (acc >= 100) {
            rank = Res.RANK_SS;
        }

        TextureRegion rankRegion = Res.SKIN_ATLAS.findRegion(rank);
        float k = rankRegion.getRegionWidth() * 1.0f / rankRegion.getRegionHeight();
        float height = Res.HEIGHT / 2.0f, width = height * k, x = (Res.WIDTH / 2.0f - width) / 2.0f;
        this.rank = new Image(rankRegion);
        this.rank.setBounds(x, height, width, height);

        comboLabel.setText(catcher.getMaxCombo() + " / " + beatmap.getFruitCount() + "x");
        accuracyLabel.setText((((int) (acc * 100.0f)) / 100.0f) + " / 100.0%");
        scoreLabel.setText(catcher.getScore() + "");
        difficultyLabel.setText("Difficulty : " + (((int) (beatmap.getDifficulty() * 10.0f)) / 10.0f));
        healthRateLabel.setText("HealthRate : " + (((int) (beatmap.getHealthRate() * 10.0f)) / 10.0f));
        bpmLabel.setText("BPM : " + (((int) (beatmap.getBPM() * 10.0f)) / 10.0f));
        rootTable.addActor(this.rank);
        rootTable.setVisible(true);
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
