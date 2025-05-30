package com.game.ui;

import java.util.List;

public interface Dialogable {
    List<String> getCurrentOptions();
    String getCurrentMessage();
    void selectOption(int index);
}