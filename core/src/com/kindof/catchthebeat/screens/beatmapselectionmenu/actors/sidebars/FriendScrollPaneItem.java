package com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebars;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kindof.catchthebeat.Chat;
import com.kindof.catchthebeat.database.IDatabase;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.chat.ChatScreen;
import com.kindof.catchthebeat.tools.Font;
import com.kindof.catchthebeat.ui.actors.scrollpanes.ScrollPane;
import com.kindof.catchthebeat.users.User;

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
                    IDatabase database = Res.GAME.getDatabase();
                    PRESSED = true;
                    Integer chatId = friend.getChatId(Res.USER.getUid());
                    Chat chat = database.getChat(chatId);
                    if (chat == null) {
                        chat = new Chat(chatId);
                        database.putChat(chat);
                    }
                    if (!Res.CHAT_SCREENS.containsKey(chatId)) {
                        Res.CURRENT_CHAT_SCREEN = new ChatScreen(chat);
                        Res.CHAT_SCREENS.put(chatId, Res.CURRENT_CHAT_SCREEN);
                    } else {
                        Res.CURRENT_CHAT_SCREEN = Res.CHAT_SCREENS.get(chatId);
                    }
                    Res.CURRENT_CHAT_SCREEN.reInitMessageItems(chat.getChatMessages());
                    Res.GAME.setScreen(Res.CURRENT_CHAT_SCREEN);
                }
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
            }
        });
    }

    private void init(Image userIcon) {
        float iconPad = 3 * Res.RESOLUTION_HEIGHT_SCALE;
        iconBackground = new Image(Res.SKIN_ATLAS.findRegion(Res.USER_ICON_BACKGROUND));
        iconBackground.setBounds(0, 0, height, height);

        icon = userIcon;
        icon.setBounds(iconPad, iconPad, height - iconPad * 2.0f, height - iconPad * 2.0f);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.WHITE;
        labelStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (20 * Res.RESOLUTION_HEIGHT_SCALE));

        nicknameLabel = new Label(friend.getNickname(), labelStyle);
        nicknameLabel.setAlignment(Align.center);
        nicknameLabel.setBounds(height, 0, width - height, height);
    }
}
