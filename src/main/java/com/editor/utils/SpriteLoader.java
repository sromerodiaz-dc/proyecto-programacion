package com.editor.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

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
     * @return La lista de ImageIcon que representan los sprites cargados.
     * @throws IllegalArgumentException Si la ruta no es una carpeta.
     */

    public ImageIcon[] loadSprites() {
        List<ImageIcon> spriteList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("maps_correspondencia/c_assets_user.txt"))))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length != 2) continue;

                int index = Integer.parseInt(parts[0].trim());
                String path = parts[1].trim();

                URL resourceUrl = getClass().getClassLoader().getResource(path);
                if (resourceUrl != null) {
                    while (spriteList.size() <= index) {
                        spriteList.add(null);
                    }
                    spriteList.set(index, new ImageIcon(resourceUrl));
                } else {
                    System.err.println("No se encontró imagen: " + path);
                    while (spriteList.size() <= index) {
                        spriteList.add(null);
                    }
                }
            }

        } catch (IOException | NullPointerException e) {
            System.err.println("No se pudo leer el archivo de correspondencia desde resources.");
            e.printStackTrace();
        }

        return spriteList.toArray(new ImageIcon[0]);
    }

    public Map<Integer, ImageIcon> loadSpritesFromCorrespondence(String fileName) {
        Map<Integer, ImageIcon> spriteMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length != 2) continue;

                int index = Integer.parseInt(parts[0].trim());
                String path = parts[1].trim();

                // Carga desde recursos internos (en el .jar o carpeta de recursos)
                URL resourceUrl = getClass().getClassLoader().getResource(path);
                if (resourceUrl != null) {
                    spriteMap.put(index, new ImageIcon(resourceUrl));
                } else {
                    System.err.println("No se encontró el recurso: " + path);
                    spriteMap.put(index, null); // O puedes poner un sprite de "error"
                }
            }

        } catch (IOException e) {
            System.err.println("Error al leer archivo de correspondencia: " + fileName);
            e.printStackTrace();
        }

        return spriteMap;
    }


    public void correspondencias(List<ImageIcon> sprites, List<String> imagePaths) {
        // Escribir la correspondencia entre el índice y la ruta de los sprites en un archivo de texto
        File file = new File("maps_correspondencia/c_assets_user.txt");
        if (file.exists())
            file.delete();
        try {
            file.createNewFile();
            // Escribir en el archivo de texto la correspondencia entre el índice y la ruta de los sprites
            writeToFile(sprites, imagePaths, file);
        } catch (IOException e) {
            // Ignorar la excepción
        }


        file = new File("maps_correspondencia/c_assets.txt");
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
