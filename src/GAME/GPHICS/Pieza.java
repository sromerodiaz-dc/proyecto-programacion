package GAME.GPHICS;

import java.awt.image.BufferedImage;
/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Esta clase define la interacción de las piezas con el entorno y el jugador.
 * */
public class Pieza {
    // Atributos
    public BufferedImage image;
    public boolean colision = false;

    public Pieza(boolean b) {
        colision = b;
    }
    public Pieza(){}
}
