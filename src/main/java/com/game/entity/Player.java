package com.game.entity;

import com.game.controller.GameController;
import com.game.controller.events.EventListener;
import com.game.controller.events.GameEvent;
import com.game.data.Properties;
import com.game.controller.KeyboardController;
import com.game.controller.TeisPanel;
import com.game.entity.object.Shield;
import com.game.entity.object.Weapon;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase define la interacción del jugador con el entorno asi como su movimiento y uso de gráficos en 2D.
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * */
public class Player extends Entity implements EventListener {

    // Propiedades del jugador
    Properties properties;

    // Propiedades propias del player
    int level;
    int attackVal;
    int defenseVal;
    Entity currentWeapon; // TODO falta por crear sprite de arma corporativa
    Entity currentShield; // TODO falta por crear sprite de nivel de defensa (jeringa fent)

    // Atributos escalables
    int strength;
    int dexterity;

    public KeyboardController keyboardController;

    public int screenX;
    public int screenY;

    // Checkea si tienes una VigoPass
    public boolean tenPass = false;

    // Constructor
    /**
     * Constructor de la clase Player, que representa al jugador en el juego.
     *
     * @param t instancia de TeisPanel, que es el panel donde se dibujará el jugador
     * @param k instancia de KeyboardController, que maneja las entradas del teclado
     */
    public Player(TeisPanel t, KeyboardController k, Properties properties) {
        // Llama al constructor de la clase padre (Entity) y pasa la instancia de TeisPanel
        super(t, properties);

        // Asigna la instancia de KeyboardController para manejar las entradas del teclado
        this.keyboardController = k;

        this.properties = properties;

        // Inicializa la posición del jugador en la pantalla
        screenX = t.screenWidth / 2 - (t.sizeFinal / 2);
        screenY = t.screenHeight / 2 - (t.sizeFinal / 2);

        // Inicializa los valores por defecto del jugador
        setValoresPorDefecto(t, properties);

        // Carga las imágenes del jugador
        getPlayerImage();

        // Carga las imágenes de ataque del jugador
        getPlayerAttackImage();
    }

    /**
     * Metodo que define el estado inicial del jugador
     */
    public void setValoresPorDefecto(TeisPanel t, Properties properties) {
        // PLAYER STATS
        setPropierties("player");
        level = 1;
        strength = 1; // The more strength, the more damage the player deals
        dexterity = 1; // The more dextery, the less damage he takes
        currentWeapon = new Weapon(t, properties);
        currentShield = new Shield(t, properties);
        attackVal = getAttackVal(); // Multiplica la fuerza por el daño del arma
        defenseVal = getDefenseVal(); // Multiplica la resistencia por el valor de defensa del escudo

        // PLAYER POS
        worldX = teisPanel.sizeFinal * 18;
        worldY = teisPanel.sizeFinal * 10;

        // PLAYER SOLIDAREA
        defaultSolidAreaX = solidArea.x;
        defaultSolidAreaY = solidArea.y;

        // PLAYER ATTACK AREA
        attackArea.width = 36;
        attackArea.height = 36;
    }

    /**
     * Carga las imágenes del jugador y las establece en las variables correspondientes.
     */
    public void getPlayerImage() {
        // Carga las imágenes del jugador caminando hacia arriba y las establece en las variables correspondientes
        up1 = setEntitySprite("player/upWalkingBehind1.png", 48, 48);
        up2 = setEntitySprite("player/upWalkingBehind2.png", 48, 48);

        // Carga las imágenes del jugador caminando hacia abajo y las establece en las variables correspondientes
        down1 = setEntitySprite("player/downWalking1.png", 48, 48);
        down2 = setEntitySprite("player/downWalking2.png", 48, 48);

        // Carga las imágenes del jugador caminando hacia la izquierda y las establece en las variables correspondientes
        left1 = setEntitySprite("player/leftWalking1.png", 48, 48);
        left2 = setEntitySprite("player/leftWalking2.png", 48, 48);

        // Carga las imágenes del jugador caminando hacia la derecha y las establece en las variables correspondientes
        right1 = setEntitySprite("player/rightWalking1.png", 48, 48);
        right2 = setEntitySprite("player/rightWalking2.png", 48, 48);

        // Carga las imágenes del jugador detenido y las establece en las variables correspondientes
        stop = setEntitySprite("player/frontStanding.png", 48, 48);
        stop2 = setEntitySprite("player/stop2.png", 48, 48);
    }

    public void getPlayerAttackImage() {
        attackUp = setEntitySprite("player/player_attack_up.png", 48, 96);
        attackRight = setEntitySprite("player/player_attack_right.png", 96, 48);
        attackLeft = setEntitySprite("player/player_attack_left.png", 96, 48);
        attackDown = setEntitySprite("player/player_attack_down.png", 48, 96);
    }

