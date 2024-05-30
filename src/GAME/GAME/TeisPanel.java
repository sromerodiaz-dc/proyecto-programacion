package GAME.GAME;

import GAME.ENTITY.Entity;
import GAME.ENTITY.Player;
import GAME.FX.KeyManager;
import GAME.FX.MapSelector;
import GAME.FX.MapSize;
import GAME.GPHICS.PiezaManager;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author Santiago Agustin Romero Diaz
* CFP Daniel Castelao
* Proyecto: Teis
* -
* Esta clase llamada "gui" o interfaz gráfica sirve para determinar la resolucion, el escalado y demas propiedades del panel dentro del JFrame
* */
public class TeisPanel extends JPanel implements Runnable{
    //Resolucion
    private static final int ResolucionPorDefecto = 16; // 16x16 (el más común)
    private static final int EscaladoPorDefecto = 3; // 3x16 (el más comun)
    public final int sizeFinal = ResolucionPorDefecto * EscaladoPorDefecto; // Esto equivale a un 48x48

    //Propiedades pantalla
    public final int maxScreenColumnas = 18;
    public final int maxScreenFilas = 12;

    //Ancho y alto (uso valores mínimos por facilidad)
    public final int screenWidth = sizeFinal * maxScreenColumnas;
    public final int screenHeight =  sizeFinal * maxScreenFilas;

    // FPS
    final int fps = 30;

    // CONFIGURACIÓN DEL MAPA
    public int maxWorldCol;
    public int maxWorldRow;

    // Implementación tiempo (RUNNABLE)
    public Thread teisThread;

    // GameModel y Game Controller
    public final Player model;
    public final GameController controller;

    // Selector de mapa
    MapSelector mapSelector = new MapSelector();
    public MapSize datos = mapSelector.getMapSize();

    // Constructor
    public TeisPanel() {
        // Implementación de la clase GAME.FX.KeyManager (Lectura de acciones de teclado)
        KeyManager key = new KeyManager(this);

        maxWorldCol = datos.maxCol;
        maxWorldRow = datos.maxRow;

        // Implementacion de backgrounds y mecanicas de colision
        PiezaManager piezaM = new PiezaManager(this);

        setPreferredSize(new Dimension(screenWidth,screenHeight));
        setBackground(Color.black);
        setDoubleBuffered(true);
        addKeyListener(key);
        setFocusable(true);

        // Inicializa el modelo y el controlador
        model = new Player(this,key);
        controller = new GameController(piezaM,this);
    }

    public void setUpItems() {
        controller.placer.setRecursos();
        /*controller.playMusic(0);*/
    }

    /**
     * Inicializa y ejecuta un nuevo hilo (thread) para el procesamiento de entidades en segundo plano.
     *
     * @since 1.0
     */
    public void startTeisThread() {
        // Crea un nuevo hilo y ejecuta la tarea en segundo plano.
        teisThread = new Thread(this);
        teisThread.start();
    }

