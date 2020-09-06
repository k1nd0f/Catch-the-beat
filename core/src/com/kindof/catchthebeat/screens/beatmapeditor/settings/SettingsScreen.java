package com.kindof.catchthebeat.screens.beatmapeditor.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
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
import com.kindof.catchthebeat.beatmaps.BeatmapEntity;
import com.kindof.catchthebeat.database.IDatabase;
import com.kindof.catchthebeat.resources.FileType;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.screens.beatmapeditor.BeatmapEditorScreen;
import com.kindof.catchthebeat.storage.IStorage;
import com.kindof.catchthebeat.screens.beatmapeditor.actors.HitLine;
import com.kindof.catchthebeat.screens.beatmapeditor.FruitEntity;
import com.kindof.catchthebeat.screens.beatmapeditor.FruitList;
import com.kindof.catchthebeat.ui.Alignment;
import com.kindof.catchthebeat.ui.Font;
import com.kindof.catchthebeat.ui.UI;
import com.kindof.catchthebeat.ui.actors.button.Button;
import com.kindof.catchthebeat.ui.actors.scrollpane.ScrollPane;
import com.kindof.catchthebeat.tools.Time;
import com.kindof.catchthebeat.ui.actors.slidevaluechanger.SlideValueChanger;
import com.kindof.catchthebeat.ui.intenthandler.IIntentHandler;
import com.kindof.catchthebeat.ui.styles.CheckBoxStyle;
import com.kindof.catchthebeat.ui.styles.LabelStyle;
import com.kindof.catchthebeat.ui.styles.TextFieldStyle;
import com.kindof.catchthebeat.ui.toastmaker.IToastMaker;

import java.util.ArrayList;

public class SettingsScreen extends BaseScreen {
    private Table scrollPaneTable;
    private Settings settings;
    private ScrollPane<Table> scrollPane;
    private ArrayList<Table> items;
    private float itemH, itemW, itemPad;
    private int maxItemsOnScreen;
    private SlideValueChanger bpmValueChanger, difValueChanger, hrValueChanger, ssValueChanger;

    public SettingsScreen(Settings settings) {
        this.settings = settings;
        initialize();
    }

    @Override
    public void initialize() {
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Globals.GAME.setScreenWithTransition(Globals.BEATMAP_EDITOR_SCREEN);
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setFillParent(true);
        rootTable.setVisible(false);

        maxItemsOnScreen = 7;
        itemPad = 5 * Globals.RESOLUTION_HEIGHT_SCALE;
        itemH = (Globals.HEIGHT - (maxItemsOnScreen - 1) * itemPad * 2) / (maxItemsOnScreen - 1);
        itemW = Globals.WIDTH - itemPad * 2.0f;
        items = new ArrayList<>(11);

        initScrollPane();
        stage.addActor(rootTable);
    }

    private void initScrollPane() {
        scrollPaneTable = new Table();
        scrollPaneTable.setVisible(true);
        scrollPaneTable.setTouchable(Touchable.enabled);

        scrollPane = new ScrollPane<>(scrollPaneTable);
        scrollPane.setFillParent(true);
        scrollPane.setMaxItemsOnScreen(maxItemsOnScreen + 1);
        scrollPane.setVisibleItemHeight(itemH + itemPad * 2.0f);
        rootTable.addActor(scrollPane);

        initScrollPaneItems();
    }

    private void initScrollPaneItems() {
        initFruitTypeButtonGroup();
        initSlideValueChangers();
        initTextFields();
        initButtons();
        scrollPane.setItems(items);
    }

