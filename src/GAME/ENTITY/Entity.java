package GAME.ENTITY;

import GAME.GAME.TeisPanel;
import GAME.GPHICS.PiezaUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Esta clase registrará valores, atributos y propiedades que serán empleadas para las estadisticas
 * como la posicion, velocidad, vida y demás utilidades que puedan surgir.
 * */
public class Entity {
    TeisPanel teisPanel;

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
    public Rectangle solidArea = new Rectangle(0,0,48,48); // Parametros por defecto
    public int defaultSolidAreaX, defaultSolidAreaY;
    public boolean collisionOn = false;

    /**
     * Esta variable define en que orientación se encuentra el personaje
     */
    public char sentido;

    public Entity (TeisPanel teisPanel) {
        this.teisPanel = teisPanel;
    }

    /**
     * Carga la imagen del sprite de la entidad desde el camino dado y la escala al tamaño deseado.
     *
     * @param path el camino a la imagen del sprite de la entidad
     * @return la imagen BufferedImage escalada del sprite de la entidad
     */
    public BufferedImage setEntitySprite(String path) {
        // Crea un nuevo objeto PiezaUtils
        PiezaUtils piezaUtils = new PiezaUtils();

        // Inicializa la variable BufferedImage
        BufferedImage image;

        try {
            // Carga la imagen del sprite del jugador desde el camino dado
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(path));

            // Escala la imagen al tamaño deseado (48x48 píxeles) utilizando el método escalado de PiezaUtils
            image = piezaUtils.escalado(image, 48, 48);
        } catch (IOException e) {
            // Lanza una RuntimeException si ocurre una IOException al cargar la imagen
            throw new RuntimeException(e);
        }

        // Devuelve la imagen BufferedImage escalada del sprite del jugador
        return image;
    }
}
