package com.kindof.catchthebeat.ui.actors.button;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

public interface TouchUpEventListener {

    void touchUp(InputEvent event, float x, float y, int pointer, int button);
}
