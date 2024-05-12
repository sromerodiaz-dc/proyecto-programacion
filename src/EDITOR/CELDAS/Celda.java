package EDITOR.CELDAS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Celda extends JPanel{
    private boolean seleccionada;

    public Celda() {
        this.seleccionada = false;

        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

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
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }
}
