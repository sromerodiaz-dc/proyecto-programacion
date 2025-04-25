package com.editorv2.view;

import com.editorv2.controller.TextureController;
import com.editorv2.model.IModelChangeListener;
import com.editorv2.model.MapModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MapEditorPanel extends JPanel implements IModelChangeListener {
    private final MapModel model;
    private final TextureController textureController;
    private int tileSize = 32;
    private int selectedTextureId = 1;

    public MapEditorPanel(MapModel model, TextureController textureController) {
        this.model = model;
        this.textureController = textureController;
        model.addListener(this);
        setupMouseListeners();
        setPreferredSize(new Dimension(model.getCols() * tileSize, model.getRows() * tileSize));
    }

    private void setupMouseListeners() {
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
        if (col >= 0 && col < model.getCols() && row >= 0 && row < model.getRows()) {
            model.setTile(row, col, selectedTextureId);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < model.getRows(); row++) {
            for (int col = 0; col < model.getCols(); col++) {
                BufferedImage texture = textureController.getTexture(model.getTile(row, col));
                if (texture != null) {
                    g.drawImage(texture, col * tileSize, row * tileSize, tileSize, tileSize, null);
                }
            }
        }
    }

    public void setSelectedTexture(int textureId) {
        this.selectedTextureId = textureId;
    }

    @Override
    public void onModelChanged() {
        repaint();
    }
}