package com.game.ui.dialogue;

import java.util.*;

public class Conversation {
    public static class ConversationNode {
        public final String nodeId;
        public String npcText;
        public List<DialogueOption> options;
        public Map<String, Boolean> requiredFlags;

        public ConversationNode(String nodeId, String npcText, List<DialogueOption> options,
                                Map<String, Boolean> requiredFlags) {
            this.nodeId = nodeId; // ID único (ej: "INTRO")
            this.npcText = npcText; // Texto del NPC
            this.options = (options == null) ? new ArrayList<>() : options; // Opciones del jugador
            this.requiredFlags = (requiredFlags == null) ? new HashMap<>() : requiredFlags; // Flags requeridas
        }
    }

    public static class DialogueOption {
        public String text; // Texto de la opción
        public String nextNodeId; // Nodo destino (ej: "RESPUESTA_A")
        public List<String> actions; // Acciones (ej: ["SET_FLAG:OFENDIDO"])

        public DialogueOption(String text, String nextNodeId, List<String> actions) {
            this.text = text;
            this.nextNodeId = nextNodeId;
            this.actions = (actions == null) ? new ArrayList<>() : actions;
        }
    }

    private final Map<String, ConversationNode> nodes = new HashMap<>();
    private String initialNodeId;

    public Conversation(String initialNodeId) { // Constructor que toma el ID del nodo inicial
        this.initialNodeId = initialNodeId;
    }

    public Conversation() {}

    public void addNode(ConversationNode node) {
        if (node != null && node.nodeId != null) {
            this.nodes.put(node.nodeId, node);
            // Si initialNodeId no fue establecido en el constructor o es nulo,
            // y este es el primer nodo que se añade, se puede usar como inicial.
            if (this.initialNodeId == null) {
                this.initialNodeId = node.nodeId;
            }
        }
    }

    public ConversationNode getNodeById(String nodeId) {
        return this.nodes.get(nodeId);
    }

    public Collection<ConversationNode> getAllNodes() {
        return this.nodes.values();
    }

    public String getInitialNodeId() {
        return initialNodeId;
    }

    public void setInitialNodeId(String initialNodeId) { // Por si quieres cambiarlo después de crear el objeto
        if (nodes.containsKey(initialNodeId)) {
            this.initialNodeId = initialNodeId;
        } else {
            System.err.println("Advertencia: El ID del nodo inicial '" + initialNodeId + "' no existe en la conversación.");
        }
    }

    public boolean containsNode(String nodeId) {
        return nodes.containsKey(nodeId);
    }
}