package com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar.currentuser;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar.currentuser.adddeletefriend.AddDeleteFriendScreen;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar.currentuser.downloadbeatmap.BeatmapDownloadScreen;
import com.kindof.catchthebeat.ui.Alignment;
import com.kindof.catchthebeat.ui.UI;
import com.kindof.catchthebeat.ui.intenthandler.IIntentHandler;
import com.kindof.catchthebeat.resources.FileType;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.ui.actors.button.Button;

public class CurrentUserScreen extends BaseScreen {
    private AddDeleteFriendScreen addDeleteFriendScreen;
    private BeatmapDownloadScreen beatmapDownloadScreen;
    private Button galleryButton, addDeleteFriendButton, beatmapDownloadButton, oszParseButton;
    private boolean isOpen;

    public CurrentUserScreen() {
        super();
    }

    @Override
    public void initialize() {
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Globals.GAME.setScreenWithTransition(Globals.BEATMAP_SELECTION_MENU_SCREEN);
                    setIsOpen(false);
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

        final IIntentHandler intentHandler = Globals.GAME.getIntentHandler();
        float
                pad = 10 * Globals.RESOLUTION_HEIGHT_SCALE,
                buttonSize = Globals.HEIGHT / 4.0f,
                buttonX = (Globals.WIDTH - buttonSize * 4.0f) / 2.0f;

        galleryButton = new Button(
                (event, x1, y1, pointer, button) -> {
                    if (Globals.CURRENT_FILE_TYPE == FileType.unknown) {
                    Globals.CURRENT_FILE_TYPE = FileType.userIcon;
                    intentHandler.getGalleryImagePath();
                }
        }, UI.GALLERY_BUTTON, UI.GALLERY_BUTTON);
        UI.calculateAndSetViewElementBounds(
                galleryButton,
                Alignment.center, pad,
                buttonX, 0, buttonSize, Globals.HEIGHT
        );

        addDeleteFriendButton = new Button(
                (event, x1, y1, pointer, button) -> Globals.GAME.setScreenWithTransition(addDeleteFriendScreen),
                UI.FRIENDS_BUTTON, UI.FRIENDS_BUTTON
        );
        UI.calculateAndSetViewElementBounds(
                addDeleteFriendButton,
                Alignment.center, pad,
                buttonX + buttonSize, 0, buttonSize, Globals.HEIGHT
        );

        beatmapDownloadButton = new Button(
                (event, x1, y1, pointer, button) -> Globals.GAME.setScreenWithTransition(beatmapDownloadScreen),
                UI.DOWNLOAD_BUTTON, UI.DOWNLOAD_BUTTON
        );
        UI.calculateAndSetViewElementBounds(
                beatmapDownloadButton,
                Alignment.center, pad,
                buttonX + buttonSize * 2.0f, 0, buttonSize, Globals.HEIGHT
        );

        oszParseButton = new Button(
                (event, x1, y1, pointer, button) -> {
                    if (Globals.CURRENT_FILE_TYPE == FileType.unknown) {
                        Globals.CURRENT_FILE_TYPE = FileType.oszFile;
                        intentHandler.getOszFilePath();
                    }
        }, UI.OSZ_PARSE_BUTTON, UI.OSZ_PARSE_BUTTON);
        UI.calculateAndSetViewElementBounds(
                oszParseButton,
                Alignment.center, pad,
                buttonX + buttonSize * 3.0f, 0, buttonSize, Globals.HEIGHT
        );

        addActors(
                galleryButton,
                addDeleteFriendButton,
                beatmapDownloadButton,
                oszParseButton
        );
        stage.addActor(rootTable);
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
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
    public void show() {
        super.show();
        isOpen = true;
    }

    @Override
    public void hide() {
        super.hide();
        isOpen = false;
    }
}
