package com.editorv2.view;

import com.editorv2.controller.TextureController;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TilePalettePanel extends JScrollPane {
    private static final int BUTTON_SIZE = 64; // Tamaño fijo para los botones
    private static final int COLUMNAS = 5; // Número de columnas en la cuadrícula

    public TilePalettePanel(TextureController textureController, MapEditorPanel editorPanel) {
        // Panel principal con GridLayout ajustado
        JPanel gridPanel = new JPanel(new GridLayout(0, COLUMNAS, 0, 0)); // 0px de espacio
        gridPanel.setBackground(Color.BLACK);

        // Añadir botones para cada textura
        for (int id : textureController.getAllTextures().keySet()) {
            JButton btn = crearBotonTextura(textureController.getTexture(id), id, editorPanel);
            gridPanel.add(btn);
        }

        // Configurar el ScrollPane
        setViewportView(gridPanel);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        setPreferredSize(new Dimension(BUTTON_SIZE * COLUMNAS + 20, 150)); // Ajuste para bordes
    }

    private JButton crearBotonTextura(BufferedImage texture, int textureId, MapEditorPanel editorPanel) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        btn.setBackground(Color.BLACK);
        btn.setFocusPainted(false); // Eliminar resaltado de enfoque
        btn.setMargin(new Insets(0, 0, 0, 0)); // Eliminar márgenes internos

        // Escalar y centrar la imagen
        ImageIcon icon = new ImageIcon(
                texture.getScaledInstance(BUTTON_SIZE - 4, BUTTON_SIZE - 4, Image.SCALE_SMOOTH) // -4px para bordes
        );
        btn.setIcon(icon);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setVerticalAlignment(SwingConstants.CENTER);

        // Acción al seleccionar
        btn.addActionListener(e -> editorPanel.setSelectedTexture(textureId));

        return btn;
    }
}