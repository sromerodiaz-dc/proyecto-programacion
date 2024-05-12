package EDITOR.FX;

import EDITOR.SELECTPANEL.Tile;

import java.io.File;
import java.io.IOException;

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
            String spriteFile = files[i].getPath();
            // Crear objeto Tile con el ID y la imagen
            sprites[i] = new Tile(i, spriteFile);
        }

        return sprites;
    }
}
