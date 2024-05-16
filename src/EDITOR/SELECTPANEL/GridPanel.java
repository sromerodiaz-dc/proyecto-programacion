package EDITOR.SELECTPANEL;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un panel de cuadrícula.
 * Cada celda en el panel puede contener una imagen y puede ser seleccionada o deseleccionada.
 *
 * @author Santiago Agustin Romero Diaz
 */
public class GridPanel extends JPanel {
    /**
     * Lista de paneles cuadrados en el panel de cuadrícula.
     */
    private static List<Celda> celdas;
    Celda celda;
    ImageIcon sprite;
    /**
     * Crea un nuevo panel de cuadrícula con el número de filas y columnas especificadas.
     *
     * @param rows número de filas en el panel de cuadrícula
     * @param cols número de columnas en el panel de cuadrícula
     * @param sprites matriz de ImageIcon que representan los sprites
     * @throws IOException si ocurre un error al crear el panel de cuadrícula
     */
    public GridPanel(int rows, int cols, ImageIcon[] sprites) {
        celdas = new ArrayList<>();

        setBackground(Color.DARK_GRAY);
        setLayout(new GridLayout(rows, cols)); // Usar GridLayout para el diseño

        int contador = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (sprites.length > contador) {
                    ImageIcon sprite = sprites[contador];
                    contador++;
                    Celda celda = new Celda(sprite);
                    celdas.add(celda);
                    add(celda);
                } else {
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