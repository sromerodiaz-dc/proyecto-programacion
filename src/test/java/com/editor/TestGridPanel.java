package com.editor;

import com.editor.panel.Celda;
import com.editor.panel.GridPanel;
import org.testng.annotations.Test;

import javax.swing.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        assertEquals(Color.DARK_GRAY, gridPanel.getBackground());
        assertEquals(new GridLayout(5, 5), gridPanel.getLayout());
        assertEquals(25, gridPanel.getComponentCount());
        assertEquals(25, GridPanel.getCeldas().size());

        // Comprueba que las celdas se hayan creado correctamente
        for (Celda celda : GridPanel.getCeldas()) {
            assertNotNull(celda);
            assertEquals(Color.DARK_GRAY, celda.getBackground());
            assertEquals(celda.getBorder(), BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            assertNotNull(celda.imageIcon);
        }
    }
}