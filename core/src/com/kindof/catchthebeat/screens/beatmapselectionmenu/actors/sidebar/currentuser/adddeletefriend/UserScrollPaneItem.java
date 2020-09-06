package com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar.currentuser.adddeletefriend;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kindof.catchthebeat.database.IDatabase;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.ui.actors.scrollpane.ScrollPane;
import com.kindof.catchthebeat.ui.styles.CheckBoxStyle;
import com.kindof.catchthebeat.user.User;

public class UserScrollPaneItem extends Table {
    private User user;
    private float width, height;
    private CheckBox checkBox;
    private ScrollPane<UserScrollPaneItem> scrollPane;

    public UserScrollPaneItem(ScrollPane<UserScrollPaneItem> scrollPane, User user, float width, float height) {
        this.scrollPane = scrollPane;
        this.user = user;
        this.width = width;
        this.height = height;

        init();
    }

    private void init() {
        CheckBoxStyle checkBoxStyle = new CheckBoxStyle(Color.GRAY, Color.WHITE, 35, height / 4.0f);
        checkBox = new CheckBox(user.getNickname(), checkBoxStyle);
        checkBox.setBounds(0, 0, width, height);
        checkBox.align(Align.left);
        checkBox.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                scrollPane.setHasScrolled(false);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!scrollPane.hasScrolled()) {
                    IDatabase database = Globals.GAME.getDatabase();
                    if (checkBox.isChecked()) {
                        Globals.USER.addToFriendsList(user.getUid());
                        user.addToFriendsList(Globals.USER.getUid());
                    } else {
                        Long chatId = Globals.USER.getChatId(user.getUid());
                        if (chatId != null) {
                            database.removeChat(chatId);
                        }
                        Globals.USER.deleteFromFriendsList(user.getUid());
                        user.deleteFromFriendsList(Globals.USER.getUid());
                    }
                    database.setUser(Globals.USER);
                    database.setUser(user);
                }
            }
        });
        if (Globals.USER.userInFriendList(user.getUid())) {
            checkBox.setChecked(true);
        }

        setTouchable(Touchable.childrenOnly);
        addActor(checkBox);
    }

    public String getNickname() {
        return user.getNickname();
    }
}
