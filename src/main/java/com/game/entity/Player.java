package com.game.entity;

import com.game.controller.eventData.EventListener;
import com.game.controller.eventData.GameEvent;
import com.game.data.GameState;
import com.game.data.Properties;
import com.game.controller.KeyboardController;
import com.game.ui.TeisPanel;
import com.game.entity.object.Shield;
import com.game.entity.object.Weapon;
import com.game.entity.stats.EntityStats;

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
    private static final int NO_VALID_INDEX = 999;
    private static final int BASE_ATTACK_AREA_SIZE = 36;
    private static final int DEFAULT_WORLD_X = 18;
    private static final int DEFAULT_WORLD_Y = 10;
    private static final int SIZE_FINAL = TeisPanel.SIZE_FINAL;
    private static final int SCREEN_WIDTH = TeisPanel.screenWidth;
    private static final int SCREEN_HEIGHT = TeisPanel.screenHeight;

    private final EntityStats stats;
    private final KeyboardController keyboardController;
    private final Weapon currentWeapon;
    private final Shield currentShield;
    private boolean tenPass = false;
    private final int screenX;
    private final int screenY;

    public Player(TeisPanel t, KeyboardController k, Properties properties) {
        super(t, properties);
        this.keyboardController = k;

        this.screenX = SCREEN_WIDTH / 2 - (SIZE_FINAL / 2);
        this.screenY = SCREEN_HEIGHT / 2 - (SIZE_FINAL / 2);

        this.currentWeapon = new Weapon(t, properties);
        this.currentShield = new Shield(t, properties);
        this.stats = new EntityStats.Builder()
                .strength(2)
                .dexterity(2)
                .baseAttack(currentWeapon.getAttackVal())
                .baseDefense(currentShield.getDefenseVal())
                .baseSpeed(5)
                .build();

        setValoresPorDefecto(t);
        getPlayerImage();
        getPlayerAttackImage();
    }

    public void setValoresPorDefecto(TeisPanel t) {
        setPropierties("player");
        worldX = SIZE_FINAL * DEFAULT_WORLD_X;
        worldY = SIZE_FINAL * DEFAULT_WORLD_Y;
        defaultSolidAreaX = solidArea.x;
        System.out.println("defaultSolidAreaX: " + defaultSolidAreaX);
        defaultSolidAreaY = solidArea.y;
        System.out.println("defaultSolidAreaY: " + defaultSolidAreaY);
        attackArea.width = BASE_ATTACK_AREA_SIZE;
        attackArea.height = BASE_ATTACK_AREA_SIZE;
        speed = getSpeed();
    }


    /**
     * Carga las imágenes del jugador y las establece en las variables correspondientes.
     */
    public void getPlayerImage() {
        // Carga las imágenes del jugador caminando hacia arriba y las establece en las variables correspondientes
        up1 = setEntitySprite("graphic/player/upWalkingBehind1.png", 48, 48);
        up2 = setEntitySprite("graphic/player/upWalkingBehind2.png", 48, 48);

        // Carga las imágenes del jugador caminando hacia abajo y las establece en las variables correspondientes
        down1 = setEntitySprite("graphic/player/downWalking1.png", 48, 48);
        down2 = setEntitySprite("graphic/player/downWalking2.png", 48, 48);

        // Carga las imágenes del jugador caminando hacia la izquierda y las establece en las variables correspondientes
        left1 = setEntitySprite("graphic/player/leftWalking1.png", 48, 48);
        left2 = setEntitySprite("graphic/player/leftWalking2.png", 48, 48);

        // Carga las imágenes del jugador caminando hacia la derecha y las establece en las variables correspondientes
        right1 = setEntitySprite("graphic/player/rightWalking1.png", 48, 48);
        right2 = setEntitySprite("graphic/player/rightWalking2.png", 48, 48);

        // Carga las imágenes del jugador detenido y las establece en las variables correspondientes
        stop = setEntitySprite("graphic/player/frontStanding.png", 48, 48);
        stop2 = setEntitySprite("graphic/player/stop2.png", 48, 48);
    }

    public void getPlayerAttackImage() {
        attackUp = setEntitySprite("graphic/player/player_attack_up.png", 48, 96);
        attackRight = setEntitySprite("graphic/player/player_attack_right.png", 96, 48);
        attackLeft = setEntitySprite("graphic/player/player_attack_left.png", 96, 48);
        attackDown = setEntitySprite("graphic/player/player_attack_down.png", 48, 96);
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
        if (id != NO_VALID_INDEX) { // Verifica si el objeto existe
            String item = teisPanel.controller.obj.get(id).name;

            switch (item) {
                case "passvigo" -> tenPass = !tenPass;
                case "door" -> {
                    if (tenPass) {
                            teisPanel.controller.obj.remove(id);
                            tenPass = false;
                            System.out.println("puerta abierta");
                        }
                }
                case "estrellagalicia" -> {
                    //teisPanel.controller.playSE(1);
                    speed -= 2;
                    teisPanel.controller.obj.remove(id);
                    stats.addExp(teisPanel.controller.obj.get(id).exp);
                    System.out.println("Estrella Galicia debuf");
                }
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
            if (i!= NO_VALID_INDEX) {
                // Cambia el estado del juego a diálogo
                teisPanel.controller.currentGameState = GameState.DIALOG;
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
        if (i!= NO_VALID_INDEX) {
            // Verifica si el enemigo no es invencible
            if (!invencible) {
                // teisPanel.controller.playSE(); // Efecto de ataque

                // Aplica daño al enemigo
                int realDamage = teisPanel.controller.enemy.get(i).attackVal - defenseVal;
                if (realDamage < 0) {
                    realDamage = 0;
                }
                life -= realDamage;

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
        if (i!= NO_VALID_INDEX) {

            Entity enemy = teisPanel.controller.enemy.get(i);
            // Verifica si el enemigo no es invencible
            if (!enemy.invencible) {
                // teisPanel.controller.playSE(); // Efecto de ataque
                int realDamage = attackVal - enemy.defenseVal;
                if (realDamage < 0) {
                    realDamage = 0;
                }

                // Aplica daño al enemigo
                enemy.life -= realDamage;
                // Llamamos al nuevo addMessage con el daño y la posición del enemigo
                teisPanel.controller.ui.addMessage(realDamage, enemy, this);

                // Hace que el enemigo sea invencible temporalmente
                enemy.invencible = true;
                enemy.timeInvencible = 0; // Reinicia el contador de invencibilidad si lo usas

                // Verifica si la vida del enemigo ha llegado a cero
                if (enemy.life <= 0) {
                    enemy.dying = true;

                    // *** NOTA SOBRE ESTOS MENSAJES ***
                    // Estos mensajes ("killed" y "exp") no son de daño.
                    // Deberás decidir si quieres:
                    // 1. Mantener un sistema antiguo de mensajes para estos (si aún existe).
                    // 2. Crear un sistema de mensajes de texto diferente (quizás más estático).
                    // 3. Adaptar DamageMessage para manejar también texto (menos recomendado
                    //    si quieres que se vean diferentes a los números de daño).
                    // Por ahora, los comentaré o asumiré que tienes otra forma de mostrarlos.
                    // teisPanel.controller.ui.addMessage(enemy.name + "'s killed!");
                    // teisPanel.controller.ui.addMessage(exp + "+!");

                    exp += enemy.exp;
                    checkLvlUp();
                }
            }
        }
    }

    private void checkLvlUp() {
        if (stats.getExp() >= stats.getNextLevelThreshold()) {
            stats.levelUp();
            maxLife += 2;
            teisPanel.controller.setGameState(GameState.DIALOG);
            teisPanel.controller.ui.dialogo = "Subiches de level manin ao nivel " + stats.getLevel();
            // teisPanel.controller.ui.dialogo = "Subiches de level manin ao nivel " + level + "\n Síntese coma se o Celta lle ganara ó Rayo (cagho en deus)";
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
                teisPanel.controller.currentGameState = GameState.DIALOG;
                teisPanel.controller.ui.dialogo = event.message();
                break;

            case HEAL:
                applyHeal(event.value());
                teisPanel.controller.currentGameState = GameState.DIALOG;
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
        return stats.getLevel();
    }

    public int getExp() {
        return stats.getExp();
    }

    public int getSpeed() {
        return stats.getBaseSpeed();
    }

    public int getAttackVal() {
        return stats.calculateAttack();
    }

    public int getDefenseVal() {
        return stats.calculateDefense();
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

    public EntityStats getStats() {
        return stats;
    }

    public boolean isTenPass() {
        return tenPass;
    }
}