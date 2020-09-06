package com.kindof.catchthebeat.screens.beatmapeditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.XmlWriter;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.screens.beatmapeditor.actors.bottompanels.BottomPanel;
import com.kindof.catchthebeat.screens.beatmapeditor.actors.HitLine;
import com.kindof.catchthebeat.screens.beatmapeditor.actors.MyScrollPane;
import com.kindof.catchthebeat.screens.beatmapeditor.settings.Settings;
import com.kindof.catchthebeat.screens.beatmapeditor.settings.SettingsScreen;
import com.kindof.catchthebeat.screens.MainMenuScreen;
import com.kindof.catchthebeat.ui.actors.buttons.Button;
import com.kindof.catchthebeat.ui.actors.buttons.TouchUpEventListener;
import com.kindof.catchthebeat.screens.game.State;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;

public class BeatmapEdiorScreen extends BaseScreen {
    private SettingsScreen settingsScreen;
    private Table leftScrollTable, mainScrollTable, mainScrollPaneTable;
    private Button settingsButton;
    private HitLine hitLine, lstHitLine;
    private FruitList fruitSet;
    private float songPos;
    private StringWriter stringWriter;
    private XmlWriter xmlWriter;
    private MyScrollPane mainScrollPane;
    private Settings settings;
    private State state;
    private Music music;
    private BottomPanel bottomPanel;
    private float resolutionWidthScale, catcherWidth, fruitDiameter;

    public BeatmapEdiorScreen() {
        super();
    }

