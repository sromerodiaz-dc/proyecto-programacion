package com.game.controller.events;

import com.game.entity.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EventManager implementa el patrón Observer para manejar eventos
 * del juego de manera desacoplada y extensible.
 */
public class EventManager {
    private final List<EventRectangle> eventRectangles;
    private final List<EventListener> listeners;

    public EventManager() {
        this.eventRectangles = new ArrayList<>();
        this.listeners = new ArrayList<>();
    }

    /**
     * Carga una nueva lista de rectángulos de evento.
     */
    public void loadEvents(List<EventRectangle> events) {
        eventRectangles.clear();
        eventRectangles.addAll(events);
    }

    /**
     * Registra un nuevo listener.
     */
    public void addListener(EventListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Elimina un listener.
     */
    public void removeListener(EventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Resetea todos los eventos.
     */
    public void resetAllEvents() {
        for (EventRectangle event : eventRectangles) {
            event.reset();
        }
    }

    /**
     * Comprueba si algún evento debe activarse en base a la posición del jugador.
     */
    public void checkEvents(Player player) {
        for (EventRectangle event : eventRectangles) {
            event.updateCooldown();

            // Calcular la posición absoluta del área sólida del jugador
            int playerSolidX = player.worldX + player.solidArea.x;
            int playerSolidY = player.worldY + player.solidArea.y;

            // Calcular la posición absoluta del evento
            int eventX = event.x;
            int eventY = event.y;

            /*
                System.out.println("---- DEPURACIÓN ----");
                System.out.println("Jugador: solidX = " + playerSolidX + ", solidY = " + playerSolidY);
                System.out.println("Rectángulo evento: x = " + eventX + ", y = " + eventY);
                System.out.println("Tamaño jugador: " + player.solidArea.width + "x" + player.solidArea.height);
                System.out.println("Tamaño evento: " + event.width + "x" + event.height);
             */

            // Crear clones temporales para la colisión
            Rectangle playerHitbox = new Rectangle(playerSolidX, playerSolidY, player.solidArea.width, player.solidArea.height);
            Rectangle eventHitbox = new Rectangle(eventX, eventY, event.width, event.height);

            if (playerHitbox.intersects(eventHitbox)) {
                // Filtrar entrada si el tipo de evento lo requiere
                boolean canCheckInput = (event.getType() != EventType.HEAL || player.getKeyboardController().isPressed);

                if (canCheckInput && event.canTrigger()) {
                    event.trigger();

                    notifyListeners(new GameEvent(
                            event.getType(),
                            event.getCol(),
                            event.getRow(),
                            event.getDirection(),
                            event.getMessage(),
                            event.getValue()
                    ));
                } else {
                    System.out.println("No se puede activar todavía (cooldown o dirección incorrecta).");
                }
            }
        }
    }

    /**
     * Notifica a todos los listeners registrados sobre un evento.
     */
    private void notifyListeners(GameEvent event) {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
