package com.game.ui.dialogue;

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
            if (potentialNode != null && meetsConditions(potentialNode, npcId)) {
                return potentialNode;
            }
            // currentNpcNodeIds.remove(npcId); // Opcional: si el nodo actual ya no es válido, fuerza la búsqueda.
        }

        String initialNodeId = conv.getInitialNodeId();
        if (initialNodeId != null) {
            Conversation.ConversationNode initialNode = conv.getNodeById(initialNodeId);
            if (initialNode != null && meetsConditions(initialNode, npcId)) {
                setCurrentNode(npcId, initialNode.nodeId);
                return initialNode;
            }
        }

        for (Conversation.ConversationNode node : conv.getAllNodes()) {
            if (meetsConditions(node, npcId)) {
                setCurrentNode(npcId, node.nodeId);
                return node;
            }
        }
        return null;
    }

    public void setCurrentNode(String npcId, String nodeId) {
        Conversation conv = conversations.get(npcId);
        if (nodeId == null) { // Permitir limpiar el nodo actual si el nextNodeId es null
            currentNpcNodeIds.remove(npcId);
            return;
        }
        if (conv != null && conv.getNodeById(nodeId) != null) {
            currentNpcNodeIds.put(npcId, nodeId);
        } else {
            System.err.println("Error: Intento de establecer un nodo actual inválido ('" + nodeId + "') para NPC '" + npcId + "'");
        }
    }

    public void clearCurrentNode(String npcId) {
        currentNpcNodeIds.remove(npcId);
    }

    private boolean meetsConditions(Conversation.ConversationNode node, String npcId) {
        if (node == null || node.requiredFlags == null || node.requiredFlags.isEmpty()) {
            return true;
        }
        return node.requiredFlags.entrySet().stream()
                .allMatch(entry ->
                        (entry.getValue() && gameState.hasFlag(entry.getKey())) ||
                                (!entry.getValue() && !gameState.hasFlag(entry.getKey()))
                );
    }

    public void triggerAction(String action) {
        if (action == null || action.isEmpty()) return;

        String[] parts = action.split(":", 2);
        String command = parts[0].toUpperCase();
        String value = (parts.length > 1) ? parts[1] : null;

        switch (command) {
            case "SET_FLAG":
                if (value != null) gameState.flags().add(value); // Acceso directo a flags del record
                break;
            case "CLEAR_FLAG":
                if (value != null) gameState.flags().remove(value); // Acceso directo
                break;
            case "GIVE_REWARD":
                // como ejecuto esto?
            default:

                break;
        }
    }
}