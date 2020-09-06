package com.kindof.catchthebeat.screens.beatmapeditor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.LinkedList;

public class FruitList extends LinkedList<FruitEntity> {
    private FruitEntity selectedItem, previousSelectedItem;
    private Table table;
    private boolean checkPosition;

    public static final Color SELECTED_ITEM_COLOR = new Color(0.65f, 0.65f, 0.65f, 1f);
    public static final Color UNSELECTED_ITEM_COLOR = Color.WHITE;

    public FruitList(Table table) {
        this.table = table;
        checkPosition = false;
        resetCurrentPreviousSelectedItem();
    }

    public void update(float scrollSpeed, float delta) {
        for (FruitEntity fruitEntity : this) {
            fruitEntity.update(scrollSpeed, delta);
            if (checkPosition) {
                fruitEntity.onPositionChanged(scrollSpeed * delta);
            }
        }
        checkPosition(false);
    }

    public void checkPosition(boolean checkPosition) {
        this.checkPosition = checkPosition;
    }

    public boolean overlapWithAnyFruit(float x, float y) {
        for (FruitEntity fruitEntity : this) {
            // ax = actor-x | ay = actor-y | aw = actor-width | ah = actor-height
            Actor actor = fruitEntity.getActor();
            float ax = actor.getX(), ay = actor.getY(), aw = actor.getWidth(), ah = actor.getHeight();
            if (x > ax && x < ax + aw && y > ay && y < ay + ah)
                return true;
        }

        return false;
    }

    public boolean markItemAsSelected(FruitEntity item) {
        item.getActor().setColor(SELECTED_ITEM_COLOR);

        if (selectedItem != null) {
            previousSelectedItem = selectedItem;
            markItemAsUnselected(previousSelectedItem);
        }

        selectedItem = item;
        return selectedItem == previousSelectedItem;
    }

    public void markItemAsUnselected(FruitEntity item) {
        item.getActor().setColor(UNSELECTED_ITEM_COLOR);
    }

    public FruitEntity getSelectedItem() {
        return selectedItem;
    }

    public void resetCurrentPreviousSelectedItem() {
        selectedItem = previousSelectedItem = null;
    }

    private void addToTable(FruitEntity item) {
        table.addActor(item.getActor());
        markItemAsUnselected(item);
    }

    private void deleteFromTable(FruitEntity item) {
        table.removeActor(item.getActor());
    }

    @Override
    public void clear() {
        for (FruitEntity fruitEntity : this)
            deleteFromTable(fruitEntity);
        super.clear();
    }

    @Override
    public void add(int index, FruitEntity item) {
        addToTable(item);
        super.add(index, item);
    }

    @Override
    public boolean add(FruitEntity item) {
        addToTable(item);
        return super.add(item);
    }

    public boolean delete(FruitEntity item) {
        deleteFromTable(item);
        resetCurrentPreviousSelectedItem();
        return remove(item);
    }

    public Table getTable() {
        return table;
    }
}
