package EDITOR.FX;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyboardListener extends KeyAdapter {
    public boolean isFPressed = false;

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F) {
            isFPressed = !isFPressed;
        }
    }

    public boolean isFPressed() {
        return isFPressed;
    }

    public void setFPressed(boolean isFPressed) {
        this.isFPressed = isFPressed;
    }
}