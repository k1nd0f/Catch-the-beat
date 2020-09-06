package com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kindof.catchthebeat.resources.FileType;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.ui.UI;
import com.kindof.catchthebeat.ui.actors.scrollpane.ScrollPane;
import com.kindof.catchthebeat.ui.styles.LabelStyle;
import com.kindof.catchthebeat.user.Friend;
import com.kindof.catchthebeat.user.User;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar.currentuser.CurrentUserScreen;

import java.util.ArrayList;

public class SideBar extends Table {
    private float width, height;
    private float startX, startY, endX;
    private float pad;
    private boolean hasDragged, isOpen;
    private MoveToAction moveToAction;
    private ScrollPane<FriendScrollPaneItem> friendScrollPane;
    private Image currentUserIcon, currentUserIconBG;
    private Label currentUserNicknameLabel;
    private ArrayList<FriendScrollPaneItem> friendList;
    private Table friendScrollPaneTable, currentUserTable, tableForFriendScrollPane;
    private float iconSize, currentUserTableX, currentUserTableY, iconPad, currentUserNicknameLabelPad;
    private CurrentUserScreen currentUserScreen;
    private float friendItemWidth, friendItemHeight, itemPadTopBottom, itemPadLeftRight;
    private int index, maxFriendsOnScreen;

    public SideBar(float startX, float startY, float width, float height, float pad) {
        super(null);
        currentUserScreen = new CurrentUserScreen();
        this.startX = startX - pad;
        this.startY = startY;
        this.endX = startX - width;
        this.pad = pad;
        this.width = width;
        this.height = height;
        hasDragged = false;
        iconSize = Globals.HEIGHT / 6.0f;
        currentUserTableX = pad;
        currentUserTableY = Globals.HEIGHT - iconSize;
        iconPad = 3 * Globals.RESOLUTION_HEIGHT_SCALE;
        currentUserNicknameLabelPad = 15 * Globals.RESOLUTION_HEIGHT_SCALE;

        init();
    }

