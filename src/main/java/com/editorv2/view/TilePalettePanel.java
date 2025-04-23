package com.editorv2.view;

import com.editorv2.controller.TextureController;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class TilePalettePanel extends JPanel {
    private int selectedTextureId = 1;

    public TilePalettePanel(TextureController textureManager) {
        setLayout(new GridLayout(0, 5));
        Map<Integer, BufferedImage> textures = textureManager.getAllTextures();

        for (Map.Entry<Integer, BufferedImage> entry : textures.entrySet()) {
            JButton btn = new JButton(new ImageIcon(entry.getValue()));
            final int textureId = entry.getKey();
            btn.addActionListener(e -> selectedTextureId = textureId);
            add(btn);
        }
    }

    public int getSelectedTextureId() {
        return selectedTextureId;
    }
}