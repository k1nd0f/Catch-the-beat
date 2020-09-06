package com.kindof.catchthebeat.screens.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kindof.catchthebeat.IIntentHandler;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.screens.beatmapeditor.settings.Velocity;
import com.kindof.catchthebeat.ui.actors.buttons.Button;
import com.kindof.catchthebeat.ui.actors.buttons.TouchHoldEventListener;
import com.kindof.catchthebeat.ui.actors.buttons.TouchUpEventListener;
import com.kindof.catchthebeat.tools.Font;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingsScreen extends BaseScreen {
    private Table scrollPaneTable;
    private SelectBox<String> chooseSkin, chooseFont;
    private CheckBox fullscreenCheckBox;
    private Label skinLabel, fontLabel, bbbLabel, mvLabel, svLabel;
    private com.kindof.catchthebeat.ui.actors.scrollpanes.ScrollPane<Table> scrollPane;
    private ArrayList<Table> scrollPaneItems;
    private float itemWidth, itemHeight, itemPad;
    private int MAX_ITEMS_ON_SCREEN;
    private Velocity mvVel, svVel, bbbVel;
    private HashMap<String, String> fontExtensions;
    private boolean hasChanged;
    private IIntentHandler intentHandler;

    /*
     *  mvVel = music-volume-velocity                       [1]
     *  svVel = sound-volume-velocity                       [2]
     *  bbbVel = beatmap-background-brightness-velocity     [3]
     *
     *  bbbLabel = beatmap-background-brightness-label      [x]
     *
     ************************************************************/

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

                    hasChanged = (!Res.SKIN_NAME.equals(skin) || !Res.FONT_NAME.equals(font) || Res.FULLSCREEN != fullscreen);

                    Res.SKIN_NAME = skin;
                    Res.FONT_NAME = font;
                    Res.FULLSCREEN = fullscreen;
                    Res.saveLocalConfigFile();
                    Res.GAME.setScreen(Res.MAIN_MENU_SCREEN);
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setFillParent(true);

        intentHandler = Res.GAME.getIntentHandler();
        hasChanged = false;

        MAX_ITEMS_ON_SCREEN = 6;
        itemWidth = Res.WIDTH;
        itemHeight = Res.HEIGHT / (MAX_ITEMS_ON_SCREEN - 1);
        itemPad = 0;

        scrollPaneItems = new ArrayList<>(6);
        scrollPaneTable = new Table();
        scrollPaneTable.setTouchable(Touchable.childrenOnly);

        scrollPane = new com.kindof.catchthebeat.ui.actors.scrollpanes.ScrollPane<>(scrollPaneTable);
        scrollPane.setVisibleItemHeight(itemHeight + itemPad * 2.0f);
        scrollPane.setMAX_ITEMS_ON_SCREEN(5);
        scrollPane.setItems(scrollPaneItems);
        scrollPane.setBounds(0, 0, Res.WIDTH, Res.HEIGHT);

        FileHandle[] fileHandles = Gdx.files.local(Res.LOCAL_PATH_TO_SKINS_DIRECTORY).list();
        Array<String> items = new Array<>(fileHandles.length);
        for (FileHandle fileHandle : fileHandles) {
            items.add(fileHandle.name());
        }

        mvVel = new Velocity();
        svVel = new Velocity();
        bbbVel = new Velocity();

        mvVel.accelerate = 0.00005f;
        svVel.accelerate = 0.00005f;
        bbbVel.accelerate = 0.00005f;

        Button
                plusButton,
                minusButton;

        float

                plusMinusButtonPad = 5 * Res.RESOLUTION_HEIGHT_SCALE,
                plusButtonWidth = itemHeight - plusMinusButtonPad * 2.0f,
                plusButtonHeight = itemHeight - plusMinusButtonPad * 2.0f,
                plusButtonX = (itemWidth / 8.0f - itemHeight) / 2.0f + 5 * itemWidth / 8.0f + plusMinusButtonPad,
                plusButtonY = plusMinusButtonPad,
                minusButtonWidth = itemHeight - plusMinusButtonPad * 2.0f,
                minusButtonHeight = itemHeight - plusMinusButtonPad * 2.0f,
                minusButtonX = (itemWidth / 8.0f - itemHeight) / 2.0f + 6 * itemWidth / 8.0f + plusMinusButtonPad,
                minusButtonY = plusMinusButtonPad;

        Table
                skinTable = new Table(),
                fontTable = new Table(),
                fullscreenTable = new Table(),
                bbbTable = new Table(),
                mvTable = new Table(),
                svTable = new Table();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.WHITE;
        labelStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (35 * Res.RESOLUTION_HEIGHT_SCALE));

        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (25 * Res.RESOLUTION_HEIGHT_SCALE));
        listStyle.fontColorUnselected = Color.WHITE;
        listStyle.fontColorSelected = Color.GOLDENROD;
        listStyle.selection = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.DROPDOWN_LIST_SELECTION));

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.DROPDOWN_LIST_BACKGROUND));

        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
        selectBoxStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (30 * Res.RESOLUTION_HEIGHT_SCALE));
        selectBoxStyle.fontColor = Color.GOLD;
        selectBoxStyle.background = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.DROPDOWN_BACKGROUND));
        selectBoxStyle.listStyle = listStyle;
        selectBoxStyle.scrollStyle = scrollPaneStyle;

        // Skin-SelectBox
        chooseSkin = new SelectBox<>(selectBoxStyle);
        chooseSkin.setItems(items);
        chooseSkin.setSelected(Res.SKIN_NAME);
        chooseSkin.setBounds(0, 0, itemWidth / 2.0f, itemHeight);
        chooseSkin.setAlignment(Align.center);

        skinLabel = new Label(" Skin", labelStyle);
        skinLabel.setTouchable(Touchable.disabled);
        skinLabel.setAlignment(Align.left);
        skinLabel.setBounds(itemWidth / 2.0f, 0, itemWidth / 2.0f, itemHeight);

        skinTable.setTouchable(Touchable.enabled);
        addActorsToTable(skinTable, chooseSkin, skinLabel);
        addItemToScrollPane(skinTable);

        // Font-SelectBox
        items.clear();
        fileHandles = Gdx.files.local(Res.LOCAL_PATH_TO_FONTS_DIRECTORY).list();
        fontExtensions = new HashMap<>();
        for (FileHandle fileHandle : fileHandles) {
            String nameWithoutExtension = fileHandle.nameWithoutExtension();
            String extension = fileHandle.extension();
            fontExtensions.put(nameWithoutExtension, extension);
            items.add(nameWithoutExtension);
        }

        String selectedFontName = Res.FONT_NAME;
        int dotIndex = selectedFontName.lastIndexOf('.');
        if (selectedFontName.lastIndexOf('.') != -1)
                selectedFontName = selectedFontName.substring(0, dotIndex);
        chooseFont = new SelectBox<>(selectBoxStyle);
        chooseFont.setItems(items);
        chooseFont.setSelected(selectedFontName);
        chooseFont.setBounds(0, 0, itemWidth / 2.0f, itemHeight);
        chooseFont.setAlignment(Align.center);

        fontLabel = new Label(" Font", labelStyle);
        fontLabel.setTouchable(Touchable.disabled);
        fontLabel.setAlignment(Align.left);
        fontLabel.setBounds(itemWidth / 2.0f, 0, itemWidth / 2.0f, itemHeight);

        fontTable.setTouchable(Touchable.enabled);
        addActorsToTable(fontTable, chooseFont, fontLabel);
        addItemToScrollPane(fontTable);

        // Fullscreen-CheckBox
        final float checkBoxPad = 10 * Res.RESOLUTION_HEIGHT_SCALE;
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.fontColor = Color.WHITE;
        checkBoxStyle.checkedFontColor = Color.GOLD;
        checkBoxStyle.font = labelStyle.font;
        checkBoxStyle.checkboxOff = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.CHECKBOX_OFF)) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                float mWidth, mHeight, mX, mY;
                mWidth = mHeight = itemHeight - checkBoxPad * 2.0f;
                mY = y - (mHeight - height) / 2.0f;
                mX = checkBoxPad / 2.0f;
                super.draw(batch, mX, mY, mWidth, mHeight);
            }
        };
        checkBoxStyle.checkboxOn = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.CHECKBOX_ON)) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                float mWidth, mHeight, mX, mY;
                mWidth = mHeight = itemHeight - checkBoxPad * 2.0f;
                mY = y - (mHeight - height) / 2.0f;
                mX = checkBoxPad / 2.0f;
                super.draw(batch, mX, mY, mWidth, mHeight);
            }
        };
        fullscreenCheckBox = new CheckBox("Fullscreen", checkBoxStyle);
        fullscreenCheckBox.setBounds(0, 0, itemWidth, itemHeight);
        fullscreenCheckBox.align(Align.left);
        fullscreenCheckBox.setChecked(Res.FULLSCREEN);

        fullscreenTable.setTouchable(Touchable.enabled);
        addActorsToTable(fullscreenTable, fullscreenCheckBox);
        addItemToScrollPane(fullscreenTable);

        // BeatmapBackgroundBrightness-Label with buttons
        bbbLabel = new Label("", labelStyle);
        bbbLabel.setTouchable(Touchable.disabled);
        bbbLabel.setAlignment(Align.left);
        bbbLabel.setBounds(0, 0, itemWidth, itemHeight);
        reInitBbbLabel();

        plusButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                bbbVel.plus = 0;
            }
        }, new TouchHoldEventListener() {
            @Override
            public void touchHold() {
                bbbVel.plus += bbbVel.accelerate;
                float bbb = Res.BEATMAP_BACKGROUND_BRIGHTNESS + bbbVel.plus;
                Res.BEATMAP_BACKGROUND_BRIGHTNESS = Math.min(1, bbb);
                reInitBbbLabel();
            }
        }, plusButtonX, plusButtonY, plusButtonWidth, plusButtonHeight, Res.BUTTON_PLUS_UP, Res.BUTTON_PLUS_PRESS);

        minusButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                bbbVel.minus = 0;
            }
        }, new TouchHoldEventListener() {
            @Override
            public void touchHold() {
                bbbVel.minus += bbbVel.accelerate;
                float bbb = Res.BEATMAP_BACKGROUND_BRIGHTNESS - bbbVel.minus;
                Res.BEATMAP_BACKGROUND_BRIGHTNESS = Math.max(0, bbb);
                reInitBbbLabel();
            }
        }, minusButtonX, minusButtonY, minusButtonWidth, minusButtonHeight, Res.BUTTON_MINUS_UP, Res.BUTTON_MINUS_PRESS);

        bbbTable.setTouchable(Touchable.enabled);
        addActorsToTable(bbbTable, bbbLabel, plusButton, minusButton);
        addItemToScrollPane(bbbTable);

        // MusicVolume-Label with buttons
        mvLabel = new Label("", labelStyle);
        mvLabel.setTouchable(Touchable.disabled);
        mvLabel.setAlignment(Align.left);
        mvLabel.setBounds(0, 0, itemWidth, itemHeight);
        reInitMvLabel();

        plusButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                mvVel.plus = 0;
            }
        }, new TouchHoldEventListener() {
            @Override
            public void touchHold() {
                mvVel.plus += mvVel.accelerate;
                float mv = Res.MUSIC_VOLUME + mvVel.plus;
                Res.MUSIC_VOLUME = Math.min(1, mv);
                reInitMvLabel();
            }
        }, plusButtonX, plusButtonY, plusButtonWidth, plusButtonHeight, Res.BUTTON_PLUS_UP, Res.BUTTON_PLUS_PRESS);

        minusButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                mvVel.minus = 0;
            }
        }, new TouchHoldEventListener() {
            @Override
            public void touchHold() {
                mvVel.minus += mvVel.accelerate;
                float mv = Res.MUSIC_VOLUME - mvVel.minus;
                Res.MUSIC_VOLUME = Math.max(0, mv);
                reInitMvLabel();
            }
        }, minusButtonX, minusButtonY, minusButtonWidth, minusButtonHeight, Res.BUTTON_MINUS_UP, Res.BUTTON_MINUS_PRESS);

        mvTable.setTouchable(Touchable.enabled);
        addActorsToTable(mvTable, mvLabel, plusButton, minusButton);
        addItemToScrollPane(mvTable);

        // SoundVolume-Label with buttons
        svLabel = new Label("", labelStyle);
        svLabel.setTouchable(Touchable.disabled);
        svLabel.setAlignment(Align.left);
        svLabel.setBounds(0, 0, itemWidth, itemHeight);
        reInitSvLabel();

        plusButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                svVel.plus = 0;
            }
        }, new TouchHoldEventListener() {
            @Override
            public void touchHold() {
                svVel.plus += svVel.accelerate;
                float sv = Res.SOUND_VOLUME + svVel.plus;
                Res.SOUND_VOLUME = Math.min(1, sv);
                reInitSvLabel();
            }
        }, plusButtonX, plusButtonY, plusButtonWidth, plusButtonHeight, Res.BUTTON_PLUS_UP, Res.BUTTON_PLUS_PRESS);

        minusButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                svVel.minus = 0;
            }
        }, new TouchHoldEventListener() {
            @Override
            public void touchHold() {
                svVel.minus += svVel.accelerate;
                float sv = Res.SOUND_VOLUME - svVel.minus;
                Res.SOUND_VOLUME = Math.max(0, sv);
                reInitSvLabel();
            }
        }, minusButtonX, minusButtonY, minusButtonWidth, minusButtonHeight, Res.BUTTON_MINUS_UP, Res.BUTTON_MINUS_PRESS);

        svTable.setTouchable(Touchable.enabled);
        addActorsToTable(svTable, svLabel, plusButton, minusButton);
        addItemToScrollPane(svTable);
        addActors(scrollPane);
        stage.addActor(rootTable);
    }

    private void addItemToScrollPane(Table item) {
        scrollPaneTable.add(item).size(itemWidth, itemHeight).pad(itemPad, 0, itemPad, 0).row();
        scrollPaneItems.add(item);
    }

    private void reInitBbbLabel() {
        bbbLabel.setText(" Background brightness: " + (((int) (Res.BEATMAP_BACKGROUND_BRIGHTNESS * 1000)) / 10.0) + " %");
    }

    private void reInitMvLabel() {
        mvLabel.setText(" Music volume: " + (((int) (Res.MUSIC_VOLUME * 1000)) / 10.0) + " %");
    }

    private void reInitSvLabel() {
        svLabel.setText(" Sound volume: " + (((int) (Res.SOUND_VOLUME * 1000)) / 10.0) + " %");
    }

    @Override
    public void hide() {
        super.hide();
        if (hasChanged) {
            intentHandler.triggerRebirth();
        }
    }
}
