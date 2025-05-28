package com.game.data;

import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.util.Objects;

public class JSONDataLoader {
    private static final Gson gson = new Gson();

    public static <T> T loadEntityData(String path, Class<T> type) {
        try (InputStreamReader reader = new InputStreamReader(
                Objects.requireNonNull(JSONDataLoader.class.getResourceAsStream("/data/entities/" + path)))) {
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            throw new RuntimeException("Error cargando " + path, e);
        }
    }
}