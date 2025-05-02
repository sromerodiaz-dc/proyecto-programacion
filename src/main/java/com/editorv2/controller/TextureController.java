package com.editorv2.controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TextureController {
    private static final String CONFIG_FILE = "src/main/resources/tiles.json";
    private static final String BACKGROUND_PATH = "src/main/resources/background/";
    private final Map<Integer, BufferedImage> textures = new HashMap<>();
    private final Map<String, Integer> fileToIdMap = new HashMap<>();
    private int nextId = 1;

    //TODO terminar de implementar por completo el Json
    //TODO implementar carga de sprites a partir de un unico archivo
    //TODO cambiar la lógica del juego para cargar el mapa desde una clase propia del juego que acceda al json y al mapa desde resources.

    public TextureController() {
        loadTexturesWithUserInput();
    }

    private void loadTexturesWithUserInput() {
        File configFile = new File(CONFIG_FILE);

        // Paso 1: Preguntar al usuario si no existe el archivo
        if (!configFile.exists()) {
            int option = JOptionPane.showConfirmDialog(null,
                    "¿Desea generar un nuevo archivo de configuración?",
                    "Archivo no encontrado",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                generateDefaultConfig(configFile);
            } else {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    configFile = chooser.getSelectedFile();
                }
            }
        }

        // Paso 2: Cargar texturas y configuración
        loadTexturesFromResources();
        if (configFile.exists()) {
            loadConfig();
            checkForNewTextures(configFile);
        }
    }

    private void loadTexturesFromResources() {
        try {
            URL resourceDir = getClass().getClassLoader().getResource(BACKGROUND_PATH);
            if (resourceDir == null) throw new IOException("Carpeta 'background' no encontrada");

            File[] files = new File(resourceDir.toURI()).listFiles((dir, name) -> name.endsWith(".png"));
            if (files == null || files.length == 0) {
                throw new IOException("No hay archivos PNG en resources/background");
            }

            // Cargar temporalmente para detección de nuevos archivos
            for (File file : files) {
                String fileName = file.getName();
                if (!fileToIdMap.containsKey(fileName)) {
                    textures.put(nextId, ImageIO.read(file));
                    fileToIdMap.put(fileName, nextId++);
                }
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException("Error cargando texturas: " + e.getMessage());
        }
    }

    private void loadConfig() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("tiles.json")) {
            if (is == null) throw new FileNotFoundException("tiles.json no encontrado en recursos");

            // Parsear JSON
            JsonNode root = mapper.readTree(is);
            JsonNode texturesNode = root.path("textures");

            for (JsonNode textureNode : texturesNode) {
                int id = textureNode.path("id").asInt();
                String path = textureNode.path("path").asText();

                // Cargar textura desde resources/
                try (InputStream imgStream = getClass().getResourceAsStream("/" + path)) {
                    if (imgStream != null) {
                        BufferedImage img = ImageIO.read(imgStream);
                        textures.put(id, img);
                        fileToIdMap.put(new File(path).getName(), id);
                        nextId = Math.max(nextId, id + 1);
                    } else {
                        System.err.println("⚠️ Textura no encontrada: " + path);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error cargando tiles.json", e);
        }
    }

    private void generateDefaultConfig(File configFile) {
        try {
            // Obtener directorio de recursos usando ClassLoader
            URL resourceDir = getClass().getClassLoader().getResource(BACKGROUND_PATH);
            if (resourceDir == null) {
                throw new IOException("Carpeta 'background' no encontrada en recursos.");
            }

            // Convertir URL a File de manera segura
            File backgroundDir = new File(resourceDir.toURI());
            File[] files = backgroundDir.listFiles((dir, name) -> name.endsWith(".png"));

            if (files == null || files.length == 0) {
                System.err.println("No hay archivos PNG en resources/background");
                return;
            }

            // Ordenar archivos alfabéticamente por nombre
            List<File> sortedFiles = Arrays.stream(files)
                    .sorted(Comparator.comparing(File::getName))
                    .toList();

            // Escribir en formato "ID: filename.png"
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(configFile))) {
                for (int i = 0; i < sortedFiles.size(); i++) {
                    String fileName = sortedFiles.get(i).getName();
                    bw.write(i + ": " + fileName);
                    bw.newLine();
                }
            }
        } catch (URISyntaxException | IOException e) {
            System.err.println("Error generando configuración: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void checkForNewTextures(File configFile) {
        try {
            URL resourceDir = getClass().getClassLoader().getResource(BACKGROUND_PATH);
            assert resourceDir != null;
            File[] currentFiles = new File(resourceDir.toURI()).listFiles((_, name) -> name.endsWith(".png"));

            Set<String> configTextures = fileToIdMap.keySet();
            boolean hasNewTextures = false;

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(configFile, true))) {
                assert currentFiles != null;
                for (File file : currentFiles) {
                    String fileName = file.getName();
                    if (!configTextures.contains(fileName)) {
                        bw.write(nextId + " " + fileName);
                        bw.newLine();

                        textures.put(nextId, ImageIO.read(file));
                        fileToIdMap.put(fileName, nextId++);
                        hasNewTextures = true;
                    }
                }
            }

            if (hasNewTextures) {
                JOptionPane.showMessageDialog(null, "Se añadieron nuevas texturas al archivo de configuración.");
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getTexture(int id) {
        return textures.getOrDefault(id, createDefaultTexture());
    }

    private BufferedImage createDefaultTexture() {
        BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 32, 32);
        g2d.dispose();
        return img;
    }

    public int getTextureId(String fileName) {
        return fileToIdMap.get(fileName);
    }

    public Map<Integer, BufferedImage> getAllTextures() {
        return textures;
    }

    public record TextureEntry(int id, String path){}
}