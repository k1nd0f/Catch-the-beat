package com.kindof.catchthebeat.ui.styles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kindof.catchthebeat.ui.Font;
import com.kindof.catchthebeat.ui.UI;

public class ListStyle extends List.ListStyle {

    public ListStyle(Color fontColorUnselected, Color fontColorSelected, int fontSize) {
        this.font = Font.getBitmapFont(fontSize);
        this.fontColorUnselected = fontColorUnselected;
        this.fontColorSelected = fontColorSelected;
        this.selection = new TextureRegionDrawable(UI.SKIN_ATLAS.findRegion(UI.DROPDOWN_LIST_SELECTION));
    }

    public ListStyle(ListStyle style) {
        super(style);
    }
}
