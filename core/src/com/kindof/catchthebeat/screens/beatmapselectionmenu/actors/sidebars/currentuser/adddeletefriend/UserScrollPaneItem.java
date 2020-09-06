package com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebars.currentuser.adddeletefriend;

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
import com.kindof.catchthebeat.database.IDatabase;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.tools.Font;
import com.kindof.catchthebeat.users.User;

public class UserScrollPaneItem extends Table {
    private String uid, nickname;
    private float width, height;
    private Label nicknameLabel;
    private CheckBox checkBox;

    public UserScrollPaneItem(String uid, String nickname, float width, float height) {
        this.uid = uid;
        this.nickname = nickname;
        this.width = width;
        this.height = height;

        init();
        addActor(checkBox);
        addActor(nicknameLabel);
    }

    private void init() {
        final float checkBoxPad = 130 * Res.RESOLUTION_HEIGHT_SCALE;
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.WHITE;
        labelStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (40.0f * Res.RESOLUTION_HEIGHT_SCALE));

        nicknameLabel = new Label(nickname, labelStyle);
        nicknameLabel.setAlignment(Align.left);
        nicknameLabel.setBounds(height - checkBoxPad, 0, width - height + checkBoxPad, height);
        nicknameLabel.setTouchable(Touchable.disabled);

        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.fontColor = Color.WHITE;
        checkBoxStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (45 * Res.RESOLUTION_HEIGHT_SCALE));
        checkBoxStyle.checkboxOff = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.CHECKBOX_OFF)) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                float mWidth, mHeight, mX, mY;
                mWidth = mHeight = UserScrollPaneItem.this.height - checkBoxPad * 2.0f;
                mY = y - (mHeight - height) / 2.0f;
                mX = checkBoxPad / 2.0f;
                super.draw(batch, mX, mY, mWidth, mHeight);
            }
        };
        checkBoxStyle.checkboxOn = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.CHECKBOX_ON)) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                float mWidth, mHeight, mX, mY;
                mWidth = mHeight = UserScrollPaneItem.this.height - checkBoxPad * 2.0f;
                mY = y - (mHeight - height) / 2.0f;
                mX = checkBoxPad / 2.0f;
                super.draw(batch, mX, mY, mWidth, mHeight);
            }
        };

        checkBox = new CheckBox("", checkBoxStyle);
        checkBox.setBounds(0, 0, width, height);
        checkBox.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                IDatabase database = Res.GAME.getDatabase();
                User friend = database.getUser(uid);
                if (checkBox.isChecked()) {
                    Res.USER.addToFriendsList(uid);
                    friend.addToFriendsList(Res.USER.getUid());
                } else {
                    database.removeChat(Res.USER.getChatId(friend.getUid()).longValue());
                    Res.USER.deleteFromFriendsList(uid);
                    friend.deleteFromFriendsList(Res.USER.getUid());
                }
                database.setUser(Res.USER);
                database.setUser(friend);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {

            }
        });
        if (Res.USER.userInFriendList(uid))
            checkBox.setChecked(true);
    }

    public String getNickname() {
        return nickname;
    }
}
