package com.game.entity;

import com.game.controller.TeisPanel;
import com.game.data.Propierties;
import com.game.gphics.PiezaUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Esta clase registrará valores, atributos y propiedades que serán empleadas para las estadisticas
 * como la posicion, velocidad, vida y demás utilidades que puedan surgir.
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * */

public class Entity { //TODO Implementar listener del nuevo patron Observer de la clase EventManager
    TeisPanel teisPanel;
    Propierties propierties; // TODO adaptar código a Singleton de propierties

    // Atributos
    public int speed;
    public int worldX, worldY;

    // Cap NPC Event
    public int capEvent = 0;
    public int intervalo = 7;

    // Vida de la entidad
    public int maxLife;
    public int life;
    int hpBarCounter = 0;
    boolean isHpBar = false;

    // Estados de entidad
    public boolean alive = true;
    public boolean dying = false;
    int dyingCounter = 0;

    // Tiempo de invencibilidad
    public boolean invencible = false;
    public int timeInvencible;

    // Entidad que colisiona |> 0 = player, 1 = npc, 2 = enemy
    public int who;

    // Objects
    public BufferedImage image, image2, image3;
    public String name;
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
            image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path)));

            // Escala la imagen al tamaño deseado (48x48 píxeles) utilizando el metodo escalado de PiezaUtils
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
        this.name = (String) fila[0];
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
     * Metodo que se encarga de establecer eventos específicos para cada entidad.
     * Debe ser sobreescrito en las clases hijas para implementar la lógica de eventos específica.
     */
    public void setEvent() {
        // Este metodo debe ser implementado en las clases hijas
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

    /**
     * Redirecciona al NPC en tu sentido para que te mire cuando te hable
     * */
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
     * Metodo que se encarga de actualizar el estado de la entidad.
     */
    public void update() {
        // Llama al metodo setEvent() para establecer eventos específicos
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
     * Metodo que se encarga de actualizar el movimiento de la entidad.
     */
    public void movement() {
        // Si no hay colisión, mueve al jugador
        if (!collisionOn && !teisPanel.model.keyboardController.isPressed) {
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
        // Crea un mapa que asocia cada dirección con un array de imágenes de sprite
        Map<Character, BufferedImage[]> sprites = new HashMap<>();
        sprites = fillSprites(sprites,stop,stop2,up1,up2,down1,down2,left1,left2,right1,right2);

        // Obtiene el array de imágenes de sprite correspondiente a la dirección actual
        BufferedImage[] images = sprites.get(sentido);

        // Obtiene la imagen de sprite correspondiente al número de sprite activo
        drawRelativeToPlayer(worldX,worldY, teisPanel, g2, images[spriteNum - 1], invencible);
    }

    /**
     * Metodo generico de dibujado de sprites
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
     * Dibuja una imagen en pantalla en una posición relativa al jugador, si está dentro del campo de visión.
     * También se encarga de mostrar la barra de vida y aplicar efectos visuales como invencibilidad o animaciones de muerte.
     *
     * @param worldX      Coordenada X del objeto en el mundo.
     * @param worldY      Coordenada Y del objeto en el mundo.
     * @param teisPanel   Panel del juego que contiene la información del jugador y la pantalla.
     * @param g2          Objeto Graphics2D utilizado para renderizar en pantalla.
     * @param image       Imagen a dibujar en pantalla.
     * @param invencible  Indica si el objeto está en estado de invencibilidad (con transparencia).
     */
    public void drawRelativeToPlayer(int worldX, int worldY, TeisPanel teisPanel, Graphics2D g2, BufferedImage image, boolean invencible) {
        int playerWorldX = teisPanel.model.worldX;
        int playerWorldY = teisPanel.model.worldY;
        int playerScreenX = teisPanel.model.screenX;
        int playerScreenY = teisPanel.model.screenY;
        int size = teisPanel.sizeFinal;

        // Coordenadas en pantalla relativas al jugador
        int screenX = worldX - playerWorldX + playerScreenX;
        int screenY = worldY - playerWorldY + playerScreenY;

        boolean isVisible =
                worldX + size > playerWorldX - playerScreenX &&
                        worldX - size < playerWorldX + playerScreenX &&
                        worldY + size > playerWorldY - playerScreenY &&
                        worldY - size < playerWorldY + playerScreenY;

        if (!isVisible) return;

        // Barra de vida
        if (who == 2 && isHpBar) {
            double oneScale = (double) size / maxLife;
            int hpBarValue = (int) (oneScale * life);

            g2.setColor(new Color(35, 35, 35));
            g2.fillRect(screenX - 1, screenY - 16, size + 2, 12);

            g2.setColor(new Color(255, 0, 30));
            g2.fillRect(screenX, screenY - 15, hpBarValue, 10);

            if (++hpBarCounter > 600) {
                hpBarCounter = 0;
                isHpBar = false;
            }
        }

        if (invencible) {
            isHpBar = true;
            hpBarCounter = 0;
            alphaAnimation(g2, 0.4f);
        }

        if (dying) dyingAnimation(g2);

        g2.drawImage(image, screenX, screenY, null);

        alphaAnimation(g2, 1f);
    }

    // TODO Nueva implementacion comprobar funcionamiento
    /**
     * Alterna entre opacidad y transparencia para dar efecto de muerte
     * */
    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;

        if (dyingCounter <= 40) {
            float alphaValue = (dyingCounter / 5) % 2 == 0 ? 0f : 1f;
            alphaAnimation(g2, alphaValue);
        } else {
            dying = false;
            alive = false;
        }
    }

    /**
     * Complementa la animacion de muerte
     */
    public void alphaAnimation(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alphaValue));
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
     * Metodo que devuelve una dirección aleatoria para mover una entidad.
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
     * Metodo que actualiza el contador de sprites y cambia el spriteNum si es necesario.
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

    // TODO Implementar comportamientos especificos para cada enemigo
}
