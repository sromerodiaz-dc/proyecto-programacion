package GAME.GAME;

import GAME.EFFECT.Sound;
import GAME.EFFECT.UserInterface;
import GAME.ENTITY.Player;
import GAME.GPHICS.PiezaManager;

import javax.sound.sampled.LineUnavailableException;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Mantiene una referencia al jugador y al gestor de piezas del juego.
 * */
public class GameController {
    // Jugador
    private Player model;

    // Gestor de piezas
    private PiezaManager piezaManager;

    // Efectos de sonido
    Sound sound = new Sound();
    Sound se = new Sound(); // SoundEffect

    // Interfaz de Usuario
    public UserInterface ui;

    /**
     * Constructor que inicializa el controlador del juego con el jugador y el gestor de piezas.
     *
     * @param model El jugador del juego.
     * @param piezaManager El gestor de piezas del juego.
     */
    public GameController(Player model, PiezaManager piezaManager,TeisPanel teisPanel) {
        this.model = model;
        this.piezaManager = piezaManager;
        ui = new UserInterface(teisPanel);
    }

    /**
     * Obtiene el gestor de piezas del juego.
     *
     * @return El gestor de piezas del juego.
     */
    public PiezaManager getPiezaManager() {
        return piezaManager;
    }

    public Sound getSound() {
        return sound;
    }

    /**
     * Actualiza el estado del juego.
     * Actualiza el estado del jugador y de las piezas del juego.
     */
    public void update() throws LineUnavailableException {
        // Actualiza el estado del jugador
        model.actualiza();
    }

    public void playMusic(int i) throws LineUnavailableException {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void stopMusic() {
        sound.stop();
    }

    public void playSelection(int i) throws LineUnavailableException {
        se.setFile(i);
        se.play();
    }
}