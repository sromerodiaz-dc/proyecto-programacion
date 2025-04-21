package com.editor.mapmodel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TileRenderer extends DefaultTableCellRenderer {
    private Map<Integer, ImageIcon> tileSet;

    public TileRenderer(Map<Integer, ImageIcon> tileSet) {
        this.tileSet = tileSet;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(...);
        int tileId = (int) value;
        label.setIcon(tileSet.get(tileId));
        return label;
    }
}