package topdownshooter.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

public class SequencialSoundFX {
    private List<Clip> clips;
    private int currentSoundIndex;
    private AtomicBoolean isPlaying;

    public SequencialSoundFX(List<String> paths) {
        this.currentSoundIndex = 0;
        this.isPlaying = new AtomicBoolean(false);

        // Preload all the sound files
        this.clips = new ArrayList<>();
        for (String path : paths) {
            clips.add(loadSound(path));
        }
    }

    private Clip loadSound(String path) {
        Clip clip = null;
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResourceAsStream(path));

            // Get a clip to play the sound
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clip;
    }

    public void update() {
        if (this.clips.isEmpty() || this.isPlaying.get()) return;

        new Thread(this::play).start();
    }

    private void play() {
        if (this.clips.isEmpty()) return;

        Clip clip = this.clips.get(currentSoundIndex);

        synchronized (this) {
            if (this.isPlaying.get()) return;
            this.isPlaying.set(true);
        }

        clip.setFramePosition(0); // Rewind the clip
        clip.start();
        
        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                synchronized (this) {
                    isPlaying.set(false);
                }
            }
        });

        this.currentSoundIndex = (this.currentSoundIndex + 1) % this.clips.size();
    }
}
