package com.editor.model.record;

import com.game.controller.eventData.EventType;

public record EventData(
        double row,
        double col,
        double width,
        double height,
        EventType type,
        String message,
        int value,
        int cooldown,
        String texturePath,
        double rotation,
        double scale
) {
    public EventData withPosition(int newCol, int newRow) {
        return new EventData(newRow, newCol, width, height, type, message, value, cooldown, texturePath, rotation, scale);
    }

    public EventData withRotation(double newRotation) {
        return new EventData(row, col, width, height, type, message, value, cooldown, texturePath, newRotation, scale);
    }

    public EventData withScale(double newScale) {
        return new EventData(row, col, width, height, type, message, value, cooldown, texturePath, rotation, newScale);
    }
}