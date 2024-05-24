/*
package EDITOR.TEST;

import EDITOR.EMPTYMAP.VacioPanel;
import EDITOR.GUI.GUI;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestVacioPanel {

    @Test
    public void testIniciarComponente() {
        // Create a VacioPanel instance
        VacioPanel vacioPanel = new VacioPanel(10, 10, new GUI());

        // Test the iniciarComponente method
        assertEquals(10, vacioPanel.getComponentCount());
        assertEquals(10, vacioPanel.formato.length);
        assertEquals(10, vacioPanel.celdaVacias.size());

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                assertEquals(vacioPanel, vacioPanel.celdaVacias.get(row * 10 + col).vacioPanel);
            }
        }
    }

    @Test
    public void testGetFormato() {
        // Create a VacioPanel instance
        VacioPanel vacioPanel = new VacioPanel(10, 10, new GUI());

        // Test the getFormato method
        ImageIcon[][] format = vacioPanel.getFormato();
        assertEquals(10, format.length);

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                assertEquals(null, format[row][col]);
            }
        }
    }
}*/
