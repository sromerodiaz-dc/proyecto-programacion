package com.editor.panel;

import com.editor.emptymap.CeldaVacia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Clase que representa una celda en un panel de cuadrícula.
 * Cada celda puede contener una imagen y puede ser seleccionada o deseleccionada.
 *
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class Celda extends JPanel {
    /**
     * Indica si la celda está seleccionada o no.
     */
    public boolean seleccionada;

    /**
     * Imagen que se muestra en la celda.
     */
    public ImageIcon imageIcon;

    /**
     * Crea una nueva celda con la imagen especificada.
     *
     * @param imageIcon imagen que se mostrará en la celda
     */
    public Celda(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;

        // Establece el fondo de la celda en gris oscuro
        setBackground(Color.DARK_GRAY);
        // Establece el borde de la celda en gris claro
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        // Si la imagen no es nula, crea un label con la imagen escalada
        if (imageIcon.getImage() != null) {
            JLabel imageLabel = new JLabel(escaladoImage(imageIcon, 32, 32));
            add(imageLabel);
        }

        // Agrega un listener de ratón a la celda
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (seleccionada)
                    deseleccionar(); // Deselecciona la celda si está seleccionada
                else {
                    CeldaVacia.imageIcon = seleccionar(); // Selecciona la celda y devuelve la imagen anterior
                    deseleccionarTodas(Celda.this); // Deselecciona todas las demás celdas
                }
            }
        });
    }

    /**
     * Selecciona la celda y devuelve la imagen que contiene.
     * * @return imagen que contiene la celda
     */
    public ImageIcon seleccionar() {
        setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2, true));
        seleccionada = true;
        return imageIcon;
    }

    /**
     * Deselecciona la celda.
     */
    public void deseleccionar() {
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        seleccionada = false;
    }

    /**
     * Deselecciona todas las celdas excepto la especificada.
     *
     * @param celda celda que no se deseleccionará
     */
    public void deseleccionarTodas(Celda celda) {
        for (Celda c : GridPanel.getCeldas()) {
            if (c != celda)
                c.deseleccionar();
        }
    }

    /**
     * Redimensiona una imagen a un tamaño específico.
     *
     * @param imageIcon imagen a redimensionar
     * @return imagen redimensionada
     */
    public static ImageIcon escaladoImage(ImageIcon imageIcon,int width, int height) {
        // Get Image from Icon
        Image originalImage = imageIcon.getImage();

        // Resize the Image object
        Image scaledImage = originalImage.getScaledInstance(width,height, Image.SCALE_SMOOTH);

        // Create a new ImageIcon from the scaled Image
        return new ImageIcon(scaledImage);
    }
}
