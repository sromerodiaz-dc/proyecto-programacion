package GAME.EFFECT;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Clase que maneja la reproducción de sonidos en el programa.
 */
public class Sound {
    /**
     * Clip de audio que se utiliza para reproducir los sonidos.
     */
    Clip clip;
    /**
     * Arreglo de URL que contienen los paths de los archivos de sonido.
     */
    URL[] soundPath = new URL[10];

    /**
     * Constructor de la clase Sound.
     */
    public Sound() {
        // Se establecen los paths de los archivos de sonido en el arreglo soundPath.
        soundPath[0] = getClass().getClassLoader().getResource("effects/chillbeat.wav");
        soundPath[1] = getClass().getClassLoader().getResource("effects/recogeobjetos.wav");
        soundPath[2] = getClass().getClassLoader().getResource("effects/normalito.wav");
    }

    /**
     * Establece el archivo de sonido que se va a reproducir.
     * @param i Índice del archivo de sonido que se va a reproducir.
     * @throws LineUnavailableException Si no se puede abrir la línea de audio.
     */
    public void setFile(int i) throws LineUnavailableException {
        try {
            // Se crea un AudioInputStream a partir del archivo de sonido seleccionado.
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundPath[i]);
            // Se crea un Clip de audio y se abre con el AudioInputStream.
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (UnsupportedAudioFileException | IOException e) {
            // Se lanza una excepción RuntimeException si ocurre un error al abrir el archivo de sonido.
            throw new RuntimeException(e);
        }
    }

    /**
     * Reproduce el sonido actual.
     */
    public void play(){
        // Se inicia la reproducción del sonido.
        clip.start();
    }

    /**
     * Reproduce el sonido en bucle continuo.
     */
    public void loop(){
        // Se establece el modo de reproducción en bucle continuo.
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Detiene la reproducción del sonido.
     */
    public void stop(){
        // Se detiene la reproducción del sonido.
        clip.stop();
    }

    /**
     * @return Clip Object
     * */
    public Clip getClip() {
        return clip;
    }
}

