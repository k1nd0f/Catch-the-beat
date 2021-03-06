package com.kindof.catchthebeat.gameobjects.fruit;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.kindof.catchthebeat.gameobjects.GameObject;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.ui.UI;

public class Fruit extends GameObject {
    private int count;
    private Overlap overlap;

    public Fruit(String type, float x, float diameter) {
        this(UI.SKIN_ATLAS.findRegion("fruit-" + type), x, Globals.HEIGHT + diameter, diameter);
    }

    public Fruit(TextureRegion texture, float x, float y, float diameter) {
        super(texture, x, y, diameter, diameter);
        bounds = new Rectangle(x + diameter / 2.0f, y + diameter * 0.25f, 0, diameter * 0.5f);
        overlap = new Overlap();
        resetCount();
    }

    public Fruit(String type, float x, float y, float diameter) {
        this(UI.SKIN_ATLAS.findRegion("fruit-" + type), x, y, diameter);
    }

    public Fruit(TextureRegion texture, float x, float diameter) {
        this(texture, x, Globals.HEIGHT + diameter, diameter);
    }

    @Override
    public void setY(float y) {
        bounds.y += y - this.y;
        this.y = y;
    }

    public Overlap boundsOverlap(Rectangle bounds) {
        if (this.bounds.y <= bounds.y + bounds.height) {
            count++;
        }

        if (count == 0 && this.bounds.x > bounds.x && this.bounds.x < bounds.x + bounds.width) {
            overlap.set(Overlap.Type.hit, 100);
        } else if (count > 0 && this.bounds.overlaps(bounds)) {
            overlap.set(Overlap.Type.hit, (this.bounds.height - (bounds.y - this.bounds.y)) / this.bounds.height * 100);
        } else if (this.bounds.y + this.bounds.height < 0) {
            overlap.set(Overlap.Type.miss, 0);
        }

        return overlap;
    }

    private void resetCount() {
        this.count = -1;
    }
}