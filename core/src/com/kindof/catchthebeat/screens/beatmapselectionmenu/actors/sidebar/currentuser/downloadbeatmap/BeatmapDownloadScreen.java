package com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar.currentuser.downloadbeatmap;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kindof.catchthebeat.database.IDatabase;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.beatmaps.BeatmapEntity;
import com.kindof.catchthebeat.ui.actors.scrollpane.ScrollPane;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar.currentuser.CurrentUserScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BeatmapDownloadScreen extends BaseScreen {
    private CurrentUserScreen currentUserScreen;
    private ScrollPane<BeatmapScrollPaneItem> scrollPane;
    private Table scrollPaneTable;
    private ArrayList<BeatmapScrollPaneItem> scrollPaneItems;
    private float itemWidth, itemHeight;
    private int index, maxItemsOnScreen;

    public BeatmapDownloadScreen(CurrentUserScreen currentUserScreen) {
        this.currentUserScreen = currentUserScreen;
        initialize();
    }

    private void initScrollPane() {
        index = 0;
        maxItemsOnScreen = 2;
        itemWidth = Globals.WIDTH;
        itemHeight = Globals.HEIGHT / (maxItemsOnScreen - 1);

        scrollPaneTable = new Table();
        scrollPaneTable.setTouchable(Touchable.enabled);
        scrollPane = new ScrollPane<>(scrollPaneTable);
        rootTable.add(scrollPane).width(Globals.WIDTH);
        scrollPane.setMaxItemsOnScreen(maxItemsOnScreen);
        scrollPane.setVisibleItemHeight(itemHeight);

        IDatabase database = Globals.GAME.getDatabase();
        Long beatmapCount = database.getBeatmapCount();
        HashMap<Long, BeatmapEntity> beatmaps = database.getBeatmaps();
        scrollPaneItems = new ArrayList<>(Integer.parseInt(beatmapCount.toString()));
        for (Map.Entry<Long, BeatmapEntity> entry : beatmaps.entrySet()) {
            addScrollPaneItem(entry.getValue());
        }

        scrollPane.setItems(scrollPaneItems);
        rootTable.addActor(scrollPane);
    }

    public void addScrollPaneItem(BeatmapEntity beatmapEntity) {
        BeatmapScrollPaneItem item = new BeatmapScrollPaneItem(scrollPane, beatmapEntity, itemWidth, itemHeight);
        scrollPaneTable.add(item).size(itemWidth, itemHeight).row();
        scrollPaneItems.add(item);
        if (index > maxItemsOnScreen) {
            item.setVisible(false);
        }
        index++;
    }

    @Override
    public void initialize() {
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Globals.GAME.setScreenWithTransition(BeatmapDownloadScreen.this.currentUserScreen);
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setBounds(0, 0, Globals.WIDTH, Globals.HEIGHT);

        initScrollPane();

        stage.addActor(rootTable);
    }
}
