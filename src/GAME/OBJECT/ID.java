package GAME.OBJECT;

import javax.imageio.ImageIO;
import java.io.IOException;

public class ID extends ObjectGame {
    public ID() {
        id = "ID";
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("objects/passvigo.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
