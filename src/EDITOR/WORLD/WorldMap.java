package EDITOR.WORLD;

import EDITOR.EMPTYMAP.CeldaVacia;
import EDITOR.SELECTPANEL.Celda;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class WorldMap extends JPanel {
    CeldaWorld celdaWorld;
    static CeldaWorld[] celdas;
    int celdaID;

    public WorldMap() {
        int rows = 16;
        int cols = 16;
        initComponents(rows, cols);
    }

    private void initComponents(int rows, int cols) {
        celdas = new CeldaWorld[256];

        setBackground(Color.DARK_GRAY);
        setLayout(new GridLayout(rows, cols));
        GridBagConstraints constraints = new GridBagConstraints();

        celdaID = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                celdaWorld = new CeldaWorld();
                celdas[celdaID] = celdaWorld;
                constraints.gridx = col;
                constraints.gridy = row;
                add(celdaWorld, constraints);
                celdaID++;
            }
        }
    }

    public static CeldaWorld[] getCeldas() {
        return celdas;
    }
}