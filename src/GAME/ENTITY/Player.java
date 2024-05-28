package GAME.ENTITY;

import GAME.FX.KeyManager;
import GAME.GAME.TeisPanel;
import GAME.GPHICS.PiezaUtils;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Esta clase define la interacción del jugador con el entorno asi como su movimiento y uso de gráficos en 2D.
 * */
public class Player extends Entity {
    // Atributos
    TeisPanel teisPanel;
    public KeyManager keyManager;

    public final int screenX;
    public final int screenY;

    // Checkea si tienes una VigoPass
    public boolean tenPass = false;

    // Constructor
    public Player(TeisPanel t, KeyManager k) {
        this.teisPanel = t;
        this.keyManager = k;

        screenX = teisPanel.screenWidth / 2 - (teisPanel.sizeFinal / 2);
        screenY = teisPanel.screenHeight / 2 - (teisPanel.sizeFinal / 2);

        // teisPanel.sizeFinal = 48
        // Como quiero que el área de colision sea MENOR al del tamaño del PJ, reduzco los pixeles de alto y ancho
        // además de la posición del propio Rectangle en (10,16), recordemos que en Java (0,0) es topLeftCorner.
        // Así que poner unas coordenadas (10,16) quiere decir que el área colisionable comienza cerca del centro del PJ.

        solidArea = new Rectangle();
        solidArea.x = 10;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        defaultSolidAreaX = solidArea.x;
        defaultSolidAreaY = solidArea.y;

        // Inicializa valores por defecto
        setValoresPorDefecto();
        getPlayerImage();
    }

    /**
     * Metodo que define el estado inicial del jugador
     */
    public void setValoresPorDefecto() {
        worldX = teisPanel.sizeFinal * 18;
        worldY = teisPanel.sizeFinal * 10;
        speed = 7;
        sentido = '0';
    }

    /**
     * Carga las imágenes del jugador y las establece en las variables correspondientes.
     */
    public void getPlayerImage() {
        // Carga las imágenes del jugador caminando hacia arriba y las establece en las variables correspondientes
        up1 = setPlayerSprite("player/upWalkingBehind1.png");
        up2 = setPlayerSprite("player/upWalkingBehind2.png");

        // Carga las imágenes del jugador caminando hacia abajo y las establece en las variables correspondientes
        down1 = setPlayerSprite("player/downWalking1.png");
        down2 = setPlayerSprite("player/downWalking2.png");

        // Carga las imágenes del jugador caminando hacia la izquierda y las establece en las variables correspondientes
        left1 = setPlayerSprite("player/leftWalking1.png");
        left2 = setPlayerSprite("player/leftWalking2.png");

        // Carga las imágenes del jugador caminando hacia la derecha y las establece en las variables correspondientes
        right1 = setPlayerSprite("player/rightWalking1.png");
        right2 = setPlayerSprite("player/rightWalking2.png");

        // Carga las imágenes del jugador detenido y las establece en las variables correspondientes
        stop = setPlayerSprite("player/frontStanding.png");
        stop2 = setPlayerSprite("player/stop2.png");
    }

    /**
     * Carga la imagen del sprite del jugador desde el camino dado y la escala al tamaño deseado.
     *
     * @param path el camino a la imagen del sprite del jugador
     * @return la imagen BufferedImage escalada del sprite del jugador
     */
    public BufferedImage setPlayerSprite(String path) {
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

    /**
     * Metodo MOVE
     * El juego al ser en 2D solo tiene dos dimensiones espaciales: X, Y
     * Moverse hacia arriba o hacia la derecha es equivalente a SUMAR en la posición
     * mientras que moverse hacia abajo o hacia la izquierda RESTA a la posición actual.
     * Además, controla los sprites por movimiento usados.
     */
    public void move(KeyManager e, TeisPanel teisPanel) throws LineUnavailableException {
        // Verifica si se ha presionado alguna tecla
        if (e.up || e.down || e.left || e.right) {
            // Inicializa el contador de parada
            stopCounter = 0;

            // Determina la dirección del movimiento
            sentido = getDirection(e);

            // Comprueba la colisión de la Pieza
            collisionOn = false;
            teisPanel.collisionCheck.checkPieza(this);

            // Colisión de objetos
            int obj = teisPanel.collisionCheck.checkObject(this, true);
            // Llama al método pickUpItem para recoger el objeto si es posible
            pickUpItem(obj);

            // Si no hay colisión, mueve al jugador
            if (!collisionOn) {
                movePlayer(sentido);
            }

            // Actualiza el contador de sprites y cambia el spriteNum si es necesario
            updateSpriteCounter();
        } else {
            // Si no se ha presionado ninguna tecla, incrementa el contador de parada
            sentido = '0';
            stopCounter++;
        }
        // Cambia el spriteNum si el contador de parada es mayor a 30
        if (stopCounter > 15) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            stopCounter = 0;
        }
    }

    /**
     * Devuelve la dirección del movimiento según la tecla presionada.
     *
     * @param e el objeto KeyManager que contiene el estado de las teclas
     * @return la dirección del movimiento como un carácter ('w', 's', 'a', 'd')
     */
    private char getDirection(KeyManager e) {
        // Si la tecla 'up' está presionada, devuelve 'w'
        if (e.up) {
            return 'w';
        }
        // Si la tecla 'down' está presionada, devuelve 's'
        else if (e.down) {
            return 's';
        }
        // Si la tecla 'left' está presionada, devuelve 'a'
        else if (e.left) {
            return 'a';
        }
        // Si la tecla 'right' está presionada, devuelve 'd'
        else {
            return 'd';
        }
    }

    /**
     * Mueve al jugador según la dirección del movimiento.
     *
     * @param sentido la dirección del movimiento como un carácter ('w', 's', 'a', 'd')
     */
    private void movePlayer(char sentido) {
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
    }

    /**
     * Actualiza el contador de sprites y cambia el spriteNum si es necesario.
     */
    private void updateSpriteCounter() {
        // Incrementa el contador de sprites
        spriteCounter++;
        // Si el contador de sprites es mayor a 15, cambia el spriteNum y resetea el contador
        if (spriteCounter > 7) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    /**
     * Metodo que actualiza la posición del jugador mediante una llamada a otro metodo heredado de Entity
     */
    public void actualiza() throws LineUnavailableException {
        move(keyManager, teisPanel);
    }

    /**
     * Método que permite al jugador recoger un objeto del mapa.
     *
     * @param id el índice del objeto que se va a recoger
     */
    public void pickUpItem(int id) throws LineUnavailableException {
        if (id != 999) { // Verifica si el objeto existe

        }
    }

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
        g2.drawImage(image, screenX, screenY, null);
    }
}