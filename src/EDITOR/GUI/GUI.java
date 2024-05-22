package EDITOR.GUI;

import EDITOR.EMPTYMAP.CeldaVacia;
import EDITOR.EMPTYMAP.VacioPanel;
import EDITOR.FX.SpriteLoader;
import EDITOR.FX.SpriteUtils;
import EDITOR.SELECTPANEL.Celda;
import EDITOR.SELECTPANEL.GridPanel;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.swing.*;

import static EDITOR.EMPTYMAP.CeldaVacia.imageIcon;

/**
 * Clase que define la interacción del jugador con el entorno, así como su movimiento y uso de gráficos en 2D.
 *
 * @author Santiago Agustin Romero Diaz
 */
public class GUI extends JFrame {
    // Propiedades de pantalla
    private static final int ANCHO = 1920;
    private static final int ALTO = 1080;
    private static final int MENU_IZQUIERDA_X = 450;
    private static final int MENU_IZQUIERDA_Y = 100;
    private static final int MENU_IZQUIERDA_ANCHO = 830;
    private static final int MENU_IZQUIERDA_ALTO = 600;
    private static final int MENU_INFERIOR_X = 615;
    private static final int MENU_INFERIOR_Y = 730;
    private static final int MENU_INFERIOR_ANCHO = 534;
    private static final int MENU_INFERIOR_ALTO = 180;
    /*private static final int MENU_MUNDO_X = 1020;
    private static final int MENU_MUNDO_Y = 60;
    private static final int MENU_MUNDO_ANCHO = 750;
    private static final int MENU_MUNDO_ALTO = 730;*/

    private final SpriteLoader spriteLoader = new SpriteLoader();
    public final SpriteUtils spriteUtils = new SpriteUtils();
    public ImageIcon[] sprites = spriteLoader.loadSprites("Assets/background").toArray(new ImageIcon[0]);

    public GUI() throws IOException {
        initGUI();
        setVisible(true);
    }

    private void initGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("TileEditor");
        setSize(ANCHO, ALTO);
        setLocationRelativeTo(null);
        setResizable(false);

        createMenuIzquierda();
        createMenuInferior();
        //createMenuMundo();
        createBotonGuardar();
        createBotonFondo();

        getContentPane().setBackground(Color.BLACK);
        setLayout(null);
        add(menuIzquierda);
        add(menuInferior);
        //add(menuMundo);
        add(botonGuardar);
        add(botonFondo);
    }

    private JPanel menuIzquierda;
    private JPanel menuInferior;
    //private JPanel menuMundo;
    public JButton botonGuardar;
    private JButton botonFondo;

    public void createMenuIzquierda() {
        int[] nums = spriteUtils.numRowsCols();
        menuIzquierda = new VacioPanel(nums[0], nums[1]);
        menuIzquierda.setBackground(Color.BLACK);
        menuIzquierda.setBounds(MENU_IZQUIERDA_X, MENU_IZQUIERDA_Y, MENU_IZQUIERDA_ANCHO, MENU_IZQUIERDA_ALTO);
    }

    public void createMenuInferior() {
        menuInferior = new GridPanel(4, 12, sprites);
        menuInferior.setBackground(Color.BLACK);
        menuInferior.setBounds(MENU_INFERIOR_X, MENU_INFERIOR_Y, MENU_INFERIOR_ANCHO, MENU_INFERIOR_ALTO);
    }

    /*private void createMenuMundo() {
        menuMundo = new WorldMap();
        menuMundo.setBackground(Color.BLACK);
        menuMundo.setBounds(MENU_MUNDO_X, MENU_MUNDO_Y, MENU_MUNDO_ANCHO, MENU_MUNDO_ALTO);
    }*/

    public void createBotonGuardar() {
        botonGuardar = new JButton("Guardar");
        botonGuardar.setBackground(Color.BLACK);
        botonGuardar.setForeground(Color.WHITE);
        botonGuardar.setBounds(ANCHO - 620, ALTO - 500, 100, 50);
        botonGuardar.addActionListener(e -> {
            try {
                botonGuardarClickeado();
                System.exit(0);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void botonGuardarClickeado() throws IOException {
        spriteUtils.generateSpriteMap(sprites, VacioPanel.formato);
    }

    public void createBotonFondo() {
        botonFondo = new JButton("Fondo");
        botonFondo.setBackground(Color.BLACK);
        botonFondo.setForeground(Color.WHITE);
        botonFondo.setBounds(ANCHO - 620, ALTO - 550, 100, 50);
        botonFondo.addActionListener(e -> {
            try {
                fondoSprite();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void fondoSprite() throws IOException {
        for (ImageIcon[] row : VacioPanel.formato) {
            Arrays.fill(row, imageIcon);
        }
        for (CeldaVacia celda : VacioPanel.celdaVacias) {
            celda.setImageIconLocal(imageIcon,celda.getWidth()-10,celda.getHeight()-10);
        }
    }

    // TEST
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