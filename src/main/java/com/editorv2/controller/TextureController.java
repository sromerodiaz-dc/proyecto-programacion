package com.editorv2.controller;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class TextureController {
    private static final String CONFIG_FILE = "textures.cfg"; // Ahora en directorio externo
    private final Map<Integer, BufferedImage> textures = new HashMap<>();
    private final Map<String, Integer> fileToIdMap = new HashMap<>();
    private int nextId = 1;

    public TextureController() {
        loadTextures();
    }

    private void loadTextures() {
        // Cargar texturas desde resources/background
        loadTexturesFromResources();

        // Cargar/Crear config desde directorio externo
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            loadConfigFile(configFile);
        } else {
            generateDefaultConfig(configFile);
        }
    }

    private void loadTexturesFromResources() {
        try {
            URL resourceDir = getClass().getClassLoader().getResource("background");
            if (resourceDir == null) throw new IOException("Carpeta 'background' no encontrada en recursos");

            File[] files = new File(resourceDir.toURI()).listFiles((dir, name) -> name.endsWith(".png"));
            if (files == null || files.length == 0) {
                throw new IOException("No hay archivos PNG en resources/background");
            }

            for (File file : files) {
                BufferedImage img = ImageIO.read(file);
                String fileName = file.getName();
                textures.put(nextId, img);
                fileToIdMap.put(fileName, nextId);
                nextId++;
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException("Error cargando texturas: " + e.getMessage());
        }
    }
    private void loadConfigFile(File configFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length != 2) continue;

                int id = Integer.parseInt(parts[0]);
                String fileName = parts[1];
                File textureFile = new File("background" + fileName);

                if (textureFile.exists()) {
                    BufferedImage img = ImageIO.read(textureFile);
                    textures.put(id, img);
                    fileToIdMap.put(fileName, id);
                    nextId = Math.max(nextId, id + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateDefaultConfig(File configFile) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(configFile))) {
            for (Map.Entry<String, Integer> entry : fileToIdMap.entrySet()) {
                bw.write(entry.getValue() + " " + entry.getKey());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkForNewTextures() {
        File dir = new File("background");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".png"));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE, true))) {
            for (File file : files) {
                String fileName = file.getName();
                if (!fileToIdMap.containsKey(fileName)) {
                    // Añadir nueva textura al final del archivo
                    bw.write(nextId + " " + fileName);
                    bw.newLine();

                    BufferedImage img = ImageIO.read(file);
                    textures.put(nextId, img);
                    fileToIdMap.put(fileName, nextId);
                    nextId++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getTexture(int id) {
        return textures.get(id);
    }

    public int getTextureId(String fileName) {
        return fileToIdMap.get(fileName);
    }

    public Map<Integer, BufferedImage> getAllTextures() {
        return textures;
    }
}