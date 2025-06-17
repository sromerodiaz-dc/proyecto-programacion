package com.game.ui.dialogue;

import java.util.*;

public class DialogueSystem {
    private final DialogueState gameState;
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

        // Obtener ID del nodo actual
        String currentNodeId = currentNpcNodeIds.get(npcId);

        // Buscar nodo base (considerando variantes)
        Conversation.ConversationNode baseNode = null;
        if (currentNodeId != null) {
            baseNode = conv.getNodeById(currentNodeId);
        }

        // Si no hay nodo base válido, usar el inicial
        if (baseNode == null) {
            currentNodeId = conv.getInitialNodeId();
            baseNode = conv.getNodeById(currentNodeId);
        }

        // Buscar variante aplicable
        Conversation.ConversationNode.NodeVariant activeVariant = null;
        if (baseNode != null && baseNode.variants != null) {
            for (Conversation.ConversationNode.NodeVariant variant : baseNode.variants) {
                if (meetsConditions(baseNode, variant.variantFlags)) {
                    activeVariant = variant;
                    break;
                }
            }
        }

        // Crear nodo compuesto (base + variante) si es necesario
        if (activeVariant != null) {
            return new Conversation.ConversationNode(
                    baseNode.nodeId,
                    activeVariant.text != null ? activeVariant.text : baseNode.npcText,
                    activeVariant.variantOptions != null ? activeVariant.variantOptions : baseNode.options,
                    baseNode.requiredFlags,
                    Collections.emptyList()
            );
        }

        return baseNode;
    }

    private Conversation.ConversationNode getVariantNode(Conversation.ConversationNode baseNode) {
        if (baseNode == null) return null;

        // Buscar variante que cumpla las condiciones
        for (Conversation.ConversationNode.NodeVariant variant : baseNode.variants) {
            if (meetsConditions(baseNode, variant.variantFlags)) {
                return new Conversation.ConversationNode(
                        baseNode.nodeId,
                        variant.text != null ? variant.text : baseNode.npcText,
                        variant.variantOptions != null ? variant.variantOptions : baseNode.options,
                        baseNode.requiredFlags,
                        Collections.emptyList() // No más variantes anidadas
                );
            }
        }

        // Si no hay variante válida, devolver nodo base
        return baseNode;
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

    private boolean meetsConditions(Conversation.ConversationNode node, Map<String, Boolean> variantFlags) {
        // Combinar flags del nodo base y de la variante
        Map<String, Boolean> allFlags = new HashMap<>();
        if (node.requiredFlags != null) {
            allFlags.putAll(node.requiredFlags);
        }
        if (variantFlags != null) {
            allFlags.putAll(variantFlags);
        }

        if (allFlags.isEmpty()) {
            return true;
        }

        return allFlags.entrySet().stream()
                .allMatch(entry -> {
                    boolean flagExists = gameState.hasFlag(entry.getKey());
                    boolean flagRequired = entry.getValue();
                    return flagRequired ? flagExists : !flagExists;
                });
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