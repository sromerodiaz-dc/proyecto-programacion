package com.game.controller;

import com.game.data.GameState;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardController implements KeyListener {
    public boolean up, down, left, right;
    public boolean Time = false;
    public boolean isPressed = false;
    private final TeisPanel teisPanel;

    public KeyboardController(TeisPanel teisPanel) {
        this.teisPanel = teisPanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (teisPanel.controller.getGameState()) {
            case LOAD:
                handleLoadScreenInput(e);
                break;
            case PLAY:
                handlePlayInput(e);
                break;
            case PAUSE:
                handlePauseInput(e);
                break;
            case DIALOG:
                handleDialogInput(e);
                break;
            case STATS:
                handleStatsState(e);
                break;
        }
    }

    private void handleLoadScreenInput(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_D:
                teisPanel.controller.ui.titleCounter--;
                if (teisPanel.controller.ui.titleCounter < 0) {
                    teisPanel.controller.ui.titleCounter = 2;
                }
                break;
            case KeyEvent.VK_A:
                teisPanel.controller.ui.titleCounter++;
                if (teisPanel.controller.ui.titleCounter > 2) {
                    teisPanel.controller.ui.titleCounter = 0;
                }
                break;
            case KeyEvent.VK_SPACE:
                if (teisPanel.controller.ui.titleCounter == 0) {
                    System.exit(0);
                } else if (teisPanel.controller.ui.titleCounter == 1) {
                    teisPanel.controller.setGameState(GameState.PLAY);
                }
                break;
            default:
                teisPanel.controller.ui.getRandomTitle();
        }
    }

    private void handlePlayInput(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = true;
            case KeyEvent.VK_S -> down = true;
            case KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_D -> right = true;
            case KeyEvent.VK_SPACE -> isPressed = true;
            case KeyEvent.VK_ESCAPE -> teisPanel.controller.setGameState(GameState.PAUSE);
            case KeyEvent.VK_I -> teisPanel.controller.setGameState(GameState.STATS);
            case KeyEvent.VK_T -> Time = !Time;
        }
    }

    private void handlePauseInput(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            teisPanel.controller.setGameState(GameState.PLAY);
        }
    }

    private void handleDialogInput(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            teisPanel.controller.setGameState(GameState.PLAY);
        }
    }

    private void handleStatsState(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_I) {
            teisPanel.controller.setGameState(GameState.PLAY);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = false;
            case KeyEvent.VK_S -> down = false;
            case KeyEvent.VK_D -> right = false;
            case KeyEvent.VK_A -> left = false;
        }
    }
}