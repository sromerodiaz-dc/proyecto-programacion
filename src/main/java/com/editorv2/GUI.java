package com.editorv2;

import com.editorv2.controller.TextureController;
import com.editorv2.model.MapModel;
import com.editorv2.util.MapData;
import com.editorv2.util.MapJsonHandler;
import com.editorv2.util.StartAction;
import com.editorv2.view.MapEditorPanel;
import com.editorv2.view.MiniMapView;
import com.editorv2.view.TilePalettePanel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import static com.editorv2.util.MapJsonHandler.showError;
import static com.editorv2.util.MapJsonHandler.showInformation;

public class GUI extends JFrame {

    private static final String CONFIG_PATH = "src/main/resources/tiles.json";
    private static final Dimension WINDOW_SIZE = new Dimension(1600, 900);
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final int RIGHT_PANEL_BORDER = 14;
    private static final double LEFT_PANEL_WEIGHT = 0.80;
    private static final double RIGHT_PANEL_WEIGHT = 0.20;

    private MapModel model;
    private MapEditorPanel editorPanel;
    private JScrollPane editorScroll;
    private MiniMapView miniMap;
    private TilePalettePanel palette;
    private TextureController textureController;
    private JPanel leftPanel; // Declare leftPanel as a class member
    private JPanel rightPanel; // Declare rightPanel as a class member

    public GUI() {
        initUI();
        StartAction action = showStartDialog();
        if (action != null) setupMap(action);
        else dispose();
    }

    private void initUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WINDOW_SIZE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new GridBagLayout());
    }

    private void setupMap(StartAction action) {
        getContentPane().removeAll();
        if (action.type == StartAction.ActionType.CREATE) {
            initMapComponents(action.rows, action.cols);
        } else {
            try {
                MapData data = MapJsonHandler.loadMapData(action.mapName);
                initMapComponents(data.rows(), data.cols());
                editorPanel.loadMapData(data.matrix());
            } catch (IOException | IllegalArgumentException e) {
                handleMapError("Error al cargar el mapa", e);
                return;
            }
        }
        initCommonComponents();
        layoutMainUI();
        refreshUI();
    }

    private GridBagConstraints createConstraints(int x, int y, double weightx, double weighty) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);
        return gbc;
    }

    private void initMapComponents(int rows, int cols) {
        model = new MapModel(rows, cols);
        textureController = new TextureController();
        editorPanel = new MapEditorPanel(model, textureController);
        editorScroll = new JScrollPane(editorPanel);
    }

    private void layoutMainUI() {
        add(createLeftPanel(), createConstraints(0, 1, LEFT_PANEL_WEIGHT, 0.75));
        add(createRightPanel(), createConstraints(1, 1, RIGHT_PANEL_WEIGHT, 0.25));
    }

    private void initCommonComponents() {
        JButton saveButton = new JButton("Guardar Mapa");
        saveButton.addActionListener(_ -> MapJsonHandler.saveMapData(model));

        // Agregar el botón con GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa ambas columnas
        gbc.insets = new Insets(5, 5, 5, 5); // Márgenes
        gbc.anchor = GridBagConstraints.NORTHWEST; // Posición
        add(saveButton, gbc); // Agregar al contenedor principal

        miniMap = new MiniMapView(model, editorScroll, textureController);
        palette = new TilePalettePanel(textureController, editorPanel);

        editorScroll.getViewport().addChangeListener(_ -> {
            Rectangle viewRect = editorScroll.getViewport().getViewRect();
            miniMap.setVisibleRect(viewRect);
        });
    }

    private JPanel createLeftPanel() {
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(editorScroll, BorderLayout.CENTER);
        setPanelStyle(leftPanel);
        return leftPanel;
    }

    private JPanel createRightPanel() {
        rightPanel = new JPanel(new BorderLayout(0, 10));
        JPanel miniMapWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        miniMapWrapper.setOpaque(false);
        miniMapWrapper.add(miniMap);

        rightPanel.add(miniMapWrapper, BorderLayout.NORTH);
        rightPanel.add(palette, BorderLayout.CENTER);
        rightPanel.setMinimumSize(new Dimension(miniMap.getPreferredSize().width + RIGHT_PANEL_BORDER, 0));
        setPanelStyle(rightPanel);
        return rightPanel;
    }

    private void setPanelStyle(JComponent panel) {
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void refreshUI() {
        revalidate();
        repaint();
        setVisible(true);
    }

    private StartAction showStartDialog() {
        Object[] options = {"Crear Mapa", "Cargar Mapa", "Cancelar"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "¿Qué desea hacer?",
                "Editor de Mapas",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            return handleCreateMapChoice();
        } else if (choice == 1) {
            return handleLoadMapChoice();
        } else {
            return null;
        }
    }

    private StartAction handleCreateMapChoice() {
        Object[] sizeOptions = {"25x25", "50x50", "75x75"};
        String sizeChoice = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el tamaño del mapa:",
                "Nuevo Mapa",
                JOptionPane.PLAIN_MESSAGE,
                null,
                sizeOptions,
                sizeOptions[0]
        );

        if (sizeChoice != null) {
            int rows = Integer.parseInt(sizeChoice.split("x")[0]);
            int cols = Integer.parseInt(sizeChoice.split("x")[1]);
            return new StartAction(StartAction.ActionType.CREATE, rows, cols);
        }
        return null;
    }

    private StartAction handleLoadMapChoice() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File(CONFIG_PATH));
            JsonNode mapsNode = root.path("maps");

            if (!mapsNode.isObject() || mapsNode.isEmpty()) {
                showInformation("No hay mapas guardados para cargar.");
                return null;
            }

            String[] mapOptions = StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(
                            mapsNode.fieldNames(),
                            Spliterator.ORDERED
                    ),
                    false
            ).toArray(String[]::new);

            String mapChoice = showMapSelectionDialog(mapOptions);

            return mapChoice != null ?
                    new StartAction(StartAction.ActionType.LOAD, mapChoice) :
                    null;

        } catch (IOException e) {
            handleLoadError(e);
            return null;
        }
    }

    private String showMapSelectionDialog(String[] mapOptions) {
        return (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el mapa que desea cargar:",
                "Cargar Mapa",
                JOptionPane.PLAIN_MESSAGE,
                null,
                mapOptions,
                mapOptions.length > 0 ? mapOptions[0] : null
        );
    }

    private void handleLoadError(IOException e) {
        String errorMessage = "Error al leer tiles.json: " + e.getMessage();
        showError(errorMessage);
        e.printStackTrace();
    }

    private void handleMapError(String context, Exception e) {
        showError(context + ": " + e.getMessage());
        e.printStackTrace();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}