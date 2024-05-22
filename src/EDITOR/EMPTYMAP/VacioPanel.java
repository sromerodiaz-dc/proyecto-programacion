package EDITOR.EMPTYMAP;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static EDITOR.EMPTYMAP.CeldaVacia.imageIcon;

/**
 * Esta clase define la interacción del jugador con el entorno asi como su movimiento y uso de gráficos en 2D.
 *
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * */
public class VacioPanel extends JPanel {
    public static ImageIcon[][] formato;
    public static ArrayList<CeldaVacia> celdaVacias;

    public VacioPanel(int rows, int cols) {
        iniciarComponente(rows, cols);
    }

    public void iniciarComponente(int rows, int cols) {
        formato = new ImageIcon[rows][cols];
        celdaVacias = new ArrayList<>();

        setBackground(Color.DARK_GRAY);
        setLayout(new GridLayout(rows, cols));
        GridBagConstraints constraints = new GridBagConstraints();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                CeldaVacia celda = new CeldaVacia(row, col,this);
                formato[row][col] = (imageIcon == null) ? null : imageIcon;
                celdaVacias.add(celda);
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
