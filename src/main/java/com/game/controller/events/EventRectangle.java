package com.game.controller.events;

import java.awt.Rectangle;

public class EventRectangle extends Rectangle {
    private static final int TILE_SIZE = 48;
    private int cooldown = 0;
    private final int cooldownLimit; // En frames (30 frames = 1 segundo)
    private boolean triggered = false;
    private final char direction;
    private final EventType type;
    private final String message;
    private final int value;
    private boolean done = false; // Marca si el evento es de una sola vez
    private final int tileSize;

    public EventRectangle(int col, int row, int tileSize, int width, int height,
                          EventType type, char direction, int cooldownLimit,
                          String message, int value) {
        // Calcula la posición del rectángulo en el mapa basado en TILE_SIZE_WITH_OFFSET
        super(col * TILE_SIZE, row * TILE_SIZE, width, height);
        this.tileSize = tileSize;
        this.type = type;
        this.direction = direction;
        this.cooldownLimit = cooldownLimit * 30; // Convertir segundos a frames
        this.message = message;
        this.value = value;
    }

    public void updateCooldown() {
        if (cooldown > 0) {
            cooldown--;
            if (cooldown == 0) {
                triggered = false;
            }
        }
    }

    public void trigger() {
        if (!done) {
            triggered = true;
            cooldown = cooldownLimit;
            done = true; // Marca el evento como completado si no puede repetirse
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

    // Verifica si el evento puede ser activado (no ha sido disparado y no está en cooldown)
    public boolean canTrigger() {  // Eliminamos el parámetro de dirección
        System.out.println("DEBUG Evento: done=" + done + ", cooldown=" + cooldown);
        return !done && !isOnCooldown();  // Eliminamos la verificación de dirección
    }

    // Getters
    public boolean isTriggered() { return triggered; }
    public char getDirection() { return direction; }
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

    public int getTileSize() {
        return tileSize;
    }

    // Metodo para obtener la columna donde se encuentra el evento
    public int getCol() {
        return (x - 23) / tileSize;
    }

    // Metodo para obtener la fila donde se encuentra el evento
    public int getRow() {
        return (y - 23) / tileSize;
    }
}

