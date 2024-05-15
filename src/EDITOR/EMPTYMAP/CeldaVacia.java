package EDITOR.EMPTYMAP;

import EDITOR.SELECTPANEL.Celda;
import EDITOR.WORLD.CeldaWorld;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CeldaVacia extends JPanel {
    private JLabel imageLabel;
    public static ImageIcon imageIcon;
    VacioPanel vacioPanel;

    public CeldaVacia(int row, int col,VacioPanel panel) {
        this.vacioPanel = panel;
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        imageLabel = new JLabel();
        add(imageLabel);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setImageIcon(imageIcon, row, col);
            }
        });
    }

    public void setImageIcon(ImageIcon imageIcon, int row, int col) {
        imageLabel.setIcon(Celda.escaladoImage(imageIcon));
        vacioPanel.getFormato()[row][col] = imageIcon;
    }
}
