import javax.swing.*;

/**
 * @author Santiago Agustin Romero Diaz
 * * CFP Daniel Castelao
 * * Proyecto: Teis
 * --------------------------------
 * Esta clase define la ventana (JFrame) sobre la que se implementaran diferentes JPanel, JButtons, etc.
 * */

public class ventana extends JFrame {
    public ventana() {
        setTitle("Teis");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