    private void initButtons() {
        float 
                bX = (itemW - itemH * 4.0f) / 2.0f,
                bPad = 10;
        
        final IIntentHandler intentHandler = Globals.GAME.getIntentHandler();
        Table table = new Table();

        Button setMusicButton = new Button((event, x, y, pointer, button) -> {
            Globals.CURRENT_FILE_TYPE = FileType.beatmapEditorMusic;
            intentHandler.getAudioPath();
        }, UI.MUSIC_BUTTON, UI.MUSIC_BUTTON);
        UI.calculateAndSetViewElementBounds(
                setMusicButton,
                Alignment.center, bPad,
                bX, 0, itemH, itemH
        );

        Button setBackgroundButton = new Button((event, x, y, pointer, button) -> {
            Globals.CURRENT_FILE_TYPE = FileType.beatmapEditorBackground;
            intentHandler.getGalleryImagePath();
        }, UI.GALLERY_BUTTON, UI.GALLERY_BUTTON);
        UI.calculateAndSetViewElementBounds(
                setBackgroundButton,
                Alignment.center, bPad,
                bX + itemH, 0, itemH, itemH
        );

        Button saveBeatmapButton = new Button((event, x, y, pointer, button) -> {
            BeatmapEditorScreen editor = Globals.BEATMAP_EDITOR_SCREEN;
            IToastMaker toastMaker = Globals.GAME.getToastMaker();
            if (editor.getFruitSet().size() > 0 && !settings.getArtist().equals("") && !settings.getTitle().equals("") && editor.getMusic() != null) {
                editor.saveBeatmapIntoTmpDirectory();
                toastMaker.makeToast("Beatmap was saved.", 0);
            } else {
                toastMaker.makeToast("The beatmap can't be saved with these settings", 1);
            }
        }, UI.SAVE_BUTTON, UI.SAVE_BUTTON);
        UI.calculateAndSetViewElementBounds(
                saveBeatmapButton,
                Alignment.center, bPad,
                bX + itemH * 2.0f, 0, itemH, itemH
        );

        Button uploadBeatmapButton = new Button((event, x, y, pointer, button) -> {
            String localPath = Globals.LOCAL_PATH_TO_BEATMAP_EDITOR_TMP_DIRECTORY;
            IStorage storage = Globals.GAME.getStorage();
            if (Globals.CURRENT_FILE_TYPE == FileType.unknown && Gdx.files.local(localPath + "config.xml").exists() && Gdx.files.local(localPath + "fruit_set.xml").exists() && Gdx.files.local(localPath + "audio.mp3").exists() && Gdx.files.local(localPath + "background").exists()) {
                String[] metadata = getTitleAndArtistFromSavedBeatmap().split("//");
                IDatabase database = Globals.GAME.getDatabase();
                long id = database.getBeatmapCount();
                Globals.CURRENT_BEATMAP_ID = id;
                Globals.CURRENT_FILE_TYPE = FileType.beatmap;
                BeatmapEntity beatmapEntity = new BeatmapEntity(id, metadata[0], metadata[1]);
                String storagePath = "Beatmaps/" + id + "/";

                database.setBeatmap(beatmapEntity);
                storage.putFile(storagePath + "config.xml", Gdx.files.local(localPath + "config.xml").file());
                storage.putFile(storagePath + "fruit_set.xml", Gdx.files.local(localPath + "fruit_set.xml").file());
                storage.putFile(storagePath + "audio.mp3", Gdx.files.local(localPath + "audio.mp3").file());
                storage.putFile(storagePath + "background", Gdx.files.local(localPath + "background").file());
            }
        }, UI.UPLOAD_BUTTON, UI.UPLOAD_BUTTON);
        UI.calculateAndSetViewElementBounds(
                uploadBeatmapButton,
                Alignment.center, bPad,
                bX + itemH * 3.0f, 0, itemH, itemH
        );

        table.addActor(setMusicButton);
        table.addActor(setBackgroundButton);
        table.addActor(saveBeatmapButton);
        table.addActor(uploadBeatmapButton);
        scrollPaneTable.add(table).size(itemW, itemH).pad(itemPad).row();
        items.add(table);
    }

    private String getTitleAndArtistFromSavedBeatmap() {
        XmlReader xmlReader = new XmlReader();
        XmlReader.Element element = xmlReader.parse(Gdx.files.local(Globals.LOCAL_PATH_TO_BEATMAP_EDITOR_TMP_DIRECTORY + "config.xml"));
        element = element.getChildByName("MetaData");
        return element.getChildByName("Title").getText() + "//" + element.getChildByName("Artist").getText();
    }

