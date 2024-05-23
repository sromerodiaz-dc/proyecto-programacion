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
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class SpriteLoader {
    /**
     * Carga sprites (imágenes) desde una carpeta y devuelve una lista de ImageIcon.
     *
     * @param folderPath La ruta de la carpeta que contiene los sprites.
     * @return La lista de ImageIcon que representan los sprites cargados.
     * @throws IllegalArgumentException Si la ruta no es una carpeta.
     */
    public List<ImageIcon> loadSprites(String folderPath) {
        // Crea un File a partir de un path para comprobar si existe
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("No es una carpeta: " + folderPath);
        }

        // Lista de sprites
        List<ImageIcon> sprites = new ArrayList<>();

        // Lista de path de los Assets
        List<String> imagePaths = new ArrayList<>();

        // Recorrer los archivos en la carpeta y cargar las imágenes
        for (File file : folder.listFiles()) {
            try {
                BufferedImage image = ImageIO.read(file);
                sprites.add(new ImageIcon(image));
                imagePaths.add(file.getPath());
            } catch (IOException e) {
                sprites.add(null);
            }
        }

        // Escribir la correspondencia entre el índice y la ruta de los sprites en un archivo de texto
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
                // Ignorar la excepción
            }
        }

        return sprites;
    }

    /**
     * Escribe la correspondencia entre el índice de un sprite y su ruta en un archivo de texto.
     *
     * @param index El índice del sprite.
     * @param path La ruta del sprite.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    private void writeToFile(int index, String path) throws IOException {
        // Crear un nuevo archivo en la ruta especificada
        File file = new File("Assets/maps_correspondencia/c_assets.txt");

        // Abrir el archivo en modo de escritura
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            StringBuilder sb = new StringBuilder();

            // Agregar el índice y la ruta del sprite al StringBuilder
            if (path != null) {
                sb.append(index).append(": ").append(path);
            } else {
                sb.append(index).append(": Error loading image");
            }

            // Escribir el StringBuilder en el archivo de texto
            writer.println(sb);
        }
    }
}
