package com.game.ui.dialogue;

import com.game.ui.dialogue.state.DialogueState;

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

        // 1. Intentar obtener el nodo actual si existe y cumple condiciones
        String currentNodeId = currentNpcNodeIds.get(npcId);
        if (currentNodeId != null) {
            Conversation.ConversationNode baseNode = conv.getNodeById(currentNodeId);
            if (baseNode != null) {
                // Primero verificar si hay variante válida para el nodo actual
                Conversation.ConversationNode variantNode = getVariantNode(baseNode);
                if (variantNode != null && meetsConditions(variantNode, null)) {
                    return variantNode;
                }

                // Si no hay variante válida, verificar si el nodo base cumple condiciones
                if (meetsConditions(baseNode, null)) {
                    return baseNode;
                }
            }
        }

        // 2. Intentar con el nodo inicial
        String initialNodeId = conv.getInitialNodeId();
        if (initialNodeId != null) {
            Conversation.ConversationNode baseNode = conv.getNodeById(initialNodeId);
            if (baseNode != null) {
                // Verificar variante para nodo inicial
                Conversation.ConversationNode variantNode = getVariantNode(baseNode);
                if (variantNode != null && meetsConditions(variantNode, null)) {
                    setCurrentNode(npcId, initialNodeId);
                    return variantNode;
                }

                // Verificar nodo base inicial
                if (meetsConditions(baseNode, null)) {
                    setCurrentNode(npcId, initialNodeId);
                    return baseNode;
                }
            }
        }

        // 3. Buscar cualquier nodo que cumpla las condiciones
        for (Conversation.ConversationNode node : conv.getAllNodes()) {
            // Verificar variante primero
            Conversation.ConversationNode variantNode = getVariantNode(node);
            if (variantNode != null && meetsConditions(variantNode, null)) {
                setCurrentNode(npcId, node.nodeId);
                return variantNode;
            }

            // Luego verificar nodo base
            if (meetsConditions(node, null)) {
                setCurrentNode(npcId, node.nodeId);
                return node;
            }
        }

        return null;
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