package com.game.controller;

import com.game.data.GameState;
import com.game.entity.Entity;
import com.game.ui.dialogue.Dialogable;
import com.game.ui.TeisPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

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
            case LOAD: handleLoadScreenInput(e); break;
            case PLAY: handlePlayInput(e); break;
            case PAUSE: handlePauseInput(e); break;
            case DIALOG: handleDialogInput(e); break;
            case STATS: handleStatsState(e); break;
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

    private void handleStatsState(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_I) {
            teisPanel.controller.setGameState(GameState.PLAY);
        }
    }

    private void handleDialogInput(KeyEvent e) {
        Entity npc = teisPanel.controller.currentTalkingNpc;

        // Caso para diálogos sin NPC (eventos)
        if (npc == null) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                teisPanel.controller.setGameState(GameState.PLAY);
            }
            return;
        }

        // 1. Saltar animación de texto
        if (npc.isTyping && e.getKeyCode() == KeyEvent.VK_SPACE) {
            npc.isTyping = false;
            npc.typingIndex = npc.currentDialog.length();
            return;
        }

        // 2. Manejar scroll de texto
        if (e.getKeyCode() == KeyEvent.VK_UP && npc.dialogScrollOffset > 0) {
            npc.dialogScrollOffset--;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN &&
                npc.dialogScrollOffset < npc.maxDialogScroll) {
            npc.dialogScrollOffset++;
        }

        // 3. Salir con SpaceBar cuando no hay opciones
        boolean typingComplete = !npc.isTyping;
        boolean hasOptions = npc instanceof Dialogable &&
                ((Dialogable)npc).getCurrentOptions() != null &&
                !((Dialogable)npc).getCurrentOptions().isEmpty();

        // Nueva condición para salida con SpaceBar
        if (typingComplete && !hasOptions && e.getKeyCode() == KeyEvent.VK_SPACE) {
            teisPanel.controller.setGameState(GameState.PLAY);
            return;
        }

        // 4. Navegación de opciones (solo si existen)
        if (typingComplete && hasOptions) {
            List<String> options = ((Dialogable) npc).getCurrentOptions();
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    if (npc.selectedOption > 0) npc.selectedOption--;
                    break;
                case KeyEvent.VK_S:
                    if (npc.selectedOption < options.size() - 1) npc.selectedOption++;
                    break;
                case KeyEvent.VK_SPACE:
                    ((Dialogable) npc).selectOption(npc.selectedOption);
                    break;
            }
        }

        // 5. Salir con ESCAPE
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
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