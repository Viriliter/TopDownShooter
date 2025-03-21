/*
 * @file SequencialSoundFX.java
 * @brief This file defines the `SequencialSoundFX` class.
 *
 * Created on Wed Mar 19 2025
 *
 * @copyright MIT License
 *
 * Copyright (c) 2025 Mert LIMONCUOGLU
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package topdownshooter.Core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

/**
 * @class SequencialSoundFX
 * @brief A class that plays a sequence of sound effects.
 * 
 * This class preloads a list of sound files and plays them sequentially.
 * It supports serialization but ensures that sound clips are reloaded upon deserialization.
 */
public class SequencialSoundFX implements Serializable{
    private List<String> paths = null;              /**< The list of sound file paths. */
    private transient List<Clip> clips = null;      /**< The list of preloaded sound clips. */
    private int currentSoundIndex = 0;              /**< The index of the current sound being played. */
    private AtomicBoolean isPlaying;                /**< Atomic flag to check if a sound is currently playing. */

    /**
     * Constructs a SequencialSoundFX instance with a list of sound file paths.
     * 
     * @param paths The list of sound file paths to be loaded and played sequentially.
     */
    public SequencialSoundFX(List<String> paths) {
        this.paths = paths;
        this.isPlaying = new AtomicBoolean(false);

        // Preload all the sound files
        this.clips = new ArrayList<>();
        for (String path : this.paths) {
            clips.add(loadSound(path));
        }
    }

    /**
     * Loads a sound file from the given path.
     * 
     * @param path The path of the sound file.
     * @return The loaded Clip object, or null if loading fails.
     */
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

    /**
     * Updates the sound system and plays the next sound if no sound is currently playing.
     */
    public void update() {
        if (this.clips.isEmpty() || this.isPlaying.get()) return;

        new Thread(this::play).start();
    }

    /**
     * Plays the current sound and moves to the next in the sequence.
     */
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

    /**
     * Custom deserialization method to reload sound clips after loading from a saved state.
     * 
     * @param in The object input stream.
     * @throws IOException If an I/O error occurs.
     * @throws ClassNotFoundException If the class cannot be found.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        // Preload all the sound files
        this.clips = new ArrayList<>();
        for (String path : this.paths) {
            this.clips.add(loadSound(path));
        }
    }
}
