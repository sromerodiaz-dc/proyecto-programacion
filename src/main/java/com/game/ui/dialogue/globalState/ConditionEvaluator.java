package com.game.ui.dialogue.globalState;

import org.json.JSONObject;

public class ConditionEvaluator {
    public static boolean evaluate(JSONObject conditions) {
        if (conditions == null) return true;

        GlobalGameState state = GlobalGameState.getInstance();

        for (String key : conditions.keySet()) {
            Object value = conditions.get(key);

            if (value instanceof Boolean) {
                if (state.hasFlag(key) != (Boolean) value) return false;
            }
            else if (value instanceof Integer) {
                if (state.getCounter(key) != (Integer) value) return false;
            }
            else if (value instanceof String) {
                if (!state.getVariable(key).equals(value)) return false;
            }
            else if (value instanceof JSONObject) {
                JSONObject subCond = (JSONObject) value;
                if ("AND".equals(key)) {
                    for (String subKey : subCond.keySet()) {
                        if (!evaluate(subCond.getJSONObject(subKey))) return false;
                    }
                }
                else if ("OR".equals(key)) {
                    boolean anyValid = false;
                    for (String subKey : subCond.keySet()) {
                        if (evaluate(subCond.getJSONObject(subKey))) {
                            anyValid = true;
                            break;
                        }
                    }
                    if (!anyValid) return false;
                }
            }
        }
        return true;
    }
}