package EDITOR.GUI;

import EDITOR.CELDAS.GridPanel;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    public GUI() {
        subcomponentes();
        setVisible(true);
    }

    public void subcomponentes(){
        // Propiedades
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);
        setSize(1280,720);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel contenedor = new JPanel(new GridLayout(1,1));

        GridPanel panelMapaSprite = new GridPanel(16,16);
        GridPanel panelSprite = new GridPanel(2,16);
        GridPanel panelMapa = new GridPanel(16,16);

        contenedor.add(panelMapaSprite);
        contenedor.add(panelSprite);
        contenedor.add(panelMapa);

        this.getContentPane().add(contenedor);
    }
}
