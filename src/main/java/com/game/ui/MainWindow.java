package com.game.ui;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;

/**
 * Clase que define la MainWindow principal de la aplicación.
 *
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class MainWindow extends JFrame {

    /**
     * Constructor que inicializa la MainWindow principal de la aplicación.
     */
    public MainWindow() throws LineUnavailableException {
        TeisPanel teisPanel = new TeisPanel(); // Crea un nuevo panel principal
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Establece la operación por defecto al cerrar la MainWindow
        setResizable(false); // Impide que la MainWindow sea redimensionable
        setTitle("Teis"); // Establece el título de la MainWindow
        add(teisPanel); // Agrega el panel principal a la MainWindow
        pack(); // Ajusta el tamaño de la MainWindow para que quepa el panel principal
        setLocationRelativeTo(null); // Centra la MainWindow en la pantalla
        setVisible(true); // Hace visible la MainWindow

        // Inicializa los items del mapa
        teisPanel.setUpItems();

        // Inicializa el hilo principal del juego
        teisPanel.startTeisThread();
    }
}
