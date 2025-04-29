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
        palette.setPreferredSize(new Dimension(
                Toolkit.getDefaultToolkit().getScreenSize().width / 3,
                150
        ));

        // Layout Principal
        setLayout(new BorderLayout(10, 10)); // Espaciado entre componentes
        getContentPane().setBackground(Color.BLACK);

        // Panel Izquierdo (Editor)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(editorScroll, BorderLayout.CENTER);
        setPanelStyle(leftPanel);

        // Panel Derecho (Minimapa)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(miniMap, BorderLayout.NORTH);
        rightPanel.setPreferredSize(new Dimension(300, 300));
        setPanelStyle(rightPanel);

        // Ensamblado
        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(palette, BorderLayout.SOUTH);

        // Sincronización
        editorScroll.getViewport().addChangeListener(e -> {
            Rectangle viewRect = editorScroll.getViewport().getViewRect();
            miniMap.setVisibleRect(viewRect); // Sin offset
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