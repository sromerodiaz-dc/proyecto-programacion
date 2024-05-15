package EDITOR.FX;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpriteLoader {
    public static ImageIcon[] loadSprites(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("No es una carpeta: " + folderPath);
        }

        File[] files = folder.listFiles();
        int numSprites = files.length;

        // Crear matriz monodimensional para almacenar ImageIcons
        ImageIcon[] sprites = new ImageIcon[numSprites];

        // Recorrer archivos en la carpeta
        for (int i = 0; i < numSprites; i++) {
            try {
                BufferedImage image = ImageIO.read(files[i]);
                sprites[i] = new ImageIcon(image);
            } catch (IOException e) {
                e.printStackTrace();
                sprites[i] = null;
            }
        }
        return sprites;
    }
}
