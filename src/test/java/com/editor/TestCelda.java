package com.editor;

import com.editor.panel.Celda;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

class TestCelda {

    @Test
    public void testCelda() {
        // Crea una nueva instancia de la clase Celda
        ImageIcon imageIcon = new ImageIcon("path/to/image.png");
        Celda celda = new Celda(imageIcon);

        // Comprueba que la celda se haya creado correctamente
        assertNotNull(celda);
        assertEquals(celda.getBackground(), Color.DARK_GRAY);
        assertEquals(celda.getBorder(), BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        assertEquals(celda.imageIcon, imageIcon);
    }

    @Test
    public void testSeleccionar() {
        // Crea una nueva instancia de la clase Celda
        ImageIcon imageIcon = new ImageIcon("path/to/image.png");
        Celda celda = new Celda(imageIcon);

        // Llama al metodo seleccionar()
        ImageIcon selectedImageIcon = celda.seleccionar();

        // Comprueba que la celda se haya seleccionado correctamente
        assertNotNull(selectedImageIcon);
        assertEquals(celda.getBorder(), BorderFactory.createLineBorder(Color.YELLOW, 2, true));
        assertTrue(celda.seleccionada);
    }

    @Test
    public void testDeseleccionar() {
        // Crea una nueva instancia de la clase Celda
        ImageIcon imageIcon = new ImageIcon("path/to/image.png");
        Celda celda = new Celda(imageIcon);

        // Llama al metodo seleccionar()
        celda.seleccionar();

        // Llama al metodo deseleccionar()
        celda.deseleccionar();

        // Comprueba que la celda se haya deseleccionado correctamente
        assertEquals(celda.getBackground(), Color.DARK_GRAY);
        assertEquals(celda.getBorder(), BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        assertFalse(celda.seleccionada);
    }

    @Test
    public void testDeseleccionarTodas() {
        // Crea una nueva instancia de la clase Celda
        ImageIcon imageIcon = new ImageIcon("path/to/image.png");
        Celda celda1 = new Celda(imageIcon);
        Celda celda2 = new Celda(imageIcon);
        Celda celda3 = new Celda(imageIcon);

        // Selecciona las celdas
        celda1.seleccionar();
        celda2.seleccionar();
        celda3.seleccionar();

        // Llama al metodo deseleccionarTodas()
        celda1.deseleccionarTodas(celda1);

        // Comprueba que solo la celda especificada no se haya deseleccionado
        assertFalse(celda1.seleccionada);
        assertTrue(celda2.seleccionada);
        assertTrue(celda3.seleccionada);
    }

    @Test
    public void testEscaladoImage() {
        // Crea una nueva instancia de la clase Celda
        ImageIcon imageIcon = new ImageIcon("path/to/image.png");

        // Llama al metodo escaladoImage()
        ImageIcon escaladoImageIcon = Celda.escaladoImage(imageIcon, 100, 100);

        // Comprueba que la imagen se haya escalado correctamente
        assertNotNull(escaladoImageIcon);
        assertEquals(escaladoImageIcon.getIconWidth(), 100);
        assertEquals(escaladoImageIcon.getIconHeight(), 100);
    }
}