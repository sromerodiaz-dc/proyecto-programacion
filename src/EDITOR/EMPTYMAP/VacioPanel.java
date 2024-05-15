package EDITOR.EMPTYMAP;

import javax.swing.*;
import java.awt.*;

import static EDITOR.EMPTYMAP.CeldaVacia.imageIcon;

public class VacioPanel extends JPanel {
    private int gridRows = 12;
    private int gridColumns = 18;
    public ImageIcon[][] formato;

    public VacioPanel() {
        iniciarComponente(gridRows, gridColumns);
    }

    public void iniciarComponente(int rows, int cols) {
        formato = new ImageIcon[rows][cols];

        setBackground(Color.DARK_GRAY);
        setLayout(new GridLayout(rows, cols));
        GridBagConstraints constraints = new GridBagConstraints();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                CeldaVacia celda = new CeldaVacia(row, col,this);
                formato[row][col] = (imageIcon == null) ? null : imageIcon;
                constraints.gridx = col;
                constraints.gridy = row;
                add(celda, constraints);
            }
        }
    }

    public ImageIcon[][] getFormato() {
        return formato;
    }
}
