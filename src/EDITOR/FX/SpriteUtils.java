package EDITOR.FX;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class SpriteUtils {
    private static String getLast8Chars(ImageIcon imageIcon) {
        Image image = imageIcon.getImage();
        ImageIcon filteredImageIcon = new ImageIcon(image.getScaledInstance(image.getWidth(null), image.getHeight(null), Image.SCALE_DEFAULT));
        return filteredImageIcon.toString().substring(filteredImageIcon.toString().length() - 8);
    }

    public static void generateSpriteMap(ImageIcon[] sprites, ImageIcon[][] formato) throws IOException {
        try (FileWriter fileWriter = new FileWriter("sprite_map.txt")) {
            for (int row = 0; row < formato.length; row++) {
                for (int col = 0; col < formato[row].length; col++) {
                    boolean foundMatch = false;
                    if (formato[row][col] != null) {
                        int spriteIndex = 0;
                        String imageName = getLast8Chars(formato[row][col]);
                        System.out.println(imageName + " -> ");
                        String spriteName = getLast8Chars(sprites[spriteIndex]);
                        while (!foundMatch && spriteIndex < sprites.length) {
                            System.out.println(imageName + "?");


                            if (imageName.equals(spriteName)) {
                                fileWriter.write(spriteIndex + " ");
                                foundMatch = true;
                            }
                            spriteIndex++;
                        }
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