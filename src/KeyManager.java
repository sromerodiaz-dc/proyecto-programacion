import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {
    // Atributos
    private boolean up,down,left,right;
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Implementar un switch case en vez de 5 IFs seguidos
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                up = true;
                break;
            case KeyEvent.VK_S:
                down = true;
            case KeyEvent.VK_A:
                left = true;
            case KeyEvent.VK_D:
                right = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
