package com.editorv2.view;

import com.editorv2.controller.TextureController;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TilePalettePanel extends JScrollPane {
    public TilePalettePanel(TextureController textureController, MapEditorPanel editorPanel) {
        JPanel panel = new JPanel(new GridLayout(0, 5, 2, 2));

        for (int id : textureController.getAllTextures().keySet()) {
            BufferedImage texture = textureController.getTexture(id);
            JButton btn = new JButton(new ImageIcon(texture.getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
            btn.addActionListener(e -> editorPanel.setSelectedTexture(id));
            panel.add(btn);
        }

        setViewportView(panel);
        setPreferredSize(new Dimension(300, 150));
    }
}