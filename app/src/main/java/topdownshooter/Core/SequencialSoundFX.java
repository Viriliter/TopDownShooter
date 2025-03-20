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

public class SequencialSoundFX implements Serializable{
    private List<String> paths = null;
    private transient List<Clip> clips = null;
    private int currentSoundIndex = 0;
    private AtomicBoolean isPlaying;

    public SequencialSoundFX(List<String> paths) {
        this.paths = paths;
        this.isPlaying = new AtomicBoolean(false);

        // Preload all the sound files
        this.clips = new ArrayList<>();
        for (String path : this.paths) {
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

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // FIXME Fix the bug after loading game: 
        // Exception in thread "AWT-EventQueue-0" java.lang.NullPointerException: Cannot invoke "java.util.List.isEmpty()" because "this.clips" is null
        //at topdownshooter.Core.SequencialSoundFX.update(SequencialSoundFX.java:47)
        System.out.println("-------------------------------------------");
        // Preload all the sound files
        this.clips = new ArrayList<>();
        for (String path : this.paths) {
            this.clips.add(loadSound(path));
        }
    }
}
