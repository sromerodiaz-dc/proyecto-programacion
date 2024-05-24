package GAME.OBJECT.OBJS;

import GAME.OBJECT.ObjectGame;

import javax.imageio.ImageIO;
import java.io.IOException;

public class Puerta extends ObjectGame {
    public Puerta() {
        id = "Puerta";
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("objects/portal.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        collision = true;
    }
}