    /**
     * Ejecuta el "Game Loop" del juego, que se encarga de actualizar la información del juego y dibujar la pantalla.
     *-
     * El Game Loop se compone de dos pasos:
     * 1. Actualizar la información del juego, como las interacciones del personaje jugador.
     * 2. Dibujar la pantalla del juego con la información actualizada.
     *-
     * El método run() ejecuta estos dos pasos de manera cíclica, llamando primero al método update() y luego al método repaint(),
     * que a su vez llama al método paintComponent() para dibujar la pantalla.
     */
    @Override
    public void run() {
        // Implementación Delta / Iterador Game Loop
        double tiempoEspera = (double) 1000000000/fps; // Calcula el tiempo de espera en nanosegundos por frame
        double delta = 0; // Variable para acumular el delta time
        long lastTime = System.nanoTime(); // Almacena el tiempo en nanosegundos del frame anterior
        long currentTime; // Variable para almacenar el tiempo en nanosegundos del frame actual

        // Implementación ShowFPS
        long timer = 0; // Variable para acumular el tiempo desde la última medición de FPS
        int espera = 0; // Contador de frames desde la última medición de FPS

        while (teisThread != null) { // Bucle principal del juego
            currentTime = System.nanoTime(); // Obtiene el tiempo en nanosegundos del frame actual
            delta += (currentTime - lastTime) / tiempoEspera; // Acumula el delta time (tiempo transcurrido desde el último frame)
            timer += (currentTime - lastTime); // Acumula el tiempo desde la última medición de FPS
            lastTime = currentTime; // Actualiza el tiempo del frame anterior con el tiempo actual

            if (delta >= 1) { // Si el delta time es mayor o igual a 1 (un frame completo)
                try {
                    update(); // Llama al método `actualiza()` para actualizar el estado del juego
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
                repaint(); // Solicita que el componente se repinte (actualice su visualización)
                delta--; // Resta 1 al delta time para que no se acumule en el siguiente frame
                espera ++; // Incrementa el contador de frames
            }

            if (timer >= 1000000000) { // Si ha pasado un segundo (1000000000 nanosegundos)
                System.out.println("FPS: "+espera); // Imprime el valor de FPS (frames por segundo)
                espera = 0; // Reinicia el contador de frames
                timer = 0; // Reinicia el temporizador de FPS
            }
        }
    }

    /**
     * Actualiza el estado del juego.
     *
     * @throws LineUnavailableException si ocurre un error al actualizar el estado del juego
     */
    public void update() throws LineUnavailableException {
        // Verificar si el juego está en estado de juego
        if (controller.estado == controller.playState) {
            // Actualizar el estado del jugador
            model.actualiza();

            // Actualizar NPCs
            for (Entity npc : controller.npc) {
                if (npc!= null) {
                    npc.update();
                }
            }

            // Actualizar enemigos
            for (Entity enemy : controller.enemy) {
                if (enemy!= null) {
                    enemy.update();
                }
            }
        }
    }

    /**
     * Método que se encarga de dibujar los componentes en la pantalla.
     *
     * @param g Objeto Graphics para dibujar los componentes.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        long tDraw = 0;
        if (model.keyManager.Time) {
            // Medición del tiempo de dibujado.
            tDraw = System.nanoTime();
        }

        if (controller.estado == controller.cargaState) {
            // Dibuja la pantalla de carga.
            controller.ui.draw(g2);
        } else {
            // Dibuja el fondo.
            drawBackground(g2);

            // Dibuja las entidades.
            drawEntities(g2);

            // Dibuja la interfaz de usuario.
            controller.ui.draw(g2);
        }

        if (model.keyManager.Time) {
            // Calcula el tiempo de dibujado.
            long tDrawEnd = System.nanoTime();
            long tiempoRestante = tDrawEnd - tDraw;
            System.out.println("Tiempo de pintado: " + tiempoRestante);
        }

        // Libera los recursos gráficos.
        g2.dispose();
    }

    /**
     * Método que se encarga de dibujar el fondo de la pantalla.
     *
     * @param g2 Objeto Graphics2D para dibujar el fondo.
     */
    private void drawBackground(Graphics2D g2) {
        // Dibuja el fondo utilizando el objeto PiezaManager.
        controller.getPiezaManager().pinta(g2);
    }

    /**
     * Método que se encarga de dibujar las entidades en la pantalla.
     *
     * @param g2 Objeto Graphics2D para dibujar las entidades.
     */
    private void drawEntities(Graphics2D g2) {
        // Limpia la lista de entidades.
        controller.entities.clear();

        // Agrega las entidades no nulas a la lista de entidades.
        controller.entities.addAll(Arrays.stream(controller.npc).filter(Objects::nonNull).toList());
        controller.entities.addAll(Arrays.stream(controller.obj).filter(Objects::nonNull).toList());
        controller.entities.addAll(Arrays.stream(controller.enemy).filter(Objects::nonNull).toList());
        controller.entities.add(model);

        // Ordena las entidades por su posición en Y.
        controller.entities.sort(Comparator.comparingInt(e -> e.worldY));

        // Dibuja cada entidad en la lista.
        for (Entity entity : controller.entities) {
            entity.draw(g2);
        }

        // Limpia la lista de entidades.
        controller.entities.clear();
    }
}