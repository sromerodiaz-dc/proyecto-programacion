package com.editor.model.event;

import com.editor.model.record.EventData;
import com.game.controller.eventData.EventType;

public class EventDataBuilder {
    public double startX;
    public double startY;
    private boolean firstPointSet = false;

    private EventData eventData;

    public void setFirstPoint(double x, double y) {  // Cambiar parámetros a double
        this.startX = x;
        this.startY = y;
        firstPointSet = true;
    }

    public void setSecondPoint(double x, double y) {  // Cambiar parámetros a double
        double scaleFactor = 48.0 / 32.0;

        double width = Math.abs(x - startX) * scaleFactor;
        double height = Math.abs(y - startY) * scaleFactor;

        if (width == 0) width = scaleFactor;
        if (height == 0) height = scaleFactor;

        double minX = Math.min(startX, x);
        double minY = Math.min(startY, y);

        eventData = new EventData(
                minY,   // row (double)
                minX,   // col (double)
                width,  // width (double)
                height, // height (double)
                EventType.DAMAGE,
                "", 0, 0,
                "", 0.0, 1.0
        );
    }


    public boolean isFirstPointSet() {
        return firstPointSet;
    }

    public EventData getEventData() {
        return eventData;
    }
}