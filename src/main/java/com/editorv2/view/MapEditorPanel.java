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
    private final int tileSize = 32;
    private int selectedTextureId = 1;

    public MapEditorPanel(MapModel model, TextureController textureController) {
        this.model = model;
        this.textureController = textureController;
        setDoubleBuffered(true); // Habilita doble buffer
        model.addListener(this);
        setupMouseListeners();
        setPreferredSize(new Dimension(model.getCols() * tileSize, model.getRows() * tileSize));
    }

    private void setupMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    deleteTile(e);
                } else {
                    paintTile(e);
                }
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

    private void deleteTile(MouseEvent e) {
        int col = e.getX() / tileSize;
        int row = e.getY() / tileSize;
        if (col >= 0 && col < model.getCols() && row >= 0 && row < model.getRows()) {
            model.setTile(row, col, 9); // Establecer ID 0 (sprite por defecto)
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Fondo general del panel
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Dibujar celdas con bordes
        for (int row = 0; row < model.getRows(); row++) {
            for (int col = 0; col < model.getCols(); col++) {
                // Coordenadas de la celda
                int x = col * tileSize;
                int y = row * tileSize;

                // Fondo negro para la celda
                g2d.setColor(Color.BLACK);
                g2d.fillRect(x, y, tileSize, tileSize);

                // Textura (si existe)
                BufferedImage texture = textureController.getTexture(model.getTile(row, col));
                if (texture != null) {
                    g2d.drawImage(texture, x, y, tileSize, tileSize, null);
                }

                // Borde blanco
                g2d.setColor(Color.WHITE);
                g2d.drawRect(x, y, tileSize, tileSize);
            }
        }
        g2d.dispose();
    }

    static void textureForTile(Graphics g, MapModel model, TextureController textureController, int tileSize) {
        for (int row = 0; row < model.getRows(); row++) {
            for (int col = 0; col < model.getCols(); col++) {
                BufferedImage texture = textureController.getTexture(model.getTile(row, col));
                if (texture != null) {
                    g.drawImage(texture, col * tileSize, row * tileSize, tileSize, tileSize, null);
                }
            }
        }
    }

    public int getTileSize() {
        return tileSize;
    }

    public void setSelectedTexture(int textureId) {
        this.selectedTextureId = textureId;
    }

    @Override
    public void onModelChanged() {
        repaint();
    }
}