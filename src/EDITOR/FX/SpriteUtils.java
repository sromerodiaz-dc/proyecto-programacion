package EDITOR.FX;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Añadir 2 cambios:
 * - Poder nombrar al archivo
 * - Modificar el boton de guardado
 * */

public class SpriteUtils {
    public static void generateSpriteMap(ImageIcon[] sprites, ImageIcon[][] formato) throws IOException {

        try (FileWriter fileWriter = new FileWriter("Assets/maps/sprite_map.txt")) {

            for (ImageIcon[] imageIcons : formato) {

                for (ImageIcon imageIcon : imageIcons) {

                    boolean foundMatch = false;
                    int spriteIndex = 0;

                    if (imageIcon != null) {

                        System.out.println("---");
                        System.out.println(imageIcon);
                        System.out.println("---");

                        for (ImageIcon sprite : sprites){

                            if (imageIcon == sprite) {

                                fileWriter.write(spriteIndex+1 + " ");
                                foundMatch = true;
                            }

                            System.out.println(sprite);
                            spriteIndex++;

                        }
                        System.out.println("---");
                    }

                    if (!foundMatch) {
                        fileWriter.write("0 ");
                    }

                }
                fileWriter.write(System.lineSeparator());
            }

        }
    }
}
