package com.kindof.catchthebeat.ui.styles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kindof.catchthebeat.ui.Font;
import com.kindof.catchthebeat.ui.UI;

public class CheckBoxStyle extends CheckBox.CheckBoxStyle {

    public CheckBoxStyle(Color fontColor, Color checkedFontColor, int fontSize, TextureRegionDrawable checkBoxOn, TextureRegionDrawable checkBoxOff, float itemHeight) {
        this.fontColor = fontColor;
        this.checkedFontColor = checkedFontColor;
        this.font = Font.getBitmapFont(fontSize);
        this.checkboxOn = new cbTextureRegionDrawable(checkBoxOn, itemHeight);
        this.checkboxOff = new cbTextureRegionDrawable(checkBoxOff, itemHeight);
    }

    public CheckBoxStyle(Color fontColor, Color checkedFontColor, int fontSize, String checkBoxOn, String checkBoxOff, float itemHeight) {
        this(
                fontColor,
                checkedFontColor,
                fontSize,
                new TextureRegionDrawable(UI.SKIN_ATLAS.findRegion(checkBoxOn)),
                new TextureRegionDrawable(UI.SKIN_ATLAS.findRegion(checkBoxOff)),
                itemHeight
        );
    }

    public CheckBoxStyle(Color fontColor, Color checkedFontColor, int fontSize, float itemHeight) {
        this(
                fontColor,
                checkedFontColor,
                fontSize,
                UI.CHECKBOX_ON,
                UI.CHECKBOX_OFF,
                itemHeight
        );
    }

    public CheckBoxStyle(CheckBoxStyle style) {
        super(style);
    }

    public CheckBoxStyle(CheckBox.CheckBoxStyle style) {
        super(style);
    }

    private static Rectangle calculateCoordinates(float itemHeight, float x, float y, float width, float height) {
        Rectangle rect = new Rectangle();
        rect.width = rect.height = itemHeight;
        rect.x = x;
        rect.y = y - (itemHeight - height) / 2.0f;
        return rect;
    }

    // cb - CheckBox
    private class cbTextureRegionDrawable extends TextureRegionDrawable {
        private float itemHeight;

        public cbTextureRegionDrawable(float itemHeight) {
            this.itemHeight = itemHeight;
        }

        public cbTextureRegionDrawable(Texture texture, float itemHeight) {
            super(texture);
            this.itemHeight = itemHeight;
        }

        public cbTextureRegionDrawable(TextureRegion region, float itemHeight) {
            super(region);
            this.itemHeight = itemHeight;
        }

        public cbTextureRegionDrawable(TextureRegionDrawable drawable, float itemHeight) {
            super(drawable);
            this.itemHeight = itemHeight;
        }

        @Override
        public void draw(Batch batch, float x, float y, float width, float height) {
            Rectangle rect = calculateCoordinates(itemHeight, x, y, width, height);
            super.draw(batch, rect.x, rect.y, rect.width, rect.height);
        }
    }
}
