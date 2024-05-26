package GAME.OBJECT.OBJS;

import GAME.OBJECT.ObjectGame;

import javax.imageio.ImageIO;
import java.io.IOException;

public class BusVitrasa extends ObjectGame {

    public BusVitrasa() {
        // Establece el identificador del Passvigo
        id = "Bus";
        // Carga la imagen del Passvigo desde un archivo de recursos
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("objects/busVitrasa.png"));
            height = image.getHeight() * 5;
            width = image.getWidth() * 5;
            solidArea.width = width;
            solidArea.height = height;
        } catch (IOException e) {
            // Si ocurre un error al cargar la imagen, lanza una excepción RuntimeException
            throw new RuntimeException(e);
        }
    }
}
