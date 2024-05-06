/*
** Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
* */

public class Main {
    public static void main(String[] args) {
        TeisPanel teisPanel = new TeisPanel();
        ventana w = new ventana();

        // Añado al JFrame un componente nuevo, en este caso, "teisPanel" que es un panel de la clase TeisPanel
        // El "pack()" ajusta la ventana a la resolucion determinada (preferred size) y layouts de sus componentes, en este caso, TeisPanel.
        w.add(teisPanel);
        w.pack();

        w.setVisible(true);
    }
}
