package EDITOR.TEST;

import EDITOR.GUI.GUI;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.Principal;

import static org.junit.Assert.*;

class TestGUI {

    @Test
    public void testCreateMenuIzquierda() throws IOException {
        // Crea una nueva instancia de la clase principal
        GUI gui = new GUI();
        // Llama al método createMenuIzquierda()
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

        // Llama al método createMenuInferior()
        gui.createMenuInferior();

        // Comprueba que el menú inferior se haya creado correctamente
        assertNotNull(gui.getMenuInferior());
    }

    @Test
    public void testBotonGuardarClickeado() throws IOException {
        // Crea una nueva instancia de la clase principal
        GUI gui = new GUI();

        // Llama al método createBotonGuardar()
        gui.createBotonGuardar();

        // Simula el clic en el botón guardar
        gui.botonGuardar.doClick();

        // Comprueba que se haya llamado al método generateSpriteMap()
        // Nota: Esto puede ser difícil de comprobar sin usar Mockito u otra biblioteca de simulación
        // En este caso, puedes comprobar si se ha creado correctamente el archivo de mapa de sprites
        // o si se ha producido alguna excepción
    }

    @Test
    public void testCreateBotonFondo() throws IOException {
        // Crea una nueva instancia de la clase principal
        GUI gui = new GUI();

        // Llama al método createBotonFondo()
        gui.createBotonFondo();

        // Comprueba que el botón fondo se haya creado correctamente
        assertNotNull(gui.getBotonFondo());
    }
}