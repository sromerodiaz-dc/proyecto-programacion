package com.game.ui.dialogue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.*;

public class LoadDialogueFromJson {
    public static Conversation loadFromJson(InputStream jsonStream) {
        JSONObject json = new JSONObject(new JSONTokener(jsonStream));
        String initialNodeId = json.getString("initialNodeId");
        Conversation conv = new Conversation(initialNodeId);

        JSONArray nodes = json.getJSONArray("nodes");
        for (int i = 0; i < nodes.length(); i++) {
            JSONObject nodeJson = nodes.getJSONObject(i);
            String id = nodeJson.getString("id");
            String text = nodeJson.getString("text");

            // Procesar opciones
            JSONArray optionsJson = nodeJson.getJSONArray("options");
            List<Conversation.DialogueOption> options = new ArrayList<>();
            for (int j = 0; j < optionsJson.length(); j++) {
                JSONObject optionJson = optionsJson.getJSONObject(j);
                options.add(new Conversation.DialogueOption(
                        optionJson.getString("text"),
                        optionJson.getString("nextNode"),
                        jsonArrayToList(optionJson.getJSONArray("actions"))
                ));
            }

            // Procesar flags requeridas
            Map<String, Boolean> requiredFlags = new HashMap<>();
            if (nodeJson.has("requiredFlags")) {
                JSONObject flagsJson = nodeJson.getJSONObject("requiredFlags");
                for (String flag : flagsJson.keySet()) {
                    requiredFlags.put(flag, flagsJson.getBoolean(flag));
                }
            }

            // Procesar variantes
            List<Conversation.ConversationNode.NodeVariant> variants = new ArrayList<>();
            if (nodeJson.has("variants")) {
                JSONArray variantsJson = nodeJson.getJSONArray("variants");
                for (int k = 0; k < variantsJson.length(); k++) {
                    JSONObject variantJson = variantsJson.getJSONObject(k);

                    String variantText = variantJson.optString("text", null);

                    // Flags para la variante
                    Map<String, Boolean> variantFlags = new HashMap<>();
                    if (variantJson.has("variantFlags")) {
                        JSONObject flagsJson = variantJson.getJSONObject("variantFlags");
                        for (String flag : flagsJson.keySet()) {
                            variantFlags.put(flag, flagsJson.getBoolean(flag));
                        }
                    }

                    // Opciones para la variante
                    List<Conversation.DialogueOption> variantOptions = new ArrayList<>();
                    if (variantJson.has("variantOptions")) {
                        JSONArray optionsJsonVariant = variantJson.getJSONArray("variantOptions");
                        for (int j = 0; j < optionsJsonVariant.length(); j++) {
                            JSONObject optionJson = optionsJsonVariant.getJSONObject(j);
                            variantOptions.add(new Conversation.DialogueOption(
                                    optionJson.getString("text"),
                                    optionJson.getString("nextNode"),
                                    jsonArrayToList(optionJson.getJSONArray("actions"))
                            ));
                        }
                    }

                    variants.add(new Conversation.ConversationNode.NodeVariant(
                            variantText, variantFlags, variantOptions
                    ));
                }
            }

            // Crear el nodo con las variantes
            conv.addNode(new Conversation.ConversationNode(
                    id,
                    text,
                    options,
                    requiredFlags,
                    variants
            ));
        }

        return conv;
    }

    private static List<String> jsonArrayToList(JSONArray array) {
        if (array == null) return Collections.emptyList();

        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }
        return list;
    }
}