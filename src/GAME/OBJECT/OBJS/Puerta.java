package GAME.OBJECT.OBJS;

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
public class Puerta extends ObjectGame {
    /**
     * Constructor que inicializa la puerta con sus propiedades específicas.
     */
    public Puerta() {
        // Establece el identificador de la puerta
        id = "Puerta";

        // Carga la imagen de la puerta desde un archivo de recursos
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("objects/portal.png"));
        } catch (IOException e) {
            // Si ocurre un error al cargar la imagen, lanza una excepción RuntimeException
            throw new RuntimeException(e);
        }

        // Establece que la puerta tiene colisiones activadas
        collision = true;
    }
}
