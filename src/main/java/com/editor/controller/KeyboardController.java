package com.editor.controller;

import com.editor.view.TilePalettePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardController implements KeyListener {
    private final TilePalettePanel palettePanel;

    public KeyboardController(TilePalettePanel palettePanel) {
        this.palettePanel = palettePanel;
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
                System.out.println("[KEY] Ejecutando navigateUp");
                palettePanel.navigateUp();
                break;
            case KeyEvent.VK_A:
                System.out.println("[KEY] Ejecutando navigateLeft");
                palettePanel.navigateLeft();
                break;
            case KeyEvent.VK_S:
                System.out.println("[KEY] Ejecutando navigateDown");
                palettePanel.navigateDown();
                break;
            case KeyEvent.VK_D:
                System.out.println("[KEY] Ejecutando navigateRight");
                palettePanel.navigateRight();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
