package com.game.controller.events;

public interface EventListener {
    /** Se llamará cuando EventManager detecte un GameEvent */
    void onEvent(GameEvent event);
}