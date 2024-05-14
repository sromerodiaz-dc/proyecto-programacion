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
    private int row;
    private int col;

    public CeldaVacia(int row, int col) {
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        imageLabel = new JLabel();
        add(imageLabel);
        this.row = row;
        this.col = col;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setImageIcon(imageIcon, row, col);
                System.out.println(imageIcon.getImage());
            }
        });
    }

    public void setImageIcon(ImageIcon imageIcon, int row, int col) {
        imageLabel.setIcon(Celda.escaladoImage(imageIcon));
        VacioPanel.getFormato()[row][col] = new ImageIcon((imageIcon).getImage());
    }
}
