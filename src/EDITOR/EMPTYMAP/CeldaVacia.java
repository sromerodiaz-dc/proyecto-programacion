package EDITOR.EMPTYMAP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CeldaVacia extends JPanel{
    private boolean seleccionada;
    ImageIcon imageIcon;

    public CeldaVacia(ImageIcon imageIcon) {
        this.seleccionada = false;
        this.imageIcon = imageIcon;

        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JLabel imageLabel = new JLabel(imageIcon);
        add(imageLabel);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!seleccionada) {
                    seleccionar();
                } else {
                    deseleccionar();
                }
            }
        });
    }

    public void seleccionar() {
        seleccionada = true;
        setBorder(BorderFactory.createLineBorder(Color.YELLOW,1,true));
    }

    public void deseleccionar() {
        seleccionada = false;
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }
}
