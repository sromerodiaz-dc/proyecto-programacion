package com.editor.model;

import com.editor.controller.TextureController;
import com.editor.model.record.EntitySpawnEvent;
import com.editor.model.record.TeleportEvent;
import com.editor.model.record.CeldaCoord;
import com.editor.model.record.TileData;

import java.util.*;

public class MapModel { //TODO Arreglar modo de colision. Cuando se hace clic en modo colision sin seleccionar texturas y se vuelve a modo normal. Las texturas se pintan y no hay manera de que vuelvan al estado normal
    private final Map<CeldaCoord, TileData> matrix = new HashMap<>();
    private final Map<CeldaCoord, Integer> modifiedCells = new HashMap<>();
    private final List<IModelChangeListener> listeners = new ArrayList<>();
    private final Set<CeldaCoord> manualCollisions = new HashSet<>(); // Colisiones manuales
    private final Set<CeldaCoord> autoCollisions = new HashSet<>(); // Colisiones por textura
    private final TileData DEFAULT_TILE = new TileData(0, false);
    private final int rows;
    private final int cols;
    private final TextureController textureController;

    // Nuevos campos para eventos
    private CeldaCoord playerSpawn;
    private final List<EntitySpawnEvent> entitySpawns = new ArrayList<>();
    private final List<TeleportEvent> teleports = new ArrayList<>();
    private CeldaCoord teleportSource; // Temporal para construcción de teleport

    public MapModel(int rows, int cols, TextureController textureController) {
        this.rows = rows;
        this.cols = cols;
        this.textureController = textureController;
    }

    public void setTile(int row, int col, int textureId) {
        CeldaCoord coord = new CeldaCoord(row, col);
        // Colisión automática se basa en la textura al momento de pintar
        boolean autoCollision = textureController.isTextureCollision(textureId);
        // Conservar colisiones manuales existentes
        boolean isManualCollision = manualCollisions.contains(coord);
        matrix.put(coord, new TileData(textureId, autoCollision || isManualCollision));
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
                    // Solo actualiza si NO tiene colisión manual
                    if (!manualCollisions.contains(coord)) {
                        boolean autoCollision = textureController.isTextureCollision(textureId);
                        matrix.put(coord, new TileData(textureId, autoCollision));
                        modifiedCells.put(coord, textureId);
                    }
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
        matrix.put(coord, new TileData(tile.textureId(), true));
        notifyListeners();
    }

    public void removeCollision(int row, int col) {
        CeldaCoord coord = new CeldaCoord(row, col);
        manualCollisions.remove(coord);
        TileData tile = matrix.getOrDefault(coord, DEFAULT_TILE);
        boolean autoCollision = textureController.isTextureCollision(tile.textureId());
        matrix.put(coord, new TileData(tile.textureId(), autoCollision));
        notifyListeners();
    }

    public Set<CeldaCoord> getCollisions() {
        Set<CeldaCoord> allCollisions = new HashSet<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                CeldaCoord coord = new CeldaCoord(i, j);
                if (isTileCollision(coord)) {
                    allCollisions.add(coord);
                }
            }
        }
        return allCollisions;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    // Métodos para eventos
    public void setPlayerSpawn(CeldaCoord coord) {
        this.playerSpawn = coord;
    }

    public CeldaCoord getPlayerSpawn() {
        return playerSpawn;
    }

    public void addEntitySpawn(EntitySpawnEvent event) {
        entitySpawns.add(event);
    }

    public void addAllEntitySpawn(List<EntitySpawnEvent> event) {
        entitySpawns.addAll(event);
    }

    public List<EntitySpawnEvent> getEntitySpawns() {
        return entitySpawns;
    }

    public void addTeleport(TeleportEvent teleport) {
        teleports.add(teleport);
    }

    public List<TeleportEvent> getTeleports() {
        return teleports;
    }

    public void setTeleportSource(CeldaCoord source) {
        this.teleportSource = source;
    }

    public CeldaCoord getTeleportSource() {
        return teleportSource;
    }

    public void clearTeleportSource() {
        this.teleportSource = null;
    }
}