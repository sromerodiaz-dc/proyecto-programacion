package GAME.OBJECT.OBJS;

import GAME.ENTITY.Entity;
import GAME.GAME.TeisPanel;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Clase que representa un Passvigo en el juego.
 * Extiende de la clase ObjectGame, que probablemente contiene atributos y métodos comunes a todos los objetos del juego.
 * */
public class Passvigo extends Entity {
    /**
     * Constructor que inicializa el Passvigo con sus propiedades específicas.
     */
    public Passvigo(TeisPanel teisPanel) {
        super(teisPanel);

        // Establece el identificador del Passvigo
        id = "Passvigo";

        stop = setEntitySprite("objects/passvigo.png");
    }
}
