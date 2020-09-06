package com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.beatmapscrollpane;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kindof.catchthebeat.beatmaps.Beatmap;

public class BeatmapScrollPaneItem extends Table {

    public static final float SELECTED_ALPHA = 1f;
    public static final Color SELECTED_BEATMAP_BACKGROUND_IMAGE_COLOR = new Color(0.9f, 0.9f, 0.9f, 1);
    public static final Color SELECTED_BACKGROUND_IMAGE_COLOR = Color.GOLD;
    public static final Color SELECTED_ARTIST_FONT_COLOR = Color.GOLDENROD;
    public static final Color SELECTED_TITLE_FONT_COLOR = Color.WHITE;

    public static final float UNSELECTED_ALPHA = 0.7f;
    public static final Color UNSELECTED_BEATMAP_BACKGROUND_IMAGE_COLOR = new Color(1, 1, 1, 0.5f);
    public static final Color UNSELECTED_BACKGROUND_IMAGE_COLOR = Color.WHITE;
    public static final Color UNSELECTED_ARTIST_FONT_COLOR = Color.LIGHT_GRAY;
    public static final Color UNSELECTED_TITLE_FONT_COLOR = Color.WHITE;

    private Beatmap beatmap;
    private Image beatmapBackgroundImage, backgroundImage;
    private Label labelTitle, labelArtist;

    public BeatmapScrollPaneItem(Beatmap beatmap, Image beatmapBackgroundImage, Image backgroundImage, Label labelTitle, Label labelArtist) {
        super(null);

        this.beatmap = beatmap;
        this.beatmapBackgroundImage = beatmapBackgroundImage;
        this.backgroundImage = backgroundImage;
        this.labelTitle = labelTitle;
        this.labelArtist = labelArtist;
    }

    public Beatmap getBeatmap() {
        return beatmap;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public Image getBeatmapBackgroundImage() {
        return beatmapBackgroundImage;
    }

    public Label getLabelTitle() {
        return labelTitle;
    }

    public Label getLabelArtist() {
        return labelArtist;
    }
}
