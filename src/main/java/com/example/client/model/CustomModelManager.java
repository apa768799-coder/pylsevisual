package com.example.client.model;

import com.example.client.config.ClientConfig;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CustomModelManager {
    private static final CustomModelManager INSTANCE = new CustomModelManager();
    public static CustomModelManager getInstance() { return INSTANCE; }

    private final Map<String, CustomModel> cache = new HashMap<>();

    public void reload() {
        cache.clear();
        Path dir = FabricLoader.getInstance().getGameDir().resolve("assets/custom_models");
        try {
            if (!Files.exists(dir)) Files.createDirectories(dir);
            try (Stream<Path> stream = Files.walk(dir)) {
                stream.filter(p -> p.toString().endsWith(".json")).forEach(this::parseModel);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void parseModel(Path path) {
        try {
            String content = Files.readString(path);
            JsonObject json = JsonParser.parseString(content).getAsJsonObject();
            String name = path.getFileName().toString().replace(".json", "");
            
            // Проверка дубликатов по хэшу или имени
            if (!cache.containsKey(name)) {
                cache.put(name, new CustomModel(name, json));
            }
        } catch (Exception e) { System.err.println("Ошибка парсинга модели: " + path); }
    }

    public CustomModel getModel(String name) { return cache.get(name); }

    public static class CustomModel {
        public final String name;
        public final JsonObject data;

        public CustomModel(String name, JsonObject data) {
            this.name = name;
            this.data = data;
        }

        public boolean shouldRenderLayer(int layer) {
            String quality = ClientConfig.modelQuality;
            if (quality.equals("LOW")) return layer == 0;
            if (quality.equals("MEDIUM")) return layer < 3;
            return true; // HIGH
        }
    }
}
