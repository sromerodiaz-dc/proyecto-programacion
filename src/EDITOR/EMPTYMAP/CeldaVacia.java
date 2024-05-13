package EDITOR.EMPTYMAP;

import EDITOR.SELECTPANEL.Celda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CeldaVacia extends JPanel{
    public static ImageIcon imageIcon;
    private JLabel imageLabel;

    public CeldaVacia() {
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        imageLabel = new JLabel();
        add(imageLabel);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (imageIcon != null) {
                    imageLabel.setIcon(Celda.escaladoImage(imageIcon));
                }
            }
        });
    }
}
