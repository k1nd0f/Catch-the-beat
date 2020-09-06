package com.kindof.catchthebeat.screens.chat;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kindof.catchthebeat.Chat;
import com.kindof.catchthebeat.database.IDatabase;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.tools.Font;
import com.kindof.catchthebeat.ui.actors.buttons.Button;
import com.kindof.catchthebeat.ui.actors.buttons.TouchUpEventListener;
import com.kindof.catchthebeat.ui.actors.scrollpanes.ScrollPane;
import com.kindof.catchthebeat.users.Message;

import java.util.ArrayList;

public class ChatScreen extends BaseScreen {
    private Table messagesTable;
    private Button sendButton;
    private TextArea textArea;
    private TextField.TextFieldStyle textFieldStyle;
    private ScrollPane<MessageItem> messagesScrollPane;
    private float itemWidth, itemHeight;
    private int MAX_ITEMS_ON_SCREEN;
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
                    Res.GAME.setScreen(Res.BEATMAP_SELECTION_MENU_SCREEN);
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setVisible(false);
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setFillParent(true);

        float buttonSize = Res.HEIGHT / 8;
        sendButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                sendMessage();
            }
        }, Res.WIDTH - buttonSize, Res.HEIGHT - buttonSize, buttonSize, buttonSize, Res.SEND_MESSAGE_BUTTON_UP, Res.SEND_MESSAGE_BUTTON_PRESS);

        textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.background = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.TEXT_AREA_BACKGROUND));
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (25 * Res.RESOLUTION_HEIGHT_SCALE));
        textFieldStyle.cursor = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.TEXT_FIELD_CURSOR)) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                super.draw(batch, x, y, Res.TEXT_FIELD_CURSOR_WIDTH, height);
            }
        };

        textArea = new TextArea("", textFieldStyle);
        textArea.setBounds(0, Res.HEIGHT - buttonSize, Res.WIDTH - buttonSize, buttonSize);
        textArea.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                String text = textArea.getText();
                if (c == '\n') {
                    textArea.setText(text + "\n");
                    textArea.moveCursorLine(textArea.getCursorLine() + 1);
                    textArea.getOnscreenKeyboard().show(true);
                }
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
        MAX_ITEMS_ON_SCREEN = 3;
        itemHeight = (Res.HEIGHT - sendButton.getHeight()) / (MAX_ITEMS_ON_SCREEN - 2);
        itemWidth = Res.WIDTH;
        messagesScrollPane = new ScrollPane<>(messagesTable);
        messagesScrollPane.setMAX_ITEMS_ON_SCREEN(MAX_ITEMS_ON_SCREEN);
        messagesScrollPane.setItems(messageItems);
        messagesScrollPane.setVisibleItemHeight(itemHeight);
        messagesScrollPane.setBounds(0, 0, Res.WIDTH,  Res.HEIGHT - sendButton.getHeight());

        addActors(messagesScrollPane);
    }

    private void addMessageItem(Message message) {
        MessageItem messageItem = new MessageItem(message, itemWidth, itemHeight);
        if (index > MAX_ITEMS_ON_SCREEN) {
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
            IDatabase database = Res.GAME.getDatabase();
            Message message = database.createNewMessage(Res.USER.getUid(), text);
            Res.GAME.getDatabase().addMessage(chat.getId(), message);
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
        Res.CURRENT_CHAT_SCREEN = null;
    }
}
