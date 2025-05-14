package com.editorv2;

import com.editorv2.controller.TextureController;
import com.editorv2.model.MapModel;
import com.editorv2.view.MapEditorPanel;
import com.editorv2.view.MiniMapView;
import com.editorv2.view.TilePalettePanel;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

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
        if (action != null) {
            setupMap(action);
        } else {
            dispose();
        }
    }

    private void initUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WINDOW_SIZE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new GridBagLayout());
    }

    private void setupMap(StartAction action) {
        if (action.type == StartAction.ActionType.CREATE) {
            createMap(action.rows, action.cols);
        } else if (action.type == StartAction.ActionType.LOAD) {
            loadMap(action.mapName);
        }
    }

    private void createMap(int rows, int cols) {
        if (model != null) {
            // Clear existing components
            getContentPane().removeAll();
        }

        model = new MapModel(rows, cols);
        textureController = new TextureController();
        editorPanel = new MapEditorPanel(model, textureController);
        editorScroll = new JScrollPane(editorPanel);
        initCommonComponents();
        layoutMainUI();
        revalidate();
        repaint();
        setVisible(true);
    }

    private void loadMap(String mapName) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File(CONFIG_PATH));
            JsonNode mapNode = root.path("maps").path(mapName);
            validateMapNode(mapNode, mapName);

            int width = mapNode.path("width").asInt();
            int height = mapNode.path("height").asInt();
            int[][] data = extractMapData(mapNode, height, width);

            if (model != null) {
                // Clear existing components
                getContentPane().removeAll();
            }

            model = new MapModel(width, height);
            textureController = new TextureController();
            editorPanel = new MapEditorPanel(model, textureController);
            editorScroll = new JScrollPane(editorPanel);
            editorPanel.loadMapData(data);
            initCommonComponents();
            layoutMainUI();
            revalidate();
            repaint();
            setVisible(true);

        } catch (IOException e) {
            showError("Error al cargar el mapa: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void initCommonComponents() {
        JButton saveButton = new JButton("Guardar Mapa");
        saveButton.addActionListener(_ -> saveMap(model));

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

    private void layoutMainUI() {
        leftPanel = createLeftPanel();
        rightPanel = createRightPanel();

        // Restricciones para leftPanel
        GridBagConstraints leftConstraints = new GridBagConstraints();
        leftConstraints.gridx = 0;
        leftConstraints.gridy = 1; // gridy = 1 para estar debajo del botón
        leftConstraints.gridwidth = 1;
        leftConstraints.gridheight = 1;
        leftConstraints.weightx = LEFT_PANEL_WEIGHT;
        leftConstraints.weighty = 0.75;
        leftConstraints.fill = GridBagConstraints.BOTH;
        add(leftPanel, leftConstraints);

        // Restricciones para rightPanel
        GridBagConstraints rightConstraints = new GridBagConstraints();
        rightConstraints.gridx = 1;
        rightConstraints.gridy = 1; // gridy = 1 para estar debajo del botón
        rightConstraints.gridwidth = 1;
        rightConstraints.gridheight = 1;
        rightConstraints.weightx = RIGHT_PANEL_WEIGHT;
        rightConstraints.weighty = 0.25;
        rightConstraints.fill = GridBagConstraints.BOTH;
        add(rightPanel, rightConstraints);
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

    private void saveMap(MapModel model) {
        String mapName = JOptionPane.showInputDialog(this, "Ingrese el nombre del mapa:");
        if (mapName == null || mapName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre de mapa inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File configFile = new File(CONFIG_PATH);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootObj = mapper.readTree(configFile);
            ObjectNode mapsNode = (ObjectNode) rootObj.path("maps");

            // Crear el nodo del mapa
            ObjectNode mapData = mapper.createObjectNode();
            mapData.put("width", model.getCols());
            mapData.put("height", model.getRows());

            // Crear el nodo de capas (por ahora, solo "ground")
            ArrayNode layers = mapper.createArrayNode();
            ObjectNode groundLayer = mapper.createObjectNode();
            groundLayer.put("name", "ground");
            groundLayer.put("type", "tilelayer");
            groundLayer.put("width", model.getCols());
            groundLayer.put("height", model.getRows());

            // Convertir la matriz del modelo a un ArrayNode
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

            // Configurar el pretty printer para formatear el JSON
            DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
            prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
            prettyPrinter.indentObjectsWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);

            // Escribir el JSON formateado
            try (FileWriter fileWriter = new FileWriter(configFile)) {
                String jsonString = mapper.writer(prettyPrinter).writeValueAsString(rootObj);

                // Use regex to replace newlines within the "data" array elements
                Pattern pattern = Pattern.compile("\\[\\s*(\\d+\\s*,\\s*\\d+.*?)\\s*\\]", Pattern.DOTALL);
                Matcher matcher = pattern.matcher(jsonString);
                while (matcher.find()) {
                    String rowData = matcher.group(1).replaceAll("\\s*,\\s*", ", "); // Remove extra spaces
                    jsonString = jsonString.replace(matcher.group(0), "[" + rowData + "]");
                }

                fileWriter.write(jsonString);
            }

            JOptionPane.showMessageDialog(null, "Mapa guardado correctamente en tiles.json");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el mapa: " + e.getMessage());
            e.printStackTrace();
        }
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

    private void validateMapNode(JsonNode mapNode, String mapName) {
        if (mapNode.isMissingNode()) {
            throw new IllegalArgumentException("Mapa no encontrado: " + mapName);
        }
    }

    private int[][] extractMapData(JsonNode mapNode, int rows, int cols) {
        return StreamSupport.stream(mapNode.path("layers").spliterator(), false)
                .findFirst()
                .map(layer -> layer.path("data"))
                .map(dataNode -> parseDataMatrix(dataNode, rows, cols))
                .orElseThrow(() -> new IllegalArgumentException("Estructura de mapa inválida"));
    }

    private int[][] parseDataMatrix(JsonNode dataNode, int rows, int cols) {
        return IntStream.range(0, rows)
                .mapToObj(row -> parseRow(dataNode.get(row), cols))
                .toArray(int[][]::new);
    }

    private int[] parseRow(JsonNode rowNode, int cols) {
        return IntStream.range(0, cols)
                .map(col -> rowNode.get(col).asInt())
                .toArray();
    }

    private void showInformation(String message) {
        JOptionPane.showMessageDialog(null, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }

    private static class StartAction {
        enum ActionType {CREATE, LOAD}

        ActionType type;
        int rows;
        int cols;
        String mapName;

        StartAction(ActionType type, int rows, int cols) {
            this.type = type;
            this.rows = rows;
            this.cols = cols;
        }

        StartAction(ActionType type, String mapName) {
            this.type = type;
            this.mapName = mapName;
        }
    }
}