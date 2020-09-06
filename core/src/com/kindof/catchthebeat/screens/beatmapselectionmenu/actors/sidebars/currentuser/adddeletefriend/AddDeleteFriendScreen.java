package com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebars.currentuser.adddeletefriend;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.ui.actors.scrollpanes.ScrollPane;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebars.currentuser.CurrentUserScreen;
import com.kindof.catchthebeat.tools.Font;
import com.kindof.catchthebeat.users.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class AddDeleteFriendScreen extends BaseScreen {
    private Table tableForUsersScrollPane, usersScrollPaneTable;
    private CurrentUserScreen currentUserScreen;
    private TextField textField;
    private ScrollPane<UserScrollPaneItem> usersScrollPane;
    private ArrayList<UserScrollPaneItem> userScrollPaneItems;
    private float itemWidth, itemHeight;
    private int MAX_ITEMS_ON_SCREEN;
    private int index;

    public AddDeleteFriendScreen(CurrentUserScreen currentUserScreen) {
        this.currentUserScreen = currentUserScreen;
        initialize();
    }

    private void initUsersScrollPane() {
        float width = Res.WIDTH, height = Res.HEIGHT - textField.getHeight();
        tableForUsersScrollPane = new Table();
        tableForUsersScrollPane.setVisible(true);
        tableForUsersScrollPane.setTouchable(Touchable.childrenOnly);
        tableForUsersScrollPane.setBounds(0, 0, width, height);

        usersScrollPaneTable = new Table();
        usersScrollPaneTable.setTouchable(Touchable.enabled);

        usersScrollPane = new ScrollPane<>(usersScrollPaneTable);
        usersScrollPane.getStyle().background = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.UI_ADD_DELETE_FRIEND_SCREEN_BACKGROUND));
        tableForUsersScrollPane.add(usersScrollPane).size(width, height);
        tableForUsersScrollPane.pack();

        rootTable.addActor(tableForUsersScrollPane);
    }

    public void initUserScrollPaneItems() {
        index = 0;
        MAX_ITEMS_ON_SCREEN = 2;
        itemWidth = tableForUsersScrollPane.getWidth();
        itemHeight = tableForUsersScrollPane.getHeight() / (MAX_ITEMS_ON_SCREEN - 1);
        usersScrollPane.setMAX_ITEMS_ON_SCREEN(MAX_ITEMS_ON_SCREEN);
        for (Map.Entry<String, User> entry : Res.GAME.getDatabase().getUsers().entrySet()) {
            User user = entry.getValue();
            if (user == Res.USER)
                continue;
            UserScrollPaneItem userScrollPaneItem = new UserScrollPaneItem(user.getUid(), user.getNickname(), itemWidth, itemHeight);
            if (index > MAX_ITEMS_ON_SCREEN)
                userScrollPaneItem.setVisible(false);
            usersScrollPaneTable.add(userScrollPaneItem).size(itemWidth, itemHeight).row();
            userScrollPaneItems.add(userScrollPaneItem);
            index++;
        }
        usersScrollPane.setVisibleItemHeight(itemHeight);
        usersScrollPane.setItems(userScrollPaneItems);
    }

    private void removeItemFromScrollPane(UserScrollPaneItem userScrollPaneItem) {
        Array<Cell> cells = usersScrollPaneTable.getCells();
        Cell cell = usersScrollPaneTable.getCell(userScrollPaneItem);
        cells.removeValue(cell, false);
        userScrollPaneItem.remove();
    }

    private void sortUsers(String key) {
        Iterator<UserScrollPaneItem> iterator = userScrollPaneItems.iterator();
        while (iterator.hasNext()) {
            UserScrollPaneItem userScrollPaneItem = iterator.next();
            String nickname = userScrollPaneItem.getNickname();
            if (!nickname.toLowerCase().contains(key.toLowerCase())) {
                removeItemFromScrollPane(userScrollPaneItem);
                index--;
            } else if (usersScrollPaneTable.getCell(userScrollPaneItem) == null) {
                usersScrollPaneTable.add(userScrollPaneItem).size(itemWidth, itemHeight).row();
                if (index > MAX_ITEMS_ON_SCREEN)
                    userScrollPaneItem.setVisible(false);
                else
                    userScrollPaneItem.setVisible(true);
                index++;
            }
        }
    }

    @Override
    public void initialize() {
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Res.BEATMAP_SELECTION_MENU_SCREEN.getSideBar().initFriendScrollPaneItems();
                    Res.GAME.setScreen(AddDeleteFriendScreen.this.currentUserScreen);
                }
                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setVisible(true);
        userScrollPaneItems = new ArrayList<>();

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = Font.getBitmapFont(Res.FONT_NAME, (int) (35 * Res.RESOLUTION_HEIGHT_SCALE));
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.TEXT_FIELD_BACKGROUND));
        textFieldStyle.cursor = new TextureRegionDrawable(Res.SKIN_ATLAS.findRegion(Res.TEXT_FIELD_CURSOR)) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                super.draw(batch, x, y, 4, height);
            }
        };

        float textFieldWidth = Res.WIDTH, textFieldHeight = Res.HEIGHT / 7.0f;
        textField = new TextField("", textFieldStyle);
        textField.setAlignment(Align.center);
        textField.setBounds(0, Res.HEIGHT - textFieldHeight, textFieldWidth, textFieldHeight);
        textField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                String text = textField.getText();
                if (c != '\b' || text.length() == 0) {
                    sortUsers(text);
                }
            }
        });

        initUsersScrollPane();

        rootTable.addActor(textField);
        stage.addActor(rootTable);
    }
}
