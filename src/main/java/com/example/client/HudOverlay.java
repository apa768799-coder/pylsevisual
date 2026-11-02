package com.example.client;

import com.example.client.config.ClientConfig;
import com.example.client.util.HelperFT;
import com.example.client.visuals.PulseVisualsUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;

public class HudOverlay {
    public static void render(DrawContext context, float tickDelta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        if (ClientConfig.enableVisuals) {
            // Target HUD
            if (mc.targetedEntity instanceof PlayerEntity target) {
                PulseVisualsUtil.drawTargetHud(context, target, 10, 10);
            }
            
            // Low HP Pulse
            float hpPercent = mc.player.getHealth() / mc.player.getMaxHealth();
            PulseVisualsUtil.drawLowHpPulse(context, hpPercent);
            
            // Combo
            PulseVisualsUtil.drawComboCounter(context);
        }

        // Helper FT Tips
        HelperFT.getInstance().render(context);
    }
}
