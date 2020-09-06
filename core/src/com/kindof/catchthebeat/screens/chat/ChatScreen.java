package com.kindof.catchthebeat.screens.chat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kindof.catchthebeat.ui.Alignment;
import com.kindof.catchthebeat.ui.UI;
import com.kindof.catchthebeat.user.chat.Chat;
import com.kindof.catchthebeat.database.IDatabase;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.ui.actors.button.Button;
import com.kindof.catchthebeat.ui.actors.scrollpane.ScrollPane;
import com.kindof.catchthebeat.ui.styles.TextFieldStyle;
import com.kindof.catchthebeat.user.chat.Message;

import java.util.ArrayList;

public class ChatScreen extends BaseScreen {
    private Table messagesTable;
    private Button sendButton;
    private TextArea textArea;
    private ScrollPane<MessageItem> messagesScrollPane;
    private float itemWidth, itemHeight;
    private int maxItemsOnScreen;
    private ArrayList<MessageItem> messageItems;
    private Chat chat;
    private int index;

    public ChatScreen(Chat chat) {
        this.chat = chat;
        initialize();
    }

    @Override
    public void initialize() {
        index = 0;
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Globals.GAME.setScreenWithTransition(Globals.BEATMAP_SELECTION_MENU_SCREEN);
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setVisible(false);
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setFillParent(true);

        float sendButtonSize = Globals.HEIGHT / 8.0f;
        sendButton = new Button((event, x, y, pointer, button) -> sendMessage(), UI.SEND_BUTTON, UI.SEND_BUTTON);
        UI.calculateAndSetViewElementBounds(
                sendButton,
                Alignment.center, 0,
                Globals.WIDTH - sendButtonSize, Globals.HEIGHT - sendButtonSize, sendButtonSize, sendButtonSize
        );

        TextFieldStyle textFieldStyle = new TextFieldStyle(Color.WHITE, 25, UI.TEXT_AREA_BG);
        textArea = new TextArea("", textFieldStyle);
        textArea.setBounds(0, Globals.HEIGHT - sendButtonSize, Globals.WIDTH - sendButtonSize, sendButtonSize);
        textArea.setTextFieldListener((textField, c) -> {
            Gdx.input.setOnscreenKeyboardVisible(true);
            if (c == '\n') {
                int cursorLine = textArea.getCursorLine();
                textArea.moveCursorLine(cursorLine + 1);
            }
        });

        initScrollPane();

        addActors(textArea, sendButton);
        stage.addActor(rootTable);
    }

    private void initScrollPane() {
        messagesTable = new Table();
        messagesTable.setTouchable(Touchable.enabled);
        messagesTable.align(Align.center);

        messageItems = new ArrayList<>();
        maxItemsOnScreen = 3;
        itemHeight = Globals.HEIGHT - sendButton.getHeight();
        itemWidth = Globals.WIDTH;
        messagesScrollPane = new ScrollPane<>(messagesTable);
        messagesScrollPane.setMaxItemsOnScreen(maxItemsOnScreen);
        messagesScrollPane.setItems(messageItems);
        messagesScrollPane.setVisibleItemHeight(itemHeight);
        messagesScrollPane.setBounds(0, 0, Globals.WIDTH,  Globals.HEIGHT - sendButton.getHeight());

        addActors(messagesScrollPane);
    }

    private void addMessageItem(Message message) {
        MessageItem messageItem = new MessageItem(message, itemWidth, itemHeight);
        if (index > maxItemsOnScreen) {
            messageItem.setVisible(false);
        }
        messagesTable.add(messageItem).size(itemWidth, itemHeight).row();
        messageItems.add(messageItem);
        index++;
    }

    public void reInitMessageItems(final ArrayList<Message> messages) {
        for (Message message : messages) {
            boolean contains = false;
            for (MessageItem messageItem : messageItems) {
                if (messageItem.getMessage().compareTo(message) == 0) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                addMessageItem(message);
            }
        }
    }

    public void scrollToLastMessage() {
        Array<Cell> cells = messagesTable.getCells();
        if (cells.size > 1) {
            Cell lastCell = cells.get(cells.size - 1);
            messagesScrollPane.scrollTo(lastCell.getActorX(), lastCell.getActorY(), lastCell.getActorWidth(), lastCell.getActorHeight());
            lastCell.getActor().setVisible(true);
        }
    }

    private void sendMessage() {
        String text = textArea.getText();
        if (text.split(" ").length != 0 && !text.equals("")) {
            IDatabase database = Globals.GAME.getDatabase();
            Message message = database.createNewMessage(Globals.USER.getUid(), text);
            Globals.GAME.getDatabase().addMessage(chat.getId(), message);
            textArea.setText("");
            textArea.getOnscreenKeyboard().show(false);
        }
    }

    public Chat getChat() {
        return chat;
    }

    @Override
    public void hide() {
        super.hide();
        Globals.CURRENT_CHAT_SCREEN = null;
    }
}
