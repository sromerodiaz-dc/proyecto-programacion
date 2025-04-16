package com.editor.gui;

import com.editor.emptymap.CeldaVacia;
import com.editor.emptymap.VacioPanel;
import com.editor.utils.KeyboardListener;
import com.editor.utils.SpriteLoader;
import com.editor.utils.SpriteUtils;
import com.editor.panel.Celda;
import com.editor.panel.GridPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import javax.swing.*;

import static com.editor.emptymap.CeldaVacia.imageIcon;

/**
 * Clase que define la interacción del jugador con el entorno, así como su movimiento y uso de gráficos en 2D.
 *
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class GUI extends JFrame {
    // Propiedades de pantalla
    private static final int ANCHO = 1920;
    private static final int ALTO = 1080;

    public int MENU_IZQUIERDA_ANCHO = (int) (ANCHO / 1.7);
    public int MENU_IZQUIERDA_ALTO = ALTO - 150 ;

    public int MENU_IZQUIERDA_X = 50;
    public int MENU_IZQUIERDA_Y = 50;

    private static final int MENU_INFERIOR_ANCHO = ANCHO / 4;
    private static final int MENU_INFERIOR_ALTO = ALTO / 5;

    public int MENU_INFERIOR_X = ANCHO - 720;
    public int MENU_INFERIOR_Y = ALTO - 400;

    // Instancias de clases cuyos métodos son necesarios
    private final SpriteLoader spriteLoader = new SpriteLoader(); // Carga los assets
    public final SpriteUtils spriteUtils = new SpriteUtils(); // Guardado del mapa
    public ImageIcon[] sprites; // Array de Sprites
    private final KeyboardListener keyboardListener = new KeyboardListener(this); // Controlador para el modo 'pincel'

    // Paneles contenedores
    private VacioPanel menuIzquierda;
    private JPanel menuInferior;
    private JPanel labelPanel;
    private JLabel isPressed;
    private JButton botonGuardar;
    private JButton botonFondo;

    // Constructor por defecto
    public GUI() throws URISyntaxException {
        initGUI();
        setVisible(true);
    }

    // Metodo inicializador de componentes
    private void initGUI() throws URISyntaxException {
        // Propiedades del JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("TileEditor");
        setSize(ANCHO, ALTO);
        setLocationRelativeTo(null);
        setResizable(false);

        // Guardado de los sprites mediante el metodo loadSprites() de GUI
        sprites = loadSprites();

        /*
        * Para hacer que en el editor de mapas se pueda cargar un mapa hay que añadir un metodo
        * que retorne un array con la lectura de un mapa (ya hecho, SpriteUtils > loadmap que retorna map[][].
        * Ahora hay que emplearlo.
        *
        * Para esto:
        * Primero se le da al usuario una de dos opciones, cargar mapa o crear nuevo mapa,
        * Si clica sobre crear nuevo mapa, el programa sigue como hasta ahora pero si le da a la otra opción:
        * - Se llama al metodo loadmap y de ahí se saca el numero máximo de columnas y filas para generar el
        *   mapa cargado. Además, también se saca la correspondencia por lo que a VacioPanel se le ha de pasar
        *   este array por parametro para que lo use dandole a cada CeldaVacia su Imagen correspondiente.
        * */

        setSegunOpcion();

        // Crea ambos botones
        createBotonGuardar(menuIzquierda);
        createBotonFondo(menuIzquierda);
        // Crea el sprite y el boton de Pincel
        createLabelPincel();

        // Propiedades del contenedor
        getContentPane().setBackground(Color.BLACK);
        setLayout(null);
        // Paneles, botones y labels añadidos al panel contenedor
        add(menuIzquierda);
        add(menuInferior);
        add(labelPanel);
        add(botonGuardar);
        add(botonFondo);

        // KeyListener añadido al panel y InputFocus puesto sobre el panel contenedor de JFrame
        addKeyListener(keyboardListener);
        setFocusable(true);
        requestFocus();
    }

    public void setSegunOpcion(){
        if (create_load_map()) {
            String mapName = JOptionPane.showInputDialog(null,"Nombre del mapa (sin extension):");
            int[][] contenido = spriteUtils.loadMap("maps/"+mapName+".txt");
            cargarMenuIzquierda(contenido, sprites);
        } else {
            int[] nums = spriteUtils.numRowsCols();
            createMenuIzquierda(nums[0], nums[1]);
        }
        createMenuInferior();
    }

    public boolean create_load_map() {
        int opcion = JOptionPane.showConfirmDialog(null, "¿Desea cargar un mapa existente?", "Cargar Mapa", JOptionPane.YES_NO_CANCEL_OPTION);

        if (opcion == JOptionPane.YES_OPTION) return true;
        else if (opcion == JOptionPane.NO_OPTION) return false;
        else System.exit(0);
        return false;
    }

    /**
     * Genera un panel de paneles a partir de la clase VacioPanel.
     * El número de filas y columnas es determinado por el usuario a través de un metodo llamado
     * desde 'spriteUtils.numRowsCols()'
     * */
    public void createMenuIzquierda(int row,int col) {
        menuIzquierda = new VacioPanel(row,col);
        menuIzquierda.setBackground(Color.BLACK);
        menuIzquierda.setBounds(MENU_IZQUIERDA_X, MENU_IZQUIERDA_Y, MENU_IZQUIERDA_ANCHO, MENU_IZQUIERDA_ALTO);
    }

    public void cargarMenuIzquierda(int[][] contenido, ImageIcon[] sprites) {
        menuIzquierda = new VacioPanel(contenido,sprites);
        menuIzquierda.setBackground(Color.BLACK);
        menuIzquierda.setBounds(MENU_IZQUIERDA_X, MENU_IZQUIERDA_Y, MENU_IZQUIERDA_ANCHO, MENU_IZQUIERDA_ALTO);
    }

    /**
     * Genera un panel de paneles clicables para la selección de texturas aplicables sobre "menuIzquierda"
     * */
    public void createMenuInferior() {
        menuInferior = new GridPanel(5, 12, sprites);
        menuInferior.setBackground(Color.BLACK);
        menuInferior.setBounds(MENU_INFERIOR_X, MENU_INFERIOR_Y, MENU_INFERIOR_ANCHO, MENU_INFERIOR_ALTO);
    }

    /**
     * Genera el botón de guardado para que una vez se haya terminado la edición o creación de un mapa
     * se guarde como una matriz de números donde cada número corresponde a un sprite.
     * */
    public void createBotonGuardar(VacioPanel panel) {
        botonGuardar = new JButton("Guardar");
        botonGuardar.setBackground(Color.BLACK);
        botonGuardar.setForeground(Color.WHITE);
        botonGuardar.setBounds(ANCHO - 455, ALTO - 770, 100, 50);
        botonGuardar.addActionListener((ActionEvent _) -> {
            try {
                botonGuardarClickeado(panel); // LLamado al metodo de guardado
                System.exit(0); // Si no se quiere guardar, simplemente se sale.
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Genera el botón de generación de fondo automático.
     * Una vez clicado este botón habiendo seleccionado un Sprite, todas las celdas pasarán a contener dicho sprite.
     * */
    public void createBotonFondo(VacioPanel panel) {
        botonFondo = new JButton("Fondo");
        botonFondo.setBackground(Color.BLACK);
        botonFondo.setForeground(Color.WHITE);
        botonFondo.setBounds(ANCHO - 585, ALTO - 770, 100, 50);
        botonFondo.addActionListener(_ -> {
            try {
                fondoSprite(panel);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Crea un nuevo panel con varias propiedades:
     * - Posición
     * - Label
     * - Borde especificado
     * - Texto escrito que describe la funcionalidad
     * */
    public void createLabelPincel() {
        // Crea el panel
        labelPanel = new JPanel();
        labelPanel.setBackground(Color.BLACK);
        labelPanel.setBounds(ANCHO - 620, ALTO - 950, 300, 150);

        // Crea el JLabel que contendra el icono del pincel
        JLabel labelPincel = new JLabel();
        labelPincel.setHorizontalAlignment(JLabel.CENTER);
        labelPincel.setVerticalAlignment(JLabel.CENTER);
        labelPincel.setIcon(Celda.escaladoImage(new ImageIcon("images/pincelBoton.png"),64,64));

        // Se crea y se añade el texto
        createFText();

        // Añade tanto el texto como el label al panel
        labelPanel.add(isPressed);
        labelPanel.add(labelPincel);
        // Por último, declara un border específico para el comienzo del programa
        labelPanel.setBorder(BorderFactory.createLineBorder(Color.white, 1,false));
    }

    /**
     * Genera el texto para el Panel anterior
     * */
    public void createFText() {
        isPressed = new JLabel();
        isPressed.setText("Presiona \"F\" para activar el modo pincel");
    }

    /**
     * @return Array de ImageIcon guardado en 'sprites'
     * */
    private ImageIcon[] loadSprites() {
        return spriteLoader.loadSprites();
    }

    public void cargarMapa() {}

    /**
     * LLama al método de generación de mapa de 'spriteUtils'
     * @param panel Recibe por parametro VacioPanel para poder acceder al array de CeldaVacias y procesar la información
     * @throws IOException Lanza una excepción de tipo INPUT / OUTPUT
     * */
    private void botonGuardarClickeado(VacioPanel panel) throws IOException {
        spriteUtils.generateSpriteMap(sprites, panel.formato);
    }

    /**
     * Llena el panel rellenable con el sprite seleccionado.
     * @param panel Recibe por parametro VacioPanel para poder acceder al array de CeldaVacias y procesar la información
     * @throws IOException Lanza una excepción de tipo INPUT / OUTPUT
     * */
    public void fondoSprite(VacioPanel panel) throws IOException {
        // Vuelve a darle el FOCUSABLE al panel contenedor #BugFixed
        requestFocus();
        for (ImageIcon[] row : panel.formato) {
            Arrays.fill(row, imageIcon);
        }
        for (CeldaVacia celda : panel.celdaVacias) {
            celda.setImageIconLocal(imageIcon,celda.getWidth()-15,celda.getHeight()-15);
        }
    }

    /**
     * Cambia tanto el borde del panel de 'pincel' como la variable local de cada CeldaVacia
     * llamada 'buttonSelected' para desactivar el modo pincel.
     * */
    public void setLabelOff(){
        labelPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1,false));
        for (CeldaVacia celdaVacia : menuIzquierda.celdaVacias) {
            celdaVacia.buttonSelected = false;
        }
    }

    /**
     * Cambia tanto el borde del panel de 'pincel' como la variable local de cada CeldaVacia
     * llamada 'buttonSelected' para activar el modo pincel.
     * */
    public void setLabelOn(){
        labelPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2,true));
        for (CeldaVacia celdaVacia : menuIzquierda.celdaVacias) {
            celdaVacia.buttonSelected = true;
        }
    }

    // METODOS PARA REALIZAR TESTs
    public Object getMenuIzquierda() {
        return menuIzquierda;
    }

    public Object getMenuInferior() {
        return menuInferior;
    }

    public Object getBotonFondo() {
        return botonFondo;
    }
}