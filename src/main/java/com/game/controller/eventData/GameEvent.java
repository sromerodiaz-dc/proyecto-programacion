package com.game.controller.eventData;

/**
 * Clase inmutable y concisa que se usa para representar datos sin necesidad de escribir
 * el código boilerplate típico de los POJOs (constructores, getters, equals, hashCode, toString, etc.).
 */
public record GameEvent(EventType type, int col, int row, String message, int value) {
}