package com.game.controller;

import com.game.controller.eventData.EventRectangle;
import com.game.controller.eventData.EventType;
import com.game.data.GameState;
import com.game.data.Properties;
import com.game.efx.Sound;
import com.game.ui.TeisPanel;
import com.game.ui.UserInterface;
import com.game.entity.collision.CollisionCheck;
import com.game.entity.Entity;
import com.game.maptile.PiezaManager;
import com.game.entity.EntityPlacer;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.util.List;

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