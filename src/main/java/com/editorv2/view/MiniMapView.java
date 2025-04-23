package com.editorv2.view;

import com.editorv2.model.IModelChangeListener;
import com.editorv2.model.MapModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MiniMapView extends JPanel implements IModelChangeListener {
    private final MapModel model;
    private final JScrollPane mainScrollPane;
    private Rectangle visibleRect;
    private final float scaleFactor = 0.2f;

    public MiniMapView(MapModel model, JScrollPane mainScrollPane) {
        this.model = model;
        this.mainScrollPane = mainScrollPane;
        model.addListener(this);
        setupDragListener();
        setPreferredSize(new Dimension(200, 200));
    }

    private void setupDragListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                updateMainViewPosition(e);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                updateMainViewPosition(e);
            }
        });
    }

    private void updateMainViewPosition(MouseEvent e) {
        JViewport viewport = mainScrollPane.getViewport();
        int viewWidth = viewport.getWidth();
        int viewHeight = viewport.getHeight();

        // Calcular nueva posición centrada
        int x = (int) (e.getX() / scaleFactor) - viewWidth / 2;
        int y = (int) (e.getY() / scaleFactor) - viewHeight / 2;

        // Limitar coordenadas a los bordes del mapa
        x = Math.max(0, Math.min(x, model.getCol() * 32 - viewWidth));
        y = Math.max(0, Math.min(y, model.getRow() * 32 - viewHeight));

        viewport.setViewPosition(new Point(x, y));
    }

    public void setVisibleRect(Rectangle viewRect) {
        this.visibleRect = new Rectangle(
                (int)(viewRect.x * scaleFactor),
                (int)(viewRect.y * scaleFactor),
                (int)(viewRect.width * scaleFactor),
                (int)(viewRect.height * scaleFactor)
        );
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Dibujar mapa completo escalado
        g2d.scale(scaleFactor, scaleFactor);
        editorPanel.paintMiniMap(g2d);  // Necesitarías implementar este método en MapEditorPanel

        // Dibujar rectángulo visible
        if (visibleRect != null) {
            g2d.setColor(new Color(255, 0, 0, 150));
            g2d.drawRect(visibleRect.x, visibleRect.y, visibleRect.width, visibleRect.height);
        }
        g2d.dispose();
    }

    @Override
    public void onModelChanged() {
        repaint();
    }
}