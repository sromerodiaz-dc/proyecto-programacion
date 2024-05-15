package EDITOR.WORLD;

import EDITOR.EMPTYMAP.CeldaVacia;

import javax.swing.*;
import java.awt.*;

public class CeldaWorld extends JPanel {
    public static ImageIcon imageIcon;
    private JLabel imageLabel;

    public CeldaWorld() {
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        imageLabel = new JLabel();
        add(imageLabel);
    }
}
