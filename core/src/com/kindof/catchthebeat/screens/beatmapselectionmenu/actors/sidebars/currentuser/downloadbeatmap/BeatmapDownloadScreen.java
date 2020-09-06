package com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebars.currentuser.downloadbeatmap;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kindof.catchthebeat.database.IDatabase;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.screens.beatmapeditor.BeatmapEntity;
import com.kindof.catchthebeat.ui.actors.scrollpanes.ScrollPane;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebars.currentuser.CurrentUserScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BeatmapDownloadScreen extends BaseScreen {
    private CurrentUserScreen currentUserScreen;
    private ScrollPane<ScrollPaneItem> scrollPane;
    private Table scrollPaneTable;
    private ArrayList<ScrollPaneItem> scrollPaneItems;
    private float itemWidth, itemHeight;
    private int index, MAX_ITEMS_ON_SCREEN;

    public BeatmapDownloadScreen(CurrentUserScreen currentUserScreen) {
        this.currentUserScreen = currentUserScreen;
        initialize();
    }

    private void initScrollPane() {
        index = 0;
        MAX_ITEMS_ON_SCREEN = 2;
        itemWidth = Res.WIDTH;
        itemHeight = Res.HEIGHT / (MAX_ITEMS_ON_SCREEN - 1);

        scrollPaneTable = new Table();
        scrollPaneTable.setTouchable(Touchable.enabled);
        scrollPane = new ScrollPane<>(scrollPaneTable);
        rootTable.add(scrollPane).size(rootTable.getWidth(), rootTable.getHeight());
        rootTable.pack();
        scrollPane.setMAX_ITEMS_ON_SCREEN(MAX_ITEMS_ON_SCREEN);
        scrollPane.setVisibleItemHeight(itemHeight);

        IDatabase database = Res.GAME.getDatabase();
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
        ScrollPaneItem item = new ScrollPaneItem(scrollPane, beatmapEntity, itemWidth, itemHeight);
        scrollPaneTable.add(item).size(itemWidth, itemHeight).pad(0, 0, 0, 0).row();
        scrollPaneItems.add(item);
        if (index > MAX_ITEMS_ON_SCREEN)
            item.setVisible(false);
        index++;
    }

    @Override
    public void initialize() {
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Res.GAME.setScreen(BeatmapDownloadScreen.this.currentUserScreen);
                }

                return super.keyDown(keyCode);
            }
        };
        rootTable = new Table();
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setBounds(0, 0, Res.WIDTH, Res.HEIGHT);

        initScrollPane();

        stage.addActor(rootTable);
    }
}
