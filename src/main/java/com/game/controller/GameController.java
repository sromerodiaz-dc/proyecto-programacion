package com.game.controller;

import com.game.controller.events.EventRectangle;
import com.game.controller.events.EventType;
import com.game.data.Properties;
import com.game.efx.Sound;
import com.game.ui.UserInterface;
import com.game.entity.CollisionCheck;
import com.game.entity.Entity;
import com.game.controller.events.EventManager;
import com.game.maptile.PiezaManager;
import com.game.entity.Placer;

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
    public Placer placer;

    // Propiedades de cada entidad
    public Properties properties = Properties.getInstance("jdbc:postgresql://localhost:5432/proyecto", "postgres", "123");

    // Manejo de objetos
    public ArrayList<Entity> obj = new ArrayList<>();

    // Estado del juego
    public GameState currentGameState = GameController.GameState.LOAD;
    // ESTADO DEL JUEGO
    public enum GameState {
        LOAD,    // Pantalla de carga
        PLAY,    // Jugando
        PAUSE,   // Juego pausado
        DIALOG,  // Diálogos
        STATS    // Menú de estadísticas
    }

    // Entidades
    public ArrayList<Entity> npc = new ArrayList<>();

    // Entidades no amistosas
    public ArrayList<Entity> enemy = new ArrayList<>();

    // Orden de renderizado
    ArrayList<Entity> entities = new ArrayList<>();

    // Manejo de eventos del juego
    public EventManager eventManager;

    /**
     * Constructor que inicializa el controlador del juego con el jugador y el gestor de piezas.
     *
     * @param piezaManager El gestor de piezas del juego.
     */
    public GameController(PiezaManager piezaManager,TeisPanel teisPanel) {
        this.piezaManager = piezaManager;
        properties.crearTablaEntidad();

        ui = new UserInterface(teisPanel, properties);
        placer = new Placer(teisPanel, properties);

        // Inicializa el controlador de colisiones
        collisionCheck = new CollisionCheck(teisPanel);

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

    public void setupInitialEvents() {
        eventManager = new EventManager();
        List<EventRectangle> events = new ArrayList<>();

        // Evento de daño (periódico) - UNA SOLA VEZ
        events.add(new EventRectangle(
                10,
                12,
                48,
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
                48,
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
     * @throws LineUnavailableException si no se puede reproducir la música
     */
    public void playMusic(int i) throws LineUnavailableException {
        // Si ya está sonando la música que queremos, no hacemos nada
        if (currentMusicIndex == i) {
            return;
        }

        // Si hay música previa, la detenemos
        if (currentMusicIndex != -1) {
            stopMusic();
        }

        // Reproducimos la nueva música
        sound.setFile(i);
        sound.play();
        sound.loop();

        // Guardamos el índice de la música actual
        currentMusicIndex = i;
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

    public void setGameState(GameController.GameState newState) {
        currentGameState = newState;
    }
    public GameState getGameState() {
        return currentGameState;
    }
}