package GAME.OBJECT.OBJS;

import GAME.OBJECT.ObjectGame;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Vida extends ObjectGame {
    public Vida() {
        // Establece el identificador de la puerta
        id = "Vida";

        // Establece que la puerta tiene colisiones activadas
        collision = true;

        // Carga la imagen de la puerta desde un archivo de recursos
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("npc/heart_full.png"));
            image2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("npc/heart_half.png"));
            image3 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("npc/heart_blank.png"));
            image = piezaUtils.escalado(image,48,48);
            image2 = piezaUtils.escalado(image2,48,48);
            image3 = piezaUtils.escalado(image3,48,48);
        } catch (IOException e) {
            // Si ocurre un error al cargar la imagen, lanza una excepción RuntimeException
            throw new RuntimeException(e);
        }
    }
}
