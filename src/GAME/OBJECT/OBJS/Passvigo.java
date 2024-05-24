package GAME.OBJECT.OBJS;

import GAME.OBJECT.ObjectGame;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Passvigo extends ObjectGame {
    public Passvigo() {
        id = "Passvigo";
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("objects/passvigo.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
