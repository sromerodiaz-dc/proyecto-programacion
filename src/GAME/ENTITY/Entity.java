package GAME.ENTITY;

import GAME.GAME.TeisPanel;
import GAME.GPHICS.PiezaUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    Propierties propierties;

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

    // Propiedades de ataque
    public BufferedImage attackUp, attackLeft, attackRight, attackDown;
    public boolean attack = false;
    public Rectangle attackArea = new Rectangle(0,0,0,0);

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
    public Entity (TeisPanel teisPanel, Propierties propierties) {
        this.teisPanel = teisPanel; this.propierties = propierties;
    }

    /**
     * Carga la imagen del sprite de la entidad desde el camino dado y la escala al tamaño deseado.
     *
     * @param path el camino a la imagen del sprite de la entidad
     * @return la imagen BufferedImage escalada del sprite de la entidad
     */
    public BufferedImage setEntitySprite(String path, int width, int height) {
        // Crea un nuevo objeto PiezaUtils
        PiezaUtils piezaUtils = new PiezaUtils();

        // Inicializa la variable BufferedImage
        BufferedImage image;

        try {
            // Carga la imagen del sprite del jugador desde el camino dado
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(path));

            // Escala la imagen al tamaño deseado (48x48 píxeles) utilizando el método escalado de PiezaUtils
            image = piezaUtils.escalado(image, width, height);
        } catch (IOException e) {
            // Lanza una RuntimeException si ocurre una IOException al cargar la imagen
            throw new RuntimeException(e);
        }

        // Devuelve la imagen BufferedImage escalada del sprite del jugador
        return image;
    }

    /**
     * Establece las propiedades de la entidad según su ID.
     *
     * @param id ID de la entidad.
     */
    public void setPropierties(String id) {
        // Obtiene los datos de las entidades desde la base de datos
        Object[][] datos = propierties.obtenerDatosEntidad();

        // Recorre cada fila de datos
        for (Object[] fila : datos) {
            // Verifica si la ID de la fila coincide con la ID proporcionada
            if ((fila[0]).equals(id)) {
                // Asigna los valores de la fila a las propiedades de la entidad
                asignarValores(fila);
                break;
            }
        }
    }

    /**
     * Asigna los valores de una fila de datos a las propiedades de la entidad.
     *
     * @param fila La fila de datos que contiene los valores a asignar.
     */
    private void asignarValores(Object[] fila) {
        // Asigna los valores de la fila a las propiedades de la entidad
        this.id = (String) fila[0];
        this.who = (int) fila[1];
        this.sentido = (char) fila[2];
        this.speed = (int) fila[3];
        this.intervalo = (int) fila[4];
        this.width = (int) fila[5];
        this.height = (int) fila[6];
        solidArea.x = (int) fila[7];
        solidArea.y = (int) fila[8];
        solidArea.width = (int) fila[9];
        solidArea.height = (int) fila[10];
        maxLife = (int) fila[11];
        life = (int) fila[12];
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

        // Checkea el tiempo de invencibilidad
        if (invencible) {
            timeInvencible++;
            if (timeInvencible > 30) {
                invencible = false;
                timeInvencible = 0;
            }
        }
    }

    /**
     * Método que se encarga de actualizar el movimiento de la entidad.
     */
    public void movement() {
        // Si no hay colisión, mueve al jugador
        if (!collisionOn && !teisPanel.model.keyManager.isPressed) {
            // Calcula la nueva posición del jugador según la dirección y velocidad
            int[] moveEnt = moveEntity(speed);

            // Actualiza la posición del jugador
            worldX = moveEnt[0];
            worldY = moveEnt[1];
        }

        // Actualiza el contador de sprites y cambia el spriteNum si es necesario
        int[] spritesCount = updateSpriteCounter();

        // Actualiza el contador de sprites y el spriteNum
        spriteNum = spritesCount[0];
        spriteCounter = spritesCount[1];
    }

    /**
     * Dibuja la entidad en el panel utilizando los métodos de dibujo proporcionados.
     *
     * @param g2 objeto Graphics2D para dibujar la entidad
     */
    public void draw (Graphics2D g2) {
        // Obtiene la imagen de sprite correspondiente al número de sprite activo y la dirección actual
        // Dibuja la imagen en la posición relativa al jugador en el panel
        drawMovement(g2);
    }

    public void drawMovement(Graphics2D g2) {
        // Crea un mapa que asocia cada dirección con un array de imágenes de sprite
        Map<Character, BufferedImage[]> sprites = new HashMap<>();
        sprites = fillSprites(sprites,stop,stop2,up1,up2,down1,down2,left1,left2,right1,right2);

        // Obtiene el array de imágenes de sprite correspondiente a la dirección actual
        BufferedImage[] images = sprites.get(sentido);

        // Obtiene la imagen de sprite correspondiente al número de sprite activo

        drawRelativeToPlayer(worldX,worldY, teisPanel, g2,images[spriteNum - 1], invencible);
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
    public void drawRelativeToPlayer(int worldX, int worldY, TeisPanel teisPanel, Graphics2D g2, BufferedImage image, boolean invencible) {
        // Calcula las coordenadas de pantalla a partir de worldX e worldY
        int screenX = worldX - teisPanel.model.worldX + teisPanel.model.screenX;
        int screenY = worldY - teisPanel.model.worldY + teisPanel.model.screenY;


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
     */
    public int[] moveEntity(int speed) {
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
     * @return un array con el nuevo spriteNum y el nuevo contador de sprites
     */
    public int[] updateSpriteCounter() {
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
}
