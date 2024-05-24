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
 */
public class VacioPanel extends JPanel {
    /** El arreglo de ImageIcon que representa el formato del panel principal. */
    public ImageIcon[][] formato;
    /** La lista de celdas vacías en el panel principal. */
    public ArrayList<CeldaVacia> celdaVacias;

    /**
     * Construye nuevo VacioPanel con las filas y columnas dadas.
     *
     * @param rows       filas.
     * @param cols       columnas.
     */
    public VacioPanel(int rows, int cols) {
        iniciarComponente(rows, cols);
    }

    /**
     * Inicializa los componentes del panel principal.
     *
     * @param rows filas.
     * @param cols columnas.
     */
    public void iniciarComponente(int rows, int cols) {
        // Inicializa el arreglo de ImageIcon y la lista de celdas vacías
        formato = new ImageIcon[rows][cols];
        celdaVacias = new ArrayList<>();

        // Establece el fondo del panel principal
        setBackground(Color.DARK_GRAY);

        // Establece el layout en una cuadrícula con las filas y columnas definidas por el usuario
        setLayout(new GridLayout(rows, cols));
        GridBagConstraints constraints = new GridBagConstraints();

        // Iterar sobre las filas y columnas del panel principal
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Crear una nueva celda vacía y agregarla al panel principal
                CeldaVacia celda = new CeldaVacia(row, col, this);
                formato[row][col] = (imageIcon == null) ? null : imageIcon;
                celdaVacias.add(celda);

                // Agregar la celda vacía a la cuadrícula con las constraints definidas
                constraints.gridx = col;
                constraints.gridy = row;
                add(celda, constraints);
            }
        }
    }

    /**
     * Obtiene el arreglo de ImageIcon que representa el formato del panel principal.
     *
     * @return El arreglo de ImageIcon que representa el formato del panel principal.
     */
    public ImageIcon[][] getFormato() {
        return formato;
    }
}
