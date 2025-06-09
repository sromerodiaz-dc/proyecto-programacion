package com.game.ui.dialogue;

import java.util.List;

public interface Dialogable {
    String getDialogueId();
    String getCurrentMessage();
    List<String> getCurrentOptions();
    void selectOption(int index);
    List<String> getFallbackDialogues(); // Añadido para que Viello pueda proveer sus propios fallbacks
    void triggerCustomAction(String action);
}