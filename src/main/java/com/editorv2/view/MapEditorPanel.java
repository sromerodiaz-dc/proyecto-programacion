package com.editorv2.view;

import com.editorv2.controller.TextureController;
import com.editorv2.model.IModelChangeListener;
import com.editorv2.model.MapModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MapEditorPanel extends JPanel implements IModelChangeListener {
    private final MapModel model;
    private final int tileSize = 32;
    private int currentTextureId = 0;
    private final TextureController textureController;

    public MapEditorPanel(MapModel model, TextureController textureController) {
        this.model = model;
        this.textureController = textureController;
        model.addListener(this);
        // Configurar eventos del mouse
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                paintTile(e);
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                paintTile(e);
            }
        });
    }

    private void paintTile(MouseEvent e) {
        int col = e.getX() / tileSize;
        int row = e.getY() / tileSize;
        model.setTile(row, col, currentTextureId);
    }

    @Override
    public void onModelChanged() {
        repaint(); // Actualizar vista cuando cambia el modelo
    }

    @Override
    protected void paintComponent(Graphics g) {
        for (int row = 0; row < model.getRow(); row++) {
            for (int col = 0; col < model.getCol(); col++) {
                int textureId = model.getTile(row, col);
                BufferedImage img = textureController.getTexture(textureId);
                g.drawImage(img, col * tileSize, row * tileSize, null);
            }
        }
    }
}