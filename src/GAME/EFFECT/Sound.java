package GAME.EFFECT;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundPath = new URL[10];

    public Sound() {
        soundPath[0] = getClass().getClassLoader().getResource("effects/chillbeat.wav");
        soundPath[1] = getClass().getClassLoader().getResource("effects/recogeobjetos.wav");
        soundPath[2] = getClass().getClassLoader().getResource("effects/normalito.wav");
    }

    public void setFile(int i) throws LineUnavailableException {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundPath[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void play(){
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){
        clip.stop();
    }
}

