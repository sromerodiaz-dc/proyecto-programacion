package com.game.controller.eventData;

public interface EventListener {
    /** Se llamará cuando EventManager detecte un GameEvent */
    void onEvent(GameEvent event);
}