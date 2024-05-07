package ENTITY;

import FX.KeyManager;

import java.awt.image.BufferedImage;

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
    public int x,y,speed;

    /*
    * BufferedImage en Java es una clase que representa una imagen digital en memoria. Proporciona un búfer de píxeles
    * manipulable para trabajar con imágenes de forma rasterizada (compuestas por una cuadrícula de píxeles individuales).
    */
    public BufferedImage stop,stopR,stopL,up1,up2,down1,down2,left1,left2,right1,right2;

    public char sentido;

    /**Metodo MOVE
     * El juego al ser en 2D solo tiene dos dimensiones espaciales: X, Y
     * Moverse hacia arriba o hacia la derecha es equivalente a SUMAR en la posición
     * mientras que moverse hacia abajo o hacia la izquierda RESTA a la posición actual
     */
    public void move(KeyManager e) {
        if (e.up) {
            y -= speed;
            sentido = 'w';
        }  else if (e.down) {
            y += speed;
            sentido = 's';
        } else if (e.left) {
            x -= speed;
            sentido = 'a';
        } else if (e.right) {
            x += speed;
            sentido = 'd';
        }
    }
}
