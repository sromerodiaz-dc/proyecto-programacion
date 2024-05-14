package EDITOR.SELECTPANEL;

import EDITOR.EMPTYMAP.CeldaVacia;
import EDITOR.WORLD.CeldaWorld;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Celda extends JPanel{
    public boolean seleccionada;
    public ImageIcon imageIcon;

    public Celda(ImageIcon imageIcon) {
        seleccionada = false;
        this.imageIcon = imageIcon;

        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        if (imageIcon.getImage() != null) {
            JLabel imageLabel = new JLabel(escaladoImage(imageIcon));
            add(imageLabel);
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (seleccionada)
                    deseleccionar();
                else {
                    CeldaVacia.imageIcon = seleccionar();
                    deseleccionarTodas();
                }
            }
        });
    }

    public ImageIcon seleccionar() {
        setBorder(BorderFactory.createLineBorder(Color.YELLOW,2,true));
        seleccionada = true;
        return imageIcon;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public void deseleccionar() {
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1));
        seleccionada = false;
    }

    public void deseleccionarTodas() {
        for (Celda celda : GridPanel.getCeldas()) {
            if (celda != this)
                celda.deseleccionar();
        }
    }

    public static ImageIcon escaladoImage (ImageIcon imageIcon) {
        // Get Image from Icon
        Image originalImage = imageIcon.getImage();

        // Resize the Image object
        Image scaledImage = originalImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);

        // Create a new ImageIcon from the scaled Image
        return new ImageIcon(scaledImage);
    }
}
