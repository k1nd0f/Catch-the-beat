package com.kindof.catchthebeat.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.kindof.catchthebeat.ui.UI;

import java.io.IOException;
import java.io.StringWriter;

public class Settings {

    // Initialized with default values
    public static float MUSIC_VOLUME = 0.25f;
    public static float SOUND_VOLUME = 0.1f;
    public static float BEATMAP_BG_BRIGHTNESS = 0.35f;
    public static boolean FULLSCREEN = true;

    private static final String USER_CONFIG_NAME = "config.xml";
    private static final StringWriter STRING_WRITER = new StringWriter();
    private static final XmlWriter XML_WRITER = new XmlWriter(STRING_WRITER);

    static {
        if (!Gdx.files.local(USER_CONFIG_NAME).exists()) {
            Globals.createLocalFile(USER_CONFIG_NAME);
            saveLocalConfigFile();
        }
        initSettingsFromLocalConfig();
    }

    public static void saveLocalConfigFile() {
        try {
            XML_WRITER.element("Config")
                    .element("Skin").text(UI.SKIN_NAME).pop()
                    .element("Font").text(UI.FONT_NAME).pop()
                    .element("BeatmapBackgroundBrightness").text(Settings.BEATMAP_BG_BRIGHTNESS).pop()
                    .element("Fullscreen").text(Settings.FULLSCREEN).pop()
                    .element("MusicVolume").text(Settings.MUSIC_VOLUME).pop()
                    .element("SoundVolume").text(Settings.SOUND_VOLUME).pop();
            XML_WRITER.close();
            Gdx.files.local(USER_CONFIG_NAME).writeString(STRING_WRITER.toString(), false);
            STRING_WRITER.getBuffer().setLength(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initSettingsFromLocalConfig() {
        XmlReader xmlReader = new XmlReader();
        XmlReader.Element element = xmlReader.parse(Gdx.files.local("config.xml"));
        UI.SKIN_NAME = element.get("Skin");
        UI.FONT_NAME = element.get("Font");
        Settings.FULLSCREEN = element.getBoolean("Fullscreen");
        Settings.BEATMAP_BG_BRIGHTNESS = element.getFloat("BeatmapBackgroundBrightness");
        Settings.MUSIC_VOLUME = element.getFloat("MusicVolume");
        Settings.SOUND_VOLUME = element.getFloat("SoundVolume");
    }
}
