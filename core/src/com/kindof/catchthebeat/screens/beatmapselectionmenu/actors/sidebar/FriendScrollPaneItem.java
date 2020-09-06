package com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kindof.catchthebeat.ui.UI;
import com.kindof.catchthebeat.user.chat.Chat;
import com.kindof.catchthebeat.database.IDatabase;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.screens.chat.ChatScreen;
import com.kindof.catchthebeat.ui.actors.scrollpane.ScrollPane;
import com.kindof.catchthebeat.ui.styles.LabelStyle;
import com.kindof.catchthebeat.user.User;

public class FriendScrollPaneItem extends Table {
    private User friend;
    private ScrollPane friendScrollPane;
    private float width, height;
    private Image icon, iconBackground;
    private Label nicknameLabel;

    static boolean PRESSED = false;

    FriendScrollPaneItem(final User friend, Image userIcon, float width, float height, ScrollPane friendScrollPane) {
        this.friend = friend;
        this.friendScrollPane = friendScrollPane;
        this.width = width;
        this.height = height;

        init(userIcon);

        addActor(iconBackground);
        addActor(icon);
        addActor(nicknameLabel);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                FriendScrollPaneItem.this.friendScrollPane.setHasScrolled(false);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!FriendScrollPaneItem.this.friendScrollPane.hasScrolled()) {
                    IDatabase database = Globals.GAME.getDatabase();
                    PRESSED = true;
                    Long chatId = friend.getChatId(Globals.USER.getUid());
                    Chat chat = database.getChat(chatId);
                    if (chat == null) {
                        chat = new Chat(chatId);
                        database.putChat(chat);
                    }
                    if (!Globals.CHAT_SCREENS.containsKey(chatId)) {
                        Globals.CURRENT_CHAT_SCREEN = new ChatScreen(chat);
                        Globals.CHAT_SCREENS.put(chatId, Globals.CURRENT_CHAT_SCREEN);
                    } else {
                        Globals.CURRENT_CHAT_SCREEN = Globals.CHAT_SCREENS.get(chatId);
                    }
                    Globals.CURRENT_CHAT_SCREEN.reInitMessageItems(chat.getChatMessages());
                    Globals.GAME.setScreenWithTransition(Globals.CURRENT_CHAT_SCREEN);
                }
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
            }
        });
    }

    private void init(Image userIcon) {
        float iconPad = 1.5f * Globals.RESOLUTION_HEIGHT_SCALE;
        iconBackground = new Image(UI.SKIN_ATLAS.findRegion(UI.USER_ICON_BG));
        iconBackground.setBounds(0, 0, height, height);

        icon = userIcon;
        icon.setBounds(iconPad, iconPad, height - iconPad * 2.0f, height - iconPad * 2.0f);

        LabelStyle labelStyle = new LabelStyle(Color.WHITE, 20);
        nicknameLabel = new Label(friend.getNickname(), labelStyle);
        nicknameLabel.setAlignment(Align.center);
        nicknameLabel.setBounds(height, 0, width - height, height);
    }
}
