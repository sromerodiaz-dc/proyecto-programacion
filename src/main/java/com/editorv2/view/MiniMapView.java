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
    private final float scaleFactor = 0.1f; // Escala única
    private final int baseTileSize = 32;

    public MiniMapView(MapModel model, JScrollPane scrollPane, TextureController textureController) {
        this.model = model;
        this.mainScrollPane = scrollPane;
        this.textureController = textureController;
        model.addListener(this);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Primero el borde
        updatePreferredSize();
        setBackground(Color.BLACK);
        setupDragListeners();
    }

    private void updatePreferredSize() {
        // Tamaño base del contenido
        int scaledWidth = (int) (model.getCols() * baseTileSize * scaleFactor);
        int scaledHeight = (int) (model.getRows() * baseTileSize * scaleFactor);

        // Añadir espacio para el borde (2px en cada lado)
        Insets insets = getInsets();
        scaledWidth += insets.left + insets.right;
        scaledHeight += insets.top + insets.bottom;

        setPreferredSize(new Dimension(scaledWidth, scaledHeight));
    }

    private void setupDragListeners() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                updateMainView(e);
            }
        });
    }

    private void updateMainView(MouseEvent e) {
        JViewport viewport = mainScrollPane.getViewport();
        int rawX = (int)(e.getX() / scaleFactor);
        int rawY = (int)(e.getY() / scaleFactor);

        int targetX = rawX - viewport.getWidth() / 2;
        int targetY = rawY - viewport.getHeight() / 2;

        int maxX = model.getCols() * baseTileSize - viewport.getWidth();
        int maxY = model.getRows() * baseTileSize - viewport.getHeight();

        viewport.setViewPosition(new Point(
                Math.max(0, Math.min(targetX, maxX)),
                Math.max(0, Math.min(targetY, maxY))
        ));
    }

    public void setVisibleRect(Rectangle viewRect) {
        this.visibleRect = new Rectangle(
                (viewRect.x),
                (viewRect.y),
                (viewRect.width),
                (viewRect.height)
        );
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Fondo
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Tiles
        g2d.scale(scaleFactor, scaleFactor);
        for (int row = 0; row < model.getRows(); row++) {
            for (int col = 0; col < model.getCols(); col++) {
                BufferedImage texture = textureController.getTexture(model.getTile(row, col));
                if (texture != null) {
                    g2d.drawImage(texture, col * baseTileSize, row * baseTileSize, null);
                }
            }
        }

        // Rectángulo de vista
        if (visibleRect != null) {
            g2d.setColor(new Color(255, 165, 0, 150));
            g2d.fillRect(visibleRect.x, visibleRect.y, visibleRect.width, visibleRect.height);
        }
        g2d.dispose();
    }

    @Override
    public void onModelChanged() {
        repaint();
    }
}