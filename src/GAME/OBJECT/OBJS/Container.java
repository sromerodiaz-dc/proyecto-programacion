package GAME.OBJECT.OBJS;

import GAME.OBJECT.ObjectGame;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Container extends ObjectGame {
    /**
     * Constructor que inicializa el Passvigo con sus propiedades específicas.
     */
    public Container() {
        // Establece el identificador del Passvigo
        id = "Container";

        // Carga la imagen del Passvigo desde un archivo de recursos
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("objects/contenedorVigo.png"));
        } catch (IOException e) {
            // Si ocurre un error al cargar la imagen, lanza una excepción RuntimeException
            throw new RuntimeException(e);
        }
    }
}
