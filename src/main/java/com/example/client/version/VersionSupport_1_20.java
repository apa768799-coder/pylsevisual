package com.example.client.version;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;

public class VersionSupport_1_20 implements IVersionSupport {
    @Override
    public int getScaledWidth(MinecraftClient client) { return client.getWindow().getScaledWidth(); }
    
    @Override
    public int getScaledHeight(MinecraftClient client) { return client.getWindow().getScaledHeight(); }
    
    @Override
    public float getPlayerHealth(PlayerEntity player) { return player.getHealth(); }
    
    @Override
    public float getPlayerMaxHealth(PlayerEntity player) { return player.getMaxHealth(); }
    
    @Override
    public void renderText(DrawContext context, String text, int x, int y, int color) {
        context.drawText(MinecraftClient.getInstance().textRenderer, text, x, y, color, true);
    }
}
