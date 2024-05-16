package GAME.GUI;

import GAME.FX.TeisPanel;
import javax.swing.*;

/**
 * @author Santiago Agustin Romero Diaz
 * * CFP Daniel Castelao
 * * Proyecto: Teis
 * --------------------------------
 * Esta clase define la GAME.GUI.ventana (JFrame) sobre la que se implementaran diferentes JPanel, JButtons, etc.
 * */
public class ventana extends JFrame {
    TeisPanel teisPanel = new TeisPanel();
    public ventana() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Teis");
        add(teisPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        teisPanel.startTeisThread();
    }
}
