/*
package main.EDITOR.TEST;

import main.EDITOR.EMPTYMAP.PanelVacio;
import main.EDITOR.GUI.GUI;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestVacioPanel {

    @Test
    public void testIniciarComponente() {
        // Create a PanelVacio instance
        PanelVacio panelVacio = new PanelVacio(10, 10, new GUI());

        // Test the iniciarComponente method
        assertEquals(10, panelVacio.getComponentCount());
        assertEquals(10, panelVacio.formato.length);
        assertEquals(10, panelVacio.celdaVacias.size());

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                assertEquals(panelVacio, panelVacio.celdaVacias.get(row * 10 + col).panelVacio);
            }
        }
    }

    @Test
    public void testGetFormato() {
        // Create a PanelVacio instance
        PanelVacio panelVacio = new PanelVacio(10, 10, new GUI());

        // Test the getFormato method
        ImageIcon[][] format = panelVacio.getFormato();
        assertEquals(10, format.length);

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                assertEquals(null, format[row][col]);
            }
        }
    }
}*/
