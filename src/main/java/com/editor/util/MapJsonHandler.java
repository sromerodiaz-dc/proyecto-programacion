package com.editor.util;

import com.editor.model.record.CeldaCoord;
import com.editor.model.record.MapData;
import com.editor.model.MapModel;
import com.editor.model.record.EntitySpawnEvent;
import com.editor.model.record.TeleportEvent;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.RawValue;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MapJsonHandler {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String MAPS_PATH = "src/main/resources/data/maps/";

    public static MapData loadMapData(String mapName) throws IOException {
        File mapFile = new File(MAPS_PATH + mapName + ".json");
        JsonNode root = mapper.readTree(mapFile);
        JsonNode mapNode = root.path(mapName);

        int cols = mapNode.path("width").asInt();
        int rows = mapNode.path("height").asInt();
        int[][] matrix = parseDataMatrix(mapNode.path("data"), rows, cols);

        // Cargar colisiones
        Set<CeldaCoord> collisions = new HashSet<>();
        JsonNode collisionsNode = mapNode.path("colisiones");
        if (collisionsNode.isArray()) {
            for (JsonNode node : collisionsNode) {
                int row = node.path("row").asInt();
                int col = node.path("col").asInt();
                collisions.add(new CeldaCoord(row, col));
            }
        }

        // Cargar eventos desde el objeto "eventos"
        CeldaCoord playerSpawn = null;
        List<EntitySpawnEvent> entitySpawns = new ArrayList<>();
        List<TeleportEvent> teleports = new ArrayList<>();

        JsonNode eventosNode = mapNode.path("eventos");
        if (!eventosNode.isMissingNode()) {
            // Cargar spawn del jugador
            JsonNode spawnNode = eventosNode.path("spawn");
            if (spawnNode.isArray() && spawnNode.size() == 2) {
                int spawnRow = spawnNode.get(0).asInt();
                int spawnCol = spawnNode.get(1).asInt();
                playerSpawn = new CeldaCoord(spawnRow, spawnCol);
            }

            // Cargar spawns de entidades
            JsonNode entitiesNode = eventosNode.path("entities");
            if (entitiesNode.isArray()) {
                for (JsonNode entityNode : entitiesNode) {
                    String entityId = entityNode.path("entityId").asText();
                    int row = entityNode.path("row").asInt();
                    int col = entityNode.path("col").asInt();
                    entitySpawns.add(new EntitySpawnEvent(entityId, row, col));
                }
            }

            // Cargar teleports
            JsonNode teleportsNode = eventosNode.path("teleports");
            if (teleportsNode.isArray()) {
                for (JsonNode teleportNode : teleportsNode) {
                    if (teleportNode.isArray() && teleportNode.size() == 4) {
                        int fromRow = teleportNode.get(0).asInt();
                        int fromCol = teleportNode.get(1).asInt();
                        int toRow = teleportNode.get(2).asInt();
                        int toCol = teleportNode.get(3).asInt();
                        teleports.add(new TeleportEvent(fromRow, fromCol, toRow, toCol));
                    }
                }
            }
        }

        return new MapData(rows, cols, matrix, collisions, playerSpawn, entitySpawns, teleports);
    }

    public static void saveMapData(MapModel model) { //TODO que las colisiones se guarden como [row,col],[...],etc. y no como {"row":row,"col":col}
        String mapName = JOptionPane.showInputDialog(null, "Ingrese el nombre del mapa:");

        if (mapName == null || mapName.trim().isEmpty()) {
            showError("Nombre de mapa inválido.");
            return;
        }

        try {
            // Crear estructura del mapa
            Map<String, Object> mapData = new LinkedHashMap<>();
            mapData.put("width", model.getCols());
            mapData.put("height", model.getRows());

            // Serializar matriz manualmente en formato compacto con espaciado correcto
            StringBuilder matrixJson = getStringBuilder(model);
            mapData.put("data", new RawValue(matrixJson.toString()));

            // Sección "colisiones"
            List<Map<String, Integer>> colisionesList = new ArrayList<>();
            for (CeldaCoord coord : model.getCollisions()) {
                Map<String, Integer> colision = new HashMap<>();
                colision.put("row", coord.row());
                colision.put("col", coord.col());
                colisionesList.add(colision);
            }
            mapData.put("colisiones", colisionesList);

            // Crear objeto "eventos"
            Map<String, Object> eventos = new LinkedHashMap<>();

            // Spawn del jugador (solo si existe)
            if (model.getPlayerSpawn() != null) {
                eventos.put("spawn", Arrays.asList(
                        model.getPlayerSpawn().row(),
                        model.getPlayerSpawn().col()
                ));
            }

            // Spawns de entidades
            List<Map<String, Object>> entities = new ArrayList<>();
            for (EntitySpawnEvent entity : model.getEntitySpawns()) {
                Map<String, Object> entityMap = new HashMap<>();
                entityMap.put("entityId", entity.id());
                entityMap.put("row", entity.row());
                entityMap.put("col", entity.col());
                entities.add(entityMap);
            }
            eventos.put("entities", entities);

            // Teleports
            List<List<Integer>> teleports = new ArrayList<>();
            for (TeleportEvent teleport : model.getTeleports()) {
                teleports.add(Arrays.asList(
                        teleport.row(),
                        teleport.col(),
                        teleport.targetRow(),
                        teleport.targetCol()
                ));
            }
            eventos.put("teleports", teleports);

            mapData.put("eventos", eventos);

            // Nodo raíz con nombre de mapa
            Map<String, Object> rootNode = new LinkedHashMap<>();
            rootNode.put(mapName, mapData);

            // Crear directorio si no existe
            File mapsDir = new File(MAPS_PATH);
            if (!mapsDir.exists()) {
                mapsDir.mkdirs();
            }

            // Guardar en archivo individual
            File mapFile = new File(MAPS_PATH + mapName + ".json");

            // Configurar formato con indentación personalizada
            DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
            prettyPrinter.indentArraysWith(new DefaultIndenter("  ", "\n"));
            prettyPrinter.indentObjectsWith(new DefaultIndenter("  ", "\n"));

            try (FileWriter fileWriter = new FileWriter(mapFile)) {
                String jsonString = mapper.writer(prettyPrinter).writeValueAsString(rootNode);

                // Ajustar el formato para coincidir exactamente con el ejemplo
                jsonString = jsonString
                        .replaceAll("\"data\" : \\[", "\"data\" : [")
                        .replaceAll("\"colisiones\" : \\[", "\"colisiones\" : [")
                        .replaceAll("\"eventos\" : \\{", "\"eventos\" : {");

                fileWriter.write(jsonString);
            }

            showSuccessDialog(mapName);

        } catch (IOException e) {
            showError("Error al guardar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static StringBuilder getStringBuilder(MapModel model) {
        StringBuilder matrixJson = new StringBuilder("[\n");
        int[][] matrix = model.getMatrixForExport();
        for (int i = 0; i < matrix.length; i++) {
            matrixJson.append("      [");
            for (int j = 0; j < matrix[i].length; j++) {
                if (j > 0) matrixJson.append(",");
                matrixJson.append(matrix[i][j]);
            }
            matrixJson.append("]");
            if (i < matrix.length - 1) matrixJson.append(",");
            matrixJson.append("\n");
        }
        matrixJson.append("    ]");
        return matrixJson;
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