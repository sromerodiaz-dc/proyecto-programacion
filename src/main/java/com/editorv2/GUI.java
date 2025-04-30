package com.editorv2;

import com.editorv2.controller.TextureController;
import com.editorv2.model.MapModel;
import com.editorv2.view.MapEditorPanel;
import com.editorv2.view.MiniMapView;
import com.editorv2.view.TilePalettePanel;
import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    public GUI() {
        MapModel model = new MapModel(100, 100);
        TextureController textureController = new TextureController();
        MapEditorPanel editorPanel = new MapEditorPanel(model, textureController);
        JScrollPane editorScroll = new JScrollPane(editorPanel);

        // Componentes
        MiniMapView miniMap = new MiniMapView(model, editorScroll, textureController);
        TilePalettePanel palette = new TilePalettePanel(textureController, editorPanel);

        // Calcular ancho del panel derecho (minimapa + bordes)
        int miniMapWidth = miniMap.getPreferredSize().width;
        int rightPanelBorder = 14; // 7px por lado (borde blanco + padding)
        int rightPanelWidth = miniMapWidth + rightPanelBorder;

        // Layout Principal usando GridBagLayout para proporciones exactas
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();

        // Panel Izquierdo (Editor: 50% ancho, 75% alto)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(editorScroll, BorderLayout.CENTER);
        setPanelStyle(leftPanel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 3; // 75% altura (3 partes de 4)
        gbc.weightx = 0.5;  // 50% ancho
        gbc.weighty = 0.75; // 75% altura
        gbc.fill = GridBagConstraints.BOTH;
        add(leftPanel, gbc);

        // Panel Derecho (Minimapa + Paleta: 50% ancho, 25% alto)
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.add(miniMap, BorderLayout.NORTH);
        rightPanel.add(palette, BorderLayout.CENTER);
        rightPanel.setPreferredSize(new Dimension(rightPanelWidth, 0)); // Ancho dinámico
        setPanelStyle(rightPanel);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 4; // 25% altura (1 parte de 4)
        gbc.weighty = 0.25;
        add(rightPanel, gbc);

        // Sincronización y configuración de la ventana
        editorScroll.getViewport().addChangeListener(_ -> {
            Rectangle viewRect = editorScroll.getViewport().getViewRect();
            miniMap.setVisibleRect(viewRect);
        });

        setSize(1600, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setPanelStyle(JComponent panel) {
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}