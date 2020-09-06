package com.kindof.catchthebeat.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.kindof.catchthebeat.resources.Res;

public class Font {
    public static BitmapFont getBitmapFont(String fontName, int size) {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.local("Fonts/" + fontName));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.characters = Res.FONT_CHARACTERS;
        fontParameter.size = size;
        fontParameter.color = Color.WHITE;

        BitmapFont font = fontGenerator.generateFont(fontParameter);
        font.setOwnsTexture(true);

        fontGenerator.dispose();
        return font;
    }
}
