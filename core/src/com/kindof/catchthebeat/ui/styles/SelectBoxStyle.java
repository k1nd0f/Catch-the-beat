package com.kindof.catchthebeat.ui.styles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kindof.catchthebeat.ui.Font;
import com.kindof.catchthebeat.ui.UI;

public class SelectBoxStyle extends SelectBox.SelectBoxStyle {

    public SelectBoxStyle(Color fontColor, int fontSize, ListStyle listStyle, ScrollPaneStyle scrollPaneStyle) {
        this.font = Font.getBitmapFont(fontSize);
        this.fontColor = fontColor;
        this.listStyle = listStyle;
        this.scrollStyle = scrollPaneStyle;
        this.background = new TextureRegionDrawable(UI.SKIN_ATLAS.findRegion(UI.DROPDOWN_BG));
    }

    public SelectBoxStyle(SelectBoxStyle style) {
        super(style);
    }
}
