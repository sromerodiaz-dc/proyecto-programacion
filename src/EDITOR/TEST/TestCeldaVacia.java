package EDITOR.TEST;

import EDITOR.EMPTYMAP.CeldaVacia;
import EDITOR.EMPTYMAP.VacioPanel;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestCeldaVacia {

    @Test
    public void testSetImageIcon() throws IOException {
        // Create a VacioPanel instance
        VacioPanel vacioPanel = new VacioPanel(10, 10, new JLabel());

        // Create an ImageIcon instance
        BufferedImage image = ImageIO.read(new File("background/alcantarilla.png"));
        ImageIcon imageIcon = new ImageIcon(image);

        // Create a CeldaVacia instance
        CeldaVacia celdaVacia = new CeldaVacia(0, 0, vacioPanel,new JLabel());

        // Test the setImageIcon method
        celdaVacia.setImageIcon(imageIcon, 0, 0, 50, 50);

        // Assert that the ImageIcon was set correctly
        assertEquals(imageIcon, vacioPanel.getFormato()[0][0]);
        assertEquals(imageIcon, celdaVacia.imageLabel.getIcon());
    }

    @Test
    public void testSetImageIconLocal() throws IOException {
        // Create an ImageIcon instance
        BufferedImage image = ImageIO.read(new File("background/alcantarilla.png"));
        ImageIcon imageIcon = new ImageIcon(image);

        // Create a CeldaVacia instance
        CeldaVacia celdaVacia = new CeldaVacia(0, 0, new VacioPanel(10, 10, new JLabel()),new JLabel());

        // Test the setImageIconLocal method
        celdaVacia.setImageIconLocal(imageIcon, 50, 50);

        // Assert that the ImageIcon was set correctly
        assertEquals(imageIcon, celdaVacia.imageLabel.getIcon());
    }
}