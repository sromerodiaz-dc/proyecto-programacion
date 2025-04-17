package com.game.gphics;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * La clase PiezaUtils proporciona métodos de utilidad para el procesamiento de imágenes.
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class PiezaUtils {
    /**
     * Escala la BufferedImage dada a las dimensiones especificadas de ancho y alto.
     *
     * @param origin la BufferedImage original para escalar
     * @param width el ancho deseado de la imagen escalada
     * @param height la altura deseada de la imagen escalada
     * @return la BufferedImage escalada
     */
    public BufferedImage escalado(BufferedImage origin, int width, int height) {
        // Crea una nueva BufferedImage con las dimensiones deseadas y el mismo tipo que la imagen original
        BufferedImage escalado = new BufferedImage(width, height, origin.getType());

        // Crea un objeto Graphics2D para dibujar la imagen
        Graphics2D g2 = escalado.createGraphics();

        // Dibuja la imagen original en la nueva BufferedImage, escalándola a las dimensiones deseadas
        g2.drawImage(origin, 0, 0, width, height, null);

        // Libera los recursos del objeto Graphics2D
        g2.dispose();

        // Devuelve la imagen escalada
        return escalado;
    }
}
