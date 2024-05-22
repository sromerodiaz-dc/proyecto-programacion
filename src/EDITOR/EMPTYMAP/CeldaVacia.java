package EDITOR.EMPTYMAP;

import EDITOR.FX.KeyboardListener;
import EDITOR.SELECTPANEL.Celda;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

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
    /** El boton será modificado desde la GUI. */
    public boolean buttonSelected = false;
    KeyboardListener keyboardListener = new KeyboardListener();
    /**
     * Constructs a new CeldaVacia with the given row and column indices.
     *
     * @param row The row index of the CeldaVacia.
     * @param col The column index of the CeldaVacia.
     * @param panel The VacioPanel that contains this CeldaVacia.
     */
    public CeldaVacia(int row, int col, VacioPanel panel,  JLabel pincel) {
        this.vacioPanel = panel;

        this.addKeyListener(keyboardListener);
        setFocusable(true);
        requestFocus();

        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        imageLabel = new JLabel();
        add(imageLabel);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setImageIcon(imageIcon, row, col, getWidthLocal() - 15, getHeightLocal() - 15);
                modoPincel(pincel);
            }
        });

        if (buttonSelected){
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    setImageIcon(imageIcon, row, col, getWidthLocal() - 15, getHeightLocal() - 15);
                }
            });
        }
    }

    public void setImageIcon(ImageIcon imageIcon, int row, int col,int width, int height) {
        imageLabel.setIcon(Celda.escaladoImage(imageIcon, width, height));
        vacioPanel.getFormato()[row][col] = imageIcon;
    }

    public void setImageIconLocal(ImageIcon imageIcon,int width, int height) {
        imageLabel.setIcon(Celda.escaladoImage(imageIcon, width, height));
    }

    public int getWidthLocal(){
        for (CeldaVacia celda : vacioPanel.celdaVacias) {
            if (celda == this)
                return celda.getWidth();
        }
    return 1;
    }

    public int getHeightLocal(){
        for (CeldaVacia celda : vacioPanel.celdaVacias) {
            if (celda == this)
                return celda.getHeight();
        }
    return 1;
    }

    public void modoPincel(JLabel label) {
        if (keyboardListener.isFPressed) {
            label.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2,true));

        } else label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1,false));
    }
}