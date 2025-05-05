package com.editorv2.model;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MapModel {
    private final int[][] matrix;
    private final List<IModelChangeListener> listeners = new ArrayList<>();
    private int row;
    private int col;

    private Set<Point> modifiedCells = new HashSet<>(); // Rastrea celdas modificadas

    public MapModel(int rows, int cols) {
        this.matrix = new int[rows][cols];
    }

    // Métodos para modificar la matriz (setTile, getTile, etc.)
    public void setTile(int row, int col, int value) {
        matrix[row][col] = value;
        modifiedCells.add(new Point(col, row)); // Registrar celda modificada
        notifyListeners();
    }

    public void addListener(IModelChangeListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (IModelChangeListener l : listeners) {
            l.onModelChanged();
        }
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int[][] getMatrixForExport() {
        int[][] exportMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                // Solo procesa celdas modificadas, otras se guardan como 0 : bloque negro
                exportMatrix[i][j] = modifiedCells.contains(new Point(j, i)) ? matrix[i][j] : 0;
            }
        }
        return exportMatrix;
    }

    public List<IModelChangeListener> getListeners() {
        return listeners;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getTile(int row, int col) {
        return matrix[row][col];
    }

    // Asegurar que existan estos métodos en MapModel
    public int getCols() {
        return matrix[0].length; // O la implementación real
    }

    public int getRows() {
        return matrix.length;
    }

    // Para optimización de renderizado
    public Set<Point> getModifiedCells() {
        return new HashSet<>(modifiedCells);
    }

    public void clearModifiedCells() {
        modifiedCells.clear();
    }
}