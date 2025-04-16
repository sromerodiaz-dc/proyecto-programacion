package com.editor.emptymap;

import com.editor.panel.Celda;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Esta clase define la interacción entre el editor y el mapa jugable.
 *
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class CeldaVacia extends JPanel {
    /** El JLabel que muestra el icono de la imagen. */
    public JLabel imageLabel;
    /** El ImageIcon que se mostrará en el JLabel. */
    public static ImageIcon imageIcon;
    /** El VacioPanel que contiene esta CeldaVacia. */
    public VacioPanel vacioPanel;
    /** El boton será modificado desde la GUI. */
    public boolean buttonSelected = false;

    int row, col;

    /**
     * Construye nueva CeldaVacia con las filas y columnas dadas.
     *
     * @param row        fila.
     * @param col        columna.
     * @param panel      Panel contenedor sobre CeldaVacia.
     */
    public CeldaVacia(int row, int col, VacioPanel panel) {
        // Inicializa el VacioPanel que contiene esta CeldaVacia
        this.vacioPanel = panel;
        this.row = row;
        this.col = col;

        // Establece el fondo y borde de la celda vacía
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        // Crea un nuevo JLabel para mostrar el icono de imagen
        imageLabel = new JLabel();
        add(imageLabel);

        // Agrega un MouseListener a la celda vacía para manejar el evento de clic del mouse
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setImageIcon(imageIcon, row, col, getWidthLocal() - 15, getHeightLocal() - 15);
            }
        });

        // Agrega un MouseMotionListener a la celda vacía para manejar el evento de movimiento del mouse
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // Solo realiza la acción si isPressed es true.
                // Esto varía dependiendo de si se presiona la tecla F o no
                if (buttonSelected) setImageIcon(imageIcon, row, col, getWidthLocal() - 15, getHeightLocal() - 15);
            }
        });
    }

    /**
     * Establece el icono de imagen en la celda vacía.
     *
     * @param imageIcon  El icono de imagen a establecer.
     * @param row        El índice de fila de la celda vacía.
     * @param col        El índice de columna de la celda vacía.
     * @param width      El ancho del icono de imagen.
     * @param height     La altura del icono de imagen.
     */
    public void setImageIcon(ImageIcon imageIcon, int row, int col,int width, int height) {
        // Establece el icono de imagen en el JLabel y actualiza el formato en el VacioPanel
        imageLabel.setIcon(Celda.escaladoImage(imageIcon, width, height));
        vacioPanel.getFormato()[row][col] = imageIcon;
    }

    /**
     * Establece el icono de imagen en la celda vacía con anchura y altura locales.
     *
     * @param imageIcon  El icono de imagen a establecer.
     * @param width      El ancho local del icono de imagen.
     * @param height     La altura local del icono de imagen.
     */
    public void setImageIconLocal(ImageIcon imageIcon,int width, int height) {
        // Establece el icono de imagen en el JLabel
        imageLabel.setIcon(Celda.escaladoImage(imageIcon, width, height));
    }

    /**
     * Obtiene el ancho local de la celda vacía.
     *
     * @return El ancho local de la celda vacía.
     */
    public int getWidthLocal(){
        // Iterar sobre las celdas vacías en el VacioPanel para encontrar esta celda vacía
        for (CeldaVacia celda : vacioPanel.celdaVacias) {
            if (celda == this)
                return celda.getWidth();
        }
    return 1;
    }

    /**
     * Obtiene el largo local de la celda vacía.
     *
     * @return El largo local de la celda vacía.
     */
    public int getHeightLocal(){
        for (CeldaVacia celda : vacioPanel.celdaVacias) {
            if (celda == this)
                return celda.getHeight();
        }
    return 1;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
}