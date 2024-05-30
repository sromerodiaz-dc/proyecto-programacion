package GAME.ENTITY;

import GAME.GAME.TeisPanel;
import GAME.GPHICS.DrawUtils;
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
    public DrawUtils drawUtils = new DrawUtils(); // Clase auxiliar de métodos generales

    // Atributos
    public int speed;
    public int worldX, worldY;

    // Cap NPC Event
    public int capEvent = 0;
    public int intervalo = 7;

    // Vida de la entidad
    public int maxLife;
    public int life;

    // Tiempo de invencibilidad
    public boolean invencible = false;
    public int timeInvencible;

    // Entidad que colisiona |> 0 = player, 1 = npc, 2 = enemy
    public int who;

    // Objects
    public BufferedImage image, image2, image3;
    public String id;
    public boolean collision = false;

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

    public String[] dialogos = new String[25]; // Dialogos
    public int dialogoIndex = 0;

    /**
     * Rectangulo que define el área de colisión de la Entidad
     * */
    public int width = 48;
    public int height = 48;
    public Rectangle solidArea = new Rectangle(0,0,width,height); // Parametros por defecto
    public int defaultSolidAreaX, defaultSolidAreaY;
    public boolean collisionOn = false;

    /**
     * Esta variable define en que orientación se encuentra el personaje
     */
    public char sentido = '0';

    /**
     * Constructor parametrizado
     * @param teisPanel gráfico del juego
     * */
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

    /**
     * Dibuja la entidad en el panel utilizando los métodos de dibujo proporcionados.
     *
     * @param g2 objeto Graphics2D para dibujar la entidad
     */
    public void draw (Graphics2D g2) {
        // Obtiene la imagen de sprite correspondiente al número de sprite activo y la dirección actual
        BufferedImage image = teisPanel.controller.drawUtils.drawMovement(spriteNum,sentido,stop,stop2,up1,up2,down1,down2,left1,left2,right1,right2);

        // Dibuja la imagen en la posición relativa al jugador en el panel
        drawUtils.drawRelativeToPlayer(worldX,worldY,teisPanel,g2,image, width, height);
    }

    /**
     * Método que se encarga de establecer eventos específicos para cada entidad.
     * Debe ser sobreescrito en las clases hijas para implementar la lógica de eventos específica.
     */
    public void setEvent() {
        // Este método debe ser implementado en las clases hijas
    }

    /**
     * Habla el NPC
     * */
    public void fala() {
        if (dialogos[dialogoIndex] == null)
            dialogoIndex = 0;
        teisPanel.controller.ui.dialogo = dialogos[dialogoIndex];
        dialogoIndex++;

        sentido = sentidoHablar();
    }

    public char sentidoHablar () {
        return switch (teisPanel.model.sentido) {
            case 'w' -> 's';
            case 's' -> 'w';
            case 'a' -> 'd';
            case 'd' -> 'a';
            default -> '0';
        };
    }

    /**
     * Método que se encarga de actualizar el estado de la entidad.
     */
    public void update() {
        // Llama al método setEvent() para establecer eventos específicos
        setEvent();

        // Checkea las colisiones de todas las entidades y piezas
        colisiones();

        // Actualiza el movimiento de la entidad
        movement();
    }

    public void colisiones() {
        // Reinicia la bandera de colisión
        collisionOn = false;

        // Verifica si hay colisión con alguna pieza en el mapa
        teisPanel.controller.collisionCheck.checkPieza(this);
        teisPanel.controller.collisionCheck.checkObject(this,false);
        teisPanel.controller.collisionCheck.checkEntity(this,teisPanel.controller.npc);
        teisPanel.controller.collisionCheck.checkEntity(this,teisPanel.controller.enemy);
        boolean hitPlayer = teisPanel.controller.collisionCheck.checkPlayer(this);

        if (this.who == 2 && hitPlayer) {
            if (!teisPanel.model.invencible) {
                teisPanel.model.life -= 1;
                teisPanel.model.invencible = true;
            }
        }
    }

    /**
     * Método que se encarga de actualizar el movimiento de la entidad.
     */
    public void movement() {
        // Si no hay colisión, mueve al jugador
        if (!collisionOn && !teisPanel.model.keyManager.isTalking) {
            // Calcula la nueva posición del jugador según la dirección y velocidad
            int[] moveEnt = drawUtils.moveEntity(sentido, worldX, worldY, speed);

            // Actualiza la posición del jugador
            worldX = moveEnt[0];
            worldY = moveEnt[1];
        }

        // Actualiza el contador de sprites y cambia el spriteNum si es necesario
        int[] spritesCount = drawUtils.updateSpriteCounter(spriteNum, spriteCounter, intervalo);

        // Actualiza el contador de sprites y el spriteNum
        spriteNum = spritesCount[0];
        spriteCounter = spritesCount[1];
    }
}
