package GAME.OBJECT;

import GAME.GAME.TeisPanel;
import GAME.GPHICS.PiezaUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Representa la generación de objetos en el mapa y su área de colisión mediante un Rectangle
 */
public class ObjectGame {
    // Atributos
    public BufferedImage image;
    public String id;
    public boolean collision = false;
    public int worldX, worldY;
    public int height = 48;
    public int width = 48;

    // El objeto entero será el área de colisión
    public Rectangle solidArea = new Rectangle(0,0, width, height);
    public int defaultObjectSolidAreaX = 0;
    public int defaultObjectSolidAreaY = 0;

    // Optimización
    public PiezaUtils piezaUtils = new PiezaUtils();

    public void draw(Graphics2D g2, TeisPanel teisPanel) {

    }
}
