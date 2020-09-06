package com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebars.currentuser;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kindof.catchthebeat.IIntentHandler;
import com.kindof.catchthebeat.resources.FileType;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.ui.actors.buttons.Button;
import com.kindof.catchthebeat.ui.actors.buttons.TouchUpEventListener;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebars.currentuser.adddeletefriend.AddDeleteFriendScreen;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebars.currentuser.downloadbeatmap.BeatmapDownloadScreen;

public class CurrentUserScreen extends BaseScreen {
    private Button selectImageButton, addDeleteFriendButton, beatmapDownloadButton, oszParseButton;
    private boolean isOpen;
    private AddDeleteFriendScreen addDeleteFriendScreen;
    private BeatmapDownloadScreen beatmapDownloadScreen;

    public CurrentUserScreen() {
        super();
    }

    @Override
    public void initialize() {
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Res.GAME.setScreen(Res.BEATMAP_SELECTION_MENU_SCREEN);
                }
                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setVisible(true);

        addDeleteFriendScreen = new AddDeleteFriendScreen(this);
        beatmapDownloadScreen = new BeatmapDownloadScreen(this);
        float
                pad = 10 * Res.RESOLUTION_HEIGHT_SCALE,
                height = Res.HEIGHT / 4.0f, width = height,
                x = (Res.WIDTH - (width * 4.0f + pad * 3.0f)) / 2.0f,
                y = (Res.HEIGHT - height) / 2.0f;

        final IIntentHandler intentHandler = Res.GAME.getIntentHandler();
        selectImageButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (Res.CURRENT_FILE_TYPE == FileType.unknown) {
                    Res.CURRENT_FILE_TYPE = FileType.userIcon;
                    intentHandler.getGalleryImagePath();
                }
            }
        }, x, y, width, height, Res.SELECT_IMAGE_ICON_BUTTON_UP, Res.SELECT_IMAGE_ICON_BUTTON_PRESS);

        addDeleteFriendButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Res.GAME.setScreen(addDeleteFriendScreen);
            }
        }, x + pad + width, y, width, height, Res.ADD_DELETE_FRIEND_BUTTON_UP, Res.ADD_DELETE_FRIEND_BUTTON_PRESS);

        beatmapDownloadButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Res.GAME.setScreen(beatmapDownloadScreen);
            }
        }, x + (pad + width) * 2.0f, y, width, height, Res.BEATMAP_DOWNLOAD_BUTTON_UP, Res.BEATMAP_DOWNLOAD_BUTTON_PRESS);

        oszParseButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (Res.CURRENT_FILE_TYPE == FileType.unknown) {
                    Res.CURRENT_FILE_TYPE = FileType.oszFile;
                    intentHandler.getOszFilePath();
                }
            }
        }, x + (pad + width) * 3.0f, y, width, height, Res.OSZ_PARSE_BUTTON_UP, Res.OSZ_PARSE_BUTTON_PRESS);

        addActors(selectImageButton, addDeleteFriendButton, beatmapDownloadButton, oszParseButton);
        stage.addActor(rootTable);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public AddDeleteFriendScreen getAddDeleteFriendScreen() {
        return addDeleteFriendScreen;
    }

    public BeatmapDownloadScreen getBeatmapDownloadScreen() {
        return beatmapDownloadScreen;
    }

    @Override
    public void hide() {
        super.hide();
        isOpen = false;
    }
}
