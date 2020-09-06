package com.kindof.catchthebeat.ui.actors.scrollpane;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import java.util.ArrayList;

public class ScrollPane<T extends Table> extends com.badlogic.gdx.scenes.scene2d.ui.ScrollPane {
    private ArrayList<T> items;
    private float visibleItemHeight, delta;
    private Float lastPixelsY;
    private int lastItemIndex;
    private boolean hasScrolled;
    private int maxItemsOnScreen;

    public ScrollPane(Actor widget) {
        super(widget);
        init();
    }

    private void init() {
        hasScrolled = false;
        lastPixelsY = null;
        items = null;
        setTouchable(Touchable.childrenOnly);
        setFadeScrollBars(false);
        setForceScroll(false, true);
        setSmoothScrolling(true);
        setScrollingDisabled(true, false);
    }

    @Override
    protected void scrollY(float pixelsY) {
        if (items != null && items.size() > 0) {
            hasScrolled = true;
            int index = -1;
            if (lastPixelsY != null) {
                float deltaY = pixelsY - lastPixelsY;
                delta += deltaY;
                index = (int) (delta / visibleItemHeight) - 1;
                if (index >= 0 && lastItemIndex >= 0) {
                    if (deltaY > 0) {
                        for (int i = lastItemIndex; i <= index; i++) {
                            items.get(i).setVisible(false);
                        }
                        if (index + maxItemsOnScreen < items.size()) {
                            for (int i = lastItemIndex + maxItemsOnScreen; i <= index + maxItemsOnScreen; i++) {
                                items.get(i).setVisible(true);
                            }
                        }
                    } else if (deltaY < 0) {
                        for (int i = lastItemIndex; i >= index; i--) {
                            items.get(i).setVisible(true);
                        }
                        if (index + maxItemsOnScreen + 1 < items.size()) {
                            for (int i = lastItemIndex + maxItemsOnScreen - 1; i >= index + maxItemsOnScreen + 1; i--) {
                                items.get(i).setVisible(false);
                            }
                        }
                    }
                }
            }
            super.scrollY(pixelsY);
            lastPixelsY = pixelsY;
            lastItemIndex = index;
        }
    }

    public void setMaxItemsOnScreen(int maxItemsOnScreen) {
        this.maxItemsOnScreen = maxItemsOnScreen;
    }

    public void setItems(ArrayList<T> items) {
        this.items = items;
    }

    public void clearItems() {
        items.clear();
    }

    public void setVisibleItemHeight(float visibleItemHeight) {
        this.visibleItemHeight = visibleItemHeight;
    }

    public void setHasScrolled(boolean hasScrolled) {
        this.hasScrolled = hasScrolled;
    }

    public boolean hasScrolled() {
        return hasScrolled;
    }
}
