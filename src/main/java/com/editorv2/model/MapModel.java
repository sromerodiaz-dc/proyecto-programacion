package com.editorv2.model;

import com.editorv2.controller.TextureController;
import com.editorv2.model.event.SpawnEvent;
import com.editorv2.model.event.TeleportEvent;

import java.util.*;

public class MapModel {
    private final Map<CeldaCoord, Integer> matrix = new HashMap<>();
    private final Map<CeldaCoord, Integer> modifiedCells = new HashMap<>();
    private final List<IModelChangeListener> listeners = new ArrayList<>();
    private final Set<CeldaCoord> manualCollisions = new HashSet<>(); // Colisiones manuales
    private final Set<CeldaCoord> autoCollisions = new HashSet<>(); // Colisiones por textura
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

    public void setTile(int row, int col, int value) {
        CeldaCoord coord = new CeldaCoord(row, col);
        Integer currentValue = matrix.get(coord);

        // Calcular si el estado de colisión ha cambiado
        boolean currentCollision = (currentValue != null)
                ? textureController.isTextureCollision(currentValue)
                : false;
        boolean newCollision = textureController.isTextureCollision(value);
        boolean collisionChanged = (currentCollision != newCollision);

        // Actualizar si el ID o el estado de colisión cambian
        if (currentValue == null || currentValue != value || collisionChanged) {
            matrix.put(coord, value);
            modifiedCells.put(coord, value);

            // Actualizar autoCollisions
            if (newCollision) {
                autoCollisions.add(coord);
            } else {
                autoCollisions.remove(coord);
            }

            notifyListeners();
        }
    }

    public int getTile(int row, int col) {
        return matrix.getOrDefault(new CeldaCoord(row, col), 0);
    }

    public int[][] getMatrixForExport() {
        int[][] exportMatrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                exportMatrix[i][j] = matrix.getOrDefault(new CeldaCoord(i, j), 0);
            }
        }
        return exportMatrix;
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
        modifiedCells.put(coord, matrix.get(coord)); // Añadir a modificadas
        notifyListeners(); // Notificar para repintar
    }

    public void removeCollision(int row, int col) {
        CeldaCoord coord = new CeldaCoord(row, col);
        manualCollisions.remove(coord);
        modifiedCells.put(coord, matrix.get(coord)); // Añadir a modificadas
        notifyListeners(); // Notificar para repintar
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