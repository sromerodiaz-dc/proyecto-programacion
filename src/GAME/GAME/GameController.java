package GAME.GAME;

import GAME.EFFECT.Sound;
import GAME.EFFECT.UserInterface;
import GAME.ENTITY.CollisionCheck;
import GAME.ENTITY.Entity;
import GAME.FX.MapSelector;
import GAME.FX.MapSize;
import GAME.GPHICS.DrawUtils;
import GAME.GPHICS.PiezaManager;
import GAME.OBJECT.ObjectGame;
import GAME.OBJECT.Placer;

import javax.sound.sampled.LineUnavailableException;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Mantiene una referencia al jugador y al gestor de piezas del juego.
 * */
public class GameController {
    // Gestor de piezas
    private PiezaManager piezaManager;

    // Efectos de sonido
    Sound sound = new Sound();
    Sound se = new Sound(); // SoundEffect

    // Interfaz de Usuario
    public UserInterface ui;

    // Controlador de colisiones
    public CollisionCheck collisionCheck;
    public Placer placer;

    // Manejo de objetos
    public ObjectGame[] obj = new ObjectGame[10];

    // ESTADO DEL JUEGO
    public int estado = 3;
    public int pauseState = 0, dialogo = 1, playState = 2, carga = 3;

    // Entidades
    public Entity[] npc = new Entity[10];

    // DrawUtils
    public DrawUtils drawUtils = new DrawUtils();

    // Selector de mapa
    MapSelector mapSelector = new MapSelector();
    public MapSize datos = mapSelector.getMapSize();

    /**
     * Constructor que inicializa el controlador del juego con el jugador y el gestor de piezas.
     *
     * @param piezaManager El gestor de piezas del juego.
     */
    public GameController(PiezaManager piezaManager,TeisPanel teisPanel) {
        this.piezaManager = piezaManager;

        ui = new UserInterface(teisPanel);
        placer = new Placer(teisPanel);

        // Inicializa el controlador de colisiones
        collisionCheck = new CollisionCheck(teisPanel);

        teisPanel.maxWorldCol = datos.maxCol;
        teisPanel.maxWorldRow = datos.maxRow;
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

    public void stopSelection(int i) throws LineUnavailableException {
        se.setFile(i);
        se.stop();
    }
}