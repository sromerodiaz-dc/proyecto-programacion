package com.editorv2.util;

import com.editorv2.model.event.MapEvent;
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
import java.util.List;

public class MapJsonHandler {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String MAPS_PATH = "src/main/resources/maps/";

    public static MapData loadMapData(String mapName) throws IOException {
        File mapFile = new File(MAPS_PATH + mapName + ".json");
        JsonNode mapNode = mapper.readTree(mapFile);

        validateMapNode(mapNode, mapName);

        int cols = mapNode.path("width").asInt();
        int rows = mapNode.path("height").asInt();
        int[][] matrix = parseDataMatrix(mapNode.path("layers").get(0).path("data"), rows, cols);

        return new MapData(rows, cols, matrix);
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
            mapData.set("layers", createLayersArray(model));
            mapData.set("events", createEventsArray(model));

            // Crear nodo raíz que contiene el mapa con el nombre como clave
            ObjectNode rootNode = mapper.createObjectNode();
            rootNode.set(mapName, mapData);

            // Crear directorio si no existe
            new File(MAPS_PATH).mkdirs();

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
            e.printStackTrace();
        }
    }

    private static ArrayNode createGroundDataArray(int[][] matrix) {
        ArrayNode dataArray = mapper.createArrayNode();
        for (int[] row : matrix) {
            ArrayNode rowArray = mapper.createArrayNode();
            for (int val : row) rowArray.add(val);
            dataArray.add(rowArray); // Cada fila como sub-array
        }
        return dataArray;
    }

    private static ArrayNode createLayersArray(MapModel model) {
        ArrayNode layers = mapper.createArrayNode();

        layers.add(mapper.createObjectNode()
                .put("name", "ground")
                .put("type", "tilelayer")
                .set("data", createGroundDataArray(model.getMatrixForExport())) // Cambio de método
        );

        return layers;
    }

    private static ArrayNode createEventsArray(MapModel model) { // TODO terminar de crear este metodo
        // Cosas a tener en cuenta, este es el formato:
        /*
            "events": {
              "playerSpawn": [10, 20],
              "teleport": [
                [10, 2],
                [2, 10]
              ]
            }
         */
        ArrayNode events = mapper.createArrayNode();

        if (model.getSpawns() != null && model.getTeleports() != null) {
            events.add(mapper.createArrayNode().add(model.getSpawns().getFirst().getRow()).add(point[1]));
        }
        return events;
    }

    private static ObjectNode createEventsData(MapModel model) {
        //TODO terminar el metodo
    }

    private static void validateMapNode(JsonNode mapNode, String mapName) {
        if (mapNode.isMissingNode()) {
            throw new IllegalArgumentException("Mapa no encontrado: " + mapName);
        }
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

