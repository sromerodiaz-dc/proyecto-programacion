package com.editor.mapmodel;

import javax.swing.table.AbstractTableModel;

public class MapTableModel extends AbstractTableModel {
    private int[][] mapData;

    public MapTableModel(int rows, int cols) {
        mapData = new int[rows][cols];
    }

    @Override
    public int getRowCount() { return mapData.length; }

    @Override
    public int getColumnCount() { return mapData[0].length; }

    @Override
    public Object getValueAt(int row, int col) {
        return mapData[row][col];
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        mapData[row][col] = (int) value;
        fireTableCellUpdated(row, col);
    }

    // Para redimensionar el mapa
    public void resize(int newRows, int newCols) {
        // Copiar datos existentes...
        mapData = new int[newRows][newCols];
        fireTableDataChanged();
    }
}