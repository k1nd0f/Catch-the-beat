package com.kindof.catchthebeat.ui.styles;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kindof.catchthebeat.ui.UI;

public class ScrollPaneStyle extends ScrollPane.ScrollPaneStyle {

    public ScrollPaneStyle() {
        this.background = new TextureRegionDrawable(UI.SKIN_ATLAS.findRegion(UI.DROPDOWN_LIST_BG));
    }

    public ScrollPaneStyle(ScrollPaneStyle style) {
        super(style);
    }
}
