package EDITOR.FX;

import EDITOR.GUI.GUI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Clase creada para la implementación del modo 'pincel'
 * "Si se presiona la F, no será necesario hacer clic sobre un panel para pintarlo"
 *
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class KeyboardListener implements KeyListener {
    /** Indica si la tecla 'F' está presionada o no. */
    public boolean isFPressed = true;
    /** La referencia a la clase GUI que se utilizará para actualizar la interfaz gráfica de usuario. */
    public GUI gui;

    /**
     * Constructs a new KeyboardListener with the given GUI reference.
     *
     * @param gui The GUI reference.
     */
    public KeyboardListener (GUI gui) {
        this.gui = gui;
    }

    /**
     * Maneja el evento de teclado cuando una tecla es presionada.
     *
     * @param e El evento de teclado a ser procesado.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F) {
            //  true = estado contrario del propio boolean
            isFPressed = !isFPressed;
        }
        // Dependiendo de si se presiona o no, se activa o no el modo 'pincel'
        if (isFPressed)
            setLabelOff();
        else setLabelOn();
    }

    /**
     * Método vacío que maneja el evento de teclado cuando una tecla es liberada.
     *
     * @param e El evento de teclado a ser procesado.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // No se realiza ninguna acción cuando una tecla es liberada
    }

    /**
     * Método vacío que maneja el evento de teclado cuando una tecla es tecleada.
     *
     * @param e El evento de teclado a ser procesado.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // No se realiza ninguna acción cuando una tecla es tecleada
    }

    /**
     * Establece el label en la interfaz gráfica de usuario en modo apagado.
     */
    public void setLabelOff(){
        gui.setLabelOff();
    }

    /**
     * Establece el label en la interfaz gráfica de usuario en modo encendido.
     */
    public void setLabelOn(){
        gui.setLabelOn();
    }
}