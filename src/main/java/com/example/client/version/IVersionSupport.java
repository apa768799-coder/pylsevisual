package com.example.client.version;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Интерфейс для абстрагирования методов, которые меняются между версиями MC.
 */
public interface IVersionSupport {
    int getScaledWidth(MinecraftClient client);
    int getScaledHeight(MinecraftClient client);
    float getPlayerHealth(PlayerEntity player);
    float getPlayerMaxHealth(PlayerEntity player);
    void renderText(DrawContext context, String text, int x, int y, int color);
}
