package com.editorv2.view;

import com.editorv2.controller.TextureController;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TilePalettePanel extends JScrollPane {
    private static final int BUTTON_SIZE = 64; // Tamaño base para los botones
    private static final int COLUMNAS = 5;     // Número de columnas
    private static final int BORDER_PADDING = 4;

    public TilePalettePanel(TextureController textureController, MapEditorPanel editorPanel) {
        JPanel gridPanel = new JPanel(new GridBagLayout()); // Usar GridBagLayout
        gridPanel.setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST; // Alinear componentes a la izquierda
        gbc.insets = new Insets(0, 0, 0, 0);       // Sin márgenes entre botones

        int x = 0, y = 0;
        for (int id : textureController.getAllTextures().keySet()) {
            JButton btn = crearBotonTextura(textureController.getTexture(id), id, editorPanel);
            gbc.gridx = x;
            gbc.gridy = y;
            gridPanel.add(btn, gbc);

            x++;
            if (x >= COLUMNAS) { // Saltar a la siguiente fila al llegar al límite de columnas
                x = 0;
                y++;
            }
        }

        setViewportView(gridPanel);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        setPreferredSize(new Dimension(BUTTON_SIZE * COLUMNAS, 150));
    }

    private JButton crearBotonTextura(BufferedImage texture, int textureId, MapEditorPanel editorPanel) {
        JButton btn = new JButton();
        // Escalar la imagen al tamaño deseado (sin bordes forzados)
        ImageIcon icon = new ImageIcon(texture.getScaledInstance(
                BUTTON_SIZE - BORDER_PADDING,
                BUTTON_SIZE - BORDER_PADDING,
                Image.SCALE_SMOOTH
        ));
        btn.setIcon(icon);
        btn.setPreferredSize(new Dimension(
                icon.getIconWidth() + BORDER_PADDING,
                icon.getIconHeight() + BORDER_PADDING
        ));
        btn.setBackground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.addActionListener(_ -> editorPanel.setSelectedTexture(textureId));
        return btn;
    }
}