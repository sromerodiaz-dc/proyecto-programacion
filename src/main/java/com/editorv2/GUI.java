package com.editorv2;

import com.editorv2.controller.TextureController;
import com.editorv2.model.MapModel;
import com.editorv2.view.MapEditorPanel;
import com.editorv2.view.MiniMapView;
import com.editorv2.view.TilePalettePanel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class GUI extends JFrame {

    public GUI() {
        MapModel model = new MapModel(70, 70);
        TextureController textureController = new TextureController();
        MapEditorPanel editorPanel = new MapEditorPanel(model, textureController);
        JScrollPane editorScroll = new JScrollPane(editorPanel);

        // Botón de guardado
        JButton saveButton = new JButton("Guardar Mapa");
        saveButton.addActionListener(e -> saveMap(model));
        add(saveButton, BorderLayout.NORTH);

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
        gbc.weightx = 0.80;  // 50% ancho
        gbc.weighty = 0.75; // 75% altura
        gbc.fill = GridBagConstraints.BOTH;
        add(leftPanel, gbc);

        // Wrapper para centrar el minimapa
        JPanel miniMapWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        miniMapWrapper.setOpaque(false); // Para mantener fondo negro si es necesario
        miniMapWrapper.add(miniMap);

        // Panel Derecho (Minimapa + Paleta: 50% ancho, 25% alto)
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.add(miniMapWrapper, BorderLayout.NORTH);
        rightPanel.add(palette, BorderLayout.CENTER);
        //rightPanel.setPreferredSize(new Dimension(rightPanelWidth, 0)); // Ancho dinámico
        rightPanel.setMinimumSize(new Dimension(rightPanelWidth,0));
        setPanelStyle(rightPanel);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 4; // 25% altura (1 parte de 4)
        gbc.weightx = 0.20;
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

    private void saveMap(MapModel model) {
        File configFile = new File("src/main/resources/tiles.json");
        if (!configFile.exists()) {
            JOptionPane.showMessageDialog(null, "tiles.json no encontrado.");
            return;
        }

        String mapName = JOptionPane.showInputDialog(null, "Nombre del mapa:");
        if (mapName == null || mapName.trim().isEmpty()) return;

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(configFile);
            ObjectNode rootObj = (ObjectNode) root;

            ObjectNode mapsNode = rootObj.with("maps");

            if (mapsNode.has(mapName)) {
                int option = JOptionPane.showConfirmDialog(null,
                        "El mapa ya existe. ¿Desea sobreescribirlo?",
                        "Confirmar sobreescritura",
                        JOptionPane.YES_NO_OPTION);
                if (option != JOptionPane.YES_OPTION) return;
            }

            ObjectNode mapData = mapper.createObjectNode();
            mapData.put("width", model.getCols());
            mapData.put("height", model.getRows());

            ArrayNode layers = mapper.createArrayNode();
            ObjectNode groundLayer = mapper.createObjectNode();
            groundLayer.put("name", "ground");

            ArrayNode dataArray = mapper.createArrayNode();
            int[][] matrix = model.getMatrixForExport();
            for (int[] row : matrix) {
                ArrayNode rowArray = mapper.createArrayNode();
                for (int val : row) {
                    rowArray.add(val);
                }
                dataArray.add(rowArray);
            }
            groundLayer.set("data", dataArray);
            layers.add(groundLayer);

            mapData.set("layers", layers);
            mapsNode.set(mapName, mapData);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(writer, rootObj);
            }

            JOptionPane.showMessageDialog(null, "Mapa guardado correctamente en tiles.json");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el mapa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}