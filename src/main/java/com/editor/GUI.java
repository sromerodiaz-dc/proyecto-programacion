package com.editor;

import com.editor.controller.KeyboardController;
import com.editor.controller.TextureController;
import com.editor.model.MapModel;
import com.editor.model.event.EventMode;
import com.editor.model.record.CeldaCoord;
import com.editor.model.record.MapData;
import com.editor.util.MapJsonHandler;
import com.editor.util.StartAction;
import com.editor.view.MapEditorPanel;
import com.editor.view.MiniMapView;
import com.editor.view.TilePalettePanel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import static com.editor.util.MapJsonHandler.showError;
import static com.editor.util.MapJsonHandler.showInformation;

public class GUI extends JFrame {
    //TODO boton para pintar todo el fondo (util para escenarios de exterior)
    //TODO añadir bloques de sprites (Ejemplo: dibujar un edificio requiere 6 tiles diferentes, pues automatizarlo)
    //TODO arreglar colisiones. Que se guarden en el array al momento de seleccionar un tile y pintar los tiles ya pintados.
    // Que se puedan quitar colisiones con clic derecho habiendo seleccionado antes el boton de colision mode
    // Las colisiones que se quiten que se pinten de manera normal de nuevo
    // Si un tile X es pintado y luego seleccionado como colisionable:
    // - Los tiles pintados se pintarán como colisionables
    // - Los tiles repintados como colisionables son individuales e independientes, esto quiere decir que se
    // puede quitar el modo colisionable haciendo clic derecho en edicion modo colision
    //TODO teclado aplicado al programa para no tener que hacer clic para nada
    private static final String CONFIG_PATH = "src/main/resources/graphic/tiles.json";
    private static final Dimension WINDOW_SIZE = new Dimension(1600, 900);
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final int RIGHT_PANEL_BORDER = 14;
    private static final double LEFT_PANEL_WEIGHT = 0.80;
    private static final double RIGHT_PANEL_WEIGHT = 0.20;

    private JButton complexEventButton;
    private JButton collisionButton;

    private MapModel model;
    private MapEditorPanel editorPanel;
    private JScrollPane editorScroll;
    private MiniMapView miniMap;
    private TilePalettePanel palette;
    private TextureController textureController;

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
                // Usar dimensiones del JSON
                initMapComponents(data.rows(), data.cols());

                editorPanel.loadMapData(data.matrix(), data.collisions());

                // Restaurar colisiones correctamente
                for (CeldaCoord coord : data.collisions()) {
                    model.addCollision(coord.row(), coord.col());
                }

