package com.kindof.catchthebeat.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.kindof.catchthebeat.resources.Globals;

public class Font {
    private static final Color DEFAULT_COLOR = Color.WHITE;

    public static BitmapFont getBitmapFont(String fontName, Color fontColor, int fontSize) {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/" + fontName));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.characters = UI.FONT_CHARS;
        fontParameter.size = (int) (fontSize * Globals.RESOLUTION_HEIGHT_SCALE);
        fontParameter.color = fontColor;

        BitmapFont font = fontGenerator.generateFont(fontParameter);
        font.setOwnsTexture(true);

        fontGenerator.dispose();
        return font;
    }

    public static BitmapFont getBitmapFont(Color fontColor, int fontSize) {
        return getBitmapFont(UI.FONT_NAME, fontColor, fontSize);
    }

    public static BitmapFont getBitmapFont(int fontSize) {
        return getBitmapFont(DEFAULT_COLOR, fontSize);
    }
}
