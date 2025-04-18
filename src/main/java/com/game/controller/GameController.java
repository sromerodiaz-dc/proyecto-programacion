package com.game.controller;

import com.game.data.Properties;
import com.game.efx.Sound;
import com.game.gui.UserInterface;
import com.game.entity.CollisionCheck;
import com.game.entity.Entity;
import com.game.controller.events.EventManager;
import com.game.gphics.PiezaManager;
import com.game.entity.Placer;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;

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
    Sound sound = new Sound();
    Sound se = new Sound(); // SoundEffect

    // Interfaz de Usuario
    public UserInterface ui;

    // Controlador de colisiones
    public CollisionCheck collisionCheck;
    public Placer placer;

    // Propiedades de cada entidad
    public Properties properties = Properties.getInstance("jdbc:postgresql://localhost:5432/proyecto", "postgres", "123");

    // Manejo de objetos
    public ArrayList<Entity> obj = new ArrayList<>();

    // ESTADO DEL JUEGO
    public int estado = 3;
    public int pauseState = 0, dialogoState = 1, playState = 2, cargaState = 3;

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
        eventManager = new EventManager(teisPanel);
    }

    /**
     * Obtiene el gestor de piezas del juego.
     *
     * @return El gestor de piezas del juego.
     */
    public PiezaManager getPiezaManager() {
        return piezaManager;
    }

    /**
     * Reproduce la música del juego según el índice proporcionado.
     *
     * @param i índice de la música a reproducir
     * @throws LineUnavailableException si no se puede reproducir la música
     */
    public void playMusic(int i) throws LineUnavailableException {
        sound.setFile(i); // Establece el archivo de música según el índice
        sound.play(); // Reproduce la música
        sound.loop(); // Repite la música en bucle
    }

    /**
     * Detiene la reproducción de la música del juego.
     */
    public void stopMusic() {
        sound.stop(); // Detiene la reproducción de la música
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
}