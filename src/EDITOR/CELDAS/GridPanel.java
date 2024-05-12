package EDITOR.CELDAS;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GridPanel extends JPanel {
    private List<Celda> celdas; // Lista de paneles cuadrados

    public GridPanel(int rows, int cols) {
        celdas = new ArrayList<>();

        setBackground(Color.DARK_GRAY);
        setLayout(new GridBagLayout()); // Usar GridBagLayout para el diseño
        GridBagConstraints constraints = new GridBagConstraints();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Celda celda = new Celda();
                celdas.add(celda);

                constraints.gridx = col;
                constraints.gridy = row;

                add(celda, constraints);
            }
        }
    }

    public List<Celda> getCeldas() {
        return celdas;
    }
}
