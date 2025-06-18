package com.game.controller.eventData;

import java.awt.Rectangle;

public class EventRectangle extends Rectangle {
    private static final int TILE_SIZE = 48;
    private int cooldown = 0;
    private final int cooldownLimit; // En frames (30 frames = 1 segundo)
    private boolean triggered = false;
    private final EventType type;
    private final String message;
    private final int value;
    private boolean done = false; // Marca si el evento es de una sola vez

    public EventRectangle(int col, int row, int width, int height,
                          EventType type, int cooldownLimit,
                          String message, int value) {
        super(col * TILE_SIZE, row * TILE_SIZE, width, height);
        this.type = type;
        this.cooldownLimit = cooldownLimit * 30;
        this.message = message;
        this.value = value;
    }

    public boolean isSingleUse() {
        return cooldownLimit == 0;  // Si cooldown es 0, es de un solo uso
    }

    public void updateCooldown() {
        if (!isSingleUse()) {  // Solo actualizar cooldown para eventos repetibles
            if (cooldown > 0) {
                cooldown--;
                if (cooldown == 0) {
                    triggered = false;
                }
            }
        }
    }

    public void trigger() {
        if (isSingleUse()) {
            if (!done) {
                triggered = true;
                done = true;
            }
        } else {
            triggered = true;
            cooldown = cooldownLimit;
        }
    }

    public boolean canTrigger() {
        if (isSingleUse()) {
            return !done;
        } else {
            return !isOnCooldown();
        }
    }

    public void reset() {
        done = false; // Permite que el evento se dispare nuevamente
        triggered = false;
        cooldown = 0;
    }

    public boolean isOnCooldown() {
        return cooldown > 0;
    }

    // Getters
    public boolean isTriggered() { return triggered; }
    public EventType getType() { return type; }
    public String getMessage() { return message; }
    public int getValue() { return value; }
    public boolean isDone() { return done; }

    public int getCooldown() {
        return cooldown;
    }

    public int getCooldownLimit() {
        return cooldownLimit;
    }

    // Metodo para obtener la columna donde se encuentra el evento
    public int getCol() {
        return (x - 23) / TILE_SIZE;
    }

    // Metodo para obtener la fila donde se encuentra el evento
    public int getRow() {
        return (y - 23) / TILE_SIZE;
    }
}

