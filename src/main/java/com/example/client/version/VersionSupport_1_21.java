package com.example.client.version;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;

/**
 * ВЕРСИОННО: Заглушка для 1.21. 
 * В реальном проекте для 1.21 этот класс будет компилироваться в отдельном source set (например, через Stonecutter).
 * Здесь используются методы 1.20.1, чтобы шаблон собирался, но добавлены комментарии где менять.
 */
public class VersionSupport_1_21 implements IVersionSupport {
    @Override
    public int getScaledWidth(MinecraftClient client) { 
        // В 1.21+ Window API может измениться, но getScaledWidth() обычно остается.
        return client.getWindow().getScaledWidth(); 
    }
    
    @Override
    public int getScaledHeight(MinecraftClient client) { return client.getWindow().getScaledHeight(); }
    
    @Override
    public float getPlayerHealth(PlayerEntity player) { return player.getHealth(); }
    
    @Override
    public float getPlayerMaxHealth(PlayerEntity player) { return player.getMaxHealth(); }
    
    @Override
    public void renderText(DrawContext context, String text, int x, int y, int color) {
        // ВЕРСИОННО: В 1.21.2+ TextRenderer может требовать LayeredDrawContext или изменится порядок аргументов.
        context.drawText(MinecraftClient.getInstance().textRenderer, text, x, y, color, true);
    }
}
