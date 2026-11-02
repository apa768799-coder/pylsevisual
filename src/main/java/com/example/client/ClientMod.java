package com.example.client;

import com.example.client.config.ClientConfig;
import com.example.client.model.CustomModelManager;
import com.example.client.util.HelperFT;
import com.example.client.util.MusicPlayerUtil;
import com.example.client.version.IVersionSupport;
import com.example.client.version.VersionSupport_1_20;
import com.example.client.version.VersionSupport_1_21;
import com.example.client.visuals.PulseVisualsUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;

/**
 * Главный класс клиента.
 * 
 * ============================================================================
 * РЕКОМЕНДУЕМЫЕ МОДЫ ДЛЯ СБОРКИ (FunTime / ReallyWorld)
 * ============================================================================
 * 1. Sodium — Оптимизация рендера, критично для PvP и паркура.
 * 2. Lithium — Оптимизация логики чанков и сущностей, снижает лаги.
 * 3. Iris Shaders — Поддержка шейдеров для красивого освещения.
 * 4. Entity Texture Features (ETF) — Динамические текстуры мобов/предметов.
 * 5. FancyMenu — Альтернатива нашему меню, если нужны сложные виджеты.
 * 6. OptiFine — Альтернатива Iris (ВНИМАНИЕ: несовместим с Sodium/Iris!).
 * 7. Custom Loading Screen — Брендинг экрана загрузки.
 * 8. Tool Stats — Показывает урон/характеристики предметов (полезно для FT).
 * 9. WTHIT / Jade — Подсказки по блокам (удобно для новичков на ReallyWorld).
 * 
 * КОНФЛИКТЫ: Iris и OptiFine ставить вместе нельзя. Sodium и OptiFine тоже.
 * ============================================================================
 * 
 * ПЛАН МИГРАЦИИ НА НОВУЮ ВЕРСИЮ (например, 1.21.x):
 * 1. Обновить версии в gradle.properties.
 * 2. Создать класс VersionSupport_1_21 (уже создан как заглушка).
 * 3. Проверить сигнатуры Screen.render() и DrawContext (в 1.21+ могут быть изменения).
 * 4. Проверить Registry (в 1.21+ изменилась работа с реестрами предметов/звуков).
 * 5. Протестировать базовые экраны и HUD.
 * ============================================================================
 */
public class ClientMod implements ClientModInitializer {

    public static IVersionSupport VERSION_SUPPORT;

    @Override
    public void onInitializeClient() {
        // Инициализация версионного адаптера
        String mcVersion = FabricLoader.getInstance().getModContainer("minecraft")
                .get().getMetadata().getVersion().getFriendlyString();
        
        if (mcVersion.startsWith("1.21")) {
            VERSION_SUPPORT = new VersionSupport_1_21();
        } else {
            VERSION_SUPPORT = new VersionSupport_1_20();
        }

        // Загрузка систем
        CustomModelManager.getInstance().reload();
        HelperFT.getInstance().loadTips();
        MusicPlayerUtil.getInstance().loadMusic("assets/music/lobby_theme.ogg");

        // Замена стандартного меню
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof TitleScreen && ClientConfig.replaceMainMenu) {
                client.execute(() -> client.setScreen(new com.example.client.menu.CustomMainMenuScreen()));
            }
        });

        // HUD Рендер
        HudRenderCallback.EVENT.register((context, tickDelta) -> HudOverlay.render(context, tickDelta));

        // Тик для обновления частиц и таймеров
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PulseVisualsUtil.onTick();
            HelperFT.getInstance().onTick();
        });
    }
}
