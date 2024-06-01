package EDITOR.EMPTYMAP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Arrays;

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
    int[][] contenido;
    ImageIcon[] sprites;

    /**
     * Construye nuevo VacioPanel con las filas y columnas dadas.
     *
     * @param rows       filas.
     * @param cols       columnas.
     */
    public VacioPanel(int rows, int cols) {
        iniciarComponente(rows, cols);
    }

    public VacioPanel(int[][] contenido, ImageIcon[] sprites){
        this.contenido = contenido;
        this.sprites = sprites;
        int[] datos = getFilasYColumnas(contenido);
        iniciarComponente(datos[1],datos[0]);
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
        int i = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Crear una nueva celda vacía
                final CeldaVacia celda = new CeldaVacia(row, col, this);
                celdaVacias.add(celda);

                // Agregar la celda vacía a la cuadrícula con las constraints definidas
                constraints.gridx = col;
                constraints.gridy = row;
                add(celda, constraints);

                // Agregar un ComponentListener para escuchar cuando la celda se ha dibujado
                celda.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        if (contenido!= null) {
                            celda.setImageIcon(sprites[contenido[celda.getCol()][celda.getRow()]], celda.getRow(), celda.getCol(), celda.getWidth()-15, celda.getHeight()-15);
                        }
                    }
                });
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

    public int[] getFilasYColumnas(int[][] datos) {
        int numFilas = datos.length;
        int numColumnas = 0;
        for (int i = 0; i < datos.length; i++) {
            numColumnas = Math.max(numColumnas, datos[i].length);
        }
        return new int[] {numFilas, numColumnas};
    }
}
