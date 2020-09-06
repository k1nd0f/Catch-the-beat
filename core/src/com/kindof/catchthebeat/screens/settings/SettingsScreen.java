package com.kindof.catchthebeat.screens.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kindof.catchthebeat.resources.Settings;
import com.kindof.catchthebeat.tools.Util;
import com.kindof.catchthebeat.ui.UI;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.screens.BaseScreen;
// import com.kindof.catchthebeat.ui.actors.scrollpane.ScrollPane;
import com.kindof.catchthebeat.ui.actors.slidevaluechanger.SlideValueChanger;
import com.kindof.catchthebeat.ui.styles.CheckBoxStyle;
import com.kindof.catchthebeat.ui.styles.LabelStyle;
import com.kindof.catchthebeat.ui.styles.ListStyle;
import com.kindof.catchthebeat.ui.styles.ScrollPaneStyle;
import com.kindof.catchthebeat.ui.styles.SelectBoxStyle;

import java.util.HashMap;

public class SettingsScreen extends BaseScreen {
    // private Table scrollPaneTable;
    private SelectBox<String> chooseSkin, chooseFont;
    private CheckBox fullscreenCheckBox;
    private Label skinLabel, fontLabel;
    private SlideValueChanger musicVolumeChanger, soundVolumeChanger, bgBrightnessChanger;
    // private ArrayList<Table> scrollPaneItems;
    private HashMap<String, String> fontExtensions;
    private float itemWidth, itemHeight, verticalPad;
    private boolean hasChanged;

    public SettingsScreen() {
        super();
    }

