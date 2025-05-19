package com.editorv2.model;

import com.editorv2.controller.TextureController;
import com.editorv2.model.event.SpawnEvent;
import com.editorv2.model.event.TeleportEvent;

import java.util.*;

public class MapModel {
    private final Map<CeldaCoord, TileData> matrix = new HashMap<>();
    private final Map<CeldaCoord, Integer> modifiedCells = new HashMap<>();
    private final List<IModelChangeListener> listeners = new ArrayList<>();
    private final Set<CeldaCoord> manualCollisions = new HashSet<>(); // Colisiones manuales
    private final Set<CeldaCoord> autoCollisions = new HashSet<>(); // Colisiones por textura
    private final TileData DEFAULT_TILE = new TileData(0, false);
    private SpawnEvent spawn;
    private final List<TeleportEvent> teleports = new ArrayList<>();
    private final int rows;
    private final int cols;
    private final TextureController textureController;

    public MapModel(int rows, int cols, TextureController textureController) {
        this.rows = rows;
        this.cols = cols;
        this.textureController = textureController;
    }

    public void setTile(int row, int col, int textureId) {
        CeldaCoord coord = new CeldaCoord(row, col);
        boolean isCollision = textureController.isTextureCollision(textureId)
                || manualCollisions.contains(coord);

        // Forzar actualización incluso si el textureId es el mismo
        matrix.put(coord, new TileData(textureId, isCollision));
        modifiedCells.put(coord, textureId);
        notifyListeners();
    }

    public int getTile(int row, int col) {
        return matrix.getOrDefault(new CeldaCoord(row, col), DEFAULT_TILE).textureId();
    }

    public void refreshTilesWithTexture(int textureId) {
        matrix.entrySet().stream()
                .filter(entry -> entry.getValue().textureId() == textureId)
                .forEach(entry -> {
                    CeldaCoord coord = entry.getKey();
                    boolean isCollision = textureController.isTextureCollision(textureId)
                            || manualCollisions.contains(coord);
                    matrix.put(coord, new TileData(textureId, isCollision));
                    modifiedCells.put(coord, textureId);
                });
        notifyListeners();
    }

    public int[][] getMatrixForExport() {
        int[][] exportMatrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                exportMatrix[i][j] = matrix.getOrDefault(new CeldaCoord(i, j), new TileData(0, false)).textureId();
            }
        }
        return exportMatrix;
    }

    public boolean isTileCollision(CeldaCoord coord) {
        return matrix.containsKey(coord) && matrix.get(coord).isCollision();
    }

    public Set<CeldaCoord> getModifiedCells() {
        return new HashSet<>(modifiedCells.keySet());
    }

    public void clearModifiedCells() {
        modifiedCells.clear();
    }

    public void addListener(IModelChangeListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (IModelChangeListener l : listeners) {
            l.onModelChanged();
        }
    }

    public void addCollision(int row, int col) {
        CeldaCoord coord = new CeldaCoord(row, col);
        manualCollisions.add(coord);

        TileData tile = matrix.getOrDefault(coord, DEFAULT_TILE);
        matrix.put(coord, new TileData(tile.textureId(), true)); // Forzar colisión
        modifiedCells.put(coord, tile.textureId());
        notifyListeners();
    }

    public void removeCollision(int row, int col) {
        CeldaCoord coord = new CeldaCoord(row, col);
        manualCollisions.remove(coord);

        TileData tile = matrix.getOrDefault(coord, DEFAULT_TILE);
        boolean isAutoCollision = textureController.isTextureCollision(tile.textureId());
        matrix.put(coord, new TileData(tile.textureId(), isAutoCollision)); // Restaurar colisión automática
        modifiedCells.put(coord, tile.textureId());
        notifyListeners();
    }

    public Set<CeldaCoord> getCollisions() {
        Set<CeldaCoord> allCollisions = new HashSet<>();
        allCollisions.addAll(manualCollisions);
        allCollisions.addAll(autoCollisions);
        return allCollisions;
    }

    // Getters/Setters para spawn y teleports
    public SpawnEvent getSpawn() { return spawn; }
    public void setSpawn(SpawnEvent spawn) { this.spawn = spawn; }
    public List<TeleportEvent> getTeleports() { return teleports; }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }
}