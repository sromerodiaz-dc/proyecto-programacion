package com.game;

import com.game.ui.MainWindow;
import javax.sound.sampled.LineUnavailableException;

/**
 * Clase main se encarga de lanzar el programa
 *
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * */
public class Main {
    public static void main(String[] args) throws LineUnavailableException {
        new MainWindow();
    }
}