    @Override
    public void initialize() {
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    boolean fullscreen = fullscreenCheckBox.isChecked();
                    String
                            skin = chooseSkin.getSelected(),
                            fontName = chooseFont.getSelected(),
                            font = fontName + "." + fontExtensions.get(fontName);

                    hasChanged = (!UI.SKIN_NAME.equals(skin) || !UI.FONT_NAME.equals(font) || Settings.FULLSCREEN != fullscreen);

                    UI.SKIN_NAME = skin;
                    UI.FONT_NAME = font;
                    Settings.FULLSCREEN = fullscreen;
                    Settings.saveLocalConfigFile();
                    Globals.GAME.setScreenWithTransition(Globals.MAIN_MENU_SCREEN);
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setFillParent(true);

        hasChanged = false;
        verticalPad = 10 * Globals.RESOLUTION_HEIGHT_SCALE;

        int maxItemsOnScreen = 8;
        itemWidth = Globals.WIDTH;
        itemHeight = Globals.HEIGHT / (maxItemsOnScreen - 1);

        /*
        scrollPaneItems = new ArrayList<>();
        scrollPaneTable = new Table();
        scrollPaneTable.setTouchable(Touchable.childrenOnly);
        ScrollPane<Table> scrollPane = new ScrollPane<>(scrollPaneTable);
        scrollPane.setVisibleItemHeight(itemHeight);
        scrollPane.setMaxItemsOnScreen(maxItemsOnScreen);
        scrollPane.setItems(scrollPaneItems);
        scrollPane.setBounds(0, 0, Globals.WIDTH, Globals.HEIGHT);
        scrollPane.setCancelTouchFocus(false);
         */

        FileHandle[] fileHandles = Gdx.files.internal(Globals.INTERNAL_PATH_TO_SKINS_DIRECTORY).list();
        Array<String> items = new Array<>(fileHandles.length);
        for (FileHandle fileHandle : fileHandles) {
            items.add(fileHandle.name());
        }

        LabelStyle labelStyle = new LabelStyle(Color.WHITE, 35);
        ListStyle listStyle = new ListStyle(Color.WHITE, Color.GOLDENROD, 25);
        ScrollPaneStyle scrollPaneStyle = new ScrollPaneStyle();
        SelectBoxStyle selectBoxStyle = new SelectBoxStyle(Color.GOLD, 30, listStyle, scrollPaneStyle);

        // Skin SelectBox
        float
                selectBoxW = itemWidth / 2.0f,
                labelW = itemWidth - selectBoxW;

        chooseSkin = new SelectBox<>(selectBoxStyle);
        chooseSkin.setItems(items);
        chooseSkin.setSelected(UI.SKIN_NAME);
        chooseSkin.setAlignment(Align.center);
        chooseSkin.setBounds(0, 0, selectBoxW, itemHeight);

        skinLabel = new Label(" Skin", labelStyle);
        skinLabel.setTouchable(Touchable.disabled);
        skinLabel.setAlignment(Align.left);
        skinLabel.setBounds(selectBoxW, 0, labelW, itemHeight);

        Table skinTable = new Table();
        skinTable.setTouchable(Touchable.enabled);
        addActorsToTable(skinTable, chooseSkin, skinLabel);
        addItemToScrollPane(skinTable);

        // Font SelectBox
        items.clear();
        fileHandles = Gdx.files.internal(Globals.INTERNAL_PATH_TO_FONTS_DIRECTORY).list();
        fontExtensions = new HashMap<>();
        for (FileHandle fileHandle : fileHandles) {
            String nameWithoutExtension = fileHandle.nameWithoutExtension();
            String extension = fileHandle.extension();
            fontExtensions.put(nameWithoutExtension, extension);
            items.add(nameWithoutExtension);
        }

        String selectedFontName = UI.FONT_NAME;
        int dotIndex = selectedFontName.lastIndexOf('.');
        if (selectedFontName.lastIndexOf('.') != -1) {
            selectedFontName = selectedFontName.substring(0, dotIndex);
        }

        chooseFont = new SelectBox<>(selectBoxStyle);
        chooseFont.setItems(items);
        chooseFont.setSelected(selectedFontName);
        chooseFont.setAlignment(Align.center);
        chooseFont.setBounds(0, 0, selectBoxW, itemHeight);

        fontLabel = new Label(" Font", labelStyle);
        fontLabel.setTouchable(Touchable.disabled);
        fontLabel.setAlignment(Align.left);
        fontLabel.setBounds(selectBoxW, 0, labelW, itemHeight);

        Table fontTable = new Table();
        fontTable.setTouchable(Touchable.enabled);
        addActorsToTable(fontTable, chooseFont, fontLabel);
        addItemToScrollPane(fontTable);

        // Fullscreen CheckBox
        float checkBoxW = itemWidth / 2.0f;
        CheckBoxStyle checkBoxStyle = new CheckBoxStyle(Color.GRAY, Color.GOLD, 35, itemHeight);
        fullscreenCheckBox = new CheckBox("Fullscreen", checkBoxStyle);
        Gdx.app.log("FULLSCREEN-INIT", Settings.FULLSCREEN + "");
        fullscreenCheckBox.setChecked(Settings.FULLSCREEN);
        fullscreenCheckBox.align(Align.left);
        fullscreenCheckBox.setBounds(0, 0, checkBoxW, itemHeight);

        Table fullscreenTable = new Table();
        fullscreenTable.setTouchable(Touchable.enabled);
        addActorsToTable(fullscreenTable, fullscreenCheckBox);
        addItemToScrollPane(fullscreenTable);

        // Beatmap BG Brightness SlideValueChanger
        bgBrightnessChanger = new SlideValueChanger(
                itemWidth / 2.0f, itemHeight, 0, 100,
                "Beatmap BG Brightness: ", " %", Align.center, 25,
                () -> Settings.BEATMAP_BG_BRIGHTNESS = Util.round(bgBrightnessChanger.getValue() / 100.0f, 2)
        );
        bgBrightnessChanger.setValue(Settings.BEATMAP_BG_BRIGHTNESS * 100.0f);
        bgBrightnessChanger.setBounds(0, 0, itemWidth / 2.0f, itemHeight);

        Table bgBrightnessChangerTable = new Table();
        bgBrightnessChangerTable.setTouchable(Touchable.enabled);
        addActorsToTable(bgBrightnessChangerTable, bgBrightnessChanger);
        addItemToScrollPane(bgBrightnessChangerTable);

        // Music Volume SlideValueChanger
        musicVolumeChanger = new SlideValueChanger(
                itemWidth / 2.0f, itemHeight, 0, 100,
                "Music Volume: ", " %", Align.center, 25,
                () -> Settings.MUSIC_VOLUME = Util.round(musicVolumeChanger.getValue() / 100.0f, 2)
        );
        musicVolumeChanger.setValue(Settings.MUSIC_VOLUME * 100.0f);
        musicVolumeChanger.setBounds(0, 0, itemWidth / 2.0f, itemHeight);

        Table musicVolumeChangerTable = new Table();
        musicVolumeChangerTable.setTouchable(Touchable.enabled);
        addActorsToTable(musicVolumeChangerTable, musicVolumeChanger);
        addItemToScrollPane(musicVolumeChangerTable);

        // Sound Volume SlideValueChanger
        soundVolumeChanger = new SlideValueChanger(
                itemWidth / 2.0f, itemHeight, 0, 100,
                "Sound Volume: ", " %", Align.center, 25,
                () -> Settings.SOUND_VOLUME = Util.round(soundVolumeChanger.getValue() / 100.0f, 2)
        );
        soundVolumeChanger.setValue(Settings.SOUND_VOLUME * 100.0f);
        soundVolumeChanger.setBounds(0, 0, itemWidth / 2.0f, itemHeight);

        Table soundVolumeChangerTable = new Table();
        soundVolumeChangerTable.setTouchable(Touchable.enabled);
        addActorsToTable(soundVolumeChangerTable, soundVolumeChanger);
        addItemToScrollPane(soundVolumeChangerTable);

        // addActors(scrollPane);
        stage.addActor(rootTable);
    }

    private void addItemToScrollPane(Table item) {
        /*
        scrollPaneTable.add(item).size(itemWidth, itemHeight).pad(verticalPad / 2.0f, 0, verticalPad / 2.0f, 0).row();
        scrollPaneItems.add(item);
         */
        rootTable.add(item).size(itemWidth, itemHeight).pad(verticalPad / 2.0f, 0, verticalPad / 2.0f, 0).row();
    }

    @Override
    public void hide() {
        super.hide();
        if (hasChanged) {
            Globals.GAME.getIntentHandler().triggerRebirth();
        }
    }
}
