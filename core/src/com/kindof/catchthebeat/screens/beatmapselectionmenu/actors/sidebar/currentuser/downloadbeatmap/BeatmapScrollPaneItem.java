package com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar.currentuser.downloadbeatmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kindof.catchthebeat.resources.FileType;
import com.kindof.catchthebeat.storage.IStorage;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.beatmaps.BeatmapEntity;
import com.kindof.catchthebeat.ui.actors.scrollpane.ScrollPane;
import com.kindof.catchthebeat.ui.styles.CheckBoxStyle;
import com.kindof.catchthebeat.ui.styles.LabelStyle;

import java.util.HashMap;

public class BeatmapScrollPaneItem extends Table {
    private BeatmapEntity beatmapEntity;
    private CheckBox checkBox;
    private Label titleLabel, artistLabel;
    private float height, width;
    private ScrollPane scrollPane;
    private static HashMap<String, FileHandle> songsMap = null;

    public BeatmapScrollPaneItem(ScrollPane scrollPane, BeatmapEntity beatmapEntity, float width, float height) {
        this.beatmapEntity = beatmapEntity;
        this.width = width;
        this.height = height;
        this.scrollPane = scrollPane;

        init();

        addActor(checkBox);
        addActor(titleLabel);
        addActor(artistLabel);
        //debugAll();
    }

    private void init() {
        setTouchable(Touchable.enabled);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                scrollPane.setHasScrolled(false);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!scrollPane.hasScrolled()) {
                    Long id = beatmapEntity.getId();
                    String localPath = Globals.LOCAL_PATH_TO_SONGS_DIRECTORY + id + "/";
                    if (!checkBox.isChecked()) {
                        checkBox.setChecked(true);
                        IStorage storage = Globals.GAME.getStorage();
                        if (Globals.CURRENT_FILE_TYPE == FileType.unknown) {
                            Gdx.files.local(localPath).file().mkdir();
                            Globals.CURRENT_BEATMAP_ID = id;
                            Globals.CURRENT_FILE_TYPE = FileType.beatmap;
                            String storagePath = "Beatmaps/" + id + "/";
                            storage.resetGetFileCounter();
                            storage.getFile(storagePath + "config.xml", Gdx.files.local(localPath + "config.xml").file());
                            storage.getFile(storagePath + "fruit_set.xml", Gdx.files.local(localPath + "fruit_set.xml").file());
                            storage.getFile(storagePath + "audio.mp3", Gdx.files.local(localPath + "audio.mp3").file());
                            storage.getFile(storagePath + "background", Gdx.files.local(localPath + "background").file());
                        } else {
                            checkBox.setChecked(false);
                        }
                    } else {
                        checkBox.setChecked(false);
                        if (Gdx.files.local(localPath).exists() && Gdx.files.local(localPath).list().length == 4) {
                            Gdx.files.local(localPath).deleteDirectory();
                            Globals.BEATMAP_SELECTION_MENU_SCREEN.removeBeatmapFromScrollPane(id);
                        } else {
                            checkBox.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

            }
        });

        float checkBoxViewSize = height / 4.0f;
        CheckBoxStyle checkBoxStyle = new CheckBoxStyle(null, null, 45, checkBoxViewSize);
        checkBox = new CheckBox("", checkBoxStyle);
        checkBox.align(Align.left);
        checkBox.setBounds(0, 0, width, height);
        checkBox.setTouchable(Touchable.disabled);

        if (songsMap.containsKey(beatmapEntity.getId() + "")) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        float
                labelPad = 10 * Globals.RESOLUTION_WIDTH_SCALE,
                labelX = labelPad + checkBoxViewSize,
                labelY = (Globals.HEIGHT - checkBoxViewSize) / 2.0f,
                labelW = width - labelX,
                labelH = checkBoxViewSize;

        LabelStyle titleStyle = new LabelStyle(Color.WHITE, 35);
        titleLabel = new Label(beatmapEntity.getTitle(), titleStyle);
        titleLabel.setAlignment(Align.topLeft);
        titleLabel.setBounds(labelX, labelY, labelW, labelH);
        titleLabel.setTouchable(Touchable.disabled);

        LabelStyle artistStyle = new LabelStyle(Color.GOLDENROD, 20);
        artistLabel = new Label(beatmapEntity.getArtist(), artistStyle);
        artistLabel.setAlignment(Align.left);
        artistLabel.setBounds(labelX, labelY, labelW, labelH);
        artistLabel.setTouchable(Touchable.disabled);
    }

    public static void initSongsMap(FileHandle[] songsList) {
        songsMap = new HashMap<>();
        for (FileHandle fileHandle : songsList) {
            songsMap.put(fileHandle.name(), fileHandle);
        }
    }
}
