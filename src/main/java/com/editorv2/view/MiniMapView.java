package com.editorv2.view;

import com.editorv2.controller.TextureController;
import com.editorv2.model.IModelChangeListener;
import com.editorv2.model.MapModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MiniMapView extends JPanel implements IModelChangeListener {
    private final MapModel model;
    private final JScrollPane mainScrollPane;
    private final TextureController textureController;
    private Rectangle visibleRect;
    private final float scale = 0.2f;

    public MiniMapView(MapModel model, JScrollPane scrollPane, TextureController textureController) {
        this.model = model;
        this.mainScrollPane = scrollPane;
        this.textureController = textureController;
        model.addListener(this);
        setPreferredSize(new Dimension(200, 200));
        setupDragListeners();
    }

    private void setupDragListeners() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                updateMainView(e.getX(), e.getY());
            }
        });
    }

    private void updateMainView(int x, int y) {
        JViewport viewport = mainScrollPane.getViewport();
        int newX = (int)(x / scale) - viewport.getWidth() / 2;
        int newY = (int)(y / scale) - viewport.getHeight() / 2;
        viewport.setViewPosition(new Point(newX, newY));
    }

    public void setVisibleRect(Rectangle viewRect) {
        this.visibleRect = new Rectangle(
                (int)(viewRect.x * scale),
                (int)(viewRect.y * scale),
                (int)(viewRect.width * scale),
                (int)(viewRect.height * scale)
        );
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Dibujar mapa
        g2d.scale(scale, scale);
        for (int row = 0; row < model.getRows(); row++) {
            for (int col = 0; col < model.getCols(); col++) {
                BufferedImage texture = textureController.getTexture(model.getTile(row, col));
                if (texture != null) {
                    g2d.drawImage(texture, col * 32, row * 32, null);
                }
            }
        }

        // Dibujar área visible
        if (visibleRect != null) {
            g2d.setColor(new Color(255, 0, 0, 100));
            g2d.fillRect(visibleRect.x, visibleRect.y, visibleRect.width, visibleRect.height);
        }
        g2d.dispose();
    }

    @Override
    public void onModelChanged() {
        repaint();
    }
}