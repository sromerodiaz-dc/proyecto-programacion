package com.game.entity.npc.passive;

import com.game.data.GameState;
import com.game.entity.Entity;
import com.game.data.Properties;
import com.game.entity.Player;
import com.game.entity.object.Shield;
import com.game.entity.object.Weapon;
import com.game.ui.Dialogable;
import com.game.ui.TeisPanel;

import java.util.*;

/**
 * Define al NPC: Viello
 * "Se dice que cuando pierde el Celta,
 * Viello, algo se entristece y, sin embargo,
 * no se sorprende"
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * */
public class Viello extends Entity implements Dialogable {
    TeisPanel teisPanel;
    Properties properties;

    // Estados de conversación
    private boolean offended = false; // Si el jugador ofendió a Viello
    private boolean knowsTeis = false; // Si el jugador respondió positivamente sobre Teis
    private boolean hasWeapons = false; // Si ya se dieron las armas
    private boolean dialogInProgress = false; // Si hay una conversación activa
    private DialogNode currentNode;

    // Nodos de diálogo
    private final Map<String, DialogNode> dialogNodes = new HashMap<>();

    // TODO patron Observer para los eventos
    // ! Aplicar el patron para que cuando el jugador lance un evento este llegue a este npc

    /**
     * Constructor de la clase Viello, que representa un anciano en el juego.
     *
     * @param teisPanel panel donde se dibujará el anciano
     */
    public Viello(TeisPanel teisPanel, Properties properties, int worldX, int worldY) {
        // Llama al constructor de la clase padre (suponiendo que es una entidad en el juego)
        super(teisPanel, properties);
        this.teisPanel = teisPanel;
        this.properties = properties;
        this.worldX = worldX;
        this.worldY = worldY;

        // Pasa por parametro el identificador = 1;
        setPropierties("Viello");

        defaultSolidAreaX = solidArea.x;
        defaultSolidAreaY = solidArea.y;

        // Carga las imágenes del anciano
        getVielloImage();
        // Carga los dialogos
        setDialogo();
    }

    public void getVielloImage() {
        // Carga las imágenes del jugador caminando hacia arriba y las establece en las variables correspondientes
        up1 = setEntitySprite("graphic/npc/viello/viello_up1.png", width, height);
        up2 = setEntitySprite("graphic/npc/viello/viello_up2.png", width, height);

        // Carga las imágenes del jugador caminando hacia abajo y las establece en las variables correspondientes
        down1 = setEntitySprite("graphic/npc/viello/viello_down1.png", width, height);
        down2 = setEntitySprite("graphic/npc/viello/viello_down2.png", width, height);

        // Carga las imágenes del jugador caminando hacia la izquierda y las establece en las variables correspondientes
        left1 = setEntitySprite("graphic/npc/viello/viello_left1.png", width, height);
        left2 = setEntitySprite("graphic/npc/viello/viello_left2.png", width, height);

        // Carga las imágenes del jugador caminando hacia la derecha y las establece en las variables correspondientes
        right1 = setEntitySprite("graphic/npc/viello/viello_right1.png", width, height);
        right2 = setEntitySprite("graphic/npc/viello/viello_right2.png", width, height);

        // Carga las imágenes del jugador detenido y las establece en las variables correspondientes
        stop = setEntitySprite("graphic/npc/viello/viello_stop1.png", width, height);
        stop2 = setEntitySprite("graphic/npc/viello/viello_stop2.png", width, height);
    }

    /**
     * Establece el evento de movimiento aleatorio del enemigo.
     */
    public void randomMovement() {
        // Incrementa el contador de eventos
        capEvent++;

        //System.out.println("Posicion del viello:" + worldX +" :" + worldY);

        // Cada 120 frames, cambia la dirección del enemigo de manera aleatoria
        if (capEvent == 120) {
            sentido = moveRandomEntity();
            capEvent = 0;
        }
    }

