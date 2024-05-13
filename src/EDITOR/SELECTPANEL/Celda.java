package EDITOR.SELECTPANEL;

import EDITOR.EMPTYMAP.CeldaVacia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Celda extends JPanel{
    public static boolean seleccionada;
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
                System.out.println(seleccionada);
                if (seleccionada)
                    deseleccionar();
                else {
                    deseleccionarTodas();
                    CeldaVacia.imageIcon = seleccionar();
                    System.out.println(seleccionada);
                    System.out.println("b");
                }

            }
        });
    }

    public ImageIcon seleccionar() {
        setBorder(BorderFactory.createLineBorder(Color.YELLOW,2,true));
        seleccionada = true;
        return imageIcon;
    }

    public void deseleccionar() {
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,1));
        seleccionada = false;
    }

    public static void deseleccionarTodas() {
        for (Celda celda : GridPanel.getCeldas()) {
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
