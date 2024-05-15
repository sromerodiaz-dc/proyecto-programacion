package EDITOR.FX;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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

        // Crear lista para almacenar los paths de las imágenes
        ArrayList<String> imagePaths = new ArrayList<>();

        // Recorrer archivos en la carpeta
        for (int i = 0; i < numSprites; i++) {
            try {
                BufferedImage image = ImageIO.read(files[i]);
                sprites[i] = new ImageIcon(image);
                imagePaths.add(files[i].getPath()); // Add path to list
            } catch (IOException e) {
                e.printStackTrace();
                sprites[i] = null;
            }
        }

        // Escribir los datos en el archivo
        for (int i = 0; i < numSprites; i++) {
            if (sprites[i] != null) {
                writeToFile(i, imagePaths.get(i));
            } else {
                writeToFile(i, null); // Write error to file
            }
        }

        return sprites;
    }

    private static void writeToFile(int index, String path) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("Assets/maps_correspondencia/c_assets.txt", true))) {
            if (path != null) {
                writer.println(index + ": " + path);
            } else {
                writer.println(index + ": Error loading image");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
