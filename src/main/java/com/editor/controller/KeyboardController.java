package com.editor.controller;

import com.editor.view.MapEditorPanel;
import com.editor.view.TilePalettePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardController implements KeyListener {
    private final TilePalettePanel palettePanel;
    private final MapEditorPanel editorPanel; // Nueva referencia

    public KeyboardController(TilePalettePanel palettePanel, MapEditorPanel editorPanel) {
        this.palettePanel = palettePanel;
        this.editorPanel = editorPanel; // Inicializar
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("[KEY] Tecla presionada: " + KeyEvent.getKeyText(e.getKeyCode()));
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_W:
                palettePanel.navigateUp();
                break;
            case KeyEvent.VK_A:
                palettePanel.navigateLeft();
                break;
            case KeyEvent.VK_S:
                palettePanel.navigateDown();
                break;
            case KeyEvent.VK_D:
                palettePanel.navigateRight();
                break;
            case KeyEvent.VK_C: // Manejar tecla C
                System.out.println("[KEY] Toggling complex event mode");
                boolean newState = !editorPanel.isComplexEventMode();
                editorPanel.setEventMode(newState);
                break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
