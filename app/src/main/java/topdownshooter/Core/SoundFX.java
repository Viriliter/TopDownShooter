package topdownshooter.Core;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundFX implements Serializable{
    private String path;
    private transient Clip clip;
    private transient FloatControl volumeControl;

    public SoundFX(String path) {
        try {
            this.path = path;
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResourceAsStream(this.path));
            this.clip = AudioSystem.getClip();
            this.clip.open(audioStream);

            this.volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void fadeOutStop(int fadeOutDurationMs) {
        if (this.clip != null && this.clip.isRunning()) {
            new Thread(() -> {
                float minVolume = volumeControl.getMinimum(); // Lowest volume (-80 dB)
                float currentVolume = volumeControl.getValue();
                int steps = 30; // Number of steps in fade-out
                int stepDelay = fadeOutDurationMs / steps;

                for (int i = 0; i < steps; i++) {
                    currentVolume -= (currentVolume - minVolume) / (steps - i);
                    volumeControl.setValue(currentVolume);
                    try {
                        Thread.sleep(stepDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                this.clip.stop(); // Stop music after fade-out
                this.clip.setFramePosition(0); // Reset for next play
            }).start();
        }
    }

    public void stop() {
        if (this.clip != null && this.clip.isRunning()) {
            this.clip.stop(); // Stop the music immediately
            this.clip.setFramePosition(0); // Reset to the beginning for next play
        }
    }

    public void play(boolean loop) {
        if (this.clip != null && !this.clip.isRunning()) {
            new Thread(() -> {
                this.clip.setFramePosition(0); // Start from beginning
                if (loop) {
                    this.clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    this.clip.start();
                }

            }).start();
        }
    }

    public void play(boolean loop, int fadeOutDurationMs) {
        if (this.clip != null) {
            new Thread(() -> {
                this.clip.setFramePosition(0); // Start from beginning
                if (loop) {
                    this.clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    this.clip.start();
                }
            }).start();
        }

        if (fadeOutDurationMs > 0) {
            fadeOutStop(fadeOutDurationMs);
        }
    }

    public void delayedPlay(boolean loop, int delayDurationMS) {
        if (this.clip != null) {
            new Thread(() -> {
                try {
                    this.clip.setFramePosition(0); // Start from beginning
                    Thread.sleep(delayDurationMS); // Delay of 1 second before playing the next sound

                    if (loop) {
                        this.clip.loop(Clip.LOOP_CONTINUOUSLY);
                    } else {
                        this.clip.start();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void pause() {
        // TODO add implementation
    }

    void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResourceAsStream(this.path));
            this.clip = AudioSystem.getClip();
            this.clip.open(audioStream);

            this.volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
