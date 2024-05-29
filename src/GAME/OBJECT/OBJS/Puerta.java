package GAME.OBJECT.OBJS;

import GAME.ENTITY.Entity;
import GAME.GAME.TeisPanel;
import GAME.OBJECT.ObjectGame;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * Clase que representa una puerta en el juego.
 * Extiende de la clase ObjectGame, que probablemente contiene atributos y métodos comunes a todos los objetos del juego.
 */
public class Puerta extends Entity {
    /**
     * Constructor que inicializa la puerta con sus propiedades específicas.
     */
    public Puerta(TeisPanel teisPanel) {
        super(teisPanel);

        // Establece el identificador de la puerta
        id = "Puerta";

        // Establece que la puerta tiene colisiones activadas
        collision = true;

        stop = setEntitySprite("objects/busVitrasa.png");
    }
}
