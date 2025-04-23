package com.editorv2;

import com.editorv2.controller.TextureController;
import com.editorv2.model.IModelChangeListener;
import com.editorv2.model.MapModel;
import com.editorv2.view.MapEditorPanel;
import com.editorv2.view.MiniMapView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class GUI extends JFrame {
    private MapModel model;
    private MapEditorPanel editorPanel;
    private MiniMapView miniMapView;
    private JScrollPane scrollPane;
    private TextureController textureController;

    public GUI() {
        model = new MapModel(100, 100);
        textureController = new TextureController();
        editorPanel = new MapEditorPanel(model, textureController);

        // Crear JScrollPane primero
        scrollPane = new JScrollPane(editorPanel);

        // Pasar el JScrollPane al MiniMapView
        miniMapView = new MiniMapView(model, scrollPane);

        // Configurar layout
        setLayout(new BorderLayout());

        // Panel principal izquierdo
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel derecho con minimapa
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(miniMapView, BorderLayout.NORTH);

        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // Sincronización scroll-minimapa
        scrollPane.getViewport().addChangeListener(e -> {
            Rectangle viewRect = scrollPane.getViewport().getViewRect();
            miniMapView.setVisibleRect(viewRect);
        });

        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void exportMap(File file) throws IOException {
        try (PrintWriter pw = new PrintWriter(file)) {
            for (int[] row : model.getMatrix()) {
                String line = Arrays.stream(row)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(" "));
                pw.println(line);
            }
        }
    }
}


