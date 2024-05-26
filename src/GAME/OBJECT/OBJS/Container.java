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

        // Establece que la puerta tiene colisiones activadas
        collision = true;
        // Carga la imagen del Passvigo desde un archivo de recursos
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("objects/contenedorVigo.png"));
            height = image.getHeight() * 4;
            width = image.getWidth() * 4;
            solidArea.width = width;
            solidArea.height = height;
        } catch (IOException e) {
            // Si ocurre un error al cargar la imagen, lanza una excepción RuntimeException
            throw new RuntimeException(e);
        }
    }
}
