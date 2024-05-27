package GAME.OBJECT;

import GAME.GAME.TeisPanel;
import GAME.GPHICS.PiezaUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
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
        int screenX = worldX - teisPanel.model.worldX + teisPanel.model.screenX;
        int screenY = worldY - teisPanel.model.worldY + teisPanel.model.screenY;

        // Para que solo se renderice lo que está alrededor del PJ se calculan estas distancias
        // empleando las coordenadas absolutas y las relativas al jugador.
        if (worldX + teisPanel.sizeFinal > teisPanel.model.worldX - teisPanel.model.screenX && worldX - teisPanel.sizeFinal < teisPanel.model.worldX + teisPanel.model.screenX &&
                worldY + teisPanel.sizeFinal > teisPanel.model.worldY - teisPanel.model.screenY && worldY - teisPanel.sizeFinal < teisPanel.model.worldY + teisPanel.model.screenY) {
            g2.drawImage(image, screenX, screenY, width, height, null);// Dibuja la imagen de la Pieza correspondiente en la posición actual.

        }
    }
}
