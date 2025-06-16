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

import java.awt.image.BufferedImage;
import java.util.*;

import com.game.ui.dialogue.globalState.FlagListener;
import com.game.ui.dialogue.globalState.GlobalGameState;
import org.json.JSONArray;
import org.json.JSONTokener;
import java.io.InputStream;


/**
 * Define al NPC: Viello
 * "Se dice que cuando pierde el Celta,
 * Viello, algo se entristece y, sin embargo,
 * no se sorprende"
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * */
public class Viello extends Entity implements Dialogable, FlagListener {
    TeisPanel teisPanel;
    Properties properties;
    private final String dialogueId = "VIELLO";
    private ArrayList<String> fallbackDialogues = new ArrayList<>();

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
        loadFallbackDialogues();
        setDialogueId(dialogueId);

        GlobalGameState.getInstance().addListener(this);
    }

    public void getVielloImage() {
        // Carga las imágenes del jugador caminando hacia arriba y las establece en las variables correspondientes
        up1 = loadImage("viello_up1.png");
        up2 = loadImage("viello_up2.png");

        // Carga las imágenes del jugador caminando hacia abajo y las establece en las variables correspondientes
        down1 = loadImage("viello_down1.png");
        down2 = loadImage("viello_down2.png");

        // Carga las imágenes del jugador caminando hacia la izquierda y las establece en las variables correspondientes
        left1 = loadImage("viello_left1.png");
        left2 = loadImage("viello_left2.png");

        // Carga las imágenes del jugador caminando hacia la derecha y las establece en las variables correspondientes
        right1 = loadImage("viello_right1.png");
        right2 = loadImage("viello_right2.png");

        // Carga las imágenes del jugador detenido y las establece en las variables correspondientes
        stop = loadImage("viello_stop1.png");
        stop2 = loadImage("viello_stop2.png");
    }

    private BufferedImage loadImage(String path) {
        return setEntitySprite("graphic/npc/viello/" + path, width, height);
    }

    private void loadFallbackDialogues() {
        try {
            InputStream is = getClass().getResourceAsStream("/data/entity/viello/fallback.json");
            if (is != null) {
                JSONArray jsonArray = new JSONArray(new JSONTokener(is));
                for (int i = 0; i < jsonArray.length(); i++) {
                    fallbackDialogues.add(jsonArray.getString(i));
                }
            } else {
                System.err.println("No se encontró el archivo de diálogos de reserva para Viello");
            }
        } catch (Exception e) {
            // Diálogos de reserva en caso de error
            fallbackDialogues.add("Erro cargando diálogos");
        }
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
        isTyping = true;
        typingIndex = 0;
        typingCounter = 0;
        sentido = sentidoHablar();
        currentDialog = getCurrentMessage();
        dialogScrollOffset = 0; // Resetear scroll al comenzar nuevo diálogo
    }

    @Override
    public String getDialogueId() {
        return "VIELLO";
    }

    @Override
    public List<String> getCurrentOptions() {
        if (teisPanel == null || teisPanel.controller == null || teisPanel.controller.dialogueSystem == null) {
            return Collections.emptyList();
        }

        DialogueSystem dialogueSystem = teisPanel.controller.dialogueSystem;
        Conversation.ConversationNode node = dialogueSystem.getCurrentNode(dialogueId);

        if (node == null) {
            return Collections.emptyList();
        }

        GlobalGameState globalState = GlobalGameState.getInstance();
        List<Conversation.DialogueOption> activeOptions = new ArrayList<>();

        // 1. Buscar variante válida (si existe)
        if (node.variants != null) {
            for (Conversation.ConversationNode.NodeVariant variant : node.variants) {
                boolean conditionsMet = true;

                if (variant.variantFlags != null) {
                    for (Map.Entry<String, Boolean> flagEntry : variant.variantFlags.entrySet()) {
                        boolean hasFlag = globalState.hasFlag(flagEntry.getKey());
                        if (hasFlag != flagEntry.getValue()) {
                            conditionsMet = false;
                            break;
                        }
                    }
                }

                if (conditionsMet && variant.variantOptions != null) {
                    activeOptions.addAll(variant.variantOptions);
                    break; // Usar primera variante válida
                }
            }
        }

        // 2. Usar opciones base si no hay variante válida
        if (activeOptions.isEmpty() && node.options != null) {
            activeOptions.addAll(node.options);
        }

        // 3. Extraer textos de las opciones
        List<String> visibleOptions = new ArrayList<>();
        for (Conversation.DialogueOption option : activeOptions) {
            visibleOptions.add(option.text);
        }

        return visibleOptions;
    }

    @Override
    public String getCurrentMessage() {
        if (teisPanel == null || teisPanel.controller == null || teisPanel.controller.dialogueSystem == null) {
            System.err.println("Viello: TeisPanel o DialogueSystem no inicializado en getCurrentMessage.");
            return getRandomFallback();
        }

        DialogueSystem dialogueSystem = teisPanel.controller.dialogueSystem;
        Conversation.ConversationNode node = dialogueSystem.getCurrentNode(dialogueId);

        // Si no hay nodo actual, usar fallback
        if (node == null) {
            return getRandomFallback();
        }

        return node.npcText;
    }

    private String getRandomFallback() {
        List<String> fallbacks = getFallbackDialogues();
        if (fallbacks.isEmpty()) {
            return "Erro cargando diálogos";
        }
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

    @Override
    public ArrayList<String> getFallbackDialogues() {
        return fallbackDialogues;
    }

    @Override
    public void onFlagChanged(String flag, boolean value) {
        if ("FB".equals(flag)) {
            System.out.println("FB true");
        }
    }

    public void unregister() {
        GlobalGameState.getInstance().removeListener(this);
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
