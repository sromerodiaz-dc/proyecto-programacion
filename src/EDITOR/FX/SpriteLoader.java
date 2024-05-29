package EDITOR.FX;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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

        correspondencias(sprites,imagePaths);
        return sprites;
    }

    public void correspondencias(List<ImageIcon> sprites, List<String> imagePaths) {
        // Escribir la correspondencia entre el índice y la ruta de los sprites en un archivo de texto
        File file = new File("Assets/maps_correspondencia/c_assets_user.txt");
        if (file.exists())
            file.delete();
        try {
            file.createNewFile();
            // Escribir en el archivo de texto la correspondencia entre el índice y la ruta de los sprites
            writeToFile(sprites, imagePaths, file);
        } catch (IOException e) {
            // Ignorar la excepción
        }


        file = new File("Assets/maps_correspondencia/c_assets.txt");
        if (file.exists())
            file.delete();
        try {
            file.createNewFile();
            // Escribir en el archivo de texto los sprites filtrados en orden para que luego el Juego no tenga que filtrar nombres
            writeToFileReal(sprites, imagePaths, file);
        } catch (IOException e) {
            // Ignorar la excepción
        }
    }

    /**
     * Escribe la correspondencia entre el índice de un sprite y su ruta en un archivo de texto.
     *
     * @param sprites La lista de ImageIcon que representan los sprites cargados.
     * @param imagePaths La lista de path de los Assets.
     * @param file El archivo donde se escribirá la correspondencia entre el índice y la ruta de los sprites.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    private void writeToFile(List<ImageIcon> sprites, List<String> imagePaths, File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            for (int i = 0; i < sprites.size(); i++) {
                if (sprites.get(i) != null) {
                    // Construir la línea a escribir en el archivo de texto
                    StringBuilder sb = new StringBuilder();
                    sb.append(i).append(": ").append(replace(imagePaths.get(i)));
                    writer.println(sb);
                } else {
                    writer.println(i + ": Error loading image");
                }
            }
        }
    }

    /**
     * Escribe los sprites filtrados en orden para que luego el Juego no tenga que filtrar nombres
     *
     * @param sprites La lista de ImageIcon que representan los sprites cargados.
     * @param imagePaths La lista de path de los Assets.
     * @param file El archivo donde se escribirán los sprites filtrados.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    private void writeToFileReal(List<ImageIcon> sprites, List<String> imagePaths, File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            for (int i = 0; i < sprites.size(); i++) {
                if (sprites.get(i) != null) {
                    String replaced = replace(imagePaths.get(i));
                    // Check if the imagePath starts with "background/pared"
                    if (replaced.startsWith("background/pared") || replaced.startsWith("background/bloque") || replaced.startsWith("background/oscuro")) {
                        // Prefix the imagePath with an asterisk
                        replaced = "*" + replaced;
                    }
                    // Escribir en el archivo de texto el sprite filtrado
                    writer.println(replaced);
                } else {
                    writer.println(i + ": Error loading image");
                }
            }
        }
    }

    /**
     * Reemplaza las barras invertidas por barras normales y elimina los espacios en blanco al inicio y al final
     * de la ruta de un archivo.
     *
     * @param path La ruta del archivo.
     * @return La ruta del archivo con las barras invertidas reemplazadas por barras normales y sin espacios en blanco
     * al inicio y al final.
     */
    private String replace(String path){
        return path.replaceFirst("^Assets\\\\", "").replaceAll("\\\\", "/").trim();
    }
}
