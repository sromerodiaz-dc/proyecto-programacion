package com.editorv2.view;

import com.editorv2.controller.TextureController;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class TilePalettePanel extends JScrollPane {
    private static final int BUTTON_SIZE = 64; // Tamaño base para los botones
    private static final int COLUMNAS = 5;     // Número de columnas
    private static final int BORDER_PADDING = 4;

    private final Map<JButton, Integer> buttonIdMap = new HashMap<>();

    public TilePalettePanel(TextureController textureController, MapEditorPanel editorPanel) {
        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, 0, 0);

        int x = 0, y = 0;
        for (int id : textureController.getAllTextures().keySet()) {
            JButton btn = crearBotonTextura(textureController.getTexture(id), id, editorPanel, textureController);
            gbc.gridx = x;
            gbc.gridy = y;
            gridPanel.add(btn, gbc);

            x++;
            if (x >= COLUMNAS) {
                x = 0;
                y++;
            }
        }

        setViewportView(gridPanel); // Establecer la vista primero
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        setPreferredSize(new Dimension(BUTTON_SIZE * COLUMNAS, 150));

        // Actualizar bordes después de configurar la vista
        actualizarBordesColisionables(textureController);
    }

    private JButton crearBotonTextura(BufferedImage texture, int textureId, MapEditorPanel editorPanel, TextureController textureController) {
        JButton btn = new JButton();
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(true);

        // Borde FIJO que reserva espacio para el máximo grosor (2px de línea + 2px de margen)
        Border fixedBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 2, 2, 2), // Margen fijo
                BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 2) // Borde transparente de 2px
        );
        btn.setBorder(fixedBorder); // Establecer borde inicial

        ImageIcon icon = new ImageIcon(texture.getScaledInstance(
                BUTTON_SIZE - BORDER_PADDING - 4, // Ajustar por el borde fijo (2px * 2)
                BUTTON_SIZE - BORDER_PADDING - 4,
                Image.SCALE_SMOOTH
        ));
        btn.setIcon(icon);

        btn.addActionListener(_ -> {
            if (editorPanel.isCollisionMode()) {
                textureController.toggleCollisionTexture(textureId);
                actualizarBordesColisionables(textureController);
            } else {
                editorPanel.setSelectedTexture(textureId);
                actualizarBordesSeleccion(textureId, textureController, editorPanel);
            }
        });

        buttonIdMap.put(btn, textureId);
        return btn;
    }

    // En TilePalettePanel.java

    // Metodo para actualizar bordes de colisión
    public void actualizarBordesColisionables(TextureController textureController) {
        SwingUtilities.invokeLater(() -> { // Ejecutar en el EDT
            Component[] components = ((JPanel) getViewport().getView()).getComponents();
            for (Component c : components) {
                if (c instanceof JButton b) {
                    int id = obtenerIdDelBoton(b);
                    Border borde = textureController.isTextureCollision(id)
                            ? BorderFactory.createLineBorder(Color.RED, 2)
                            : BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 2);
                    b.setBorder(borde);
                    b.repaint();
                }
            }
        });
    }

    // Metodo para actualizar borde de selección
    private void actualizarBordesSeleccion(int selectedId, TextureController textureController, MapEditorPanel editorPanel) {
        SwingUtilities.invokeLater(() -> { // Ejecutar en el EDT
            Component[] components = ((JPanel) getViewport().getView()).getComponents();
            for (Component c : components) {
                if (c instanceof JButton b) {
                    int id = obtenerIdDelBoton(b);
                    boolean esColision = textureController.isTextureCollision(id);
                    Border borde;

                    if (id == selectedId) {
                        borde = editorPanel.isCollisionMode()
                                ? BorderFactory.createLineBorder(new Color(255, 50, 0), 2)
                                : BorderFactory.createLineBorder(Color.YELLOW, 2);
                    } else {
                        borde = esColision
                                ? BorderFactory.createLineBorder(Color.RED, 2)
                                : BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 2);
                    }

                    b.setBorder(borde);
                }
            }
        });
    }

    private int obtenerIdDelBoton(JButton boton) {
        return buttonIdMap.getOrDefault(boton, -1); // Acceso directo
    }
}