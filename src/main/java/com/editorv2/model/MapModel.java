package com.editorv2.model;

import java.util.ArrayList;
import java.util.List;

public class MapModel {
    private final int[][] matrix;
    private final List<IModelChangeListener> listeners = new ArrayList<>();
    private int col;
    private int row;

    public MapModel(int col, int row) {
        this.matrix = new int[col][row];
    }

    // Métodos para modificar la matriz (setTile, getTile, etc.)
    public void setTile(int row, int col, int value) {
        matrix[row][col] = value;
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
}