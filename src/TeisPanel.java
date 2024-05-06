import javax.swing.*;
import java.awt.*;

/*
* Santiago Agustin Romero Diaz
* CFP Daniel Castelao
* Proyecto: Teis
*
* Esta clase llamada "gui" o interfaz gráfica sirve para determinar la resolucion, el escalado y demas propiedades del panel dentro del JFrame
* */

public class TeisPanel extends JPanel {
    //Resolucion
    private static final int ResolucionPorDefecto = 16; // 16x16 (el más común)
    private static final int EscaladoPorDefecto = 3; // 3x16 (el más comun)
    private static final int sizeFinal = ResolucionPorDefecto * EscaladoPorDefecto; // Esto equivale a un 48x48

    //Propiedades pantalla
    private static final int maxScreenColumnas = 18;
    private static final int maxScreenFilas = 12;
    //Ancho y alto (uso valores mínimos por facilidad)
    private static final int screenWidth = sizeFinal * maxScreenColumnas;
    private static final int screenHeight =  sizeFinal * maxScreenFilas;


    public TeisPanel() {
        setPreferredSize(new Dimension(screenWidth,screenHeight));
        setBackground(Color.black);
        setDoubleBuffered(true);
    }
}
