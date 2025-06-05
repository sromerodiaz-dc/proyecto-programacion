package com.game.ui.dialogue;

import com.game.ui.Dialogable;
import com.game.ui.dialogue.state.DialogueState;

import java.util.*;

public class DialogueSystem {
    private final DialogueState gameState; // Usará el record directamente
    private final Map<String, Conversation> conversations = new HashMap<>();
    private final Map<String, String> currentNpcNodeIds = new HashMap<>();

    public DialogueSystem(DialogueState gameState) {
        this.gameState = gameState;
    }

    public void registerConversation(String npcId, Conversation conversation) {
        conversations.put(npcId, conversation);
    }

    public Conversation.ConversationNode getCurrentNode(String npcId) {
        Conversation conv = conversations.get(npcId);
        if (conv == null) return null;

        String currentNodeId = currentNpcNodeIds.get(npcId);
        Conversation.ConversationNode potentialNode = null;

        if (currentNodeId != null) {
            potentialNode = conv.getNodeById(currentNodeId);
            if (potentialNode != null && meetsConditions(potentialNode)) {
                return potentialNode;
            }
            // currentNpcNodeIds.remove(npcId); // Opcional: si el nodo actual ya no es válido, fuerza la búsqueda.
        }

        String initialNodeId = conv.getInitialNodeId();
        if (initialNodeId != null) {
            Conversation.ConversationNode initialNode = conv.getNodeById(initialNodeId);
            if (initialNode != null && meetsConditions(initialNode)) {
                setCurrentNode(npcId, initialNode.nodeId);
                return initialNode;
            }
        }

        for (Conversation.ConversationNode node : conv.getAllNodes()) {
            if (meetsConditions(node)) {
                setCurrentNode(npcId, node.nodeId);
                return node;
            }
        }
        return null;
    }

    public void setCurrentNode(String npcId, String nodeId) {
        Conversation conv = conversations.get(npcId);
        if (nodeId == null) {
            currentNpcNodeIds.remove(npcId);
            return;
        }

        if (conv == null) {
            System.err.println("Error: No existe conversación para NPC '" + npcId + "'");
            return;
        }

        if (!conv.containsNode(nodeId)) {
            System.err.println("Error: Nodo '" + nodeId + "' no existe en conversación de '" + npcId + "'");
            return;
        }

        currentNpcNodeIds.put(npcId, nodeId);
    }

    public void clearCurrentNode(String npcId) {
        currentNpcNodeIds.remove(npcId);
    }

    private boolean meetsConditions(Conversation.ConversationNode node) {
        if (node == null || node.requiredFlags == null || node.requiredFlags.isEmpty()) {
            return true;
        }
        return node.requiredFlags.entrySet().stream()
                .allMatch(entry ->
                        (entry.getValue() && gameState.hasFlag(entry.getKey())) ||
                                (!entry.getValue() && !gameState.hasFlag(entry.getKey()))
                );
    }

    public void triggerAction(String action, Dialogable npc) {
        if (action == null || action.isEmpty()) return;

        String[] parts = action.split(":", 2);
        String command = parts[0].toUpperCase();
        String value = (parts.length > 1) ? parts[1] : null;

        switch (command) {
            case "SET_FLAG":
                if (value != null) gameState.flags().add(value);
                break;
            case "CLEAR_FLAG":
                if (value != null) gameState.flags().remove(value);
                break;
            default:
                if (npc != null) {
                    npc.triggerCustomAction(command);
                }
                break;
        }
    }
}