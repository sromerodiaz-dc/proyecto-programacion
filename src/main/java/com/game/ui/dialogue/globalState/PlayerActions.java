package com.game.ui.dialogue.globalState;

public class PlayerActions {
    public void killInnocentEntity(String entityId) {
        GlobalGameState state = GlobalGameState.getInstance();
        state.setFlag("killed_" + entityId);
        state.modifyCounter("innocentKills", 1);

        if (state.getCounter("innocentKills") > 3) {
            state.setFlag("notorious_killer");
        }
    }

    public void completeQuest(String questId) {
        GlobalGameState state = GlobalGameState.getInstance();
        state.setFlag("completed_" + questId);
        state.modifyCounter("completedQuests", 1);
    }
}