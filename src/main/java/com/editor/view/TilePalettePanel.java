package com.editor.view;

import com.editor.controller.KeyboardController;
import com.editor.controller.TextureController;
import com.editor.model.MapModel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TilePalettePanel extends JScrollPane {
    private static final int BUTTON_SIZE = 64;
    private static final int COLUMNS = 5;
    private static final int BORDER_PADDING = 4;

    // Colores predefinidos para bordes
    private static final Color SELECTED_NORMAL = Color.YELLOW;
    private static final Color SELECTED_COLLISION = new Color(255, 50, 0); // Naranja
    private static final Color COLLISION_BORDER = Color.RED;
    private static final Color TRANSPARENT_BORDER = new Color(0, 0, 0, 0);

    private final Map<JButton, Integer> buttonIdMap = new HashMap<>();
    private final Map<JButton, Point> buttonPositionMap = new HashMap<>();
    private final List<Integer> textureIdsInOrder = new ArrayList<>();

    private int currentCol = 0;
    private int currentRow = 0;
    private final int totalRows;
    private final JPanel gridPanel;
    private final TextureController textureController;
    private final MapEditorPanel editorPanel;
    private final MapModel model;

    public TilePalettePanel(TextureController textureController, MapEditorPanel editorPanel, MapModel model) {
        this.textureController = textureController;
        this.editorPanel = editorPanel;
        this.model = model;

        gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(Color.BLACK);

        // Hacer el panel enfocable y prepararlo para recibir eventos
        gridPanel.setFocusable(true);
        gridPanel.setRequestFocusEnabled(true);

        // Depuración de foco
        gridPanel.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                System.out.println("[FOCUS] gridPanel ganó el foco");
            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("[FOCUS] gridPanel perdió el foco");
            }
        });

        textureIdsInOrder.addAll(textureController.getAllTextures().keySet());
        int textureCount = textureIdsInOrder.size();
        totalRows = (int) Math.ceil((double) textureCount / COLUMNS);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, 0, 0);

        int x = 0, y = 0;
        for (int id : textureIdsInOrder) {
            JButton btn = createTextureButton(textureController.getTexture(id), id);
            gbc.gridx = x;
            gbc.gridy = y;
            gridPanel.add(btn, gbc);
            buttonPositionMap.put(btn, new Point(x, y));

            x++;
            if (x >= COLUMNS) {
                x = 0;
                y++;
            }
        }

        if (!textureIdsInOrder.isEmpty()) {
            currentCol = 0;
            currentRow = 0;
            editorPanel.setSelectedTexture(textureIdsInOrder.get(0));
            updateSelectionBorders();
        }

        // Registrar el KeyListener
        KeyboardController keyboardController = new KeyboardController(this);
        gridPanel.addKeyListener(keyboardController);
        System.out.println("[DEBUG] KeyListener registrado en gridPanel");

        // Configurar el scroll pane para no robar foco
        setFocusable(false);
        getViewport().setFocusable(false);
        getViewport().setRequestFocusEnabled(false);

        // Forzar foco al hacer clic
        gridPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gridPanel.requestFocusInWindow();
                System.out.println("[FOCUS] Foco solicitado manualmente");
            }
        });

        // Solicitar foco inicial
        SwingUtilities.invokeLater(() -> {
            gridPanel.requestFocusInWindow();
            System.out.println("[FOCUS] Foco inicial solicitado");
        });

        setViewportView(gridPanel);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        setPreferredSize(new Dimension(BUTTON_SIZE * COLUMNS, 150));
    }

    private JButton createTextureButton(BufferedImage texture, int textureId) {
        JButton btn = new JButton();
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(true);

        // Borde base transparente
        Border fixedBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 2, 2, 2),
                BorderFactory.createLineBorder(TRANSPARENT_BORDER, 2)
        );
        btn.setBorder(fixedBorder);

        // Escalar imagen para el botón
        ImageIcon icon = new ImageIcon(texture.getScaledInstance(
                BUTTON_SIZE - BORDER_PADDING - 4,
                BUTTON_SIZE - BORDER_PADDING - 4,
                Image.SCALE_SMOOTH
        ));
        btn.setIcon(icon);

        btn.addActionListener(_ -> {
            Point pos = buttonPositionMap.get(btn);
            if (pos != null) {
                currentCol = pos.x;
                currentRow = pos.y;
            }

            if (editorPanel.isCollisionMode()) {
                textureController.toggleCollisionTexture(textureId, model);
            } else {
                editorPanel.setSelectedTexture(textureId);
            }
            updateSelectionBorders();
        });

        buttonIdMap.put(btn, textureId);
        return btn;
    }

    public void updateSelectionBorders() {
        SwingUtilities.invokeLater(() -> {
            int selectedId = editorPanel.getSelectedTexture();
            boolean collisionMode = editorPanel.isCollisionMode();

            for (Component c : gridPanel.getComponents()) {
                if (c instanceof JButton b) {
                    int id = buttonIdMap.getOrDefault(b, -1);
                    boolean isCollision = textureController.isTextureCollision(id);

                    Border border = determineBorder(id, selectedId, isCollision, collisionMode);
                    b.setBorder(border);
                }
            }
        });
    }

    private Border determineBorder(int id, int selectedId, boolean isCollision, boolean collisionMode) {
        if (id == selectedId) {
            return BorderFactory.createLineBorder(
                    collisionMode ? SELECTED_COLLISION : SELECTED_NORMAL, 3
            );
        } else if (isCollision) {
            return BorderFactory.createLineBorder(COLLISION_BORDER, 2);
        } else {
            return BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(2, 2, 2, 2),
                    BorderFactory.createLineBorder(TRANSPARENT_BORDER, 2)
            );
        }
    }

    public void navigateUp() {
        System.out.println("[DEBUG] Navegando hacia arriba");
        int newRow = (currentRow - 1 + totalRows) % totalRows;
        selectTextureAtPosition(currentCol, newRow);
    }

    public void navigateDown() {
        System.out.println("[DEBUG] Navegando hacia abajo");
        int newRow = (currentRow + 1) % totalRows;
        selectTextureAtPosition(currentCol, newRow);
    }

    public void navigateLeft() {
        System.out.println("[DEBUG] Navegando hacia izquierda");
        int newCol = (currentCol - 1 + COLUMNS) % COLUMNS;
        selectTextureAtPosition(newCol, currentRow);
    }

    public void navigateRight() {
        System.out.println("[DEBUG] Navegando hacia derecha");
        int newCol = (currentCol + 1) % COLUMNS;
        selectTextureAtPosition(newCol, currentRow);
    }

    private void selectTextureAtPosition(int col, int row) {
        System.out.println("[DEBUG] Seleccionando en posición: col=" + col + ", row=" + row);
        int index = row * COLUMNS + col;
        if (index < textureIdsInOrder.size()) {
            int textureId = textureIdsInOrder.get(index);
            currentCol = col;
            currentRow = row;

            if (editorPanel.isCollisionMode()) {
                System.out.println("[DEBUG] Modo colisión - Alternando textura: " + textureId);
                textureController.toggleCollisionTexture(textureId, model);
            } else {
                System.out.println("[DEBUG] Estableciendo textura seleccionada: " + textureId);
                editorPanel.setSelectedTexture(textureId);
            }
            updateSelectionBorders();
        } else {
            System.out.println("[DEBUG] Índice fuera de rango: " + index);
        }
    }
}