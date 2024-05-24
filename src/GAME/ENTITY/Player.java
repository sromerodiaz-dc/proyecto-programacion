package GAME.ENTITY;

import GAME.FX.KeyManager;
import GAME.GAME.TeisPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
    KeyManager keyManager;

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
        worldX = teisPanel.sizeFinal * 2;
        worldY = teisPanel.sizeFinal * 2;
        speed = 4;
        sentido = '0';
    }

    /**
     * Este metodo recibe una ruta por parametro en getResourceAsStream para luego acceder a dicho recurso
     * y pasarlo por un Loader para poder leerlo como archivo decodificando el stream de datos de la imagen.
     * Esta imagen rehecha por ImageIO.read() es almacenada en cada uno de los posibles movimientos del jugador.
     */
    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/upWalkingBehind1.png"));
            up2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/upWalkingBehind2.png"));
            down1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/downWalking1.png"));
            down2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/downWalking2.png"));
            left1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/leftWalking1.png"));
            left2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/leftWalking2.png"));
            right1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/rightWalking1.png"));
            right2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/rightWalking2.png"));
            stop = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/frontStanding.png"));
            stop2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/stop2.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo MOVE
     * El juego al ser en 2D solo tiene dos dimensiones espaciales: X, Y
     * Moverse hacia arriba o hacia la derecha es equivalente a SUMAR en la posición
     * mientras que moverse hacia abajo o hacia la izquierda RESTA a la posición actual.
     * Además controla los sprites por movimiento usados.
     */
    public void move(KeyManager e, TeisPanel teisPanel) {
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
        if (stopCounter > 30) {
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
        if (spriteCounter > 15) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }

    /**
     * Metodo que actualiza la posición del jugador mediante una llamada a otro metodo heredado de Entity
     */
    public void actualiza() {
        move(keyManager, teisPanel);
    }

    /**
     * Método que permite al jugador recoger un objeto del mapa.
     *
     * @param id el índice del objeto que se va a recoger
     */
    public void pickUpItem(int id) {
        if (id != 999) { // Verifica si el objeto existe
            // Verifica el tipo de objeto y realiza la acción correspondiente
            switch (teisPanel.obj[id].id) {
                case "Passvigo":
                    teisPanel.obj[id] = null; // Elimina el objeto del mapa
                    tenPass = true; // True si tienes las PassVigo
                    break;
                case "Puerta":
                    // Verifica si el jugador tiene PassVigo
                    if (tenPass) {
                        teisPanel.obj[id] = null;
                    }
                    break;
            }
        }

    }
}