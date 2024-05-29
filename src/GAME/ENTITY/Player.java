package GAME.ENTITY;

import GAME.FX.KeyManager;
import GAME.GAME.TeisPanel;

import javax.sound.sampled.LineUnavailableException;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Esta clase define la interacción del jugador con el entorno asi como su movimiento y uso de gráficos en 2D.
 * */
public class Player extends Entity {

    public KeyManager keyManager;

    public final int screenX;
    public final int screenY;

    // Checkea si tienes una VigoPass
    public boolean tenPass = false;

    // Constructor
    /**
     * Constructor de la clase Player, que representa al jugador en el juego.
     *
     * @param t instancia de TeisPanel, que es el panel donde se dibujará el jugador
     * @param k instancia de KeyManager, que maneja las entradas del teclado
     */
    public Player(TeisPanel t, KeyManager k) {
        // Llama al constructor de la clase padre (Entity) y pasa la instancia de TeisPanel
        super(t);

        // Asigna la instancia de KeyManager para manejar las entradas del teclado
        this.keyManager = k;

        // Inicializa la posición del jugador en la pantalla
        screenX = t.screenWidth / 2 - (t.sizeFinal / 2);
        screenY = t.screenHeight / 2 - (t.sizeFinal / 2);

        // Inicializa el área sólida del jugador
        // Es menor que el sprite del jugador (48x48px) porque de esta manera no hay colisiones excesivas con el entorno.
        solidArea.x = 10;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        // Guarda los valores por defecto del área sólida
        defaultSolidAreaX = solidArea.x;
        defaultSolidAreaY = solidArea.y;

        // Inicializa los valores por defecto del jugador
        setValoresPorDefecto();

        // Carga las imágenes del jugador
        getPlayerImage();
    }

    /**
     * Metodo que define el estado inicial del jugador
     */
    public void setValoresPorDefecto() {
        worldX = teisPanel.sizeFinal * 18;
        worldY = teisPanel.sizeFinal * 10;
        speed = 6;
        sentido = '0';

        // Estado actual del jugador
        maxLife = 10; // 5 Corazones que aguantan 2 golpes cada uno (5x2 = 6)
        life = maxLife;
    }

    /**
     * Carga las imágenes del jugador y las establece en las variables correspondientes.
     */
    public void getPlayerImage() {
        // Carga las imágenes del jugador caminando hacia arriba y las establece en las variables correspondientes
        up1 = setEntitySprite("player/upWalkingBehind1.png");
        up2 = setEntitySprite("player/upWalkingBehind2.png");

        // Carga las imágenes del jugador caminando hacia abajo y las establece en las variables correspondientes
        down1 = setEntitySprite("player/downWalking1.png");
        down2 = setEntitySprite("player/downWalking2.png");

        // Carga las imágenes del jugador caminando hacia la izquierda y las establece en las variables correspondientes
        left1 = setEntitySprite("player/leftWalking1.png");
        left2 = setEntitySprite("player/leftWalking2.png");

        // Carga las imágenes del jugador caminando hacia la derecha y las establece en las variables correspondientes
        right1 = setEntitySprite("player/rightWalking1.png");
        right2 = setEntitySprite("player/rightWalking2.png");

        // Carga las imágenes del jugador detenido y las establece en las variables correspondientes
        stop = setEntitySprite("player/frontStanding.png");
        stop2 = setEntitySprite("player/stop2.png");
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
            teisPanel.controller.collisionCheck.checkPieza(this);

            // Colisión de objetos
            int obj = teisPanel.controller.collisionCheck.checkObject(this, true);
            // Llama al método pickUpItem para recoger el objeto si es posible
            pickUpItem(obj);

            // Colision entre entidades
            int npc = teisPanel.controller.collisionCheck.checkEntity(this, teisPanel.controller.npc);
            interactuarNPC(npc);

            movement();
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

    public void interactuarNPC(int i) {
        if (i != 999) {
            if (teisPanel.model.keyManager.isTalking) {
                teisPanel.controller.estado = teisPanel.controller.dialogo;
                teisPanel.controller.npc[i].fala();
            }
        }
        teisPanel.model.keyManager.isTalking = false;
    }

    /**Metodo PINTA
     * Metodo empleado para mostrar por pantalla al jugador.
     * Este metodo instancia un Buffer de Imagenes en "image". Este dependiendo de la accion entrante por teclado
     * cambia el sprite empleado por otro nuevo.
     */
    public void pinta(Graphics2D g2, int screenX, int screenY) {
        BufferedImage image = teisPanel.controller.drawUtils.drawMovement(spriteNum,sentido,stop,stop2,up1,up2,down1,down2,left1,left2,right1,right2);
        g2.drawImage(image, screenX, screenY, null);
    }
}