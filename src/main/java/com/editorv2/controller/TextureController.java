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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.input.BOMInputStream;

public class TextureController {
    private static final String CONFIG_FILE = "tiles.json";
    private static final String BACKGROUND_PATH = "background";
    private final Map<Integer, BufferedImage> textures = new HashMap<>();
    private final Map<String, Integer> fileToIdMap = new HashMap<>();
    private int nextId = 1;
    private File externalConfigFile;

    public TextureController() {
        externalConfigFile = new File(CONFIG_FILE); // Archivo externo en directorio de trabajo
        loadTexturesWithUserInput();
    }

    private void loadTexturesWithUserInput() {
        // Paso 1: Generar o seleccionar archivo de configuración
        if (!externalConfigFile.exists()) {
            int option = JOptionPane.showConfirmDialog(null,
                    "¿Desea generar un nuevo archivo de configuración?",
                    "Archivo no encontrado",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                generateDefaultConfig(externalConfigFile);
            } else {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    externalConfigFile = chooser.getSelectedFile();
                }
            }
        }

        // Paso 2: Cargar texturas y configuración
        loadTexturesFromResources();
        if (externalConfigFile.exists()) {
            loadConfig(externalConfigFile);
            checkForNewTextures(externalConfigFile);
        }
    }

    private void loadTexturesFromResources() {
        try {
            URL resourceDir = getClass().getClassLoader().getResource(BACKGROUND_PATH);
            if (resourceDir == null) throw new IOException("Carpeta 'background' no encontrada");

            File[] files = new File(resourceDir.toURI()).listFiles((_, name) -> name.endsWith(".png"));
            if (files == null || files.length == 0) {
                throw new IOException("No hay archivos PNG en resources/background");
            }

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

    private void loadConfig(File configFile) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = new FileInputStream(configFile)) {
            BOMInputStream bomIs = new BOMInputStream(is); // Manejar BOM
            JsonNode root = mapper.readTree(bomIs);
            JsonNode texturesNode = root.path("tiles");

            for (JsonNode textureNode : texturesNode) {
                int id = textureNode.path("id").asInt();
                String path = textureNode.path("path").asText();

                try (InputStream imgStream = getClass().getResourceAsStream("/" + BACKGROUND_PATH + "/" + path)) {
                    if (imgStream != null) {
                        BufferedImage img = ImageIO.read(imgStream);
                        textures.put(id, img);
                        fileToIdMap.put(new File(path).getName(), id);
                        nextId = Math.max(nextId, id + 1);
                    } else {
                        System.err.println("⚠️ Textura no encontrada: " + BACKGROUND_PATH + "/" + path);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error cargando configuración: " + e.getMessage());
        }
    }

    private void generateDefaultConfig(File configFile) {
        try {
            URL resourceDir = getClass().getClassLoader().getResource(BACKGROUND_PATH);
            if (resourceDir == null) {
                throw new IOException("Carpeta 'background' no encontrada en recursos.");
            }

            File backgroundDir = new File(resourceDir.toURI());
            File[] files = backgroundDir.listFiles((_, name) -> name.endsWith(".png"));

            if (files == null || files.length == 0) {
                System.err.println("No hay archivos PNG en resources/background");
                return;
            }

            List<File> sortedFiles = Arrays.stream(files)
                    .sorted(Comparator.comparing(File::getName))
                    .toList();

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            ArrayNode tilesArray = rootNode.putArray("tiles");

            for (int i = 0; i < sortedFiles.size(); i++) {
                ObjectNode tile = mapper.createObjectNode();
                tile.put("id", i);
                tile.put("path", sortedFiles.get(i).getName());
                tilesArray.add(tile);
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(writer, rootNode);
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

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root;
            try (InputStream is = new FileInputStream(configFile)) {
                BOMInputStream bomIs = new BOMInputStream(is); // Manejar BOM
                root = mapper.readTree(bomIs);
            }
            ArrayNode tilesArray = (ArrayNode) root.path("tiles");

            Set<String> configTextures = new HashSet<>();
            for (JsonNode node : tilesArray) {
                configTextures.add(node.path("path").asText());
            }

            boolean hasNewTextures = false;

            for (File file : Objects.requireNonNull(currentFiles)) {
                String fileName = file.getName();
                if (!configTextures.contains(fileName)) {
                    BufferedImage img = ImageIO.read(file);
                    textures.put(nextId, img);
                    fileToIdMap.put(fileName, nextId);

                    ObjectNode newTile = mapper.createObjectNode();
                    newTile.put("id", nextId);
                    newTile.put("path", fileName);
                    tilesArray.add(newTile);

                    nextId++;
                    hasNewTextures = true;
                }
            }

            if (hasNewTextures) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
                    mapper.writerWithDefaultPrettyPrinter().writeValue(writer, root);
                }
                JOptionPane.showMessageDialog(null, "Se añadieron nuevas texturas al archivo de configuración.");
            }

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    // Resto de métodos sin cambios...
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
}