    private Rectangle calculateFruitDrawableViewBounds(float x, float y, float width, float height) {
        Rectangle rect = new Rectangle();

        rect.width = rect.height = itemH * 0.75f;
        rect.y = y - (rect.height - height) / 2.0f;
        rect.x = x + (width - rect.width) / 2.0f;

        return rect;
    }

    private void initFruitTypeButtonGroup() {
        final float bWidth = (Globals.WIDTH - itemPad * 2.0f) / 4.0f;
        TextureRegionDrawable
                apple = new TextureRegionDrawable(UI.SKIN_ATLAS.findRegion(UI.FRUIT_APPLE)) {
                    @Override
                    public void draw(Batch batch, float x, float y, float width, float height) {
                        Rectangle viewBounds = calculateFruitDrawableViewBounds(x, y, width, height);
                        super.draw(batch, viewBounds.x, viewBounds.y, viewBounds.width, viewBounds.height);
                    }
                },
                grape = new TextureRegionDrawable(UI.SKIN_ATLAS.findRegion(UI.FRUIT_GRAPE)) {
                    @Override
                    public void draw(Batch batch, float x, float y, float width, float height) {
                        Rectangle viewBounds = calculateFruitDrawableViewBounds(x, y, width, height);
                        super.draw(batch, viewBounds.x, viewBounds.y, viewBounds.width, viewBounds.height);
                    }
                },
                orange = new TextureRegionDrawable(UI.SKIN_ATLAS.findRegion(UI.FRUIT_ORANGE)) {
                    @Override
                    public void draw(Batch batch, float x, float y, float width, float height) {
                        Rectangle viewBounds = calculateFruitDrawableViewBounds(x, y, width, height);
                        super.draw(batch, viewBounds.x, viewBounds.y, viewBounds.width, viewBounds.height);
                    }
                },
                pear = new TextureRegionDrawable(UI.SKIN_ATLAS.findRegion(UI.FRUIT_PEAR)) {
                    @Override
                    public void draw(Batch batch, float x, float y, float width, float height) {
                        Rectangle viewBounds = calculateFruitDrawableViewBounds(x, y, width, height);
                        super.draw(batch, viewBounds.x, viewBounds.y, viewBounds.width, viewBounds.height);
                    }
                };

        final CheckBox appleCheckBox, grapeCheckBox, orangeCheckBox, pearCheckBox;
        CheckBoxStyle appleCheckBoxStyle, grapeCheckBoxStyle, orangeCheckBoxStyle, pearCheckBoxStyle;
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.fontColor = Color.WHITE;
        checkBoxStyle.checkedFontColor = Color.GOLD;
        checkBoxStyle.font = Font.getBitmapFont(15);

        appleCheckBoxStyle = new CheckBoxStyle(checkBoxStyle);
        appleCheckBoxStyle.checkboxOn = apple;
        appleCheckBoxStyle.checkboxOff = apple;

        grapeCheckBoxStyle = new CheckBoxStyle(checkBoxStyle);
        grapeCheckBoxStyle.checkboxOn = grape;
        grapeCheckBoxStyle.checkboxOff = grape;

        orangeCheckBoxStyle = new CheckBoxStyle(checkBoxStyle);
        orangeCheckBoxStyle.checkboxOn = orange;
        orangeCheckBoxStyle.checkboxOff = orange;

        pearCheckBoxStyle = new CheckBoxStyle(checkBoxStyle);
        pearCheckBoxStyle.checkboxOn = pear;
        pearCheckBoxStyle.checkboxOff = pear;

        appleCheckBox = new CheckBox("apple", appleCheckBoxStyle);
        appleCheckBox.align(Align.left);
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

        grapeCheckBox = new CheckBox("grape", grapeCheckBoxStyle);
        grapeCheckBox.align(Align.left);
        grapeCheckBox.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (grapeCheckBox.isChecked()) {
                    settings.setFruitType("grape");
                }
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

            }
        });

        orangeCheckBox = new CheckBox("orange", orangeCheckBoxStyle);
        orangeCheckBox.align(Align.left);
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
        pearCheckBox.align(Align.left);
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

        ButtonGroup<CheckBox> buttonGroup = new ButtonGroup<>(appleCheckBox, grapeCheckBox, orangeCheckBox, pearCheckBox);
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);
        buttonGroup.setUncheckLast(true);

        Table table = new Table();
        table.add(appleCheckBox).size(bWidth, itemH);
        table.add(grapeCheckBox).size(bWidth, itemH);
        table.add(orangeCheckBox).size(bWidth, itemH);
        table.add(pearCheckBox).size(bWidth, itemH);

        scrollPaneTable.add(table).size(itemW, itemH).pad(itemPad, itemPad, itemPad, itemPad).row();
        items.add(table);
    }

    private void initSlideValueChangers() {
        Table
                ssTable = new Table(),
                hrTable = new Table(),
                difTable = new Table(),
                bpmTable = new Table();

        float
                w = itemW / 2.0f,
                h = itemH,
                x = (itemW - w) / 2.0f,
                y = 0;


        ssValueChanger = new SlideValueChanger(
                w, h, 0.01f, 100,
                "Scroll Speed: ", "", Align.center, 25,
                () -> {
                    float
                            prevScrollSpeed = settings.getScrollSpeed(),
                            currentScrollSpeed = ssValueChanger.getValue() * 100.0f,
                            deltaScrollSpeed = currentScrollSpeed - prevScrollSpeed;

                    settings.setScrollSpeed(currentScrollSpeed);
                    onScrollSpeedChange(deltaScrollSpeed);
                }
        );
        ssValueChanger.setBounds(x, y, w, h);
        ssValueChanger.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                scrollPane.cancel();
            }
        });

        hrValueChanger = new SlideValueChanger(
                w, h, 0.01f, 20,
                "Health Rate: ", "", Align.center, 25,
                () -> settings.setHealthRate(hrValueChanger.getValue())
        );
        hrValueChanger.setBounds(x, y, w, h);
        hrValueChanger.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                scrollPane.cancel();
            }
        });

        difValueChanger = new SlideValueChanger(
                w, h, 0, 12,
                "Difficulty: ", "", Align.center, 25,
                () -> settings.setDifficulty(difValueChanger.getValue())
        );
        difValueChanger.setBounds(x, y, w, h);
        difValueChanger.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                scrollPane.cancel();
            }
        });

        bpmValueChanger = new SlideValueChanger(
                w, h, 0, 1000,
                "BPM: ", "", Align.center, 25,
                () -> settings.setBpm(bpmValueChanger.getValue())
        );
        bpmValueChanger.setBounds(x, y, w, h);
        bpmValueChanger.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                scrollPane.cancel();
            }
        });

        // Adding to Table //
        ssTable.addActor(ssValueChanger);
        hrTable.addActor(hrValueChanger);
        difTable.addActor(difValueChanger);
        bpmTable.addActor(bpmValueChanger);

        // Adding to Scroll Pane Table //
        scrollPaneTable.add(ssTable).size(itemW, itemH).pad(itemPad).row();
        scrollPaneTable.add(hrTable).size(itemW, itemH).pad(itemPad).row();
        scrollPaneTable.add(difTable).size(itemW, itemH).pad(itemPad).row();
        scrollPaneTable.add(bpmTable).size(itemW, itemH).pad(itemPad).row();

        // Adding to Scroll Pane Items //
        items.add(ssTable);
        items.add(hrTable);
        items.add(difTable);
        items.add(bpmTable);
    }

    private void initTextFields() {
        Table
                titleTable = new Table(),
                artistTable = new Table(),
                tagsTable = new Table(),
                samplesTable = new Table();

        LabelStyle labelStyle = new LabelStyle(Color.WHITE, 25);
        Label
                titleLabel = new Label("Title ", labelStyle),
                artistLabel = new Label("Artist ", labelStyle),
                tagsLabel = new Label("Tags ", labelStyle),
                samplesLabel = new Label("Samples ", labelStyle);

        float
                w = itemW / 2,
                h = itemH,
                x = (itemW - w) / 2.0f,
                y = 0,
                lX = 0,
                lW = x;

        TextFieldStyle textFieldStyle = new TextFieldStyle(Color.WHITE, 25);

        TextField titleTextField = new TextField("", textFieldStyle);
        titleTextField.setBounds(x, y, w, h);
        titleTextField.setAlignment(Align.center);
        titleTextField.setTextFieldListener((textField, c) -> settings.setTitle(textField.getText()));
        titleTextField.setTextFieldFilter((textField, c) -> c != '/' && (textField.getText().length() > 0 || c != ' '));

        TextField artistTextField = new TextField("", textFieldStyle);
        artistTextField.setBounds(x, y, w, h);
        artistTextField.setAlignment(Align.center);
        artistTextField.setTextFieldListener((textField, c) -> settings.setArtist(textField.getText()));
        artistTextField.setTextFieldFilter((textField, c) -> c != '/' && (textField.getText().length() > 0 || c != ' '));

        TextField tagsTextField = new TextField("", textFieldStyle);
        tagsTextField.setBounds(x, y, w, h);
        tagsTextField.setAlignment(Align.center);
        tagsTextField.setTextFieldListener((textField, c) -> settings.setTags(textField.getText()));
        tagsTextField.setTextFieldFilter((textField, c) -> textField.getText().length() > 0 || c != ' ');

        TextField samplesTextField = new TextField("", textFieldStyle);
        samplesTextField.setBounds(x, y, w, h);
        samplesTextField.setAlignment(Align.center);
        samplesTextField.setTextFieldListener((textField, c) -> settings.setSamples(textField.getText()));
        samplesTextField.setTextFieldFilter((textField, c) -> textField.getText().length() > 0 || c != ' ');

        titleLabel.setBounds(lX, y, lW, h);
        titleLabel.setAlignment(Align.right);

        artistLabel.setBounds(lX, y, lW, h);
        artistLabel.setAlignment(Align.right);

        tagsLabel.setBounds(lX, y, lW, h);
        tagsLabel.setAlignment(Align.right);

        samplesLabel.setBounds(lX, y, lW, h);
        samplesLabel.setAlignment(Align.right);

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
        scrollPaneTable.add(titleTable).size(itemW, itemH).pad(itemPad).row();
        scrollPaneTable.add(artistTable).size(itemW, itemH).pad(itemPad).row();
        scrollPaneTable.add(tagsTable).size(itemW, itemH).pad(itemPad).row();
        scrollPaneTable.add(samplesTable).size(itemW, itemH).pad(itemPad).row();

        // Adding to List //
        items.add(titleTable);
        items.add(artistTable);
        items.add(tagsTable);
        items.add(samplesTable);
    }

    private void onScrollSpeedChange(float deltaScrollSpeed) {
        float
                currentScrollSpeed = settings.getScrollSpeed() * Globals.RESOLUTION_HEIGHT_SCALE,
                previousScrollSpeed = currentScrollSpeed - deltaScrollSpeed * Globals.RESOLUTION_HEIGHT_SCALE,
                k = currentScrollSpeed / previousScrollSpeed;

        FruitList fruitSet = Globals.BEATMAP_EDITOR_SCREEN.getFruitSet();
        HitLine hitLine = Globals.BEATMAP_EDITOR_SCREEN.getHitLine();
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


    /*
    private void onOffsetChange(float deltaOffset) {
        float
                scrollSpeed = settings.getScrollSpeed() * Globals.RESOLUTION_HEIGHT_SCALE,
                deltaDistance = scrollSpeed * Time.millisToSeconds(deltaOffset);

        BeatmapEditorScreen editor = Globals.BEATMAP_EDITOR_SCREEN;
        FruitList fruitSet = editor.getFruitSet();
        for (FruitEntity fruitEntity : fruitSet) {
            Actor actor = fruitEntity.getActor();
            actor.setY(actor.getY() + deltaDistance);
            fruitEntity.calculateBounds();
            fruitEntity.setSpawnTime(fruitEntity.getSpawnTime() + deltaOffset);
        }

        editor.setMusicPosition(editor.getSongPos() - Time.millisToSeconds(deltaOffset));
    }
     */

    public void updateValues() {
        ssValueChanger.silentSetValue(settings.getScrollSpeed() / 100.0f);
        hrValueChanger.silentSetValue(settings.getHealthRate());
        difValueChanger.silentSetValue(settings.getDifficulty());
        bpmValueChanger.silentSetValue(settings.getBpm());
    }
}
