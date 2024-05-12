package EDITOR.SELECTPANEL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Celda extends JPanel{
    public boolean seleccionada;
    ImageIcon imageIcon;

    public Celda(ImageIcon imageIcon) {
        seleccionada = false;
        this.imageIcon = imageIcon;

        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        if (imageIcon.getImage()!=null) {
            JLabel imageLabel = new JLabel(escaladoImage(imageIcon));
            add(imageLabel);
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!seleccionada) {
                    deseleccionarTodas();
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
    public ImageIcon getImageIcon(){
        return imageIcon;
    }
    public static void deseleccionarTodas() {
        for (Celda celda : GridPanel.getCeldas()) {
            celda.deseleccionar();
        }
    }
    public ImageIcon escaladoImage (ImageIcon imageIcon) {
        // Get Image from Icon
        Image originalImage = imageIcon.getImage();

        // Resize the Image object
        Image scaledImage = originalImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);

        // Create a new ImageIcon from the scaled Image
        return new ImageIcon(scaledImage);
    }
}
