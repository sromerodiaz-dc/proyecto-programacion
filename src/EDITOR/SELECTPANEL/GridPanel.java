package EDITOR.SELECTPANEL;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un panel de cuadrícula.
 * Cada celda en el panel puede contener una imagen y puede ser seleccionada o deseleccionada.
 *
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class GridPanel extends JPanel {
    /**
     * Lista de paneles cuadrados en el panel de cuadrícula.
     */
    private static List<Celda> celdas;
    /**
     * Crea un nuevo panel de cuadrícula con el número de filas y columnas especificadas.
     *
     * @param rows número de filas en el panel de cuadrícula
     * @param cols número de columnas en el panel de cuadrícula
     * @param sprites matriz de ImageIcon que representan los sprites
     */
    public GridPanel(int rows, int cols, ImageIcon[] sprites) {
        celdas = new ArrayList<>();

        // Establece el fondo del panel en gris oscuro
        setBackground(Color.DARK_GRAY);
        // Establece el diseño del panel en una cuadrícula con el número de filas y columnas especificado
        setLayout(new GridLayout(rows, cols));

        int contador = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Si hay suficientes imágenes en el array, crea una celda con la imagen correspondiente
                if (sprites.length > contador) {
                    ImageIcon sprite = sprites[contador];
                    contador++;
                    Celda celda = new Celda(sprite);
                    celdas.add(celda);
                    add(celda);
                } else {
                    // Si no hay suficientes imágenes en el array, crea una celda vacía
                    Celda celda = new Celda(new ImageIcon());
                    celdas.add(celda);
                    add(celda);
                }
            }
        }
    }

    /**
     * Obtiene la lista de paneles cuadrados en el panel de cuadrícula.
     *
     * @return lista de paneles cuadrados
     */
    public static List<Celda> getCeldas() {
        return celdas;
    }
}