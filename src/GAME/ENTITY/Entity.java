package GAME.ENTITY;

import GAME.GAME.TeisPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Esta clase registrará valores, atributos y propiedades que serán empleadas para las estadisticas
 * como la posicion, velocidad, vida y demás utilidades que puedan surgir.
 * */
public class Entity {
    // Atributos
    public int speed;
    public int worldX, worldY;

    /**
    * BufferedImage en Java es una clase que representa una imagen digital en memoria. Proporciona un búfer de píxeles
    * manipulable para trabajar con imágenes de forma rasterizada (compuestas por una cuadrícula de píxeles individuales).
    */
    public BufferedImage stop,stop2,up1,up2,down1,down2,left1,left2,right1,right2;

    /**
     * Para realizar un movimiento fluido, es decir, 2 sprites por movimiento es necesario un contador.
     * El contador que controla los Sprites es spriteNum y spriteCounter.
     * El contador que controla que estés quieto es stopNum
     */
    public int spriteCounter = 0;
    public int stopCounter = 0;
    public int spriteNum = 1;

    /**
     * Rectangulo que define el área de colisión del PJ
     * */
    public Rectangle solidArea;
    public int defaultSolidAreaX, defaultSolidAreaY;
    public boolean collisionOn = false;

    /**
     * Esta variable define en que orientación se encuentra el personaje
     */
    public char sentido;

    /**Metodo PINTA
     * Metodo empleado para mostrar por pantalla al jugador.
     * Este metodo instancia un Buffer de Imagenes en "image". Este dependiendo de la accion entrante por teclado
     * cambia el sprite empleado por otro nuevo.
     */
    public void pinta(Graphics2D g2, TeisPanel teis, int screenX, int screenY) {
        // Crea un mapa que asocia cada dirección con un array de imágenes de sprite
        Map<Character, BufferedImage[]> sprites = new HashMap<>();
        sprites.put('w', new BufferedImage[]{up1, up2});
        sprites.put('a', new BufferedImage[]{left1, left2});
        sprites.put('s', new BufferedImage[]{down1, down2});
        sprites.put('d', new BufferedImage[]{right1, right2});
        sprites.put('0', new BufferedImage[]{stop, stop2});

        // Obtiene el array de imágenes de sprite correspondiente a la dirección actual
        BufferedImage[] images = sprites.get(sentido);

        // Obtiene la imagen de sprite correspondiente al número de sprite activo
        BufferedImage image = images[spriteNum - 1];

        // Dibuja la imagen con IMAGE en la posición por defecto (100, 100) con los valores por defecto de
        // resolución 16x16 y su respectivo escalado "sizeFinal"
        g2.drawImage(image, screenX, screenY, teis.sizeFinal, teis.sizeFinal, null);
    }
}
