package EDITOR.GUI;

import EDITOR.EMPTYMAP.VacioPanel;
import EDITOR.SELECTPANEL.GridPanel;
import EDITOR.WORLD.WorldMap;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.*;

public class GUI extends JFrame {
    // Propiedades de pantalla
    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;
    private static final int MENU_LEFT_X = 90;
    private static final int MENU_LEFT_Y = 60;
    private static final int MENU_LEFT_WIDTH = 750;
    private static final int MENU_LEFT_HEIGHT = 730;
    private static final int MENU_BOTTOM_X = 90;
    private static final int MENU_BOTTOM_Y = 820;
    private static final int MENU_BOTTOM_WIDTH = 750;
    private static final int MENU_BOTTOM_HEIGHT = 95;
    private static final int MENU_WORLD_X = 820;
    private static final int MENU_WORLD_Y = 60;
    private static final int MENU_WORLD_WIDTH = 750;
    private static final int MENU_WORLD_HEIGHT = 730;


    public GUI() throws IOException {
        initGUI();
        setVisible(true);
    }

    private void initGUI() throws IOException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("TileEditor");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);

        createMenuLeft();
        createMenuBottom();
        createMenuWorld();

        getContentPane().setBackground(Color.BLACK);
        setLayout(null);
        add(menuLeft);
        add(menuBottom);
        add(menuWorld);
    }

    private JPanel menuLeft;
    private JPanel menuBottom;
    private JPanel menuWorld;

    private void createMenuLeft() {
        menuLeft = new VacioPanel();
        menuLeft.setBackground(Color.BLACK);
        menuLeft.setBounds(MENU_LEFT_X, MENU_LEFT_Y, MENU_LEFT_WIDTH, MENU_LEFT_HEIGHT);
    }

    private void createMenuBottom() throws IOException {
        menuBottom = new GridPanel(2, 16);
        menuBottom.setBackground(Color.BLACK);
        menuBottom.setBounds(MENU_BOTTOM_X, MENU_BOTTOM_Y, MENU_BOTTOM_WIDTH, MENU_BOTTOM_HEIGHT);
    }

    private void createMenuWorld() {
        menuWorld = new WorldMap();
        menuWorld.setBackground(Color.BLACK);
        menuWorld.setBounds(MENU_WORLD_X,MENU_WORLD_Y, MENU_WORLD_WIDTH,MENU_WORLD_HEIGHT);
    }
}