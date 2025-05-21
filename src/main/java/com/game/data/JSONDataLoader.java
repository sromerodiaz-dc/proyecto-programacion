package com.game.data;

import com.google.gson.Gson;
import java.io.InputStreamReader;

public class JSONDataLoader {
    private static final Gson gson = new Gson();

    public static <T> T loadEntityData(String path, Class<T> type) {
        try (InputStreamReader reader = new InputStreamReader(
                JSONDataLoader.class.getResourceAsStream("/data/entities/" + path))) {
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            throw new RuntimeException("Error cargando " + path, e);
        }
    }
}