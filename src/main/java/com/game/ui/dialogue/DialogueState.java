package com.game.ui.dialogue;

import java.util.HashSet;

public record DialogueState (HashSet<String> flags){
    public boolean hasFlag(String flag) {
        return flags.contains(flag);
    }
}
