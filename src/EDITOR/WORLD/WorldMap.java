package EDITOR.WORLD;

import EDITOR.EMPTYMAP.CeldaVacia;


import java.awt.*;
import javax.swing.*;

public class WorldMap extends JPanel {
    public WorldMap() {
        int rows = 16;
        int cols = 16;
        initComponents(rows, cols);
    }

    private void initComponents(int rows, int cols) {
        setBackground(Color.DARK_GRAY);
        setLayout(new GridLayout(rows, cols));
        GridBagConstraints constraints = new GridBagConstraints();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                CeldaVacia celdaVacia = new CeldaVacia();
                constraints.gridx = col;
                constraints.gridy = row;
                add(celdaVacia, constraints);
            }
        }
    }
}