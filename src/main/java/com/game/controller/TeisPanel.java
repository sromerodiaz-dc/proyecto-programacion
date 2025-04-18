package com.game.controller;

import com.game.entity.Entity;
import com.game.entity.Player;
import com.game.map.MapSelector;
import com.game.map.MapSize;
import com.game.gphics.PiezaManager;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

/**
 * Esta clase llamada "gui" o interfaz gráfica sirve para determinar la resolucion, el escalado y demas propiedades del panel dentro del JFrame
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class TeisPanel extends JPanel implements Runnable{
    //-> RESOLUCION
    private static final int ResolucionPorDefecto = 16; // 16x16 (el más común)
    private static final int EscaladoPorDefecto = 3; // 3x16 (el más comun)
    public final int sizeFinal = ResolucionPorDefecto * EscaladoPorDefecto; // Esto equivale a un 48x48

    //-> PROPIEDADES DE LA (VENTANA) PANTALLA
    public final int maxScreenColumnas = 18;
    public final int maxScreenFilas = 12;
    public final int screenWidth = sizeFinal * maxScreenColumnas;
    public final int screenHeight =  sizeFinal * maxScreenFilas;

    //-> FPS
    final int fps = 30;

    //-> CONFIGURACIÓN DEL MAPA
    public int maxWorldCol;
    public int maxWorldRow;

    //-> INSTANCIAS DE LOS RECURSOS NECESARIOS
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
        // Implementación de la clase main.GAME.FX.KeyboardController (Lectura de acciones de teclado)
        KeyboardController key = new KeyboardController(this);

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
        controller = new GameController(piezaM,this);
        model = new Player(this, key, controller.properties);

        // Inicia la mísica del juego
        // controller.playMusic(0);
    }

    public void setUpItems() {
        controller.placer.setRecursos();
        // controller.playMusic(0);
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
     * El metodo run() ejecuta estos dos pasos de manera cíclica, llamando primero al metodo update() y luego al metodo repaint(),
     * que a su vez llama al metodo paintComponent() para dibujar la pantalla.
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
                    update(); // Llama al metodo `actualiza()` para actualizar el estado del juego
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
            Iterator<Entity> iterator = controller.enemy.iterator();
            while (iterator.hasNext()) {
                Entity enemy = iterator.next();
                if (enemy != null) {
                    if (enemy.alive && !enemy.dying) {
                        enemy.update();
                    }
                    if (!enemy.alive) {
                        iterator.remove(); // elimina el enemigo directamente
                    }
                }
            }

        }
    }

    /**
     * Metodo que se encarga de dibujar los componentes en la pantalla.
     *
     * @param g Objeto Graphics para dibujar los componentes.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        long tDraw = 0;
        if (model.keyboardController.Time) {
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

        if (model.keyboardController.Time) {
            // Calcula el tiempo de dibujado.
            long tDrawEnd = System.nanoTime();
            long tiempoRestante = tDrawEnd - tDraw;
            System.out.println("Tiempo de pintado: " + tiempoRestante);
        }

        // Libera los recursos gráficos.
        g2.dispose();
    }

    /**
     * Metodo que se encarga de dibujar el fondo de la pantalla.
     *
     * @param g2 Objeto Graphics2D para dibujar el fondo.
     */
    private void drawBackground(Graphics2D g2) {
        // Dibuja el fondo utilizando el objeto PiezaManager.
        controller.getPiezaManager().pinta(g2);
    }

    /**
     * Metodo que se encarga de dibujar las entidades en la pantalla.
     *
     * @param g2 Objeto Graphics2D para dibujar las entidades.
     */
    private void drawEntities(Graphics2D g2) {
        // Limpia la lista de entidades.
        controller.entities.clear();

        // Agrega las entidades no nulas a la lista de entidades.
        controller.entities.addAll(controller.npc.stream().filter(Objects::nonNull).toList());
        controller.entities.addAll(controller.obj.stream().filter(Objects::nonNull).toList());
        controller.entities.addAll(controller.enemy.stream().filter(Objects::nonNull).toList());
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