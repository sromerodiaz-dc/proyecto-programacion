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

    /** Metodo que inicializa un "Thread"
     * ----------------------------------
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
    /**
     * Metodo que de manera inherente no retorna nada por ser implementacion de RUNNABLE.
     * Ejecutará el "Game Loop" (Explicación de lo que es en el README.MD).
     * Funcionalidades (dividido en dos metodos desarrollados debajo):
     * 1- ACTUALIZAR la información como las interacciones del PJ (Personaje). Por ejemplo, al pulsar la letra "W", caminar hacia adelante.
     * 2- DIBUJAR / MOSTRAR la pantalla del juego con la INFORMACIÓN actualizada.
     * -----------------------------------------------------------------------------------------
     * El esquema se podría ver de la siguiente manera:
     *             /<============================================\
     * GAME LOOP { \=>ACTUALIZAR INFORMACIÓN => PINTAR PANTALLA=>/ }
     * -
     * Se llama al metodo Run() ejecutandose primero el metodo ACTUALIZA y seguido del metodo REPAINT el cual
     * llama lo antes posible al metodo "paint" del componente siendo en este caso el metodo "paintComponent".
     * */
    @Override
    public void run() {
        while (teisThread != null) {
            actualiza();
            repaint();
        }
    }

    /**
     * Actualizará información del juego
     * */
    public void actualiza() {

    }
    /**
     * Dibujará la pantalla del juego con la información actualizada. El objeto Graphics proporciona métodos para dibujar formas, líneas, texto e imágenes en el componente.
     * Siempre que se llame un metodo de la siguiente manera es un override de paintComponent de JComponent o mejor dicho, metodo ESTANDAR.
     * IMPORTANTE:
     * Dentro del método, la `línea super.paintComponent(g)` llama al método paintComponent de la clase padre.
     * Este paso es importante porque los componentes Swing predeterminados (como botones, paneles) a menudo tienen su propia lógica de pintado.
     * Al llamar a super.paintComponent(g), te aseguras de que el comportamiento de pintado predeterminado se ejecute primero, lo que normalmente se encarga de dibujar el fondo y el borde del componente.
     * */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        // Como el juego es en 2D, instanciaré la clase Graphics2D (extension de Graphics) para tener un control más sofisticado sobre la geometría, coordenadas, colores y textos.
        // Es como usar GridBagLayout en vez de BorderLayout.
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.white);

        g2.fillRect(100,100,sizeFinal,sizeFinal);

        g2.dispose();
    }
}
