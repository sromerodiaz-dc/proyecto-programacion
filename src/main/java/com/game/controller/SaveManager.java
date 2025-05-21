package com.game.controller;

import com.game.data.SaveState;
import com.google.gson.Gson;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveManager {
    private static final Gson gson = new Gson();

    public static void saveGame(String slot, SaveState state) {
        try (FileWriter writer = new FileWriter("data/saves/" + slot + ".json")) {
            gson.toJson(state, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SaveState loadGame(String slot) {
        try {
            String content = new String(Files.readAllBytes(Paths.get("data/saves/" + slot + ".json")));
            return gson.fromJson(content, SaveState.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}