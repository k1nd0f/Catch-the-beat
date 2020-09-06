package com.kindof.catchthebeat.screens.beatmapeditor.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.XmlReader;
import com.kindof.catchthebeat.database.IDatabase;
import com.kindof.catchthebeat.IIntentHandler;
import com.kindof.catchthebeat.resources.FileType;
import com.kindof.catchthebeat.storage.IStorage;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.beatmapeditor.actors.HitLine;
import com.kindof.catchthebeat.screens.beatmapeditor.BeatmapEdiorScreen;
import com.kindof.catchthebeat.screens.beatmapeditor.BeatmapEntity;
import com.kindof.catchthebeat.screens.beatmapeditor.FruitEntity;
import com.kindof.catchthebeat.screens.beatmapeditor.FruitList;
import com.kindof.catchthebeat.ui.actors.buttons.Button;
import com.kindof.catchthebeat.ui.actors.buttons.TouchHoldEventListener;
import com.kindof.catchthebeat.ui.actors.buttons.TouchUpEventListener;
import com.kindof.catchthebeat.ui.actors.scrollpanes.ScrollPane;
import com.kindof.catchthebeat.tools.Font;
import com.kindof.catchthebeat.tools.Time;

import java.util.ArrayList;

public class SettingsScreen implements Screen {
    private Stage stage;
    private Table rootTable, scrollPaneTable;
    private Settings settings;
    private ScrollPane<Table> scrollPane;
    private ArrayList<Table> items;
    private float itemHeight, itemWidth, itemPad;
    private int MAX_ITEMS_ON_SCREEN;
    private Label osLabel, bpmLabel, difLabel, hrLabel, ssLabel;
    private TextField titleTextField, artistTextField, tagsTextField, samplesTextField;
    private Velocity osVel, ssVel, bpmVel, difVel, hrVel;
    private Button setMusicButton, setBackgroundButton, saveBeatmapButton, uploadBeatmapButton;

    /*
     * os - Offset          [1]
     * bpm - BPM            [2]
     * dif - Difficulty     [3]
     * hr - HealthRate      [4]
     * ss - ScrollSpeed     [5]
     *
     **********************************/

    /*
     * Title                [1]
     * Artist               [2]
     * Tags                 [3]
     * Samples              [4]
     *
     **********************************/

    public SettingsScreen(Settings settings) {
        this.settings = settings;
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Res.GAME.setScreen(Res.BEATMAP_EDITOR_SCREEN);
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setFillParent(true);
        rootTable.setVisible(false);

        init();

        stage.addActor(rootTable);
    }

    private void init() {
        osVel = new Velocity();
        hrVel = new Velocity();
        ssVel = new Velocity();
        bpmVel = new Velocity();
        difVel = new Velocity();

        osVel.accelerate = 0.5f;
        bpmVel.accelerate = 0.005f;
        difVel.accelerate = 0.0005f;
        hrVel.accelerate = 0.0005f;
        ssVel.accelerate = 0.05f;

        MAX_ITEMS_ON_SCREEN = 6;
        itemPad = 5 * Res.RESOLUTION_HEIGHT_SCALE;
        itemHeight = (Res.HEIGHT - (MAX_ITEMS_ON_SCREEN - 1) * itemPad * 2) / (MAX_ITEMS_ON_SCREEN - 1);
        itemWidth = Res.WIDTH - itemPad * 2.0f;
        items = new ArrayList<>(11);

        initScrollPane();
    }

    private void initScrollPane() {
        scrollPaneTable = new Table();
        scrollPaneTable.setVisible(true);
        scrollPaneTable.setTouchable(Touchable.enabled);

        scrollPane = new ScrollPane<>(scrollPaneTable);
        scrollPane.setFillParent(true);
        scrollPane.setMAX_ITEMS_ON_SCREEN(MAX_ITEMS_ON_SCREEN);
        scrollPane.setVisibleItemHeight(itemHeight + itemPad * 2.0f);
        rootTable.addActor(scrollPane);

        initScrollPaneItems();
    }

