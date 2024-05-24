package EDITOR.TEST;

import EDITOR.SELECTPANEL.Celda;
import EDITOR.SELECTPANEL.GridPanel;
import org.junit.Test;

import javax.swing.*;

import java.awt.*;

import static org.junit.Assert.*;

public class TestGridPanel {

    @Test
    public void testGridPanel() {
        // Crea una nueva instancia de la clase GridPanel
        ImageIcon[] sprites = new ImageIcon[10];
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = new ImageIcon("path/to/image.png");
        }
        GridPanel gridPanel = new GridPanel(5, 5, sprites);

        // Comprueba que el panel se haya creado correctamente
        assertNotNull(gridPanel);
        assertEquals(gridPanel.getBackground(), Color.DARK_GRAY);
        assertEquals(gridPanel.getLayout(), new GridLayout(5, 5));
        assertEquals(gridPanel.getComponentCount(), 25);
        assertEquals(GridPanel.getCeldas().size(), 25);

        // Comprueba que las celdas se hayan creado correctamente
        for (Celda celda : GridPanel.getCeldas()) {
            assertNotNull(celda);
            assertEquals(celda.getBackground(), Color.DARK_GRAY);
            assertEquals(celda.getBorder(), BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            assertNotNull(celda.imageIcon);
        }
    }
}