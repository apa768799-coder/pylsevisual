package com.example.client.util;

import com.example.client.config.ClientConfig;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Музыкальный плеер. 
 * Примечание: Использует стандартный javax.sound.sampled, так как загрузка сырых .ogg 
 * в ванильный SoundManager Minecraft без ресурс-пака крайне сложна и зависит от версии.
 */
public class MusicPlayerUtil {
    private static final MusicPlayerUtil INSTANCE = new MusicPlayerUtil();
    public static MusicPlayerUtil getInstance() { return INSTANCE; }

    private final List<String> tracks = new ArrayList<>();
    private int currentTrackIndex = 0;
    private Clip currentClip;
    private float volume = 0.5f;

    public void loadMusic(String path) {
        File file = new File(path);
        if (file.exists()) {
            tracks.add(file.getAbsolutePath());
        } else {
            System.out.println("[Music] Трек не найден: " + path);
        }
    }

    public void playMusic() {
        if (!ClientConfig.isMusicEnabled || tracks.isEmpty()) return;
        stopMusic();
        try {
            AudioInputStream input = AudioSystem.getAudioInputStream(new File(tracks.get(currentTrackIndex)));
            currentClip = AudioSystem.getClip();
            currentClip.open(input);
            setVolume(volume);
            currentClip.start();
            currentClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println("[Music] Ошибка воспроизведения: " + e.getMessage());
        }
    }

    public void stopMusic() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
            currentClip.close();
        }
    }

    public void pauseMusic() {
        if (currentClip != null && currentClip.isRunning()) currentClip.stop();
    }

    public void setVolume(float value) {
        this.volume = Math.max(0.0f, Math.min(1.0f, value));
        if (currentClip != null) {
            FloatControl gain = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gain.getMaximum() - gain.getMinimum();
            float gainValue = (range * this.volume) + gain.getMinimum();
            gain.setValue(gainValue);
        }
    }

    public void nextTrack() {
        if (tracks.isEmpty()) return;
        currentTrackIndex = (currentTrackIndex + 1) % tracks.size();
        playMusic();
    }

    public String getCurrentTrackName() {
        if (tracks.isEmpty()) return "Нет треков";
        return new File(tracks.get(currentTrackIndex)).getName();
    }
}