    /**
     * Metodo MOVE
     * El juego al ser en 2D solo tiene dos dimensiones espaciales: X, Y
     * Moverse hacia arriba o hacia la derecha es equivalente a SUMAR en la posición
     * mientras que moverse hacia abajo o hacia la izquierda RESTA a la posición actual.
     * Además, controla los sprites por movimiento usados.
     */
    public void move(KeyboardController e, TeisPanel teisPanel) {
        if (attack) {
            attack();
        } else if (e.up || e.down || e.left || e.right || e.isPressed) {
            // Inicializa el contador de parada
            stopCounter = 0;

            // Determina la dirección del movimiento
            sentido = getDirection(e);

            // Comprueba la colisión de la Pieza
            collisionOn = false;
            teisPanel.controller.collisionCheck.checkPieza(this);

            // Colisión de objetos
            int obj = teisPanel.controller.collisionCheck.checkObject(this, true);

            // Llama al metodo pickUpItem para recoger el objeto si es posible
            pickUpItem(obj);

            // Colision entre entidades
            int npc = teisPanel.controller.collisionCheck.checkEntity(this, teisPanel.controller.npc);
            interactuarNPC(npc);

            // Colision con enemigos
            int enemy = teisPanel.controller.collisionCheck.checkEntity(this, teisPanel.controller.enemy);
            interactuarEnemy(enemy);

            // Trigger de eventos
            teisPanel.controller.eventManager.checkEvents(this);

            movement();

            keyboardController.isPressed = false;
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

        if (invencible) {
            timeInvencible++;
            if (timeInvencible > 30) {
                invencible = false;
                timeInvencible = 0;
            }
        }
    }

    /**
     * Devuelve la dirección del movimiento según la tecla presionada.
     *
     * @param e el objeto KeyManager que contiene el estado de las teclas
     * @return la dirección del movimiento como un carácter ('w', 's', 'a', 'd')
     */
    private char getDirection(KeyboardController e) {
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
        else if (e.right) {
            return 'd';
        } // Devuelve 'd'
        else return '0';
    }

    /**
     * Metodo que actualiza la posición del jugador mediante una llamada a otro metodo heredado de Entity
     */
    @Override
    public void update() {
        move(keyboardController, teisPanel);
    }

    /**
     * Metodo que permite al jugador recoger un objeto del mapa.
     *
     * @param id el índice del objeto que se va a recoger
     */
    public void pickUpItem(int id) {
        if (id != 999) { // Verifica si el objeto existe
            String item = teisPanel.controller.obj.get(id).name;

            switch (item) {
                case "passvigo":
                    //teisPanel.controller.playSE(1);
                    tenPass = !tenPass;
                    System.out.println("Passvigo recogida");
                    break;
                case "door":
                    //teisPanel.controller.playSE(1);
                    if (tenPass) {
                        teisPanel.controller.obj.remove(id);
                        tenPass = false;
                        System.out.println("puerta abierta");
                    }
                    System.out.println("puerta");
                    break;
                case "estrellagalicia":
                    //teisPanel.controller.playSE(1);
                    speed -= 2;
                    teisPanel.controller.obj.remove(id);
                    System.out.println("Estrella Galicia debuf");
                    break;
            }
        }
    }

    /**
     * Interactúa con un NPC (Non-Player Character) específico.
     *
     * @param i El índice del NPC con el que se interactúa.
     */
    public void interactuarNPC(int i) {
        // Verifica si se ha presionado una tecla
        if (keyboardController.isPressed) {
            // Verifica si el índice es válido (no es 999)
            if (i!= 999) {
                // Cambia el estado del juego a diálogo
                teisPanel.controller.currentGameState = GameController.GameState.DIALOG;
                // Hace que el NPC hable
                teisPanel.controller.npc.get(i).fala();
                // teisPanel.controller.playSE(); // Efecto de habla
            } else {
                // teisPanel.controller.playSE(); // Efecto de ataque
                // Activa el ataque
                attack = true;
            }
        }
    }

    /**
     * Interactúa con un enemigo específico.
     *
     * @param i El índice del enemigo con el que se interactúa.
     */
    public void interactuarEnemy(int i) {
        // Verifica si el índice es válido (no es 999)
        if (i!= 999) {
            // Verifica si el enemigo no es invencible
            if (!invencible) {
                // teisPanel.controller.playSE(); // Efecto de ataque
                // Aplica daño al enemigo
                life -= 1;
                // Hace que el enemigo sea invencible temporalmente
                invencible = true;
            }
        }
    }

    /**
     * Metodo de ataque, cuando termina la animación, el boolean vuelve a FALSE
     * */
    public void attack() {
        spriteCounter++;
        if (spriteCounter < 10) {
            spriteNum = 1;

            // Guarda las propiedades de posición del jugador
            int currentX = worldX;
            int currentY = worldY;
            int solidAreaW = solidArea.width;
            int solidAreaH = solidArea.width;

            // Ajusta estas variables al área de ataque
            switch (sentido) {
                case 'w': worldY -= attackArea.height; break;
                case 's': worldY += attackArea.height; break;
                case 'a': worldX -= attackArea.width; break;
                case 'd': worldX += attackArea.width; break;
            }
            // Transforma el área sólida del ataque
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            // Checkea la colisión con enemigos
            int enemy = teisPanel.controller.collisionCheck.checkEntity(this,teisPanel.controller.enemy);
            dealDamage(enemy);

            // Restaura los valores por defecto.
            worldX = currentX;
            worldY = currentY;
            solidArea.width = solidAreaW;
            solidArea.height = solidAreaH;
        } else {
            spriteCounter = 0;
            attack = false;
        }
    }

    /**
     * Aplica daño a un enemigo específico.
     *
     * @param i El índice del enemigo que recibirá el daño.
     */
    public void dealDamage(int i) {
        // Verifica si el índice es válido (no es 999)
        if (i!= 999) {

            // Verifica si el enemigo no es invencible
            if (!teisPanel.controller.enemy.get(i).invencible) {
                // teisPanel.controller.playSE(); // Efecto de ataque

                // Aplica daño al enemigo
                teisPanel.controller.enemy.get(i).life -= 1;

                // Hace que el enemigo sea invencible temporalmente
                teisPanel.controller.enemy.get(i).invencible = true;

                // Verifica si la vida del enemigo ha llegado a cero
                if (teisPanel.controller.enemy.get(i).life <= 0) {

                    // Elimina al enemigo (lo marca como nulo)
                    teisPanel.controller.enemy.get(i).dying = true;

                }
            }
        }
    }

    /**
     * Dibuja una imagen en una posición relativa al jugador en un panel.
     */
    public void draw (Graphics2D g2) {
        // Obtiene la imagen de sprite correspondiente al número de sprite activo y la dirección actual
        // Dibuja la imagen en la posición relativa al jugador en el panel
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        // Crea un mapa que asocia cada dirección con un array de imágenes de sprite
        Map<Character, BufferedImage[]> sprites = new HashMap<>();
        if (!attack) {
            sprites = fillSprites(sprites,stop,stop2,up1,up2,down1,down2,left1,left2,right1,right2);
        } else {
            sprites = fillSprites(sprites,attackDown,attackDown,attackUp,attackUp,attackDown,attackDown,attackLeft,attackLeft,attackRight,attackRight);
            if (sentido == 'a') tempScreenX -= 48;
            if (sentido == 'w') tempScreenY -= 48;
        }

        // Obtiene el array de imágenes de sprite correspondiente a la dirección actual
        BufferedImage[] images = sprites.get(sentido);

        // Obtiene la imagen de sprite correspondiente al número de sprite activo
        BufferedImage image = images[spriteNum - 1];

        // Pasa tempScreenX y tempScreenY al metodo drawPlayer
        drawPlayer(g2, image, tempScreenX, tempScreenY);
    }

    /**
     * Dibuja al player en la posicion que corresponde
     * */
    public void drawPlayer(Graphics2D g2, BufferedImage image, int tempX, int tempY) {
        // Dibuja la imagen en la posición correspondiente
        if (invencible) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.4f));
        g2.drawImage(image, tempX, tempY, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
    }

