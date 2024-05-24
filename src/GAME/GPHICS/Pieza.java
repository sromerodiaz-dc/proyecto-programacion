package GAME.GPHICS;

import java.awt.image.BufferedImage;
/**
 * Clase que define la interacción de las piezas con el entorno y el jugador.
 *
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class Pieza {
    /**
     * La imagen de la pieza.
     */
    public BufferedImage image;

    /**
     * Indica si la pieza ha colisionado con el entorno o el jugador.
     */
    public boolean colision = false;

    /**
     * Constructor que inicializa la pieza con un valor de colisión.
     *
     * @param b El valor de colisión de la pieza.
     */
    public Pieza(boolean b) {
        colision = b;
    }

    /**
     * Constructor por defecto.
     */
    public Pieza(){}
}
