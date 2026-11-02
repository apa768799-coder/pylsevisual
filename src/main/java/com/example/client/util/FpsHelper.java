package com.example.client.util;

import net.minecraft.client.MinecraftClient;

public class FpsHelper {
    public static int getFps() {
        return MinecraftClient.getInstance().getCurrentFps();
    }

    public static boolean isLowFps() {
        int fps = getFps();
        return fps > 0 && fps < 60; // Если FPS < 60, считаем его низким
    }

    public static long getUsedMemoryMb() {
        Runtime rt = Runtime.getRuntime();
        return (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);
    }
}
