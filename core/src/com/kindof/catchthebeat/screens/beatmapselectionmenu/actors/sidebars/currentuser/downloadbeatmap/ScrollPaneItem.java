package com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebars.currentuser.downloadbeatmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kindof.catchthebeat.resources.FileType;
import com.kindof.catchthebeat.storage.IStorage;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.beatmapeditor.BeatmapEntity;
import com.kindof.catchthebeat.ui.actors.scrollpanes.ScrollPane;
import com.kindof.catchthebeat.tools.Font;

import java.util.HashMap;

public class ScrollPaneItem extends Table {
    private BeatmapEntity beatmapEntity;
    private CheckBox checkBox;
    private Label titleLabel, artistLabel;
    private float height, width;
    private ScrollPane scrollPane;
    private static HashMap<String, FileHandle> songsMap = null;

    public ScrollPaneItem(ScrollPane scrollPane, BeatmapEntity beatmapEntity, float width, float height) {
        this.beatmapEntity = beatmapEntity;
        this.width = width;
        this.height = height;
        this.scrollPane = scrollPane;

        init();

        addActor(checkBox);
        addActor(titleLabel);
        addActor(artistLabel);
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
                    String localPath = Res.LOCAL_PATH_TO_SONGS_DIRECTORY + id + "/";
                    if (!checkBox.isChecked()) {
                        checkBox.setChecked(true);
                        IStorage storage = Res.GAME.getStorage();
                        if (Res.CURRENT_FILE_TYPE == FileType.unknown) {
                            Gdx.files.local(localPath).file().mkdir();
                            Res.CURRENT_BEATMAP_ID = id;
                            Res.CURRENT_FILE_TYPE = FileType.beatmap;
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
                            Res.BEATMAP_SELECTION_MENU_SCREEN.removeBeatmapFromScrollPane(id);
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

        final float checkBoxPad = 170 * Res.RESOLUTION_HEIGHT_SCALE;
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.fontColor = Color.WHITE;
        checkBoxStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (45 * Res.RESOLUTION_HEIGHT_SCALE));
        checkBoxStyle.checkboxOff = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.CHECKBOX_OFF)) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                float mWidth, mHeight, mX, mY;
                mWidth = mHeight = ScrollPaneItem.this.height - checkBoxPad * 2.0f;
                mY = y - (mHeight - height) / 2.0f;
                mX = checkBoxPad / 2.0f;
                super.draw(batch, mX, mY, mWidth, mHeight);
            }
        };
        checkBoxStyle.checkboxOn = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.CHECKBOX_ON)) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                float mWidth, mHeight, mX, mY;
                mWidth = mHeight = ScrollPaneItem.this.height - checkBoxPad * 2.0f;
                mY = y - (mHeight - height) / 2.0f;
                mX = checkBoxPad / 2.0f;
                super.draw(batch, mX, mY, mWidth, mHeight);
            }
        };
        checkBox = new CheckBox("", checkBoxStyle);
        checkBox.setBounds(0, 0, width, height);
        checkBox.setTouchable(Touchable.disabled);

        if (songsMap.containsKey(beatmapEntity.getId() + "")) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        // l = label
        float
                lx = height - checkBoxPad,
                ly = checkBoxPad,
                lw = width - lx,
                lh = height - checkBoxPad * 2.0f;

        Label.LabelStyle
                titleStyle = new Label.LabelStyle(),
                artistStyle = new Label.LabelStyle();

        titleStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (40.0f * Res.RESOLUTION_HEIGHT_SCALE));
        titleStyle.fontColor = Color.WHITE;
        titleLabel = new Label(beatmapEntity.getTitle(), titleStyle);
        titleLabel.setAlignment(Align.topLeft);
        titleLabel.setBounds(lx, ly, lw, lh);
        titleLabel.setTouchable(Touchable.disabled);

        artistStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (25.0f * Res.RESOLUTION_HEIGHT_SCALE));
        artistStyle.fontColor = Color.GOLDENROD;
        artistLabel = new Label(beatmapEntity.getArtist(), artistStyle);
        artistLabel.setAlignment(Align.left);
        artistLabel.setBounds(lx, ly, lw, lh);
        artistLabel.setTouchable(Touchable.disabled);
    }

    public static void initSongsMap(FileHandle[] songsList) {
        songsMap = new HashMap<>();
        for (FileHandle fileHandle : songsList) {
            songsMap.put(fileHandle.name(), fileHandle);
        }
    }
}
