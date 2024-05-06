import javax.swing.*;
import java.awt.*;

/**
* @author Santiago Agustin Romero Diaz
* CFP Daniel Castelao
* Proyecto: Teis
*
* Esta clase llamada "gui" o interfaz gráfica sirve para determinar la resolucion, el escalado y demas propiedades del panel dentro del JFrame
* */

public class TeisPanel extends JPanel implements Runnable{
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

    // Implementación tiempo (RUNNABLE)
    Thread teisThread;

    // Constructor
    public TeisPanel() {
        setPreferredSize(new Dimension(screenWidth,screenHeight));
        setBackground(Color.black);
        setDoubleBuffered(true);
    }

    // Metodo que inicializa un "Thread"
    /**
     * Este código de Java define un método para crear e iniciar un hilo (Thread) llamado "teisThread".
     * -
     * public void startTeisThread(): Esta línea define un método público llamado startTeisThread que no devuelve ningún valor (indicated by void).
     * -
     * teisThread = new Thread(this);: Aquí se crea un nuevo objeto de tipo Thread.
     * La parte new Thread(this) indica que este mismo objeto (la clase actual) será el que se ejecute dentro del hilo.
     * -
     * teisThread.start();: Esta línea le dice al objeto teisThread que comience a ejecutarse como un hilo independiente.
     * -
     * En resumen, este código crea un nuevo hilo y lo inicia, pero aún no le dice qué hacer.
     * */
    public void startTeisThread() {
        teisThread = new Thread(this);
        teisThread.start();
    }
    @Override
    public void run() {

    }
}
