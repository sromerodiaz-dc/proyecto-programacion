package GAME.GPHICS;

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
 * Esta clase define métodos de dibujado generales debido a que es código que se puede rehusar muchas veces.
 * Dicho de otra manera, es una clase de "herramientas"
 * */
public class DrawUtils {
    /**
     * Dibuja una imagen de sprite en función del número de sprite y la dirección actual.
     *
     * @param spriteNum número de sprite activo
     * @param sentido dirección actual (w, a, s, d o 0)
     * @param stop imagen de sprite para la dirección 0
     * @param stop2 imagen de sprite para la dirección 0
     * @param up1 imagen de sprite para la dirección up
     * @param up2 imagen de sprite para la dirección up
     * @param down1 imagen de sprite para la dirección down
     * @param down2 imagen de sprite para la dirección down
     * @param left1 imagen de sprite para la dirección left
     * @param left2 imagen de sprite para la dirección left
     * @param right1 imagen de sprite para la dirección right
     * @param right2 imagen de sprite para la dirección right
     * @return imagen de sprite correspondiente al número de sprite activo y la dirección actual
     */
    public BufferedImage drawMovement(int spriteNum, char sentido, BufferedImage stop, BufferedImage stop2, BufferedImage up1, BufferedImage up2, BufferedImage down1, BufferedImage down2, BufferedImage left1, BufferedImage left2, BufferedImage right1, BufferedImage right2) {
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
        return images[spriteNum - 1];
    }

    /**
     * Dibuja una imagen en una posición relativa al jugador en un panel.
     *
     * @param worldX coordenada X del mundo
     * @param worldY coordenada Y del mundo
     * @param teisPanel panel donde se dibujará la imagen
     * @param g2 objeto Graphics2D para dibujar la imagen
     * @param image imagen a dibujar
     * @param width ancho de la imagen
     * @param height alto de la imagen
     */
    public void drawRelativeToPlayer(int worldX, int worldY, TeisPanel teisPanel, Graphics2D g2, BufferedImage image, int width, int height) {
        // Calcula las coordenadas de pantalla para dibujar la imagen
        int screenX = worldX - teisPanel.model.worldX + teisPanel.model.screenX;
        int screenY = worldY - teisPanel.model.worldY + teisPanel.model.screenY;

        // Verifica si la imagen está dentro de la pantalla
        if (worldX + teisPanel.sizeFinal > teisPanel.model.worldX - teisPanel.model.screenX &&
                worldX - teisPanel.sizeFinal < teisPanel.model.worldX + teisPanel.model.screenX &&
                worldY + teisPanel.sizeFinal > teisPanel.model.worldY - teisPanel.model.screenY &&
                worldY - teisPanel.sizeFinal < teisPanel.model.worldY + teisPanel.model.screenY) {
            // Dibuja la imagen en la posición correspondiente
            g2.drawImage(image, screenX, screenY, width, height, null);
        }
    }
}
