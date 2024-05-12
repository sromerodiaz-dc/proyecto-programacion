package EDITOR.FX;

import EDITOR.CELDAS.Tile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteLoader {
    public static Tile[] loadSprites(String folderPath) throws IOException {
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
            File spriteFile = files[i];
            if (!spriteFile.isFile()) {
                continue; // Omitir si no es un archivo
            }

            // Cargar imagen como BufferedImage
            BufferedImage image = ImageIO.read(spriteFile);

            // Crear objeto Tile con el ID y la imagen
            sprites[i] = new Tile(i, image);
        }

        return sprites;
    }
}
