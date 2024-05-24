package GAME.GUI;

import GAME.GAME.TeisPanel;
import javax.swing.*;

/**
 * Clase que define la ventana principal de la aplicación.
 *
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class ventana extends JFrame {
    /**
     * El panel principal del juego.
     */
    private TeisPanel teisPanel;

    /**
     * Constructor que inicializa la ventana principal de la aplicación.
     */
    public ventana() {
        teisPanel = new TeisPanel(); // Crea un nuevo panel principal
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Establece la operación por defecto al cerrar la ventana
        setResizable(false); // Impide que la ventana sea redimensionable
        setTitle("Teis"); // Establece el título de la ventana
        add(teisPanel); // Agrega el panel principal a la ventana
        pack(); // Ajusta el tamaño de la ventana para que quepa el panel principal
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setVisible(true); // Hace visible la ventana

        // Inicializa los items del mapa
        teisPanel.setUpItems();

        // Inicializa el hilo principal del juego
        teisPanel.startTeisThread();
    }
}
