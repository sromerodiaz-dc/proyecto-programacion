package com.editorv2.controller;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Comparator;

public class TextureController {
    private static final String CONFIG_FILE = "background/textures.cfg";
    private Map<Integer, BufferedImage> textures = new HashMap<>();
    private Map<String, Integer> fileToIdMap = new HashMap<>();
    private int nextId = 1;

    public TextureController() {
        loadTextures();
    }

    private void loadTextures() {
        // Cargar archivo de configuración si existe
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            loadConfigFile(configFile);
        } else {
            // Generar uno nuevo ordenando los archivos alfabéticamente
            generateDefaultConfig();
        }

        // Verificar si hay nuevos archivos no registrados
        checkForNewTextures();
    }

    private void loadConfigFile(File configFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length != 2) continue;

                int id = Integer.parseInt(parts[0]);
                String fileName = parts[1];
                File textureFile = new File("background/" + fileName);

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

    private void generateDefaultConfig() {
        File dir = new File("background/");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".png"));
        Arrays.sort(files, Comparator.comparing(File::getName));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            for (File file : files) {
                String fileName = file.getName();
                bw.write(nextId + " " + fileName);
                bw.newLine();

                BufferedImage img = ImageIO.read(file);
                textures.put(nextId, img);
                fileToIdMap.put(fileName, nextId);
                nextId++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkForNewTextures() {
        File dir = new File("background/");
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