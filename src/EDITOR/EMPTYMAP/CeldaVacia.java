package EDITOR.EMPTYMAP;

import EDITOR.SELECTPANEL.Celda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Esta clase define la interacción entre el editor y el mapa jugable.
 *
 * @author Santiago Agustin Romero Diaz
 * @version 1.0
 * @since 2023-03-01
 */
public class CeldaVacia extends JPanel {
    /** El JLabel que muestra el icono de la imagen. */
    public JLabel imageLabel;

    /** El ImageIcon que se mostrará en el JLabel. */
    public static ImageIcon imageIcon;

    /** El VacioPanel que contiene esta CeldaVacia. */
    public VacioPanel vacioPanel;

    /**
     * Constructs a new CeldaVacia with the given row and column indices.
     *
     * @param row The row index of the CeldaVacia.
     * @param col The column index of the CeldaVacia.
     * @param panel The VacioPanel that contains this CeldaVacia.
     */
    public CeldaVacia(int row, int col, VacioPanel panel) {
        this.vacioPanel = panel;
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        add(imageLabel);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                imageLabel.setHorizontalAlignment(JLabel.CENTER);
                imageLabel.setVerticalAlignment(JLabel.CENTER);
                setImageIcon(imageIcon, row, col,imageLabel.getWidth(),imageLabel.getHeight());
            }
        });
    }

    /**
     * Sets the ImageIcon of the JLabel and updates the corresponding cell in the VacioPanel's format array.
     *
     * @param imageIcon The ImageIcon to be displayed.
     * @param row The row index of the cell to be updated.
     * @param col The column index of the cell to be updated.
     */
    public void setImageIcon(ImageIcon imageIcon, int row, int col,int width, int height) {
        imageLabel.setIcon(Celda.escaladoImage(imageIcon, width, height));
        vacioPanel.getFormato()[row][col] = imageIcon;
    }

    /**
     * Sets the ImageIcon of the JLabel.
     *
     * @param imageIcon The ImageIcon to be displayed.
     */
    public void setImageIconLocal(ImageIcon imageIcon,int width, int height) {
        imageLabel.setIcon(Celda.escaladoImage(imageIcon,width,height));
    }

    public int getWidthLocal(){
        for (CeldaVacia celda : VacioPanel.celdaVacias) {
            if (celda == this)
                return celda.getWidth();
        }
        return 1;
    }

    public int getHeightLocal(){
        for (CeldaVacia celda : VacioPanel.celdaVacias) {
            if (celda == this)
                return celda.getHeight();
        }
        return 1;
    }
}
