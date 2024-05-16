package EDITOR.FX;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que se encarga de cargar sprites (imágenes) desde una carpeta y devuelve una lista de ImageIcon.
 *
 * @author Santiago Agustin Romero Diaz
 */
public class SpriteLoader {
    /**
     * Carga sprites (imágenes) desde una carpeta y devuelve una lista de ImageIcon.
     *
     * @param folderPath ruta de la carpeta que contiene los sprites
     * @return lista de ImageIcon que representan los sprites cargados
     * @throws IOException si ocurre un error al leer la carpeta o los archivos
     */
    public List<ImageIcon> loadSprites(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("No es una carpeta: " + folderPath);
        }

        List<ImageIcon> sprites = new ArrayList<>();
        List<String> imagePaths = new ArrayList<>();

        for (File file : folder.listFiles()) {
            try {
                BufferedImage image = ImageIO.read(file);
                sprites.add(new ImageIcon(image));
                imagePaths.add(file.getPath());
            } catch (IOException e) {
                sprites.add(null);
            }
        }

        File file = new File("Assets/maps_correspondencia/c_assets.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
                for (int i = 0; i < sprites.size(); i++) {
                    if (sprites.get(i) != null) {
                        writeToFile(i, imagePaths.get(i));
                    } else {
                        writeToFile(i, null);
                    }
                }
            } catch (IOException e) {

            }
        }

        return sprites;
    }

    /**
     * Escribe la correspondencia entre el índice de un sprite y su ruta en un archivo de texto.
     *
     * @param index índice del sprite
     * @param path ruta del sprite
     * @throws IOException si ocurre un error al escribir el archivo
     */
    private void writeToFile(int index, String path) throws IOException {
        File file = new File("Assets/maps_correspondencia/c_assets.txt");
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            StringBuilder sb = new StringBuilder();
            if (path != null) {
                sb.append(index).append(": ").append(path);
            } else {
                sb.append(index).append(": Error loading image");
            }
            writer.println(sb);
        }
    }
}
