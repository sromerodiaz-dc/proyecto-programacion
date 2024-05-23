package EDITOR.FX;

import EDITOR.GUI.GUI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {
    public boolean isFPressed = true;
    public GUI gui;

    public KeyboardListener (GUI gui) {
        this.gui=gui;
    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F) {
            isFPressed = !isFPressed;
        }
        if (isFPressed)
            setLabelOff();
        else setLabelOn();
    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void setLabelOff(){
        gui.setLabelOff();
    }

    public void setLabelOn(){
        gui.setLabelOn();
    }
}