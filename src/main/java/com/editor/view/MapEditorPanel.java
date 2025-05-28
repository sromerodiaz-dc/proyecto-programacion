package com.editor.view;

import com.editor.controller.TextureController;
import com.editor.model.CeldaCoord;
import com.editor.model.IModelChangeListener;
import com.editor.model.MapModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Set;

public class MapEditorPanel extends JPanel implements IModelChangeListener {
    private final MapModel model;
    private final TextureController textureController;
    private final int tileSize = 32;
    private int selectedTextureId = 1;
    private boolean collisionMode = false;

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

        System.out.println("[DEBUG] paintTile() - row: " + row + ", col: " + col);
        System.out.println("[DEBUG]   - collisionMode: " + collisionMode);

        if (col < 0 || col >= model.getCols() || row < 0 || row >= model.getRows()) {
            System.out.println("[DEBUG]   - Coordenadas fuera de rango.");
            return;
        }

        if (collisionMode) {
            // Modo colisión manual (se guarda en manualCollisions)
            if (SwingUtilities.isLeftMouseButton(e)) {
                model.addCollision(row, col);
            } else if (SwingUtilities.isRightMouseButton(e)) {
                model.removeCollision(row, col);
            }
        } else {
            // Al pintar, se aplica colisión automática de la textura actual
            int currentTileId = model.getTile(row, col);
            if (currentTileId != selectedTextureId) {
                model.setTile(row, col, selectedTextureId);
            }
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

        // Configurar antialiasing para líneas nítidas
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

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

                // Borde blanco nitido
                g2d.setColor(Color.WHITE);
                g2d.drawRect(x, y, tileSize - 1, tileSize - 1);
            }
        }

        // Dibujar colisiones (manuales + automáticas)
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        g2d.setColor(Color.RED);
        for (int row = 0; row < model.getRows(); row++) {
            for (int col = 0; col < model.getCols(); col++) {
                CeldaCoord coord = new CeldaCoord(row, col);
                if (model.isTileCollision(coord)) {
                    int x = col * tileSize;
                    int y = row * tileSize;
                    g2d.fillRect(x, y, tileSize, tileSize);
                }
            }
        }
        g2d.setComposite(AlphaComposite.SrcOver);

        g2d.dispose();
    }

    public void loadMapData(int[][] data, Set<CeldaCoord> collisions) {
        int rows = model.getRows();
        int cols = model.getCols();

        if (data.length != rows || (data.length > 0 && data[0].length != cols)) {
            throw new IllegalArgumentException("Dimensiones de los datos cargados (" + data.length + "x" + (data.length > 0 ? data[0].length : 0) + ") no coinciden con las dimensiones del mapa (" + rows + "x" + cols + ")");
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                model.setTile(row, col, data[row][col]);
            }
        }

        collisions.forEach(coord -> model.addCollision(coord.row(), coord.col()));

        setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));
        revalidate();
        repaint();
    }

    public void setSelectedTexture(int textureId) {
        this.selectedTextureId = textureId;
        repaint();
    }

    @Override
    public void onModelChanged() {
        Set<CeldaCoord> modified = model.getModifiedCells();
        System.out.println("[DEBUG] onModelChanged() - Celdas modificadas: " + modified.size());

        if (modified.isEmpty()) {
            System.out.println("[DEBUG]   - No hay celdas modificadas.");
            return;
        }

        modified.forEach(p -> {
            int x = p.col() * tileSize;
            int y = p.row() * tileSize;
            System.out.println("[DEBUG]   - Repintando celda: " + p);
            repaint(x, y, tileSize, tileSize);
        });
        model.clearModifiedCells();
    }

    public void setCollisionMode(boolean active) {
        collisionMode = active;
    }

    public boolean isCollisionMode() {
        return collisionMode;
    }
}