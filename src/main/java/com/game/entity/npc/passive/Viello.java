package com.game.entity.npc.passive;

import com.game.entity.Entity;
import com.game.data.Properties;
import com.game.entity.Player;
import com.game.entity.object.Shield;
import com.game.entity.object.Weapon;
import com.game.ui.dialogue.Dialogable;
import com.game.ui.TeisPanel;
import com.game.ui.dialogue.Conversation;
import com.game.ui.dialogue.DialogueSystem;

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
    private final String dialogueId = "VIELLO";

    /**
     * Constructor de la clase Viello, que representa un anciano en el juego.
     *
     * @param teisPanel panel donde se dibujará el anciano
     */
    public Viello(TeisPanel teisPanel, Properties properties, int worldX, int worldY) {
        super(teisPanel, properties);
        this.teisPanel = teisPanel;
        this.properties = properties;
        this.worldX = worldX;
        this.worldY = worldY;

        setPropierties("Viello");
        defaultSolidAreaX = solidArea.x;
        defaultSolidAreaY = solidArea.y;
        getVielloImage();

        // Configurar diálogos de reserva
        addFallbackDialogue("a migración á Redondela está acabando con Teis");
        addFallbackDialogue("Sonido de Teis foi á Doppler... eu non estaba");
        setDialogueId(dialogueId);
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

    @Override
    public void fala() {
        // Reiniciar estado de escritura
        isTyping = true;
        typingIndex = 0;
        typingCounter = 0;
        sentido = sentidoHablar();

        // Establecer diálogo actual
        currentDialog = getCurrentMessage();
    }

    @Override
    public String getDialogueId() {
        return "VIELLO";
    }

    @Override
    public List<String> getCurrentOptions() {
        // Asegurarse que teisPanel y su controller y dialogueSystem no son null
        if (teisPanel == null || teisPanel.controller == null || teisPanel.controller.dialogueSystem == null) {
            System.err.println("Viello: TeisPanel o DialogueSystem no inicializado en getCurrentOptions.");
            return Collections.emptyList();
        }
        DialogueSystem dialogueSystem = teisPanel.controller.dialogueSystem;

        Conversation.ConversationNode node = dialogueSystem.getCurrentNode(dialogueId);
        if (node == null || node.options == null) { // Chequea también node.options
            return Collections.emptyList();
        }

        List<String> optionsTexts = new ArrayList<>();

        for (Conversation.DialogueOption option : node.options) { // Iterar sobre node.OPTIONS
            optionsTexts.add(option.text);
        }
        return optionsTexts;
    }

    @Override
    public String getCurrentMessage() {
        if (teisPanel == null || teisPanel.controller == null || teisPanel.controller.dialogueSystem == null) {
            System.err.println("Viello: TeisPanel o DialogueSystem no inicializado en getCurrentMessage.");
            return getFallbackDialogues().get(new Random().nextInt(getFallbackDialogues().size())); // Fallback si no hay sistema
        }
        DialogueSystem dialogueSystem = teisPanel.controller.dialogueSystem;

        Conversation.ConversationNode node = dialogueSystem.getCurrentNode(dialogueId);
        if (node != null) {
            return node.npcText;
        }
        // Si no hay nodo actual en el sistema, usa los fallbacks locales de Viello
        List<String> fallbacks = getFallbackDialogues();
        return fallbacks.get(new Random().nextInt(fallbacks.size()));
    }

    @Override
    public void selectOption(int index) {
        if (teisPanel == null || teisPanel.controller == null || teisPanel.controller.dialogueSystem == null) {
            System.err.println("Viello: TeisPanel o DialogueSystem no inicializado en selectOption.");
            return;
        }
        DialogueSystem dialogueSystem = teisPanel.controller.dialogueSystem;

        Conversation.ConversationNode currentNode = dialogueSystem.getCurrentNode(dialogueId);
        // Chequeos más robustos
        if (currentNode != null && currentNode.options != null && index >= 0 && index < currentNode.options.size()) {
            Conversation.DialogueOption selectedOption = currentNode.options.get(index);

            // Actualizar el nodo PRIMERO antes de procesar acciones
            dialogueSystem.setCurrentNode(dialogueId, selectedOption.nextNodeId);

            if (selectedOption.actions != null) {
                for (String action : selectedOption.actions) {
                    dialogueSystem.triggerAction(action, this);
                }
            }

            fala();
        } else {
            System.err.println("Viello: Intento de seleccionar una opción inválida. Index: " + index + ", Node: " + (currentNode != null ? currentNode.nodeId : "null"));
        }
    }

    @Override
    public void triggerCustomAction(String action) {
        if ("GIVE_REWARD".equals(action)) {
            giveReward();
        }
    }

    private void giveReward() {
        Player player = teisPanel.player;

        // Crear arma y escudo (valores de ejemplo)
        Weapon arma = new Weapon(teisPanel, properties);
        Shield escudo = new Shield(teisPanel, properties);

        player.setWeapon(arma);
        player.setShield(escudo);

        System.out.println("personaje armado");
    }
}
