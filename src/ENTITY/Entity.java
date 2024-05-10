package ENTITY;

import FX.KeyManager;
import FX.TeisPanel;

import java.awt.*;
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
    public int spriteNum = 1;
    public int stopNum;

    /**
     * Esta variable define en que orientación se encuentra el personaje
     */
    public char sentido;


    // METODOS

    /**Metodo MOVE
     * El juego al ser en 2D solo tiene dos dimensiones espaciales: X, Y
     * Moverse hacia arriba o hacia la derecha es equivalente a SUMAR en la posición
     * mientras que moverse hacia abajo o hacia la izquierda RESTA a la posición actual.
     * Además controla los sprites por movimiento usados.
     */
    public void move(KeyManager e) {
        /*
         * Este condicional comprueba que una tecla haya sido presionada para inicializar el stopNum a 0 o no.
         * Si sí que es presionado, se inicializa en 0 y no suma ningun valor a stopNum,
         * una vez hace eso pasa a comprobar que tecla ha sido presionada y actua en consecuencia.
         * Si no ha sido presionada ninguna tecla en el momento entonces stopNum incrementa.
        */
        if (e.up || e.down || e.left || e.right) {
            stopNum = 0;
            if (e.up) {
                y -= speed;
                sentido = 'w';
            } else if (e.down) {
                y += speed;
                sentido = 's';
            } else if (e.left) {
                x -= speed;
                sentido = 'a';
            } else {
                x += speed;
                sentido = 'd';
            }
        } else {
            stopNum++;
        }

        /*
        * Si stopNum es mayor a 1, el metodo pinta() de la clase player cambiara la imagen a la default (imagen stop).
        * El contador de Sprites ha de incrementar siempre para controlar el movimiento fluido para cualquier tipo
        * de movimiento.
        * */
        spriteCounter++;
        if (stopNum > 1) {
            sentido = '0'; // Valor elegido arbitrariamente por mi para que en el switch case llegue al case default.
        }
        // Cada 10 frames el spriteNum varía
        if (spriteCounter > 10) {
            if (spriteNum == 1){
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            // Cada 10 frames los contadores son reseteados para que no incrementen al infinito y haya un control
            // sobre las animaciones a lo largo del tiempo de juego.
            stopNum = 1;
            spriteCounter = 0;
        }
    }

    /**Metodo PINTA
     * Metodo empleado para mostrar por pantalla al jugador.
     * Este metodo instancia un Buffer de Imagenes en "image". Este dependiendo de la accion entrante por teclado
     * cambia el sprite empleado por otro nuevo.
     */
    public void pinta(Graphics2D g2){
        // Este código asigna la imagen de sprite adecuada a la variable 'image'
        // en función de la dirección actual ('sentido') y el número de sprite activo ('spriteNum').

        BufferedImage image = null; // Inicializa la variable de imagen a null

        switch (sentido) {
            case 'w': // El personaje se mueve hacia arriba
                if (spriteNum == 1) {
                    image = up1; // Asigna la primera imagen de sprite hacia arriba
                } else if (spriteNum == 2) {
                    image = up2; // Asigna la segunda imagen de sprite hacia arriba
                }
                break;
            case 'a': // El personaje se mueve hacia la izquierda
                if (spriteNum == 1) {
                    image = left1; // Asigna la primera imagen de sprite hacia la izquierda
                } else if (spriteNum == 2) {
                    image = left2; // Asigna la segunda imagen de sprite hacia la izquierda
                }
                break;
            case 's': // El personaje se mueve hacia abajo
                if (spriteNum == 1) {
                    image = down1; // Asigna la primera imagen de sprite hacia abajo
                } else if (spriteNum == 2) {
                    image = down2; // Asigna la segunda imagen de sprite hacia abajo
                }
                break;
            case 'd': // El personaje se mueve hacia la derecha
                if (spriteNum == 1) {
                    image = right1; // Asigna la primera imagen de sprite hacia la derecha
                } else if (spriteNum == 2) {
                    image = right2; // Asigna la segunda imagen de sprite hacia la derecha
                }
                break;
            default: // El personaje no se mueve (caso por defecto)
                if (spriteNum == 1) {
                    image = stop; // Asigna la primera imagen de sprite en reposo
                } else if (spriteNum == 2) {
                    image = stop2; // Asigna la segunda imagen de sprite en reposo
                }
                break;
        }
        // Dibuja la imagen con IMAGE en la posicion por defecto (100, 100) con los valores por defecto de
        // resolucion 16x16 y su respectivo escalado "sizeFinal")
        g2.drawImage(image,x,y,TeisPanel.sizeFinal,TeisPanel.sizeFinal,null);
    }
}
