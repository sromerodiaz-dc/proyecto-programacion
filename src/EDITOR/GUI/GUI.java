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

        JPanel contenedor = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        GridPanel panelMapa = new GridPanel(10,10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        contenedor.add(panelMapa);

        GridPanel panelMapaSprites = new GridPanel(16,16);
        gbc.gridx = 2;
        gbc.gridy = 2;
        contenedor.add(panelMapaSprites);

        GridPanel panelSprites = new GridPanel(2,8);
        gbc.gridx = 0;
        gbc.gridy = 5;
        contenedor.add(panelSprites);

        contenedor.setBackground(Color.BLACK);
        add(contenedor);
    }
}