    @Override
    public void onEvent(GameEvent event) {
        System.out.println("Evento recibido: " + event.message() + " " + event.type());
        switch (event.type()) {
            case DAMAGE:
                takeDamage(event.value());
                teisPanel.controller.currentGameState = GameController.GameState.DIALOG;
                teisPanel.controller.ui.dialogo = event.message();
                break;

            case HEAL:
                applyHeal(event.value());
                teisPanel.controller.currentGameState = GameController.GameState.DIALOG;
                teisPanel.controller.ui.dialogo = event.message();
                break;
            case CHECKPOINT:
                break;
            case POWER_UP:
                break;
            case DIALOG:
                break;
            case TELEPORT:
                break;
        }
    }

    private void takeDamage(int amount) {
        life = Math.max(0, life - amount);
    }

    private void applyHeal(int amount) {
        life = Math.min(maxLife, life + amount);
    }

    public int getLevel() {
        return level;
    }

    public int getAttackVal() {
        return strength * currentWeapon.attackVal;
    }

    public int getDefenseVal() {
        return dexterity * currentShield.defenseVal;
    }

    public Entity getCurrentWeapon() {
        return currentWeapon;
    }

    public Entity getCurrentShield() {
        return currentShield;
    }

    public KeyboardController getKeyboardController() {
        return keyboardController;
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public boolean isTenPass() {
        return tenPass;
    }
}