    private void init() {
        setBounds(this.startX, startY, width, height);
        setBackground(new TextureRegionDrawable(UI.SKIN_ATLAS.findRegion(UI.SIDE_BAR_BG)));
        setVisible(true);
        setTouchable(Touchable.enabled);

        currentUserTable = new Table();
        currentUserTable.setBounds(currentUserTableX, currentUserTableY, width - pad, iconSize);
        currentUserTable.setVisible(true);
        currentUserTable.setTouchable(Touchable.enabled);
        currentUserTable.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                hasDragged = false;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!hasDragged) {
                    Globals.GAME.setScreenWithTransition(currentUserScreen);
                    currentUserScreen.setIsOpen(true);
                }
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
            }
        });

        initCurrentUserIconBackground();

        addActor(currentUserTable);
        addListener(new InputListener() {
            private float startX = SideBar.this.startX;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                friendScrollPane.setHasScrolled(false);
                FriendScrollPaneItem.PRESSED = false;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if ((moveToAction == null || moveToAction.isComplete()) && !friendScrollPane.hasScrolled() && !currentUserScreen.isOpen() && !FriendScrollPaneItem.PRESSED) {
                    if (startX + (endX - startX) / 4.0 < getX() && hasDragged || isOpen) {
                        isOpen = false;
                        initMoveToAction(startX);
                    } else {
                        isOpen = true;
                        initMoveToAction(endX);
                    }

                    addAction(moveToAction);
                    hasDragged = false;
                }
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (Math.abs(Gdx.input.getDeltaX()) >= 5 || hasDragged) {
                    hasDragged = true;
                    if (getX() + Gdx.input.getDeltaX() > endX) {
                        setX(getX() + Gdx.input.getDeltaX());
                    }
                }
            }
        });
    }

    public void initCurrentUserIcon(Image userIcon) {
        if (currentUserIcon != null) {
            currentUserTable.removeActor(currentUserIcon);
        }

        currentUserIcon = userIcon;
        UI.calculateAndSetViewElementBounds(
                currentUserIcon,
                null, iconPad,
                0, 0, iconSize, iconSize
        );
        currentUserTable.addActor(currentUserIcon);
    }

    private void initCurrentUserIconBackground() {
        currentUserIconBG = new Image(UI.SKIN_ATLAS.findRegion(UI.USER_ICON_BG));
        currentUserIconBG.setBounds(0, 0, iconSize, iconSize);
        currentUserTable.addActor(currentUserIconBG);
    }

    public void initCurrentUserNicknameLabel() {
        LabelStyle labelStyle = new LabelStyle(Color.WHITE, 17); // "UI_SIDE_BAR_CURRENT_USER_BACKGROUND" doesnt exist in "shadeofpink" skin
        currentUserNicknameLabel = new Label(Globals.USER.getNickname(), labelStyle);
        currentUserNicknameLabel.setAlignment(Align.center);
        UI.calculateAndSetViewElementBounds(
                currentUserNicknameLabel,
                null, currentUserNicknameLabelPad,
                iconSize, 0, width - pad - iconSize, iconSize
        );
        currentUserTable.addActor(currentUserNicknameLabel);
    }

    public void initFriendScrollPane() {
        maxFriendsOnScreen = 11;
        itemPadTopBottom = 2.5f * Globals.RESOLUTION_HEIGHT_SCALE;
        itemPadLeftRight = 7 * Globals.RESOLUTION_WIDTH_SCALE;
        friendItemWidth = width - pad;
        friendItemHeight = Globals.HEIGHT / (maxFriendsOnScreen - 1);
        friendList = new ArrayList<>(Globals.USER.getFriends().size());

        tableForFriendScrollPane = new Table();
        tableForFriendScrollPane.setTouchable(Touchable.childrenOnly);
        tableForFriendScrollPane.setVisible(true);
        tableForFriendScrollPane.setBounds(currentUserTableX, startY, width - pad, height - iconSize);

        friendScrollPaneTable = new Table();
        friendScrollPaneTable.setTouchable(Touchable.childrenOnly);

        friendScrollPane = new ScrollPane<>(friendScrollPaneTable);
        friendScrollPane.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (Math.abs(Gdx.input.getDeltaY()) > 2.5) {
                    friendScrollPane.setHasScrolled(true);
                }
            }
        });
        friendScrollPane.setVisibleItemHeight(friendItemHeight + 2 * itemPadTopBottom);
        friendScrollPane.setMaxItemsOnScreen(maxFriendsOnScreen);
        tableForFriendScrollPane.add(friendScrollPane).size(width - pad, height - iconSize);
        addActor(tableForFriendScrollPane);

        initFriendScrollPaneItems();
    }

    public void initFriendScrollPaneItems() {
        friendScrollPaneTable.clearChildren();
        friendScrollPaneTable.getCells().clear();
        Globals.CURRENT_FILE_TYPE = FileType.friendIcon;
        for (Friend friend : Globals.USER.getFriends()) {
            String friendUid = friend.getUid();
            User friendAsUser = Globals.GAME.getDatabase().getUser(friendUid);
            String pathToFriendDirectory = Globals.LOCAL_PATH_TO_USERS_DIRECTORY + friendUid + "/";
            if (!Gdx.files.local(pathToFriendDirectory).exists()) {
                Gdx.files.local(pathToFriendDirectory).file().mkdir();
            }
            Globals.GAME.getStorage().getFile(pathToFriendDirectory + "icon", Gdx.files.local(pathToFriendDirectory + "icon").file(), friendAsUser, friendItemWidth, friendItemHeight, itemPadLeftRight, itemPadTopBottom);
        }
        Globals.CURRENT_FILE_TYPE = FileType.unknown;
    }

    public FriendScrollPaneItem initFriendScrollPaneItemWithIcon(User friend, float friendItemWidth, float friendItemHeight) {
        Texture texture = new Texture(Gdx.files.local(Globals.LOCAL_PATH_TO_USERS_DIRECTORY + friend.getUid() + "/icon"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return new FriendScrollPaneItem(friend, new Image(texture), friendItemWidth, friendItemHeight, friendScrollPane);
    }

    public FriendScrollPaneItem initFriendScrollPaneItemWithDefaultIcon(User friend, float friendItemWidth, float friendItemHeight) {
        return new FriendScrollPaneItem(friend, new Image(UI.SKIN_ATLAS.findRegion(UI.USER_DEFAULT_ICON)), friendItemWidth, friendItemHeight, friendScrollPane);
    }

    public void addFriendScrollPaneItem(FriendScrollPaneItem friendScrollPaneItem, float friendItemWidth, float friendItemHeight, float itemPadLeftRight, float itemPadTopBottom) {
        if (index > maxFriendsOnScreen) {
            friendScrollPaneItem.setVisible(false);
        }
        friendScrollPaneTable.add(friendScrollPaneItem).size(friendItemWidth - 2.0f * itemPadLeftRight, friendItemHeight).padTop(itemPadTopBottom).padBottom(itemPadTopBottom).row();
        friendList.add(friendScrollPaneItem);
        index++;
    }

    public CurrentUserScreen getCurrentUserScreen() {
        return currentUserScreen;
    }

    public float getPad() {
        return pad;
    }

    private void initMoveToAction(float x) {
        moveToAction = new MoveToAction();
        moveToAction.setInterpolation(Interpolation.fastSlow);
        moveToAction.setDuration(0.85f * Math.abs(x - getX()) / getWidth());
        moveToAction.setX(x);
    }
}
