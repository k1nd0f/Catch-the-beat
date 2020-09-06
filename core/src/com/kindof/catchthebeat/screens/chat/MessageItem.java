package com.kindof.catchthebeat.screens.chat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.tools.Font;
import com.kindof.catchthebeat.users.Message;

class MessageItem extends Table {
    private float width, height;
    private Message message;
    private Label dateLabel, timeLabel, textLabel, senderLabel, nicknameLabel;
    private Label.LabelStyle downLabelStyle, upLabelStyle, timeLabelStyle, nicknameLabelStyle;

    MessageItem(Message message, float width, float height) {
        this.message = message;
        this.width = width;
        this.height = height;

        init();
    }

    private void init() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.WHITE;

        // Time + Sender
        float upHeight = height / 8, upWidth = width / 3, upY = height * 7 / 8;
        upLabelStyle = new Label.LabelStyle(labelStyle);
        upLabelStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (19 * Res.RESOLUTION_HEIGHT_SCALE));

        timeLabelStyle = new Label.LabelStyle(upLabelStyle);
        timeLabelStyle.fontColor = new Color(1f, 0.33f, 0.33f, 1f);

        String nickname = Res.GAME.getDatabase().getUser(message.getSenderUid()).getNickname();
        nicknameLabelStyle = new Label.LabelStyle(upLabelStyle);
        nicknameLabelStyle.fontColor = nickname.equals(Res.USER.getNickname()) ? Color.GOLDENROD : Color.GREEN;

        String[] dateS = message.getTime().split("//");
        dateLabel = new Label(dateS[0], upLabelStyle);
        dateLabel.setAlignment(Align.center);
        dateLabel.setBounds(0, upY, upWidth, upHeight);
        dateLabel.setEllipsis(true);

        timeLabel = new Label(dateS[1], timeLabelStyle);
        timeLabel.setAlignment(Align.center);
        timeLabel.setBounds(upWidth, upY, upWidth, upHeight);

        senderLabel = new Label("Sender: ", upLabelStyle);
        senderLabel.setAlignment(Align.right);
        senderLabel.setBounds(upWidth * 2, upY, upWidth / 2, upHeight);
        senderLabel.setEllipsis(true);

        nicknameLabel = new Label(" " + nickname, nicknameLabelStyle);
        nicknameLabel.setAlignment(Align.left);
        nicknameLabel.setBounds(upWidth * 2.5f, upY, upWidth / 2, upHeight);
        nicknameLabel.setEllipsis(true);

        // Text
        downLabelStyle = new Label.LabelStyle();
        downLabelStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (25 * Res.RESOLUTION_HEIGHT_SCALE));
        downLabelStyle.fontColor = Color.LIGHT_GRAY;

        textLabel = new Label(message.getText(), downLabelStyle);
        textLabel.setAlignment(Align.topLeft);
        textLabel.setBounds(0, 0, width, height * 7 / 8);
        textLabel.setWrap(true);

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
