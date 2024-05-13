package EDITOR.EMPTYMAP;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VacioPanel extends JPanel {
    private int rows = 16;
    private int cols = 16;
    private List<CeldaVacia> celdas;

    public VacioPanel() {
        iniciarComponente(rows, cols);
    }

    public void iniciarComponente(int rows, int cols) {
        celdas = new ArrayList<>();

        setBackground(Color.DARK_GRAY);
        setLayout(new GridLayout(rows, cols));
        GridBagConstraints constraints = new GridBagConstraints();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                CeldaVacia celda = new CeldaVacia();
                celdas.add(celda);
                constraints.gridx = col;
                constraints.gridy = row;
                add(celda, constraints);
            }
        }
    }

    public List<CeldaVacia> getCeldas() {
        return celdas;
    }
}