    public void setDialogo() {
        // Crear nodos de diálogo
        dialogNodes.put("start", new DialogNode(
                "mozo... \nsabes o que din dos pimentitos de padrón...?",
                Arrays.asList("qúe dices?", "AJJAJAJAJAJAJJAJAJA", "?"),
                null
        ));

        dialogNodes.put("que_dices", new DialogNode(
                "Deus deume o peor dos destinos deste mundo, \nser do Celta.",
                Arrays.asList("Hueles raro", "la verdad que el celta es el amor de mi vida"),
                null
        ));

        dialogNodes.put("offended", new DialogNode(
                "Vete de aquí, mocoso malcriado!",
                Collections.emptyList(),
                (_) -> {
                    offended = true;
                    return true; // Cierra diálogo
                }
        ));

        dialogNodes.put("celtalove", new DialogNode(
                "unha cousa mi tigre, qué opinas de Teis",
                Arrays.asList("Qué desagradable", "Nací en Teis, muero en Teis"),
                null
        ));

        dialogNodes.put("teis_dislike", new DialogNode(
                "...",
                Collections.emptyList(),
                (_) -> {
                    knowsTeis = true; // Marcar como ya preguntado
                    return true; // Cierra diálogo
                }
        ));

        dialogNodes.put("teis_like", new DialogNode(
                "te falta Teis bro",
                Collections.emptyList(),
                (_) -> {
                    knowsTeis = true;
                    return false; // Continúa diálogo
                }
        ));

        dialogNodes.put("final_question", new DialogNode(
                "sabes teis? diselo manin, diselo manin, la profe me dijo que iba a...?",
                Arrays.asList("qué?", "la profe me dijo que iba a repetir, fuck Coia", "me tengo que ir bro"),
                null
        ));

        dialogNodes.put("weapons_given", new DialogNode(
                "tú eres mi hermano, tú eres mi colega",
                Collections.emptyList(),
                (player) -> {
                    if (!hasWeapons) {
                        player.setWeapon(new Weapon(player.getTeisPanel(), player.getProperties()));
                        player.setShield(new Shield(player.getTeisPanel(), player.getProperties()));
                        hasWeapons = true;
                    }
                    return true; // Cierra diálogo
                }
        ));

        // Resetear al estado inicial
        resetDialog();
    }

    public void resetDialog() {
        DialogNode node;
        if (offended) {
            node = dialogNodes.get("offended");
        } else if (knowsTeis) {
            if (!hasWeapons) {
                node = dialogNodes.get("final_question");
            } else {
                List<String> keys = new ArrayList<>(dialogNodes.keySet());
                node = dialogNodes.get(keys.get(new Random().nextInt(keys.size())));
            }
        } else {
            node = dialogNodes.get("start");
        }

        dialogInProgress = true;
        selectedOption = 0;
        isTyping = true;
        typingIndex = 0;

        if (node != null) {
            currentNode = node; // FIJAR EL NODO ACTUAL
            currentDialog = node.message();
        } else {
            currentDialog = "";
        }
    }

    @Override
    public void fala() {
        if (!dialogInProgress) {
            resetDialog();
        } else {
            // Solo reiniciar si no hay opciones disponibles
            if (currentNode != null && currentNode.options().isEmpty()) {
                resetDialog();
            }
        }

        // Iniciar efecto de escritura
        isTyping = true;
        typingIndex = 0;
        typingCounter = 0;
        sentido = sentidoHablar();
    }

    public void processOptionSelection(int selectedOption, Player player) {
        if (currentNode == null) return;

        // Guardar el nodo actual para transición
        DialogNode previousNode = currentNode;

        // Actualizar el nodo actual basado en la selección
        switch (currentNode.message()) {
            case "mozo... \nsabes o que din dos pimentitos de padrón...?":
                if (selectedOption == 0) currentNode = dialogNodes.get("que_dices");
                else if (selectedOption == 1) currentNode = dialogNodes.get("celtalove");
                break;

            case "Deus deume o peor dos destinos deste mundo, \nser do Celta.":
                if (selectedOption == 0) currentNode = dialogNodes.get("offended");
                else if (selectedOption == 1) currentNode = dialogNodes.get("celtalove");
                break;

            case "unha cousa mi tigre, qué opinas de Teis":
                if (selectedOption == 0) currentNode = dialogNodes.get("teis_dislike");
                else if (selectedOption == 1) currentNode = dialogNodes.get("teis_like");
                break;

            case "sabes teis? diselo manin, diselo manin, la profe me dijo que iba a...?":
                if (selectedOption == 1) currentNode = dialogNodes.get("weapons_given");
                else {
                    // Cerrar diálogo para respuestas incorrectas
                    teisPanel.controller.currentGameState = GameState.PLAY;
                    dialogInProgress = false;
                    return;
                }
                break;
        }

        // Verificar si realmente cambiamos de nodo
        if (currentNode != null && currentNode != previousNode) {
            currentDialog = currentNode.message();
            isTyping = true;
            typingIndex = 0;

            // Ejecutar acción asociada si existe
            if (currentNode.action() != null) {
                boolean shouldClose = currentNode.action().execute(player);
                if (shouldClose) {
                    teisPanel.controller.currentGameState = GameState.PLAY;
                    dialogInProgress = false;
                }
            }
        } else {
            // Si no hay cambio, cerrar el diálogo
            teisPanel.controller.currentGameState = GameState.PLAY;
            dialogInProgress = false;
        }
    }

    @Override
    public List<String> getCurrentOptions() {
        return (currentNode != null) ? currentNode.options() : Collections.emptyList();
    }

    @Override
    public String getCurrentMessage() {
        return currentDialog;
    }

    @Override
    public void selectOption(int index) {
        processOptionSelection(index, teisPanel.player);
    }

    // Fix record access
    public record DialogNode(
            String message,
            List<String> options, // Make public
            DialogAction action
    ) {}

    private interface DialogAction {
        boolean execute(Player player);
    }
}
