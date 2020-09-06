package com.kindof.catchthebeat.ui.styles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kindof.catchthebeat.ui.Font;
import com.kindof.catchthebeat.ui.UI;

public class TextFieldStyle extends TextField.TextFieldStyle {
    public static final float CURSOR_WIDTH = 4;

    public TextFieldStyle(Color fontColor, int fontSize) {
        this(fontColor, fontSize, UI.TEXT_FIELD_BG);
    }

    public TextFieldStyle(Color fontColor, int fontSize, String backgroundName) {
        this.fontColor = fontColor;
        this.font = Font.getBitmapFont(fontSize);
        this.background = new TextureRegionDrawable(UI.SKIN_ATLAS.findRegion(backgroundName));
        this.cursor = new TextureRegionDrawable(UI.SKIN_ATLAS.findRegion(UI.TEXT_FIELD_CURSOR)) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                super.draw(batch, x, y, CURSOR_WIDTH, height);
            }
        };
    }

    public TextFieldStyle(TextFieldStyle style) {
        super(style);
    }
}
