package EDITOR.GUI;

import EDITOR.EMPTYMAP.CeldaVacia;
import EDITOR.EMPTYMAP.VacioPanel;
import EDITOR.FX.KeyboardListener;
import EDITOR.FX.SpriteLoader;
import EDITOR.FX.SpriteUtils;
import EDITOR.SELECTPANEL.Celda;
import EDITOR.SELECTPANEL.GridPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
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

    public GUI() {
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
        createBotonGuardar(menuIzquierda);
        createBotonFondo(menuIzquierda);
        createLabelPincel();

        getContentPane().setBackground(Color.BLACK);
        setLayout(null);
        add(menuIzquierda);
        add(menuInferior);
        add(labelPanel);
        add(botonGuardar);
        add(botonFondo);
    }

    private VacioPanel menuIzquierda;
    private JPanel menuInferior;
    private JPanel labelPanel;
    //private JPanel menuMundo;
    public JLabel labelPincel;
    public JLabel isPressed;
    public JButton botonGuardar;
    private JButton botonFondo;


    public void createMenuIzquierda() {
        int[] nums = spriteUtils.numRowsCols();
        menuIzquierda = new VacioPanel(nums[0], nums[1], labelPincel);
        menuIzquierda.setBackground(Color.BLACK);
        menuIzquierda.setBounds(MENU_IZQUIERDA_X, MENU_IZQUIERDA_Y, MENU_IZQUIERDA_ANCHO, MENU_IZQUIERDA_ALTO);
    }

    public void createMenuInferior() {
        menuInferior = new GridPanel(4, 12, sprites);
        menuInferior.setBackground(Color.BLACK);
        menuInferior.setBounds(MENU_INFERIOR_X, MENU_INFERIOR_Y, MENU_INFERIOR_ANCHO, MENU_INFERIOR_ALTO);
    }

    public void createLabelPincel() {
        labelPanel = new JPanel();
        labelPanel.setBackground(Color.BLACK);

        labelPincel = new JLabel();
        labelPincel.setHorizontalAlignment(JLabel.CENTER);
        labelPincel.setVerticalAlignment(JLabel.CENTER);
        labelPincel.setIcon(Celda.escaladoImage(new ImageIcon("images/pincelBoton.png"),64,64));
        createFText();
        labelPanel.setBounds(ANCHO - 520, ALTO - 950, 300, 150);

        labelPanel.add(isPressed);
        labelPanel.add(labelPincel);
    }

    public void createFText() {
        isPressed = new JLabel();
        isPressed.setText("Presiona \"F\" para activar el modo pincel");
    }

    public void createBotonGuardar(VacioPanel panel) {
        botonGuardar = new JButton("Guardar");
        botonGuardar.setBackground(Color.BLACK);
        botonGuardar.setForeground(Color.WHITE);
        botonGuardar.setBounds(ANCHO - 620, ALTO - 500, 100, 50);
        botonGuardar.addActionListener((ActionEvent _) -> {
            try {
                botonGuardarClickeado(panel);
                System.exit(0);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void botonGuardarClickeado(VacioPanel panel) throws IOException {
        spriteUtils.generateSpriteMap(sprites, panel.formato);
    }

    public void createBotonFondo(VacioPanel panel) {
        botonFondo = new JButton("Fondo");
        botonFondo.setBackground(Color.BLACK);
        botonFondo.setForeground(Color.WHITE);
        botonFondo.setBounds(ANCHO - 620, ALTO - 550, 100, 50);
        botonFondo.addActionListener(e -> {
            try {
                fondoSprite(panel);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void fondoSprite(VacioPanel panel) throws IOException {
        for (ImageIcon[] row : panel.formato) {
            Arrays.fill(row, imageIcon);
        }
        for (CeldaVacia celda : panel.celdaVacias) {
            celda.setImageIconLocal(imageIcon,celda.getWidth()-15,celda.getHeight()-15);
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