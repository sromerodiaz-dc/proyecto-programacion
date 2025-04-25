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

        // Layout
        setLayout(new BorderLayout());
        add(editorScroll, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(miniMap, BorderLayout.NORTH);
        add(rightPanel, BorderLayout.EAST);

        add(palette, BorderLayout.SOUTH);

        // Sincronización Scroll-MiniMapa
        editorScroll.getViewport().addChangeListener(e ->
                miniMap.setVisibleRect(editorScroll.getViewport().getViewRect())
        );

        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}