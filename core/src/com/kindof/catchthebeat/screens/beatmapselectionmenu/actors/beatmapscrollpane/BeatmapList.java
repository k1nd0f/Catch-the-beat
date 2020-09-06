package com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.beatmapscrollpane;

import java.util.ArrayList;

public class BeatmapList extends ArrayList<BeatmapScrollPaneItem> {
    private BeatmapScrollPaneItem selectedItem, previousSelectedItem;
    public static final int MAX_ITEMS_ON_SCREEN = 5;

    public BeatmapList() {
        resetCurrentPreviousSelectedItem();
    }

    public boolean markItemAsSelected(BeatmapScrollPaneItem item) {
        item.setColor(1, 1, 1, BeatmapScrollPaneItem.SELECTED_ALPHA);
        item.getBeatmapBackgroundImage().setColor(BeatmapScrollPaneItem.SELECTED_BEATMAP_BACKGROUND_IMAGE_COLOR);
        item.getBackgroundImage().setColor(BeatmapScrollPaneItem.SELECTED_BACKGROUND_IMAGE_COLOR);
        item.getLabelArtist().getStyle().fontColor = BeatmapScrollPaneItem.SELECTED_ARTIST_FONT_COLOR;
        item.getLabelTitle().getStyle().fontColor = BeatmapScrollPaneItem.SELECTED_TITLE_FONT_COLOR;

        if (selectedItem != null) {
            previousSelectedItem = selectedItem;
            markItemAsUnselected(previousSelectedItem);
        }

        selectedItem = item;
        return selectedItem == previousSelectedItem;
    }

    private void markItemAsUnselected(BeatmapScrollPaneItem item) {
        item.setColor(1, 1, 1, BeatmapScrollPaneItem.UNSELECTED_ALPHA);
        item.getBeatmapBackgroundImage().setColor(BeatmapScrollPaneItem.UNSELECTED_BEATMAP_BACKGROUND_IMAGE_COLOR);
        item.getBackgroundImage().setColor(BeatmapScrollPaneItem.UNSELECTED_BACKGROUND_IMAGE_COLOR);
        item.getLabelArtist().getStyle().fontColor = BeatmapScrollPaneItem.UNSELECTED_ARTIST_FONT_COLOR;
        item.getLabelTitle().getStyle().fontColor = BeatmapScrollPaneItem.UNSELECTED_TITLE_FONT_COLOR;
    }

    public BeatmapScrollPaneItem getSelectedItem() {
        return selectedItem;
    }

    public void resetCurrentPreviousSelectedItem() {
        selectedItem = previousSelectedItem = null;
    }

    @Override
    public boolean add(BeatmapScrollPaneItem item) {
        markItemAsUnselected(item);
        return super.add(item);
    }
}