    private void initScrollPaneItems() {
        initFruitTypeButtonGroup();
        initLabels();
        initTextFields();
        initButtons();
        scrollPane.setItems(items);
    }

    private void initButtons() {
        Table table = new Table();
        float x = (itemWidth - itemHeight * 4.0f) / 2.0f;
        final IIntentHandler intentHandler = Res.GAME.getIntentHandler();

        // SetMusicButton //
        setMusicButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Res.CURRENT_FILE_TYPE = FileType.beatmapEditorMusic;
                intentHandler.getAudioPath();
            }
        }, x, 0, itemHeight, itemHeight, Res.UI_BEATMAP_EDITOR_SETTINGS_MUSIC_BUTTON_UP, Res.UI_BEATMAP_EDITOR_SETTINGS_MUSIC_BUTTON_PRESS);

        // SetBackgroundButton //
        setBackgroundButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Res.CURRENT_FILE_TYPE = FileType.beatmapEditorBackground;
                intentHandler.getGalleryImagePath();
            }
        }, x + itemHeight, 0, itemHeight, itemHeight, Res.UI_BEATMAP_EDITOR_SETTINGS_BACKGROUND_BUTTON_UP, Res.UI_BEATMAP_EDITOR_SETTINGS_BACKGROUND_BUTTON_PRESS);

        // SaveBeatmapButton //
        saveBeatmapButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                BeatmapEdiorScreen editor = Res.BEATMAP_EDITOR_SCREEN;
                if (editor.getFruitSet().size() > 0) {
                    editor.saveBeatmapIntoTmpDirectory();
                    Res.GAME.getToastMaker().makeToast("Beatmap was saved.", 0);
                }
            }
        }, x + itemHeight * 2.0f, 0, itemHeight, itemHeight, Res.UI_BEATMAP_EDITOR_SETTINGS_SAVE_BEATMAP_BUTTON_UP, Res.UI_BEATMAP_EDITOR_SETTINGS_SAVE_BEATMAP_BUTTON_PRESS);

        // UploadBeatmapButton //
        uploadBeatmapButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                String localPath = Res.LOCAL_PATH_TO_BEATMAP_EDITOR_TMP_DIRECTORY;
                IStorage storage = Res.GAME.getStorage();
                if (Res.CURRENT_FILE_TYPE == FileType.unknown && Gdx.files.local(localPath + "config.xml").exists() && Gdx.files.local(localPath + "fruit_set.xml").exists() && Gdx.files.local(localPath + "audio.mp3").exists() && Gdx.files.local(localPath + "background").exists()) {
                    String[] metadata = getTitleAndArtistFromSavedBeatmap().split("//");
                    IDatabase database = Res.GAME.getDatabase();
                    storage.resetPutFileCounter();
                    long id = database.getBeatmapCount();
                    Res.CURRENT_BEATMAP_ID = id;
                    Res.CURRENT_FILE_TYPE = FileType.beatmap;
                    BeatmapEntity beatmapEntity = new BeatmapEntity(id, metadata[0], metadata[1]);
                    String storagePath = "Beatmaps/" + id + "/";
                    database.setBeatmap(beatmapEntity);
                    storage.putFile(storagePath + "config.xml", Gdx.files.local(localPath + "config.xml").file());
                    storage.putFile(storagePath + "fruit_set.xml", Gdx.files.local(localPath + "fruit_set.xml").file());
                    storage.putFile(storagePath + "audio.mp3", Gdx.files.local(localPath + "audio.mp3").file());
                    storage.putFile(storagePath + "background", Gdx.files.local(localPath + "background").file());
                }
            }
        }, x + itemHeight * 3.0f, 0, itemHeight, itemHeight, Res.UI_BEATMAP_EDITOR_SETTINGS_UPLOAD_BEATMAP_BUTTON_UP, Res.UI_BEATMAP_EDITOR_SETTINGS_UPLOAD_BEATMAP_BUTTON_PRESS);

        table.addActor(setMusicButton);
        table.addActor(setBackgroundButton);
        table.addActor(saveBeatmapButton);
        table.addActor(uploadBeatmapButton);
        scrollPaneTable.add(table).size(itemWidth, itemHeight).pad(itemPad, itemPad, itemPad, itemPad).row();
        items.add(table);
    }

    private String getTitleAndArtistFromSavedBeatmap() {
        XmlReader xmlReader = new XmlReader();
        XmlReader.Element element = xmlReader.parse(Gdx.files.local(Res.LOCAL_PATH_TO_BEATMAP_EDITOR_TMP_DIRECTORY + "config.xml"));
        element = element.getChildByName("MetaData");
        return element.getChildByName("Title").getText() + "//" + element.getChildByName("Artist").getText();
    }

    private void initFruitTypeButtonGroup() {
        final float bWidth = (Res.WIDTH - itemPad * 2.0f) / 5.0f;
        TextureRegionDrawable
                apple = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.FRUIT_APPLE)) {
                    @Override
                    public void draw(Batch batch, float x, float y, float width, float height) {
                        float mWidth, mHeight, mX, mY;
                        mWidth = mHeight = itemHeight / 3.0f;
                        mY = y - (mHeight - height) / 2.0f;
                        mX = x + (width - mWidth) / 2.0f;
                        super.draw(batch, mX, mY, mWidth, mHeight);
                    }
                },
                bananas = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.FRUIT_BANANAS)) {
                    @Override
                    public void draw(Batch batch, float x, float y, float width, float height) {
                        float mWidth, mHeight, mX, mY;
                        mWidth = mHeight = itemHeight / 3.0f;
                        mY = y - (mHeight - height) / 2.0f;
                        mX = x + (width - mWidth) / 2.0f;
                        super.draw(batch, mX, mY, mWidth, mHeight);
                    }
                },
                grapes = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.FRUIT_GRAPES)) {
                    @Override
                    public void draw(Batch batch, float x, float y, float width, float height) {
                        float mWidth, mHeight, mX, mY;
                        mWidth = mHeight = itemHeight / 3.0f;
                        mY = y - (mHeight - height) / 2.0f;
                        mX = x + (width - mWidth) / 2.0f;
                        super.draw(batch, mX, mY, mWidth, mHeight);
                    }
                },
                orange = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.FRUIT_ORANGE)) {
                    @Override
                    public void draw(Batch batch, float x, float y, float width, float height) {
                        float mWidth, mHeight, mX, mY;
                        mWidth = mHeight = itemHeight / 3.0f;
                        mY = y - (mHeight - height) / 2.0f;
                        mX = x + (width - mWidth) / 2.0f;
                        super.draw(batch, mX, mY, mWidth, mHeight);
                    }
                },
                pear = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.FRUIT_PEAR)) {
                    @Override
                    public void draw(Batch batch, float x, float y, float width, float height) {
                        float mWidth, mHeight, mX, mY;
                        mWidth = mHeight = itemHeight / 3.0f;
                        mY = y - (mHeight - height) / 2.0f;
                        mX = x + (width - mWidth) / 2.0f;
                        super.draw(batch, mX, mY, mWidth, mHeight);
                    }
                };

        final CheckBox appleCheckBox, bananasCheckBox, grapesCheckBox, orangeCheckBox, pearCheckBox;
        CheckBox.CheckBoxStyle checkBoxStyle, appleCheckBoxStyle, bananasCheckBoxStyle, grapesCheckBoxStyle, orangeCheckBoxStyle, pearCheckBoxStyle;

        checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (20 * Res.RESOLUTION_HEIGHT_SCALE));
        checkBoxStyle.fontColor = Color.WHITE;
        checkBoxStyle.checkedFontColor = Color.GOLD;

        appleCheckBoxStyle = new CheckBox.CheckBoxStyle(checkBoxStyle);
        appleCheckBoxStyle.checkboxOn = apple;
        appleCheckBoxStyle.checkboxOff = apple;

        bananasCheckBoxStyle = new CheckBox.CheckBoxStyle(checkBoxStyle);
        bananasCheckBoxStyle.checkboxOn = bananas;
        bananasCheckBoxStyle.checkboxOff = bananas;

        grapesCheckBoxStyle = new CheckBox.CheckBoxStyle(checkBoxStyle);
        grapesCheckBoxStyle.checkboxOn = grapes;
        grapesCheckBoxStyle.checkboxOff = grapes;

        orangeCheckBoxStyle = new CheckBox.CheckBoxStyle(checkBoxStyle);
        orangeCheckBoxStyle.checkboxOn = orange;
        orangeCheckBoxStyle.checkboxOff = orange;

        pearCheckBoxStyle = new CheckBox.CheckBoxStyle(checkBoxStyle);
        pearCheckBoxStyle.checkboxOn = pear;
        pearCheckBoxStyle.checkboxOff = pear;

        appleCheckBox = new CheckBox("apple", appleCheckBoxStyle);
        appleCheckBox.align(Align.center);
        appleCheckBox.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (appleCheckBox.isChecked()) {
                    settings.setFruitType("apple");
                }
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

            }
        });
        appleCheckBox.setChecked(true);

        bananasCheckBox = new CheckBox("bananas", bananasCheckBoxStyle);
        bananasCheckBox.align(Align.center);
        bananasCheckBox.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (bananasCheckBox.isChecked()) {
                    settings.setFruitType("bananas");
                }
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

            }
        });

        grapesCheckBox = new CheckBox("grapes", grapesCheckBoxStyle);
        grapesCheckBox.align(Align.center);
        grapesCheckBox.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (grapesCheckBox.isChecked()) {
                    settings.setFruitType("grapes");
                }
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

            }
        });

        orangeCheckBox = new CheckBox("orange", orangeCheckBoxStyle);
        orangeCheckBox.align(Align.center);
        orangeCheckBox.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (orangeCheckBox.isChecked()) {
                    settings.setFruitType("orange");
                }
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

            }
        });

        pearCheckBox = new CheckBox("pear", pearCheckBoxStyle);
        pearCheckBox.align(Align.center);
        pearCheckBox.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pearCheckBox.isChecked()) {
                    settings.setFruitType("pear");
                }
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

            }
        });

        ButtonGroup<CheckBox> buttonGroup = new ButtonGroup<>(appleCheckBox, bananasCheckBox, grapesCheckBox, orangeCheckBox, pearCheckBox);
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);
        buttonGroup.setUncheckLast(true);

        Table table = new Table();
        table.add(appleCheckBox).size(bWidth, itemHeight);
        table.add(bananasCheckBox).size(bWidth, itemHeight);
        table.add(grapesCheckBox).size(bWidth, itemHeight);
        table.add(orangeCheckBox).size(bWidth, itemHeight);
        table.add(pearCheckBox).size(bWidth, itemHeight);

        scrollPaneTable.add(table).size(itemWidth, itemHeight).pad(itemPad, itemPad, itemPad, itemPad).row();
        items.add(table);
    }

    private void initLabels() {
        float
                plusButtonX = (itemWidth / 8.0f - itemHeight) / 2.0f + 5 * itemWidth / 8.0f,
                plusButtonY = 0,
                plusButtonWidth = itemHeight,
                plusButtonHeight = itemHeight,
                minusButtonX = (itemWidth / 8.0f - itemHeight) / 2.0f + 6 * itemWidth / 8.0f,
                minusButtonY = 0,
                minusButtonWidth = itemHeight,
                minusButtonHeight = itemHeight;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (25 * Res.RESOLUTION_HEIGHT_SCALE));
        labelStyle.fontColor = Color.WHITE;

        Table
                osTable = new Table(),
                bpmTable = new Table(),
                difTable = new Table(),
                hrTable = new Table(),
                ssTable = new Table();


        // Offset //
        osLabel = new Label("", labelStyle);
        osLabel.setTouchable(Touchable.disabled);
        osLabel.setAlignment(Align.center);
        osLabel.setBounds(0, 0, itemWidth / 2.0f, itemHeight);

        Button
                plusButton = new Button(new TouchUpEventListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        osVel.plus = 0;
                    }
                }, new TouchHoldEventListener() {
                    @Override
                    public void touchHold() {
                        osVel.plus += osVel.accelerate;
                        settings.setOffset(settings.getOffset() + osVel.plus);
                        reInitOffsetLabel();
                        onOffsetChange(osVel.plus);
                    }
                }, plusButtonX, plusButtonY, plusButtonWidth, plusButtonHeight, Res.BUTTON_PLUS_UP, Res.BUTTON_PLUS_PRESS),

                minusButton = new Button(new TouchUpEventListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        osVel.minus = 0;
                    }
                }, new TouchHoldEventListener() {
                    @Override
                    public void touchHold() {
                        osVel.minus += osVel.accelerate;
                        float offset = settings.getOffset() - osVel.minus;
                        settings.setOffset(Math.max(0, offset));
                        reInitOffsetLabel();
                        onOffsetChange(-osVel.minus);
                    }
                }, minusButtonX, minusButtonY, minusButtonWidth, minusButtonHeight, Res.BUTTON_MINUS_UP, Res.BUTTON_MINUS_PRESS);

        reInitOffsetLabel();
        osTable.setTouchable(Touchable.childrenOnly);
        osTable.addActor(osLabel);
        osTable.addActor(plusButton);
        osTable.addActor(minusButton);
        scrollPaneTable.add(osTable).size(itemWidth, itemHeight).pad(itemPad, itemPad, itemPad, itemPad).row();

        // BPM //
        bpmLabel = new Label("", labelStyle);
        bpmLabel.setTouchable(Touchable.disabled);
        bpmLabel.setAlignment(Align.center);
        bpmLabel.setBounds(0, 0, itemWidth / 2.0f, itemHeight);

        plusButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                bpmVel.plus = 0;
            }
        }, new TouchHoldEventListener() {
            @Override
            public void touchHold() {
                bpmVel.plus += bpmVel.accelerate;
                settings.setBpm(settings.getBpm() + bpmVel.plus);
                reInitBpmLabel();
            }
        }, plusButtonX, plusButtonY, plusButtonWidth, plusButtonHeight, Res.BUTTON_PLUS_UP, Res.BUTTON_PLUS_PRESS);

        minusButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                bpmVel.minus = 0;
            }
        }, new TouchHoldEventListener() {
            @Override
            public void touchHold() {
                bpmVel.minus += bpmVel.accelerate;
                float bpm = settings.getBpm() - bpmVel.minus;
                settings.setBpm(Math.max(0, bpm));
                reInitBpmLabel();
            }
        }, minusButtonX, minusButtonY, minusButtonWidth, minusButtonHeight, Res.BUTTON_MINUS_UP, Res.BUTTON_MINUS_PRESS);

        reInitBpmLabel();
        bpmTable.setTouchable(Touchable.childrenOnly);
        bpmTable.addActor(bpmLabel);
        bpmTable.addActor(plusButton);
        bpmTable.addActor(minusButton);
        scrollPaneTable.add(bpmTable).size(itemWidth, itemHeight).pad(itemPad, itemPad, itemPad, itemPad).row();

        // Difficulty //
        difLabel = new Label("", labelStyle);
        difLabel.setTouchable(Touchable.disabled);
        difLabel.setAlignment(Align.center);
        difLabel.setBounds(0, 0, itemWidth / 2.0f, itemHeight);

        plusButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                difVel.plus = 0;
            }
        }, new TouchHoldEventListener() {
            @Override
            public void touchHold() {
                difVel.plus += difVel.accelerate;
                settings.setDifficulty(settings.getDifficulty() + difVel.plus);
                reInitDifficultyLabel();
            }
        }, plusButtonX, plusButtonY, plusButtonWidth, plusButtonHeight, Res.BUTTON_PLUS_UP, Res.BUTTON_PLUS_PRESS);

        minusButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                difVel.minus = 0;
            }
        }, new TouchHoldEventListener() {
            @Override
            public void touchHold() {
                difVel.minus += difVel.accelerate;
                float dif = settings.getDifficulty() - difVel.minus;
                settings.setDifficulty(Math.max(0, dif));
                reInitDifficultyLabel();
            }
        }, minusButtonX, minusButtonY, minusButtonWidth, minusButtonHeight, Res.BUTTON_MINUS_UP, Res.BUTTON_MINUS_PRESS);

        reInitDifficultyLabel();
        difTable.setTouchable(Touchable.childrenOnly);
        difTable.addActor(difLabel);
        difTable.addActor(plusButton);
        difTable.addActor(minusButton);
        scrollPaneTable.add(difTable).size(itemWidth, itemHeight).pad(itemPad, itemPad, itemPad, itemPad).row();

        // HealthRate //
        hrLabel = new Label("", labelStyle);
        hrLabel.setTouchable(Touchable.disabled);
        hrLabel.setAlignment(Align.center);
        hrLabel.setBounds(0, 0, itemWidth / 2.0f, itemHeight);

        plusButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                hrVel.plus = 0;
            }
        }, new TouchHoldEventListener() {
            @Override
            public void touchHold() {
                hrVel.plus += hrVel.accelerate;
                settings.setHealthRate(settings.getHealthRate() + hrVel.plus);
                reInitHealthRateLabel();
            }
        }, plusButtonX, plusButtonY, plusButtonWidth, plusButtonHeight, Res.BUTTON_PLUS_UP, Res.BUTTON_PLUS_PRESS);

        minusButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                hrVel.minus = 0;
            }
        }, new TouchHoldEventListener() {
            @Override
            public void touchHold() {
                hrVel.minus += hrVel.accelerate;
                float hr = settings.getHealthRate() - hrVel.minus;
                settings.setHealthRate(Math.max(hr, 0.01f));
                reInitHealthRateLabel();
            }
        }, minusButtonX, minusButtonY, minusButtonWidth, minusButtonHeight, Res.BUTTON_MINUS_UP, Res.BUTTON_MINUS_PRESS);

        reInitHealthRateLabel();
        hrTable.setTouchable(Touchable.childrenOnly);
        hrTable.addActor(hrLabel);
        hrTable.addActor(plusButton);
        hrTable.addActor(minusButton);
        scrollPaneTable.add(hrTable).size(itemWidth, itemHeight).pad(itemPad, itemPad, itemPad, itemPad).row();

        // ScrollSpeed //
        ssLabel = new Label("", labelStyle);
        ssLabel.setTouchable(Touchable.disabled);
        ssLabel.setAlignment(Align.center);
        ssLabel.setBounds(0, 0, itemWidth / 2.0f, itemHeight);

        plusButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ssVel.plus = 0;
            }
        }, new TouchHoldEventListener() {
            @Override
            public void touchHold() {
                ssVel.plus += ssVel.accelerate;
                settings.setScrollSpeed(settings.getScrollSpeed() + ssVel.plus);
                reInitScrollSpeedLabel();
                onScrollSpeedChange(ssVel.plus);
            }
        }, plusButtonX, plusButtonY, plusButtonWidth, plusButtonHeight, Res.BUTTON_PLUS_UP, Res.BUTTON_PLUS_PRESS);

        minusButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ssVel.minus = 0;
            }
        }, new TouchHoldEventListener() {
            @Override
            public void touchHold() {
                ssVel.minus += ssVel.accelerate;
                float ss = settings.getScrollSpeed();
                settings.setScrollSpeed(Math.max(settings.getScrollSpeed() - ssVel.minus, 1));
                reInitScrollSpeedLabel();
                if (ss != settings.getScrollSpeed())
                    onScrollSpeedChange(-ssVel.minus);
            }
        }, minusButtonX, minusButtonY, minusButtonWidth, minusButtonHeight, Res.BUTTON_MINUS_UP, Res.BUTTON_MINUS_PRESS);

        reInitScrollSpeedLabel();
        ssTable.setTouchable(Touchable.childrenOnly);
        ssTable.addActor(ssLabel);
        ssTable.addActor(plusButton);
        ssTable.addActor(minusButton);
        scrollPaneTable.add(ssTable).size(itemWidth, itemHeight).pad(itemPad, itemPad, itemPad, itemPad).row();

        // Adding to list
        items.add(osTable);
        items.add(bpmTable);
        items.add(difTable);
        items.add(hrTable);
        items.add(ssTable);
    }

    private void initTextFields() {
        Table
                titleTable = new Table(),
                artistTable = new Table(),
                tagsTable = new Table(),
                samplesTable = new Table();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.WHITE;
        labelStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (25 * Res.RESOLUTION_HEIGHT_SCALE));

        Label
                titleLabel = new Label("Title", labelStyle),
                artistLabel = new Label("Artist", labelStyle),
                tagsLabel = new Label("Tags", labelStyle),
                samplesLabel = new Label("Samples", labelStyle);

        float
                w = itemWidth * 3.0f / 6.0f,
                h = itemHeight,
                x = (itemWidth - w) / 2.0f + (itemWidth - w) / 4.0f,
                y = 0,
                lx = (itemWidth - w) / 4.0f, // label-x
                lw = x - lx; // label-width
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (25 * Res.RESOLUTION_HEIGHT_SCALE));
        textFieldStyle.background = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.TEXT_FIELD_BACKGROUND));
        textFieldStyle.cursor = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.TEXT_FIELD_CURSOR)) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                super.draw(batch, x, y, Res.TEXT_FIELD_CURSOR_WIDTH, height);
            }
        };

        titleTextField = new TextField("", textFieldStyle);
        titleTextField.setBounds(x, y, w, h);
        titleTextField.setAlignment(Align.center);
        titleTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                settings.setTitle(textField.getText());
            }
        });
        titleTextField.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return c != '/';
            }
        });
        
        artistTextField = new TextField("", textFieldStyle);
        artistTextField.setBounds(x, y, w, h);
        artistTextField.setAlignment(Align.center);
        artistTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                settings.setArtist(textField.getText());
            }
        });
        artistTextField.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return c != '/';
            }
        });

        tagsTextField = new TextField("", textFieldStyle);
        tagsTextField.setBounds(x, y, w, h);
        tagsTextField.setAlignment(Align.center);
        tagsTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                settings.setTags(textField.getText());
            }
        });

        samplesTextField = new TextField("", textFieldStyle);
        samplesTextField.setBounds(x, y, w, h);
        samplesTextField.setAlignment(Align.center);
        samplesTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                settings.setSamples(textField.getText());
            }
        });

        titleLabel.setBounds(lx, y, lw, h);
        titleLabel.setAlignment(Align.center);

        artistLabel.setBounds(lx, y, lw, h);
        artistLabel.setAlignment(Align.center);

        tagsLabel.setBounds(lx, y, lw, h);
        tagsLabel.setAlignment(Align.center);

        samplesLabel.setBounds(lx, y, lw, h);
        samplesLabel.setAlignment(Align.center);

        // Adding to Table //
        titleTable.addActor(titleLabel);
        titleTable.addActor(titleTextField);

        artistTable.addActor(artistLabel);
        artistTable.addActor(artistTextField);

        tagsTable.addActor(tagsLabel);
        tagsTable.addActor(tagsTextField);

        samplesTable.addActor(samplesLabel);
        samplesTable.addActor(samplesTextField);

        // Adding to ScrollPaneTable //
        scrollPaneTable.add(titleTable).size(itemWidth, itemHeight).pad(itemPad, itemPad, itemPad, itemPad).row();
        scrollPaneTable.add(artistTable).size(itemWidth, itemHeight).pad(itemPad, itemPad, itemPad, itemPad).row();
        scrollPaneTable.add(tagsTable).size(itemWidth, itemHeight).pad(itemPad, itemPad, itemPad, itemPad).row();
        scrollPaneTable.add(samplesTable).size(itemWidth, itemHeight).pad(itemPad, itemPad, itemPad, itemPad).row();

        // Adding to List //
        items.add(titleTable);
        items.add(artistTable);
        items.add(tagsTable);
        items.add(samplesTable);
    }

    private void onScrollSpeedChange(float deltaScrollSpeed) {
        float
                currentScrollSpeed = settings.getScrollSpeed(),
                previousScrollSpeed = currentScrollSpeed - deltaScrollSpeed,
                k;

        currentScrollSpeed *= Res.RESOLUTION_HEIGHT_SCALE;
        previousScrollSpeed *= Res.RESOLUTION_HEIGHT_SCALE;
        k = currentScrollSpeed / previousScrollSpeed;

        FruitList fruitSet = Res.BEATMAP_EDITOR_SCREEN.getFruitSet();
        HitLine hitLine = Res.BEATMAP_EDITOR_SCREEN.getHitLine();
        for (FruitEntity fruitEntity : fruitSet) {
            Actor actor = fruitEntity.getActor();
            float
                    deltaDistanceToHitLine = actor.getY() - hitLine.getY(),
                    newDeltaDistanceToHitLine = deltaDistanceToHitLine * k,
                    delta = newDeltaDistanceToHitLine - deltaDistanceToHitLine,
                    deltaTime = delta / currentScrollSpeed;

            actor.setY(actor.getY() + delta);
            fruitEntity.calculateBounds();
            fruitEntity.setSpawnTime(fruitEntity.getSpawnTime() + deltaTime);
        }
    }

    private void onOffsetChange(float deltaOffset) {
        float
                scrollSpeed = settings.getScrollSpeed(),
                deltaDistance = scrollSpeed * Time.millisToSeconds(deltaOffset);

        BeatmapEdiorScreen editor = Res.BEATMAP_EDITOR_SCREEN;
        FruitList fruitSet = editor.getFruitSet();
        for (FruitEntity fruitEntity : fruitSet) {
            Actor actor = fruitEntity.getActor();
            actor.setY(actor.getY() + deltaDistance);
            fruitEntity.calculateBounds();
        }

        editor.setMusicPosition(editor.getSongPos() - Time.millisToSeconds(deltaOffset));
    }

    public void reInitOffsetLabel() {
        osLabel.setText("Offset: " + ((int) (settings.getOffset())) + " ms");
    }

    private void reInitBpmLabel() {
        bpmLabel.setText("BPM: " + (((int) (settings.getBpm() * 10.0f)) / 10.0f));
    }

    private void reInitDifficultyLabel() {
        difLabel.setText("Difficulty: " + (((int) (settings.getDifficulty() * 100.0f)) / 100.0f));
    }

    private void reInitHealthRateLabel() {
        hrLabel.setText("HealthRate: " + (((int) (settings.getHealthRate() * 100.0f)) / 100.0f));
    }

    private void reInitScrollSpeedLabel() {
        ssLabel.setText("ScrollSpeed: " + ((int) (settings.getScrollSpeed()) / 100.0f));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        rootTable.setVisible(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        rootTable.setVisible(false);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
