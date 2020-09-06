package com.kindof.catchthebeat.screens.chat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.ui.styles.LabelStyle;
import com.kindof.catchthebeat.user.chat.Message;

class MessageItem extends Table {
    private float width, height;
    private Message message;

    MessageItem(Message message, float width, float height) {
        this.message = message;
        this.width = width;
        this.height = height;

        init();
    }

    private void init() {
        // Time + Sender
        float upHeight = height / 8, upWidth = width / 3, upY = height * 7 / 8;
        String[] dateS = message.getTime().split("//");

        LabelStyle upLabelStyle = new LabelStyle(Color.WHITE, 19);
        Label dateLabel = new Label(dateS[0], upLabelStyle);
        dateLabel.setAlignment(Align.center);
        dateLabel.setBounds(0, upY, upWidth, upHeight);

        LabelStyle timeLabelStyle = new LabelStyle(new Color(1f, 0.33f, 0.33f, 1f), 19);
        Label timeLabel = new Label(dateS[1], timeLabelStyle);
        timeLabel.setAlignment(Align.center);
        timeLabel.setBounds(upWidth, upY, upWidth, upHeight);

        Label senderLabel = new Label("Sender: ", upLabelStyle);
        senderLabel.setAlignment(Align.right);
        senderLabel.setBounds(upWidth * 2, upY, upWidth / 2, upHeight);

        String nickname = Globals.GAME.getDatabase().getUser(message.getSenderUid()).getNickname();
        LabelStyle nicknameLabelStyle = new LabelStyle(nickname.equals(Globals.USER.getNickname()) ? Color.GOLDENROD : Color.GREEN, 19);
        Label nicknameLabel = new Label(" " + nickname, nicknameLabelStyle);
        nicknameLabel.setAlignment(Align.left);
        nicknameLabel.setBounds(upWidth * 2.5f, upY, upWidth / 2, upHeight);
        nicknameLabel.setEllipsis(true);

        // Text
        LabelStyle downLabelStyle = new LabelStyle(Color.LIGHT_GRAY, 25);
        Label textLabel = new Label(message.getText(), downLabelStyle);
        textLabel.setAlignment(Align.topLeft);
        textLabel.setBounds(0, 0, width, height * 7 / 8);
        textLabel.setWrap(true);
        textLabel.setEllipsis(true);

        addActor(timeLabel);
        addActor(senderLabel);
        addActor(nicknameLabel);
        addActor(timeLabel);
        addActor(textLabel);
        addActor(dateLabel);
    }

    public Message getMessage() {
        return message;
    }
}
