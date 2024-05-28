package GAME.FX;

import GAME.GAME.TeisPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Clase que gestiona las entradas de teclado del usuario.
 *
 * Esta clase implementa la interfaz `KeyListener` y define los métodos `keyTyped()`, `keyPressed()` y `keyReleased()`.
 * Cuando se presiona o se suelta una tecla, se actualizan las variables booleanas `up`, `down`, `left` y `right`.
 *
 * @author Santiago Agustin Romero Diaz
 * @version 1.0
 */
public class KeyManager implements KeyListener {
    // Atributos

    /**Indica que direccion toma el personaje*/
    public boolean up,down,left,right;

    public boolean Time = false;

    TeisPanel teisPanel;

    public KeyManager (TeisPanel teisPanel) {
        this.teisPanel = teisPanel;
    }

    /**
     * Método que se ejecuta cuando se teclea una tecla.
     *
     * Este método está vacío y no hace nada.
     *
     * @param e el evento de teclado
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // Metodo sin uso de momento
    }

    /**
     * Método que se ejecuta cuando se presiona una tecla.
     *
     * Cuando se presiona una tecla, se actualiza la variable correspondiente a la tecla presionada.
     *
     * @param e el evento de teclado
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                up = true;
                break;
            case KeyEvent.VK_S:
                down = true;
                break;
            case KeyEvent.VK_A:
                left = true;
                break;
            case KeyEvent.VK_D:
                right = true;
                break;
            case KeyEvent.VK_ESCAPE:
                teisPanel.controller.pauseState = !teisPanel.controller.pauseState;
                break;
            case KeyEvent.VK_T: // DEBUGGING
                Time = !Time;
                break;
        }
    }

    /**
     * Método que se ejecuta cuando se suelta una tecla.
     *
     * Cuando se suelta una tecla, se actualiza la variable correspondiente a la tecla soltada.
     *
     * @param e el evento de teclado
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                up = false;
                break;
            case KeyEvent.VK_S:
                down = false;
                break;
            case KeyEvent.VK_D:
                right = false;
                break;
            case KeyEvent.VK_A:
                left = false;
                break;
        }
    }
}
