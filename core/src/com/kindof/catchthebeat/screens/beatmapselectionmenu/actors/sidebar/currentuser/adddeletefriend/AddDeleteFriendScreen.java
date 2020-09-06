package com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar.currentuser.adddeletefriend;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.ui.actors.scrollpane.ScrollPane;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar.currentuser.CurrentUserScreen;
import com.kindof.catchthebeat.ui.styles.TextFieldStyle;
import com.kindof.catchthebeat.user.User;

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
    private int maxItemsOnScreen;
    private int index;

    public AddDeleteFriendScreen(CurrentUserScreen currentUserScreen) {
        this.currentUserScreen = currentUserScreen;
        initialize();
    }

    private void initUsersScrollPane() {
        float width = Globals.WIDTH, height = Globals.HEIGHT - textField.getHeight();
        float horizontalPad = 30 * Globals.RESOLUTION_WIDTH_SCALE;
        tableForUsersScrollPane = new Table();
        tableForUsersScrollPane.setVisible(true);
        tableForUsersScrollPane.setTouchable(Touchable.childrenOnly);
        tableForUsersScrollPane.setBounds(horizontalPad / 2.0f, 0, width - horizontalPad, height);

        usersScrollPaneTable = new Table();
        usersScrollPaneTable.setTouchable(Touchable.enabled);

        usersScrollPane = new ScrollPane<>(usersScrollPaneTable);
        tableForUsersScrollPane.add(usersScrollPane).width(width);

        rootTable.addActor(tableForUsersScrollPane);
    }

    public void initUserScrollPaneItems() {
        index = 0;
        maxItemsOnScreen = 2;
        itemWidth = tableForUsersScrollPane.getWidth();
        itemHeight = tableForUsersScrollPane.getHeight() / (maxItemsOnScreen - 1);
        usersScrollPane.setMaxItemsOnScreen(maxItemsOnScreen);
        for (Map.Entry<String, User> entry : Globals.GAME.getDatabase().getUsers().entrySet()) {
            User user = entry.getValue();
            if (user == null || user == Globals.USER) {
                continue;
            }
            UserScrollPaneItem userScrollPaneItem = new UserScrollPaneItem(usersScrollPane, user, itemWidth, itemHeight);
            if (index > maxItemsOnScreen) {
                userScrollPaneItem.setVisible(false);
            }
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
                if (index > maxItemsOnScreen) {
                    userScrollPaneItem.setVisible(false);
                } else {
                    userScrollPaneItem.setVisible(true);
                }
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
                    Globals.BEATMAP_SELECTION_MENU_SCREEN.getSideBar().initFriendScrollPaneItems();
                    Globals.GAME.setScreenWithTransition(AddDeleteFriendScreen.this.currentUserScreen);
                }
                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setVisible(true);
        userScrollPaneItems = new ArrayList<>();

        float textFieldWidth = Globals.WIDTH, textFieldHeight = Globals.HEIGHT / 7.0f;
        TextFieldStyle textFieldStyle = new TextFieldStyle(Color.WHITE, 35);
        textField = new TextField("", textFieldStyle);
        textField.setAlignment(Align.center);
        textField.setBounds(0, Globals.HEIGHT - textFieldHeight, textFieldWidth, textFieldHeight);
        textField.setTextFieldListener((textField, c) -> {
            String text = textField.getText();
            if (c != '\b' || text.length() == 0) {
                sortUsers(text);
            }
        });

        initUsersScrollPane();

        rootTable.addActor(textField);
        stage.addActor(rootTable);
    }
}
