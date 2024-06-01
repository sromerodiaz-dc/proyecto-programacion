package GAME.GPHICS;

import GAME.GAME.TeisPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    public Pair<BufferedImage, Integer, Integer> drawMovement(int spriteNum, char sentido, BufferedImage stop, BufferedImage stop2, BufferedImage up1, BufferedImage up2, BufferedImage down1, BufferedImage down2, BufferedImage left1, BufferedImage left2, BufferedImage right1, BufferedImage right2, BufferedImage attackUp, BufferedImage attackLeft, BufferedImage attackRight, BufferedImage attackDown, boolean attack, int x, int y) {
        // Crea un mapa que asocia cada dirección con un array de imágenes de sprite
        Map<Character, BufferedImage[]> sprites = new HashMap<>();
        if (!attack) {
            sprites = fillSprites(sprites,stop,stop2,up1,up2,down1,down2,left1,left2,right1,right2);
        } else {
            sprites = fillSprites(sprites,attackDown,attackDown,attackUp,attackUp,attackDown,attackDown,attackLeft,attackLeft,attackRight,attackRight);
            if (x != 0 && y != 0) {
                if (sentido == 'a') x -= 48;
                if (sentido == 'w') y -= 48;
            }
        }

        // Obtiene el array de imágenes de sprite correspondiente a la dirección actual
        BufferedImage[] images = sprites.get(sentido);

        // Obtiene la imagen de sprite correspondiente al número de sprite activo
        BufferedImage image = images[spriteNum - 1];

        // Devuelve una clase Pair con datos de coordenadas e imagen.
        return new Pair<>(image, x, y);
    }

    /**
     * Método generico de dibujado de spritems
     * */
    public Map<Character, BufferedImage[]> fillSprites(Map<Character, BufferedImage[]> sprites, BufferedImage stop, BufferedImage stop2, BufferedImage up1, BufferedImage up2, BufferedImage down1, BufferedImage down2, BufferedImage left1, BufferedImage left2, BufferedImage right1, BufferedImage right2) {
        sprites.put('w', new BufferedImage[]{up1, up2});
        sprites.put('a', new BufferedImage[]{left1, left2});
        sprites.put('s', new BufferedImage[]{down1, down2});
        sprites.put('d', new BufferedImage[]{right1, right2});
        sprites.put('0', new BufferedImage[]{stop, stop2});
        return sprites;
    }

    /**
     * Dibuja una imagen en una posición relativa al jugador en un panel.
     *
     * @param worldX coordenada X del mundo
     * @param worldY coordenada Y del mundo
     * @param teisPanel panel donde se dibujará la imagen
     * @param g2 objeto Graphics2D para dibujar la imagen
     * @param image imagen a dibujar
     */
    public void drawRelativeToPlayer(int tempX, int tempY, int worldX, int worldY, TeisPanel teisPanel, Graphics2D g2, BufferedImage image, boolean invencible) {
        int screenX, screenY;
        // Verifica si se proporcionaron valores para tempX e tempY
        if (tempX != 0 && tempY != 0) {
            // Usa tempX e tempY en lugar de worldX e worldY
            screenX = tempX;
            screenY = tempY;
        } else {
            // Calcula las coordenadas de pantalla a partir de worldX e worldY
            screenX = worldX - teisPanel.model.worldX + teisPanel.model.screenX;
            screenY = worldY - teisPanel.model.worldY + teisPanel.model.screenY;
        }

        // Verifica si la imagen está dentro de la pantalla
        if (worldX + teisPanel.sizeFinal > teisPanel.model.worldX - teisPanel.model.screenX &&
                worldX - teisPanel.sizeFinal < teisPanel.model.worldX + teisPanel.model.screenX &&
                worldY + teisPanel.sizeFinal > teisPanel.model.worldY - teisPanel.model.screenY &&
                worldY - teisPanel.sizeFinal < teisPanel.model.worldY + teisPanel.model.screenY) {
            // Dibuja la imagen en la posición correspondiente
            if (invencible) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.4f));
            g2.drawImage(image, screenX, screenY, null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
        }
    }

    /**
     * Mueve a la entidad según la dirección del movimiento.
     *
     * @param sentido la dirección del movimiento como un carácter ('w', 's', 'a', 'd')
     */
    public int[] moveEntity(char sentido, int worldX, int worldY, int speed) {
        int[] coordenadas = new int[2];
        // Mueve al jugador según la dirección del movimiento
        switch (sentido) {
            case 'w':
                worldY -= speed;
                break;
            case 's':
                worldY += speed;
                break;
            case 'a':
                worldX -= speed;
                break;
            case 'd':
                worldX += speed;
                break;
        }
        coordenadas[0] = worldX;
        coordenadas[1] = worldY;
        return coordenadas;
    }

    /**
     * Método que devuelve una dirección aleatoria para mover una entidad.
     * La dirección se elige aleatoriamente entre 'w', '', 'a' y 'd'.
     *
     * @return una dirección aleatoria como carácter ('w', '', 'a' o 'd')
     */
    public char moveRandomEntity(){
        Random random = new Random();
        int i = random.nextInt(100)+1; // Genera un número aleatorio entre 1 y 100

        // Asigna una dirección aleatoria según el rango del número generado
        if (i < 23) return 'w'; // 23% de probabilidad de moverse hacia arriba
        else if (i < 46) return 's'; // 23% de probabilidad de moverse hacia abajo
        else if (i < 69) return 'a'; // 23% de probabilidad de moverse hacia abajo
        else if (i < 92) return 'd'; // 23% de probabilidad de moverse hacia la derecha
        else return '0'; // 8% de probabilidad de quedarse quiero
    }

    /**
     * Método que actualiza el contador de sprites y cambia el spriteNum si es necesario.
     *
     * @param spriteNum número actual del sprite
     * @param spriteCounter contador actual de sprites
     * @param intervalo intervalo de cambio de sprite
     * @return un array con el nuevo spriteNum y el nuevo contador de sprites
     */
    public int[] updateSpriteCounter(int spriteNum, int spriteCounter, int intervalo) {
        int[] counters = new int[2];

        // Incrementa el contador de sprites
        spriteCounter++;

        // Si el contador de sprites es mayor a 15, cambia el spriteNum y resetea el contador
        if (spriteCounter > intervalo) {
            spriteNum = (spriteNum == 1)? 2 : 1; // Cambia el spriteNum entre 1 y 2
            spriteCounter = 0; // Resetea el contador de sprites
        }

        counters[0] = spriteNum;
        counters[1] = spriteCounter;
        return counters;
    }


    /**
     * Se emplea para guardar los datos de dibujado y devolver una imagen u otra
     * */
    public static class Pair<T, X, Y> {
        public T first;
        public X second;
        public Y third;

        public Pair(T first, X second, Y third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
    }
}
