package com.game.controller;

import com.game.controller.eventData.EventRectangle;
import com.game.controller.eventData.EventType;
import com.game.data.GameState;
import com.game.data.Properties;
import com.game.efx.Sound;
import com.game.ui.TeisPanel;
import com.game.ui.UserInterface;
import com.game.controller.collision.CollisionCheck;
import com.game.entity.Entity;
import com.game.maptile.PiezaManager;
import com.game.entity.EntityPlacer;
import com.game.ui.dialogue.Conversation;
import com.game.ui.dialogue.DialogueSystem;
import com.game.ui.dialogue.state.DialogueState;

import javax.sound.sampled.LineUnavailableException;
import java.util.*;

/**
 * Mantiene una referencia al jugador y al gestor de piezas del juego.
 *
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class GameController {
    // Gestor de piezas
    private final PiezaManager piezaManager;

    // Efectos de sonido
    Sound sound = new Sound("sound");
    Sound se = new Sound("se"); // SoundEffect
    private int currentMusicIndex = -1; // -1 significa que no hay música sonando

    // Interfaz de Usuario
    public UserInterface ui;

    // Controlador de colisiones
    public CollisionCheck collisionCheck;
    public EntityPlacer entityPlacer;

    // Propiedades de cada entidad
    public Properties properties = Properties.getInstance();

    // Manejo de objetos
    public ArrayList<Entity> obj = new ArrayList<>();

    // Estado del juego
    public GameState currentGameState = GameState.LOAD;

    // Entidades
    public ArrayList<Entity> npc = new ArrayList<>();
    public Entity currentTalkingNpc;

    // Entidades no amistosas
    public ArrayList<Entity> enemy = new ArrayList<>();

    // Controlador de dialogos
    public DialogueSystem dialogueSystem;

    // Manejo de eventos del juego
    public EventManager eventManager;

    /**
     * Constructor que inicializa el controlador del juego con el jugador y el gestor de piezas.
     *
     * @param piezaManager El gestor de piezas del juego.
     */
    public GameController(PiezaManager piezaManager, TeisPanel teisPanel) {
        this.piezaManager = piezaManager;

        initializeComponents(teisPanel);

        // Inicializa el manejo de eventos
        setupInitialEvents();

        // Inicializa el sistema de dialogos
        initDialogueSystem();
    }

    /**
     * Obtiene el gestor de piezas del juego.
     *
     * @return El gestor de piezas del juego.
     */
    public PiezaManager getPiezaManager() {
        return piezaManager;
    }

    private void initializeComponents(TeisPanel teisPanel) {
        ui = new UserInterface(teisPanel, properties);
        entityPlacer = new EntityPlacer(teisPanel, properties);
        collisionCheck = new CollisionCheck(teisPanel);
    }

    public void setupInitialEvents() { //TODO cargar eventos desde un JSON
        eventManager = new EventManager();
        List<EventRectangle> events = new ArrayList<>();

        // Evento de daño (periódico) - UNA SOLA VEZ
        events.add(new EventRectangle(
                10,
                12,
                32,
                32,
                EventType.DAMAGE,
                '0',
                0, // Cooldown irrelevante para eventos de un solo uso
                "\"Encontras tirado no chan un periódico...\nO Celta volveu perder, non che sorprende,\nsó entrischécete\"",
                5
        ));

        // Evento de cura (estrellagalicia) - REPETIBLE CON COOLDOWN
        events.add(new EventRectangle(
                14,
                13,
                32,
                32,
                EventType.HEAL,
                '0',
                30, // 30 segundos de cooldown
                "\"Bebiches unha estrela.\nSíntese coma se o Vialia nunca fora edificado\"",
                3
        ));

        eventManager.loadEvents(events);
        System.out.println("Eventos cargados: " + events.size());
    }

    /**
     * Reproduce la música del juego según el índice proporcionado.
     *
     * @param i índice de la música a reproducir
     */
    public void playMusic(int i) {
        try {
            if (currentMusicIndex == i) return;
            if (currentMusicIndex != -1) stopMusic();
            sound.setFile(i);
            sound.play();
            sound.loop();
            currentMusicIndex = i;
        } catch (LineUnavailableException e) {
            System.err.println("Error de audio: " + e.getMessage());
        }
    }

    private void initDialogueSystem() {
        DialogueState dialogueState = new DialogueState(new HashSet<>());
        dialogueSystem = new DialogueSystem(dialogueState);

        // --- Configurar conversación para Viello ---
        // Debes proveer un ID para el nodo inicial de la conversación.
        Conversation vielloConv = new Conversation("VIELLO_INTRO"); // <--- USA EL CONSTRUCTOR CON ID INICIAL

        // NODO 1: "VIELLO_INTRO" - Agregar acción para establecer LAUGHED
        List<Conversation.DialogueOption> introOptions = new ArrayList<>();
        introOptions.add(new Conversation.DialogueOption(
                "qúe dices?",
                "VIELLO_CELTA_RESPONSE",
                null
        ));
        introOptions.add(new Conversation.DialogueOption(
                "AJJAJAJAJAJAJJAJAJA",
                "VIELLO_TEIS_OPINION_PROMPT",
                List.of("SET_FLAG:LAUGHED")  // ESTABLECER LA FLAG AQUÍ
        ));

        vielloConv.addNode(new Conversation.ConversationNode( // <--- USA addNode
                "VIELLO_INTRO", // 1. ID de este nodo
                "mozo... \nsabes o que din dos pimentitos de padrón...?", // 2. Texto del NPC
                introOptions, // 3. Opciones del jugador para este nodo
                null // 4. Flags requeridas para VER este nodo (si aplica)
        ));

        // NODO 2: "VIELLO_CELTA_RESPONSE" - "Deus deume o peor dos destinos deste mundo, \nser do Celta."
        List<Conversation.DialogueOption> celtaResponseOptions = new ArrayList<>();
        celtaResponseOptions.add(new Conversation.DialogueOption(
                "Hueles raro",
                "VIELLO_OFFENDED_RESPONSE", // ID del nodo ofendido
                List.of("SET_FLAG:OFFENDED") // Acción: marcar como ofendido
        ));
        celtaResponseOptions.add(new Conversation.DialogueOption(
                "la verdad que el celta es el amor de mi vida",
                "VIELLO_TEIS_OPINION_PROMPT", // ID del nodo de opinión sobre Teis
                null
        ));
        vielloConv.addNode(new Conversation.ConversationNode(
                "VIELLO_CELTA_RESPONSE", // ID de este nodo
                "Deus deume o peor dos destinos deste mundo, \nser do Celta.", // Texto del NPC
                celtaResponseOptions,
                null // Flags requeridas
        ));

        // NODO 3: "VIELLO_TEIS_OPINION_PROMPT" - "unha cousa mi tigre, qué opinas de Teis"
        // Este nodo era referenciado por las opciones anteriores.
        List<Conversation.DialogueOption> teisOpinionOptions = new ArrayList<>();
        teisOpinionOptions.add(new Conversation.DialogueOption(
                "Teis é incrible!",
                "VIELLO_INTRO", // Volver al inicio, por ejemplo
                List.of("SET_FLAG:PRAISED_TEIS")
        ));
        teisOpinionOptions.add(new Conversation.DialogueOption(
                "Non sei que dicir...",
                "VIELLO_RIDDLE", // Volver al inicio
                null
        ));
        vielloConv.addNode(new Conversation.ConversationNode(
                "VIELLO_TEIS_OPINION_PROMPT", // ID de este nodo
                "Unha cousa, meu tigre, que opinas de Teis?", // Texto del NPC
                teisOpinionOptions,
                null
        ));


        // NODO 4: "VIELLO_OFFENDED_RESPONSE" - "Tira pralá' co teu flow manin"
        // Este nodo se muestra si la flag "OFFENDED" está activa O si se llega a él por la opción.
        // Si solo se llega por la opción, no necesitas 'requiredFlags' aquí,
        // pero si es un estado al que el sistema puede volver si la flag está activa, entonces sí.
        Map<String, Boolean> offendedFlags = new HashMap<>();
        offendedFlags.put("OFFENDED", true); // Solo se muestra si la flag OFFENDED es true

        vielloConv.addNode(new Conversation.ConversationNode(
                "VIELLO_OFFENDED_RESPONSE", // ID de este nodo
                "Tira pralá' co teu flow manin", // Texto del NPC
                Collections.emptyList(), // Sin opciones, la conversación podría terminar aquí o necesitar un reset
                offendedFlags // Este nodo se activará si la flag "OFFENDED" es true y el sistema busca un nodo.
                // Si la transición es solo por la opción, y no quieres que sea un punto de entrada
                // independiente basado en flags, puedes poner 'null' aquí.
        ));

        // Agregar nodo de recompensa
        vielloConv.addNode(new Conversation.ConversationNode(
                "VIELLO_REWARD",
                "estas armas son as que usaba eu\nna guerra Teis / Coia...\n\nSonido de Teis antonte era un grupo armado\nToma, as armas que xa non usamos",
                Collections.emptyList(),
                null
        ));

        Map<String, Boolean> laughedFlags = new HashMap<>();
        laughedFlags.put("LAUGHED", true); // Flag que se activa con la Opción B inicial

        vielloConv.addNode(new Conversation.ConversationNode(
                "VIELLO_RIDDLE",
                "Diselo manin, diselo manin...",
                Arrays.asList(
                        new Conversation.DialogueOption(
                                "diselo manin, que te quiero",
                                "VIELLO_REWARD",
                                Arrays.asList("GIVE_REWARD") // Acción personalizada
                        ),
                        new Conversation.DialogueOption(
                                "diselo manin, que non sei",
                                "VIELLO_INTRO",
                                null
                        )
                ),
                laughedFlags // Solo visible si tiene flag LAUGHED
        ));

        dialogueSystem.registerConversation("VIELLO", vielloConv);
    }

    /**
     * Detiene la reproducción de la música del juego.
     */
    public void stopMusic() {
        sound.stop();
        currentMusicIndex = -1;
    }

    /**
     * Reproduce el sonido de selección del juego según el índice proporcionado.
     *
     * @param i índice del sonido de selección a reproducir
     * @throws LineUnavailableException si no se puede reproducir el sonido
     */
    public void playSE(int i) throws LineUnavailableException {
        se.setFile(i); // Establece el archivo de sonido de selección según el índice
        se.play(); // Reproduce el sonido de selección
    }

    /**
     * Detiene la reproducción del sonido de selección del juego según el índice proporcionado.
     *
     * @param i índice del sonido de selección a detener
     * @throws LineUnavailableException si no se puede detener el sonido
     */
    public void stopSelection(int i) throws LineUnavailableException {
        se.setFile(i); // Establece el archivo de sonido de selección según el índice
        se.stop(); // Detiene la reproducción del sonido de selección
    }

    public GameState getGameState() {
        return currentGameState;
    }
    public void decrementTitleCounter() {
        ui.titleCounter--;
        if (ui.titleCounter < 0) ui.titleCounter = 2;
    }

    public void incrementTitleCounter() {
        ui.titleCounter++;
        if (ui.titleCounter > 2) ui.titleCounter = 0;
    }

    public void exitGame() {
        // Lógica de limpieza previa a salir
        System.exit(0);
    }

    public int getTitleCounter() {
        return ui.titleCounter;
    }

    public void setGameState(GameState gameState) {
        currentGameState = gameState;
    }
}