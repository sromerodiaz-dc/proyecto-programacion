package com.game.ui.dialogue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            conv.addNode(new Conversation.ConversationNode(id, text, options, requiredFlags));
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
