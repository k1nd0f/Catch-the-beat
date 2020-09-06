package com.kindof.catchthebeat.ui.styles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kindof.catchthebeat.ui.Font;
import com.kindof.catchthebeat.ui.UI;

public class LabelStyle extends Label.LabelStyle {
    public LabelStyle(Color fontColor, int fontSize) {
        this.fontColor = fontColor;
        this.font = Font.getBitmapFont(fontSize);
    }

    public LabelStyle(Color fontColor, int fontSize, TextureRegionDrawable background) {
        this(fontColor, fontSize);
        this.background = background;
    }

    public LabelStyle(Color fontColor, int fontSize, String background) {
        this(fontColor, fontSize, new TextureRegionDrawable(UI.SKIN_ATLAS.findRegion(background)));
    }

    public LabelStyle(LabelStyle style) {
        super(style);
    }
}
