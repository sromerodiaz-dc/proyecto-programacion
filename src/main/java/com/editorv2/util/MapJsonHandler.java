package com.editorv2.util;

import com.editorv2.model.CeldaCoord;
import com.editorv2.model.MapModel;
import com.editorv2.model.event.SpawnEvent;
import com.editorv2.model.event.TeleportEvent;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapJsonHandler {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String MAPS_PATH = "src/main/resources/maps/";

    public static MapData loadMapData(String mapName) throws IOException {
        File mapFile = new File(MAPS_PATH + mapName + ".json");
        JsonNode root = mapper.readTree(mapFile);
        JsonNode mapNode = root.path(mapName);

        int cols = mapNode.path("width").asInt();
        int rows = mapNode.path("height").asInt();
        int[][] matrix = parseDataMatrix(mapNode.path("datos"), rows, cols);

        // Cargar colisiones
        Set<CeldaCoord> collisions = new HashSet<>();
        JsonNode collisionsNode = mapNode.path("colisiones");
        for (JsonNode node : collisionsNode) {
            int row = node.get("row").asInt();
            int col = node.get("col").asInt();
            collisions.add(new CeldaCoord(row, col));
        }

        // Cargar eventos
        SpawnEvent spawn = null;
        List<TeleportEvent> teleports = new ArrayList<>();
        JsonNode eventsNode = mapNode.path("eventos");
        if (eventsNode.has("spawn")) {
            JsonNode spawnNode = eventsNode.path("spawn");
            spawn = new SpawnEvent(spawnNode.get(0).asInt(), spawnNode.get(1).asInt());
        }
        if (eventsNode.has("teleport")) {
            for (JsonNode tpNode : eventsNode.path("teleport")) {
                teleports.add(new TeleportEvent(
                        tpNode.get(0).asInt(),
                        tpNode.get(1).asInt(),
                        tpNode.get(2).asInt(),
                        tpNode.get(3).asInt()
                ));
            }
        }

        return new MapData(rows, cols, matrix, collisions, spawn, teleports);
    }


    public static void saveMapData(MapModel model) {
        String mapName = JOptionPane.showInputDialog(null, "Ingrese el nombre del mapa:");

        if (mapName == null || mapName.trim().isEmpty()) {
            showError("Nombre de mapa inválido.");
            return;
        }

        try {
            // Crear estructura del mapa
            ObjectNode mapData = mapper.createObjectNode();
            mapData.put("width", model.getCols());
            mapData.put("height", model.getRows());
            mapData.set("data", createDataArray(model.getMatrixForExport()));

            // Sección "colisiones"
            mapData.set("colisiones", createCollisionsArray(model.getCollisions()));

            // Sección "eventos"
            mapData.set("eventos", createEventsObject(model));

            // Crear nodo raíz que contiene el mapa con el nombre como clave
            ObjectNode rootNode = mapper.createObjectNode();
            rootNode.set(mapName, mapData);

            // Crear directorio si no existe
            if (new File(MAPS_PATH).mkdirs()) System.out.println("MAPS_PATH CREATED");

            // Guardar en archivo individual
            File mapFile = new File(MAPS_PATH + mapName + ".json");

            // Configurar formato
            DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
            prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);

            try (FileWriter fileWriter = new FileWriter(mapFile)) {
                String jsonString = mapper.writer(prettyPrinter).writeValueAsString(rootNode);
                fileWriter.write(jsonString);
            }

            showSuccessDialog(mapName);

        } catch (IOException e) {
            showError("Error al guardar: " + e.getMessage());
        }
    }

    private static ArrayNode createCollisionsArray(Set<CeldaCoord> collisions) {
        ArrayNode collisionsNode = mapper.createArrayNode();
        for (CeldaCoord coord : collisions) {
            ObjectNode collision = mapper.createObjectNode();
            collision.put("row", coord.row());
            collision.put("col", coord.col());
            collisionsNode.add(collision);
        }
        return collisionsNode;
    }

    private static ArrayNode createDataArray(int[][] matrix) {
        ArrayNode dataNode = mapper.createArrayNode();
        for (int[] row : matrix) {
            ArrayNode rowNode = mapper.createArrayNode();
            for (int val : row) rowNode.add(val);
            dataNode.add(rowNode);
        }
        return dataNode;
    }

    private static ObjectNode createEventsObject(MapModel model) {
        ObjectNode eventsNode = mapper.createObjectNode();

        // Spawn
        if (model.getSpawn() != null) {
            ArrayNode spawnNode = mapper.createArrayNode()
                    .add(model.getSpawn().getRow())
                    .add(model.getSpawn().getCol());
            eventsNode.set("spawn", spawnNode);
        }

        // Teleports
        ArrayNode teleportNode = mapper.createArrayNode();
        for (TeleportEvent teleport : model.getTeleports()) {
            ArrayNode tpData = mapper.createArrayNode()
                    .add(teleport.getRow())
                    .add(teleport.getCol())
                    .add(teleport.getTargetRow())
                    .add(teleport.getTargetCol());
            teleportNode.add(tpData);
        }
        eventsNode.set("teleport", teleportNode);

        return eventsNode;
    }

    private static int[][] parseDataMatrix(JsonNode dataNode, int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            JsonNode rowNode = dataNode.get(i);
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = rowNode.get(j).asInt();
            }
        }
        return matrix;
    }

    // ================== MANEJO DE UI ==================
    private static void showSuccessDialog(String mapName) {
        JOptionPane.showMessageDialog(
                null,
                "Mapa '" + mapName + "' guardado en:\n" + MAPS_PATH,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
    public static void showInformation(String message) {
        JOptionPane.showMessageDialog(null, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