                model.setPlayerSpawn(data.playerSpawn());
                model.addAllEntitySpawn(data.spawnEvent());
                model.getTeleports().addAll(data.teleports());
            } catch (IOException | IllegalArgumentException e) {
                handleMapError("Error al cargar el mapa", e);
                return;
            }
        }
        initCommonComponents();
        layoutMainUI();
        refreshUI();
    }

    private GridBagConstraints createConstraints(int x, double weightx, double weighty) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = 1;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);
        return gbc;
    }

    private void initMapComponents(int rows, int cols) {
        textureController = new TextureController();
        model = new MapModel(rows, cols, textureController);
        editorPanel = new MapEditorPanel(model, textureController);
        editorScroll = new JScrollPane(editorPanel);
    }

    private void layoutMainUI() {
        add(createLeftPanel(), createConstraints(0, LEFT_PANEL_WEIGHT, 0.75));
        add(createRightPanel(), createConstraints(1, RIGHT_PANEL_WEIGHT, 0.25));
    }

    private void initCommonComponents() {
        // Agregar el panel de botones al contenedor principal
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa ambas columnas
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        miniMap = new MiniMapView(model, editorScroll, textureController);
        palette = new TilePalettePanel(textureController, editorPanel, model);

        KeyboardController keyboardController = new KeyboardController(palette, editorPanel);
        editorPanel.addKeyListener(keyboardController);
        editorPanel.setFocusable(true);

        // Panel para botones (Guardar, Colisión, Eventos)
        JPanel buttonPanel = getButtonPanel(palette);
        add(buttonPanel, gbc);

        editorScroll.getViewport().addChangeListener(_ -> {
            Rectangle viewRect = editorScroll.getViewport().getViewRect();
            miniMap.setVisibleRect(viewRect);
        });
    }

    private JPanel getButtonPanel(TilePalettePanel tilePalettePanel) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setOpaque(false);

        JButton saveButton = new JButton("Guardar Mapa");
        collisionButton = new JButton("Colisión"); // Usar variable de instancia
        JButton eventButton = new JButton("Eventos");
        complexEventButton = new JButton("Eventos Complejos"); // Usar variable de instancia

        complexEventButton.addActionListener(_ -> handleComplexEventAction());
        saveButton.addActionListener(_ -> MapJsonHandler.saveMapData(model));
        collisionButton.addActionListener(_ -> handleCollisionAction(tilePalettePanel)); // Eliminar parámetro
        eventButton.addActionListener(_ -> handleEventAction());

        buttonPanel.add(saveButton);
        buttonPanel.add(collisionButton);
        buttonPanel.add(eventButton);
        buttonPanel.add(complexEventButton);
        return buttonPanel;
    }

    private void handleComplexEventAction() {
        // Corregir lógica de toggle
        boolean newState = !editorPanel.isComplexEventMode();
        editorPanel.setEventMode(newState);
        complexEventButton.setBackground(newState ? Color.GREEN : null);
        collisionButton.setBackground(null);
    }

    private void handleCollisionAction(TilePalettePanel tilePalettePanel) {
        editorPanel.setCollisionMode(!editorPanel.isCollisionMode());
        collisionButton.setBackground(editorPanel.isCollisionMode() ? Color.RED : null);
        complexEventButton.setBackground(null); // Desactivar botón complejo
        editorPanel.setSelectedTexture(-1);
        tilePalettePanel.updateSelectionBorders();
    }

    private void handleEventAction() {
        // Cargar IDs de entidades desde JSON
        ArrayList<String> entityIds = loadEntityIds();
        entityIds.remove("player"); // Excluir jugador
        complexEventButton.setBackground(null);

        // Crear diálogo de eventos
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Seleccione tipo de evento:"));

        JButton playerSpawnBtn = new JButton("Spawn del jugador");
        JButton entitySpawnBtn = new JButton("Spawn de entidades");
        JButton teleportBtn = new JButton("Teleport");

        panel.add(playerSpawnBtn);
        panel.add(entitySpawnBtn);
        panel.add(teleportBtn);

        JDialog dialog = new JDialog(this, "Eventos", true);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);

        // Manejar acciones
        playerSpawnBtn.addActionListener(_ -> {
            editorPanel.setEventMode(EventMode.PLAYER_SPAWN);
            dialog.dispose();
        });

        entitySpawnBtn.addActionListener(_ -> {
            if (entityIds.isEmpty()) {
                showInformation("No hay entidades disponibles");
                return;
            }

            String selected = (String) JOptionPane.showInputDialog(
                    this,
                    "Seleccione entidad:",
                    "Spawn de entidades",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    entityIds.toArray(),
                    entityIds.getFirst()
            );

            if (selected != null) {
                editorPanel.setEventMode(EventMode.ENTITY_SPAWN, selected);
                dialog.dispose();
            }
        });

        teleportBtn.addActionListener(_ -> {
            editorPanel.setEventMode(EventMode.TELEPORT_SOURCE);
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    private ArrayList<String> loadEntityIds() {
        ArrayList<String> ids = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File("src/main/resources/data/entity/entity.json"));

            if (root.isArray()) {
                for (JsonNode entity : root) {
                    if (!entity.path("id").asText().equals("player")) {
                        ids.add(entity.path("id").asText());
                    }
                }
            } else {
                showError("El archivo JSON no contiene un array de entidades.");
            }
        } catch (IOException e) {
            showError("Error cargando entidades: " + e.getMessage());
        }
        return ids;
    }

    private JPanel createLeftPanel() {
        // Declare leftPanel as a class member
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(editorScroll, BorderLayout.CENTER);
        setPanelStyle(leftPanel);
        return leftPanel;
    }

    private JPanel createRightPanel() {
        // Declare rightPanel as a class member
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
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
        File mapsDir = new File("src/main/resources/data/maps");
        if (!mapsDir.exists() || !mapsDir.isDirectory()) {
            showInformation("No hay mapas guardados para cargar.");
            return null;
        }

        // Listar archivos .json en el directorio
        File[] mapFiles = mapsDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
        if (mapFiles == null || mapFiles.length == 0) {
            showInformation("No hay mapas guardados para cargar.");
            return null;
        }

        // Extraer nombres sin extensión
        String[] mapOptions = Arrays.stream(mapFiles)
                .map(File::getName)
                .map(name -> name.substring(0, name.lastIndexOf('.')))
                .toArray(String[]::new);

        String mapChoice = showMapSelectionDialog(mapOptions);

        return mapChoice != null ?
                new StartAction(StartAction.ActionType.LOAD, mapChoice) :
                null;
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
    }

    private void handleMapError(String context, Exception e) {
        showError(context + ": " + e.getMessage());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}