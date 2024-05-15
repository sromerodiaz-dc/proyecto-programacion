package EDITOR.GUI;

import EDITOR.EMPTYMAP.VacioPanel;
import EDITOR.FX.SpriteLoader;
import EDITOR.FX.SpriteUtils;
import EDITOR.SELECTPANEL.GridPanel;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.*;
/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Esta clase define la interacción del jugador con el entorno asi como su movimiento y uso de gráficos en 2D.
 * */
public class GUI extends JFrame {
    // Propiedades de pantalla
    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;
    private static final int MENU_LEFT_X = 445;
    private static final int MENU_LEFT_Y = 120;
    private static final int MENU_LEFT_WIDTH = 864;
    private static final int MENU_LEFT_HEIGHT = 576;
    private static final int MENU_BOTTOM_X = 615;
    private static final int MENU_BOTTOM_Y = 730;
    private static final int MENU_BOTTOM_WIDTH = 534;
    private static final int MENU_BOTTOM_HEIGHT = 180;
    /*private static final int MENU_WORLD_X = 1020;
    private static final int MENU_WORLD_Y = 60;
    private static final int MENU_WORLD_WIDTH = 750;
    private static final int MENU_WORLD_HEIGHT = 730;*/

    SpriteLoader spriteLoader = new SpriteLoader();
    SpriteUtils spriteUtils;
    public ImageIcon[] sprites = spriteLoader.loadSprites("Assets/background").toArray(new ImageIcon[0]);

    public GUI() throws IOException {
        initGUI();
        setVisible(true);
        System.out.println(Arrays.toString(sprites));
    }

    private void initGUI() throws IOException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("TileEditor");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);

        createMenuLeft();
        createMenuBottom();
        //createMenuWorld();
        createSaveButton();

        getContentPane().setBackground(Color.BLACK);
        setLayout(null);
        add(menuLeft);
        add(menuBottom);
        //add(menuWorld);
        add(saveButton);
    }

    private JPanel menuLeft;
    private JPanel menuBottom;
    //private JPanel menuWorld;
    private JButton saveButton;

    private void createMenuLeft() {

        menuLeft = new VacioPanel();
        menuLeft.setBackground(Color.BLACK);
        menuLeft.setBounds(MENU_LEFT_X, MENU_LEFT_Y, MENU_LEFT_WIDTH, MENU_LEFT_HEIGHT);
    }

    private void createMenuBottom() throws IOException {
        menuBottom = new GridPanel(4, 12,sprites);
        menuBottom.setBackground(Color.BLACK);
        menuBottom.setBounds(MENU_BOTTOM_X, MENU_BOTTOM_Y, MENU_BOTTOM_WIDTH, MENU_BOTTOM_HEIGHT);
    }

    /*private void createMenuWorld() {
        menuWorld = new WorldMap();
        menuWorld.setBackground(Color.BLACK);
        menuWorld.setBounds(MENU_WORLD_X,MENU_WORLD_Y, MENU_WORLD_WIDTH,MENU_WORLD_HEIGHT);
    }*/

    private void createSaveButton() {
        saveButton = new JButton("Guardar");
        saveButton.setBackground(Color.BLACK);
        saveButton.setForeground(Color.WHITE);
        saveButton.setBounds(WIDTH - 550, HEIGHT - 500, 100, 50);
        saveButton.addActionListener(e -> {
            try {
                saveButtonClicked();
                System.exit(0);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void saveButtonClicked() throws IOException {
        spriteUtils = new SpriteUtils();
        spriteUtils.generateSpriteMap(sprites,VacioPanel.formato);
    }
}