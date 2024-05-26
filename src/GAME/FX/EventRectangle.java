package GAME.FX;

import java.awt.*;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Clase que representa un rectángulo de evento en el mapa.
 * Extiende la clase Rectangle para agregar funcionalidades adicionales.
 */
public class EventRectangle extends Rectangle {
    /**
     * Posición x original del rectángulo de evento.
     */
    int defaultX;

    /**
     * Posición y original del rectángulo de evento.
     */
    int defaultY;

    /**
     * Tiempo de cooldown del evento en segundos.
     */
    int cooldown;

    /**
     * Indica si el evento ya se ha realizado.
     */
    boolean done = false;

    /**
     * Resetea el cooldown del evento.
     *
     * @param seg_cooldown tiempo de cooldown en segundos
     */
    public void resetCooldowns(int seg_cooldown) {
        // Incrementa el cooldown en 1 frame
        cooldown++;

        // Verifica si el cooldown ha expirado
        if (cooldown > seg_cooldown * 30) {
            // Resetea el cooldown y permite que el evento se vuelva a realizar
            cooldown = 0;
            done = false;
        }
    }
}
