/*
package main.EDITOR.TEST;

import main.EDITOR.EMPTYMAP.VacioPanel;
import main.EDITOR.GUI.GUI;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.IOException;
import java.security.Principal;

import static org.junit.Assert.*;

class TestGUI {

    @Test
    public void testCreateMenuIzquierda() throws IOException {
        // Crea una nueva instancia de la clase principal
        GUI gui = new GUI();
        // Llama al metodo createMenuIzquierda()
        gui.createMenuIzquierda();

        // Comprueba que el menú izquierdo se haya creado correctamente
        assertNotNull(gui.getMenuIzquierda());
        assertEquals(gui.getMenuIzquierda(), gui.spriteUtils.numRowsCols()[0]);
        assertEquals(gui.getMenuIzquierda(), gui.spriteUtils.numRowsCols()[1]);
    }

    @Test
    public void testCreateMenuInferior() throws IOException {
        // Crea una nueva instancia de la clase principal
        GUI gui = new GUI();

        // Llama al metodo createMenuInferior()
        gui.createMenuInferior();

        // Comprueba que el menú inferior se haya creado correctamente
        assertNotNull(gui.getMenuInferior());
    }

    @Test
    public void testBotonGuardarClickeado() throws IOException {
        // Crea una nueva instancia de la clase principal
        GUI gui = new GUI();

        // Llama al metodo createBotonGuardar()
        gui.createBotonGuardar(new VacioPanel(1,1,new GUI()));

        // Simula el clic en el botón guardar
        gui.botonGuardar.doClick();

        // Comprueba que se haya llamado al metodo generateSpriteMap()
        // Nota: Esto puede ser difícil de comprobar sin usar Mockito u otra biblioteca de simulación
        // En este caso, puedes comprobar si se ha creado correctamente el archivo de mapa de sprites
        // o si se ha producido alguna excepción
    }

    @Test
    public void testCreateBotonFondo() throws IOException {
        // Crea una nueva instancia de la clase principal
        GUI gui = new GUI();

        // Llama al metodo createBotonFondo()
        gui.createBotonFondo(new VacioPanel(1,1, new GUI()));

        // Comprueba que el botón fondo se haya creado correctamente
        assertNotNull(gui.getBotonFondo());
    }
}*/
