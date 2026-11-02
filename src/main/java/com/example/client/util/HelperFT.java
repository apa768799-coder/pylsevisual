package com.example.client.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Система подсказок Helper FT.
 * Формат JSON: [{"text": "Совет", "category": "PvP"}]
 */
public class HelperFT {
    private static final HelperFT INSTANCE = new HelperFT();
    public static HelperFT getInstance() { return INSTANCE; }

    private final List<Tip> tips = new ArrayList<>();
    private Tip currentTip = null;
    private int displayTicks = 0;
    private final int MAX_TICKS = 20 * 15; // 15 секунд

    public void loadTips() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("helper_tips.json");
        try {
            if (Files.exists(path)) {
                String json = Files.readString(path);
                JsonArray array = JsonParser.parseString(json).getAsJsonArray();
                for (JsonElement el : array) {
                    tips.add(new Tip(el.getAsJsonObject().get("text").getAsString(), 
                                     el.getAsJsonObject().get("category").getAsString()));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void showNextTip() {
        if (tips.isEmpty()) return;
        currentTip = tips.get((int)(Math.random() * tips.size()));
        displayTicks = 0;
    }

    public void onTick() {
        if (currentTip != null) {
            displayTicks++;
            if (displayTicks > MAX_TICKS) currentTip = null;
        }
    }

    public void render(DrawContext context) {
        if (currentTip == null) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        int w = mc.getWindow().getScaledWidth();
        
        int x = w / 2 - 100;
        int y = 10;
        
        // Полупрозрачный фон
        context.fill(x, y, x + 200, y + 40, 0xAA000000);
        context.drawText(mc.textRenderer, "Совет (" + currentTip.category + "):", x + 5, y + 5, 0xFF8A2BE2, true);
        context.drawText(mc.textRenderer, currentTip.text, x + 5, y + 20, 0xFFFFFF, true);
    }

    private static class Tip {
        String text, category;
        Tip(String text, String category) { this.text = text; this.category = category; }
    }
}