    @Override
    public void initialize() {
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Res.GAME.setScreen(Res.MAIN_MENU_SCREEN);
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setFillParent(true);

        settings = new Settings();
        settingsScreen = new SettingsScreen(settings);
        stringWriter = new StringWriter();
        xmlWriter = new XmlWriter(stringWriter);
        songPos = 0;

        // lst = left-scroll-table
        float lstWidth = Res.WIDTH / 10.0f;
        leftScrollTable = new Table();
        leftScrollTable.setTouchable(Touchable.disabled);
        leftScrollTable.setBounds(0, lstWidth, lstWidth, Res.HEIGHT - lstWidth);

        lstHitLine = new HitLine(0, leftScrollTable.getY() + (leftScrollTable.getHeight() - leftScrollTable.getY()) / 2.0f, leftScrollTable.getWidth(), 1);

        mainScrollTable = new Table();
        mainScrollTable.setTouchable(Touchable.childrenOnly);
        mainScrollTable.setBounds(lstWidth, 0, Res.WIDTH - lstWidth, Res.HEIGHT);

        resolutionWidthScale = mainScrollTable.getWidth() / Res.DEFAULT_WIDTH;
        catcherWidth = Res.DEFAULT_CATCHER_WIDTH * resolutionWidthScale;
        fruitDiameter = catcherWidth / 2.0f;

        // l = line
        float
                lX = lstWidth,
                lY = Res.CATCHER_HEIGHT,
                lWidth = Res.WIDTH - lX,
                lHeight = 4;
        hitLine = new HitLine(lX, lY, lWidth, lHeight);

        settingsButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (state != State.pause)
                    setPause();
                Res.GAME.setScreen(settingsScreen);
            }
        }, 0, 0, lstWidth, lstWidth, Res.UI_BEATMAP_EDITOR_SETTINGS_BUTTON_UP, Res.UI_BEATMAP_EDITOR_SETTINGS_BUTTON_PRESS);

        initMainScrollPane();
        initBottomPanel();

        rootTable.addActor(leftScrollTable);
        rootTable.addActor(mainScrollTable);
        rootTable.addActor(settingsButton);
        stage.addActor(rootTable);

        setPause();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
        draw();
    }

    private void update(float delta) {
        stage.act(delta);
        if (state == State.run) {
            float scrollSpeed = settings.getScrollSpeed();
            fruitSet.update(scrollSpeed * Res.RESOLUTION_HEIGHT_SCALE, delta);

            if (music != null) {
                songPos += delta;
                if (!music.isPlaying() && songPos >= 0)
                    music.play();
            }
        }
    }

    private void draw() {
        stage.draw();
        hitLine.draw();
        lstHitLine.draw();
    }

    public void setPause() {
        state = State.pause;
        bottomPanel.changeStateOfPauseResumeButton();
        mainScrollPane.setTouchable(true);
        if (music != null) {
            music.pause();
        }
    }

    public void setResume() {
        state = State.run;
        bottomPanel.changeStateOfPauseResumeButton();
        mainScrollPane.setTouchable(false);
        if (music != null && songPos >= 0) {
            music.play();
        }
    }

    public void setBackground(Drawable background) {
        mainScrollTable.setBackground(background);
    }

    public void setMusic(Music music) {
        this.music = music;
        music.setVolume(Res.MUSIC_VOLUME);
    }

    public float getSongPos() {
        return songPos;
    }

    public void setMusicPosition(float position) {
        this.songPos = position;
        if (music != null) {
            music.setPosition(position);
        }
    }

    public FruitList getFruitSet() {
        return fruitSet;
    }

    public Music getMusic() {
        return music;
    }

    public HitLine getHitLine() {
        return hitLine;
    }

    public void initBackground() {
        Texture backgroundTexture = new Texture(Gdx.files.local(Res.LOCAL_PATH_TO_BEATMAP_EDITOR_TMP_DIRECTORY + "background"));
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(backgroundTexture) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                Color color = new Color(batch.getColor());
                float bright = Res.BEATMAP_BACKGROUND_BRIGHTNESS;
                batch.setColor(bright, bright, bright, 1);
                super.draw(batch, x, y, width, height);
                batch.setColor(color);
            }
        };
        setBackground(backgroundDrawable);
    }

    private void initBottomPanel() {
        float
                width = mainScrollTable.getWidth(),
                height = hitLine.getY(),
                startX = (mainScrollTable.getWidth() - width) / 2.0f,
                startY = -height,
                pad = height / 5.0f;

        bottomPanel = new BottomPanel(this, fruitSet, startX, startY, width, height, pad);
        mainScrollTable.addActor(bottomPanel);
    }

    private void initMainScrollPane() {
        mainScrollPaneTable = new Table();
        mainScrollPaneTable.setVisible(true);
        mainScrollPaneTable.setTouchable(Touchable.enabled);
        mainScrollPaneTable.setFillParent(true);
        mainScrollPaneTable.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mainScrollPane.setHasScrolled(false);
                return !fruitSet.overlapWithAnyFruit(x, y);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, final int button) {
                if (!mainScrollPane.hasScrolled() && music != null) {
                    // fx = fruit-x | fy = fruit-y | fsize = fruit-size
                    float
                            fsize = fruitDiameter,
                            fx = x - fsize / 2.0f,
                            fy = y - fsize / 2.0f;

                    if (fx < 0)
                        fx = 0;
                    else if (fx > mainScrollTable.getWidth() - fsize)
                        fx = mainScrollTable.getWidth() - fsize;

                    float spawnTime = songPos - (Res.HEIGHT - fy) / (settings.getScrollSpeed() * Res.RESOLUTION_HEIGHT_SCALE);
                    final FruitEntity fruitEntity = new FruitEntity(settings.getFruitType(), fx, fy, fsize, mainScrollTable.getWidth(), spawnTime);
                    fruitEntity.getActor().addListener(new InputListener() {
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            mainScrollPane.setHasScrolled(false);
                            return true;
                        }

                        @Override
                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                            if (!mainScrollPane.hasScrolled()) {
                                if (fruitSet.getSelectedItem() == fruitEntity) {
                                    fruitSet.markItemAsUnselected(fruitEntity);
                                    fruitSet.resetCurrentPreviousSelectedItem();
                                } else {
                                    fruitSet.markItemAsSelected(fruitEntity);
                                }
                            }
                            mainScrollPane.setTouchable(true);
                        }

                        @Override
                        public void touchDragged(InputEvent event, float x, float y, int pointer) {
                            if (fruitSet.getSelectedItem() == fruitEntity) {
                                // ax = actor-x | ay = actor-y | aw = actor-width
                                Actor actor = fruitEntity.getActor();
                                float
                                        deltaX = Gdx.input.getDeltaX(),
                                        deltaY = Gdx.input.getDeltaY(),
                                        ax = actor.getX() + deltaX,
                                        ay = actor.getY() - deltaY,
                                        aw = actor.getWidth();

                                if (ax < 0) {
                                    ax = 0;
                                } else if (ax > mainScrollTable.getWidth() - aw) {
                                    ax = mainScrollPaneTable.getWidth() - aw;
                                }

                                actor.setPosition(ax, ay);
                                mainScrollPane.setHasScrolled(true);
                                fruitEntity.onPositionChanged(deltaY);
                                fruitEntity.calculateBounds();
                                fruitEntity.calculateKx();
                            }
                            mainScrollPane.setTouchable(false);
                        }
                    });
                    fruitSet.add(fruitEntity);
                    Collections.sort(fruitSet);
                }
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

            }
        });
        fruitSet = new FruitList(mainScrollPaneTable);
        mainScrollPane = new MyScrollPane(mainScrollPaneTable, fruitSet);
        mainScrollPane.setFillParent(true);
        mainScrollPane.setSettings(settings);
        mainScrollTable.addActor(mainScrollPane);
    }

    public Settings getSettings() {
        return settings;
    }

    public void saveBeatmapIntoTmpDirectory() {
        String localPath = Res.LOCAL_PATH_TO_BEATMAP_EDITOR_TMP_DIRECTORY;
        deleteLocalFile(localPath + "config.xml");
        createLocalFile(localPath + "config.xml");
        deleteLocalFile(localPath + "fruit_set.xml");
        createLocalFile(localPath + "fruit_set.xml");
        writeFruitSetFile();
        writeConfigFile();
    }

    private boolean writeConfigFile() {
        try {
            xmlWriter.element("Config")
                    .element("General")
                        .element("AudioFileName", "audio.mp3")
                        .element("BackgroundFileName", "background")
                        .element("Offset", settings.getOffset())
                        .element("Samples", settings.getSamples())
                        .element("FruitSetFileName", "fruit_set.xml")
                    .pop()
                    .element("MetaData")
                        .element("Title", settings.getTitle())
                        .element("Artist", settings.getArtist())
                        .element("Creator", Res.USER.getNickname())
                        .element("Tags", settings.getTags())
                    .pop()
                    .element("Parameters")
                        .element("ScrollSpeed", settings.getScrollSpeed())
                        .element("HealthRate", settings.getHealthRate())
                        .element("Difficulty", settings.getDifficulty())
                        .element("BPM", settings.getBpm())
                        .element("FruitCount", fruitSet.size())
                    .pop();
            xmlWriter.close();
            Gdx.files.local(Res.LOCAL_PATH_TO_BEATMAP_EDITOR_TMP_DIRECTORY + "config.xml").writeString(stringWriter.toString(), false);
            stringWriter.getBuffer().setLength(0);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean writeFruitSetFile() {
        try {
            Collections.sort(fruitSet);
            xmlWriter.element("FruitSet");
            for (FruitEntity fruitEntity : fruitSet) {
                xmlWriter.element("Fruit")
                        .attribute("type", fruitEntity.getType())
                        .attribute("x", fruitEntity.getKx())
                        .attribute("spawn_time", fruitEntity.getSpawnTime()).pop();
            }
            xmlWriter.close();
            Gdx.files.local(Res.LOCAL_PATH_TO_BEATMAP_EDITOR_TMP_DIRECTORY + "fruit_set.xml").writeString(stringWriter.toString(), false);
            Gdx.app.log("fruit_set.xml", stringWriter.toString());
            stringWriter.getBuffer().setLength(0);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean createLocalFile(String localPath) {
        try {
            return Gdx.files.local(localPath).file().createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean deleteLocalFile(String localPath) {
        FileHandle fileHandle = Gdx.files.local(localPath);
        return fileHandle.file().delete();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (music != null)
            music.dispose();
    }
}
