package EDITOR.FX;

import EDITOR.SELECTPANEL.Tile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpriteLoader {
    public static Tile[] loadSprites(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("No es una carpeta: " + folderPath);
        }

        File[] files = folder.listFiles();
        int numSprites = files.length;

        // Crear matriz monodimensional para almacenar sprites
        Tile[] sprites = new Tile[numSprites];

        // Recorrer archivos en la carpeta
        for (int i = 0; i < numSprites; i++) {
            String spriteFile = files[i].getPath();
            // Crear objeto Tile con el ID y la imagen
            sprites[i] = new Tile(i, spriteFile);
        }

        return sprites;
    }
    public static ImageIcon[] loadSpritesAsImageIcons() {
        File folder = new File("Assets/player");
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("No es una carpeta: Assets/player");
        }

        File[] files = folder.listFiles();
        int numSprites = files.length;

        // Crear matriz monodimensional para almacenar ImageIcons
        ImageIcon[] sprites = new ImageIcon[numSprites];

        // Recorrer archivos en la carpeta
        for (int i = 0; i < numSprites; i++) {
            String spriteFile = files[i].getPath();
            try {
                BufferedImage image = ImageIO.read(files[i]);
                sprites[i] = new ImageIcon(image);
            } catch (IOException e) {
                System.err.println("Error al cargar la imagen: " + spriteFile);
                e.printStackTrace();
                sprites[i] = null;
            }
        }

        return sprites;
    }
}
