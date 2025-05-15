package com.editorv2.model;

import com.editorv2.model.event.MapEvent;
import com.editorv2.model.event.SpawnEvent;
import com.editorv2.model.event.TeleportEvent;

import java.util.*;

public class MapModel {
    private final Map<CeldaCoord, Integer> matrix = new HashMap<>();
    private final Map<CeldaCoord, Integer> modifiedCells = new HashMap<>();
    private final List<IModelChangeListener> listeners = new ArrayList<>();
    private List<int[]> collisions = new ArrayList<>();
    private SpawnEvent spawn; //TODO terminar de implementar el punto de spawn
    private List<TeleportEvent> teleports = new ArrayList<>();
    private final int rows;
    private final int cols;

    public MapModel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public void setTile(int row, int col, int value) {
        CeldaCoord coord = new CeldaCoord(row, col);
        Integer current = matrix.get(coord);
        if (Objects.equals(current, value)) return; // evita actualizaciones redundantes

        matrix.put(coord, value);
        modifiedCells.put(coord, value);
        notifyListeners();
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

    public List<int[]> getCollisions() {
        return collisions;
    }

    public void addCollision(int x, int y) {
        collisions.add(new int[]{x, y});
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public List<TeleportEvent> getTeleports() {
        return teleports;
    }

    public void addTeleport(TeleportEvent teleport) {
        teleports.add(teleport);
    }
}