package GAME.OBJECT.OBJS;

import GAME.OBJECT.ObjectGame;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * Clase que representa un Passvigo en el juego.
 * Extiende de la clase ObjectGame, que probablemente contiene atributos y métodos comunes a todos los objetos del juego.
 */
public class Passvigo extends ObjectGame {
    /**
     * Constructor que inicializa el Passvigo con sus propiedades específicas.
     */
    public Passvigo() {
        // Establece el identificador del Passvigo
        id = "Passvigo";

        // Carga la imagen del Passvigo desde un archivo de recursos
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("objects/passvigo.png"));
            piezaUtils.escalado(image,48,48);

            height = image.getHeight();
            width = image.getWidth();
            solidArea.width = width;
            solidArea.height = height;
        } catch (IOException e) {
            // Si ocurre un error al cargar la imagen, lanza una excepción RuntimeException
            throw new RuntimeException(e);
        }
    }
}
