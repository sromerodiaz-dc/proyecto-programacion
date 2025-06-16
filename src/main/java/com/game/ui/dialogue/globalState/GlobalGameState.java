package com.game.ui.dialogue.globalState;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GlobalGameState {
    private static GlobalGameState instance;
    private final Set<String> flags = new HashSet<>();
    private final Map<String, Integer> counters = new HashMap<>();
    private final Map<String, String> variables = new HashMap<>();
    private final List<FlagListener> listeners = new CopyOnWriteArrayList<>();

    public static GlobalGameState getInstance() {
        if (instance == null) instance = new GlobalGameState();
        return instance;
    }

    // Métodos para contadores
    public void setCounter(String key, int value) { counters.put(key, value); }
    public void modifyCounter(String key, int delta) {
        counters.put(key, counters.getOrDefault(key, 0) + delta);
    }
    public int getCounter(String key) { return counters.getOrDefault(key, 0); }

    // Métodos para variables
    public void setVariable(String key, String value) { variables.put(key, value); }
    public String getVariable(String key) { return variables.getOrDefault(key, ""); }

    public void setFlag(String flag) {
        flags.add(flag);
        notifyListeners(flag, true);
    }

    public void clearFlag(String flag) {
        flags.remove(flag);
        notifyListeners(flag, false);
    }

    public boolean hasFlag(String flag) {
        return flags.contains(flag);
    }

    // Métodos para listeners
    public void addListener(FlagListener listener) {
        listeners.add(listener);
    }

    public void removeListener(FlagListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(String flag, boolean value) {
        for (FlagListener listener : listeners) {
            listener.onFlagChanged(flag, value);
        }
    }
}