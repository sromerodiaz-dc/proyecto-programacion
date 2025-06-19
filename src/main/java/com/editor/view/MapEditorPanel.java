package com.editor.view;

import com.editor.controller.TextureController;
import com.editor.model.event.EventDataBuilder;
import com.editor.model.event.EventDialog;
import com.editor.model.record.CeldaCoord;
import com.editor.model.IModelChangeListener;
import com.editor.model.MapModel;
import com.editor.model.record.EntitySpawnEvent;
import com.editor.model.event.EventMode;
import com.editor.model.record.EventData;
import com.editor.model.record.TeleportEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.prefs.Preferences;

public class MapEditorPanel extends JPanel implements IModelChangeListener, MouseListener, MouseMotionListener {
    private final MapModel model;
    private final TextureController textureController;
    private final int tileSize = 32;
    private int selectedTextureId = 1;
    private boolean collisionMode = false;
    private static final int HANDLE_SIZE = 8;

    // Nuevos campos para gestión de eventos
    private EventMode eventMode = EventMode.NONE;
    private String selectedEntityId;

    private EventDataBuilder eventBuilder;
    private Rectangle previewRect;
    private boolean complexEventMode = false;
    private EventData selectedEvent;
    private int lastMouseX, lastMouseY;

    private static final Preferences PREFS = Preferences.userNodeForPackage(MapEditorPanel.class);
    private static final String SHOW_INSTRUCTIONS_KEY = "showComplexEventInstructions";


    public MapEditorPanel(MapModel model, TextureController textureController) {
        this.model = model;
        this.textureController = textureController;
        setDoubleBuffered(true); // Habilita doble buffer
        model.addListener(this);
        setupMouseListeners();
        setPreferredSize(new Dimension(model.getCols() * tileSize, model.getRows() * tileSize));
    }

    private void setupMouseListeners() {
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Selección de evento existente
                for (EventData event : model.getEvents()) {
                    Rectangle eventRect = new Rectangle(
                            (int) (event.col() * tileSize),
                            (int) (event.row() * tileSize),
                            (int) (event.width() * (tileSize / 48.0)),
                            (int) (event.height() * (tileSize / 48.0))
                    );

                    if (eventRect.contains(e.getPoint())) {
                        selectedEvent = event;
                        lastMouseX = e.getX();
                        lastMouseY = e.getY();

                        // Doble clic para editar
                        if (e.getClickCount() == 2) {
                            editSelectedEvent();
                        }
                        return;
                    }
                }

                // Manejo de eventos complejos
                if (complexEventMode) {
                    handleComplexEventClick(e);
                    return;
                }

                double col = (double) e.getX() / tileSize;
                double row = (double) e.getY() / tileSize;

                if (SwingUtilities.isRightMouseButton(e)) {
                    eventMode = EventMode.NONE; // Cancelar modo evento con clic derecho
                    model.clearTeleportSource();
                    repaint();
                    return;
                }

                if (eventMode != EventMode.NONE) {
                    handleEventAction(row, col);
                    return;
                }

                if (SwingUtilities.isRightMouseButton(e)) {
                    deleteTile(e);
                } else {
                    paintTile(e);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedEvent != null) {
                    // Manejo de transformaciones
                    handleEventTransformations(e);
                } else if (!complexEventMode) {
                    paintTile(e);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (complexEventMode) {
                    handleComplexEventPreview(e);
                }
            }
        };

        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }


    private void handleEventTransformations(MouseEvent e) {
        double x = selectedEvent.col() * tileSize;
        double y = selectedEvent.row() * tileSize;
        double width = selectedEvent.width() * (tileSize / 48.0);
        double height = selectedEvent.height() * (tileSize / 48.0);

        Rectangle[] handles = getEventHandles(x, y, width, height);
        Point mouse = e.getPoint();

        // Identificar qué control se está manipulando
        int activeHandle = -1;
        for (int i = 0; i < handles.length; i++) {
            if (handles[i].contains(mouse)) {
                activeHandle = i;
                break;
            }
        }

        // Aplicar transformación según el control
        switch (activeHandle) {
            case 0: // NW - Redimensionar
                handleResize(e, x, y, width, height, true, true);
                break;
            case 1: // NE - Redimensionar
                handleResize(e, x, y, width, height, false, true);
                break;
            case 2: // SW - Redimensionar
                handleResize(e, x, y, width, height, true, false);
                break;
            case 3: // SE - Redimensionar (predeterminado)
                handleResize(e, x, y, width, height, false, false);
                break;
            case 4: // Centro - Rotación
                handleRotation(e, x, y, width, height);
                break;
            case 5: // E - Redimensionar horizontal
                handleResize(e, x, y, width, height, false, null);
                break;
            case 6: // S - Redimensionar vertical
                handleResize(e, x, y, width, height, null, false);
                break;
            default: // Mover evento
                handleMove(e);
                break;
        }

        lastMouseX = e.getX();
        lastMouseY = e.getY();
        repaint();
    }

    private void editSelectedEvent() {
        if (selectedEvent == null) return;

        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        if (parentWindow instanceof Frame) {
            EventDialog dialog = new EventDialog((Frame) parentWindow, selectedEvent);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                model.removeEvent(selectedEvent);
                model.addEvent(dialog.getEventData());
                selectedEvent = dialog.getEventData();
                repaint();
            }
        }
    }

    private Rectangle[] getEventHandles(double x, double y, double width, double height) {
        return new Rectangle[]{
                new Rectangle((int) (x - 4), (int) (y - 4), 8, 8), // NW
                new Rectangle((int) (x + width - 4), (int) (y - 4), 8, 8), // NE
                new Rectangle((int) (x - 4), (int) (y + height - 4), 8, 8), // SW
                new Rectangle((int) (x + width - 4), (int) (y + height - 4), 8, 8), // SE
                new Rectangle((int) (x + width / 2 - 4), (int) (y + height / 2 - 4), 8, 8), // Centro
                new Rectangle((int) (x + width - 4), (int) (y + height / 2 - 4), 8, 8), // E
                new Rectangle((int) (x + width / 2 - 4), (int) (y + height - 4), 8, 8)  // S
        };
    }

    private void handleResize(MouseEvent e, double x, double y, double width, double height, Boolean left, Boolean top) {
        double deltaX = e.getX() - lastMouseX;
        double deltaY = e.getY() - lastMouseY;

        // CAMBIO: Usar double para cálculos de posición
        double newX = (left != null && left) ? x + deltaX : x;
        double newY = (top != null && top) ? y + deltaY : y;
        double newWidth = width + (left != null && left ? -deltaX : (left == null ? 0 : deltaX));
        double newHeight = height + (top != null && top ? -deltaY : (top == null ? 0 : deltaY));

        // Actualizar evento con valores double
        updateEventPosition(
                newX / tileSize,
                newY / tileSize,
                newWidth * (48.0 / tileSize),
                newHeight * (48.0 / tileSize)
        );
    }

    private void handleRotation(MouseEvent e, double x, double y, double width, double height) {
        // CAMBIO: Usar double para cálculos de posición
        double centerX = x + width / 2;
        double centerY = y + height / 2;
        double angle = Math.atan2(e.getY() - centerY, e.getX() - centerX);
        double degrees = Math.toDegrees(angle);

        model.removeEvent(selectedEvent);
        selectedEvent = selectedEvent.withRotation(degrees);
        model.addEvent(selectedEvent);
    }

    private void handleMove(MouseEvent e) {
        double deltaCol = (double) (e.getX() - lastMouseX) / tileSize;
        double deltaRow = (double) (e.getY() - lastMouseY) / tileSize;

        if (deltaCol != 0 || deltaRow != 0) {
            updateEventPosition(
                     (selectedEvent.col() + deltaCol),
                    selectedEvent.row() + deltaRow,
                    selectedEvent.width(),
                    selectedEvent.height()
            );
        }
    }

    private void handleComplexEventClick(MouseEvent e) {
        if (!complexEventMode || eventBuilder == null) return;

        double col = (double) e.getX() / tileSize;
        double row = (double) e.getY() / tileSize;

        int tileCol = (int) Math.floor(col);
        int tileRow = (int) Math.floor(row);

        // Validar colisión
        if (model.isTileCollision(new CeldaCoord(tileRow, tileCol))) {
            JOptionPane.showMessageDialog(this,
                    "No se pueden colocar eventos en celdas con colisión",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (SwingUtilities.isLeftMouseButton(e)) {
            if (!eventBuilder.isFirstPointSet()) {
                eventBuilder.setFirstPoint(col, row);  // Corregido: parámetros double
            } else {
                eventBuilder.setSecondPoint(col, row);  // Corregido: parámetros double
                openEventDialog();
            }
        }
    }

    private void handleComplexEventPreview(MouseEvent e) {
        if (eventBuilder != null && eventBuilder.isFirstPointSet()) {
            int startXPixel = (int) (eventBuilder.startX * tileSize);
            int startYPixel = (int) (eventBuilder.startY * tileSize);

            previewRect = new Rectangle(
                    Math.min(startXPixel, e.getX()),
                    Math.min(startYPixel, e.getY()),
                    Math.abs(e.getX() - startXPixel),
                    Math.abs(e.getY() - startYPixel)
            );
            repaint();
        }
    }

    private void updateEventPosition(double col, double row, double width, double height) {
        model.removeEvent(selectedEvent);
        selectedEvent = new EventData(
                row,
                col,
                width,
                height,
                selectedEvent.type(),
                selectedEvent.message(),
                selectedEvent.value(),
                selectedEvent.cooldown(),
                selectedEvent.texturePath(),
                selectedEvent.rotation(),
                selectedEvent.scale()
        );
        model.addEvent(selectedEvent);
    }

    private void handleEventAction(double row, double col) {
        // Convertir a coordenadas enteras usando Math.floor()
        CeldaCoord coord = new CeldaCoord((int) Math.floor(row), (int) Math.floor(col));

        // Verificar si la celda es colisionable
        if (model.isTileCollision(coord)) {
            JOptionPane.showMessageDialog(this, "No se puede colocar eventos en celdas con colisión",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch (eventMode) {
            case PLAYER_SPAWN:
                model.setPlayerSpawn(coord);
                eventMode = EventMode.NONE;
                break;

            case TELEPORT_SOURCE:
                model.setTeleportSource(coord);
                eventMode = EventMode.TELEPORT_DEST;
                break;

            case ENTITY_SPAWN:
                model.addEntitySpawn(new EntitySpawnEvent(
                        selectedEntityId,
                        (int) Math.floor(row),  // Convertir a int
                        (int) Math.floor(col)   // Convertir a int
                ));
                break;

            case TELEPORT_DEST:
                model.addTeleport(new TeleportEvent(
                        model.getTeleportSource().row(),
                        model.getTeleportSource().col(),
                        (int) Math.floor(row),  // Convertir a int
                        (int) Math.floor(col)   // Convertir a int
                ));
                break;
        }
        repaint();
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

    public void setEventMode(EventMode mode) {
        this.eventMode = mode;
        this.complexEventMode = false; // Desactivar modo complejo
        this.collisionMode = false;
        repaint();
    }


    public void setEventMode(EventMode mode, String entityId) {
        this.eventMode = mode;
        this.selectedEntityId = entityId;
        this.collisionMode = false; // Desactivar modo colisión
        repaint();
    }

    public void setEventMode(boolean active) {
        this.complexEventMode = active;

        if (active) {
            eventBuilder = new EventDataBuilder();
            previewRect = null;
            this.eventMode = EventMode.NONE;
            this.collisionMode = false;
            this.selectedTextureId = -1; // Deseleccionar textura

            // Solo mostrar instrucciones si es primera vez
            boolean showInstructions = PREFS.getBoolean(SHOW_INSTRUCTIONS_KEY, true);
            if (showInstructions) {
                showComplexEventInstructions();
            }
        } else {
            eventBuilder = null;
            previewRect = null;
        }
        repaint();
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

        drawEvents(g2d);

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

        // Dibujar previsualización
        if (previewRect != null) {
            g.setColor(new Color(0, 255, 0, 100));
            g.fillRect(previewRect.x, previewRect.y, previewRect.width, previewRect.height);
            g.setColor(Color.GREEN);
            g.drawRect(previewRect.x, previewRect.y, previewRect.width, previewRect.height);
        }

        // Dibujar eventos existentes
        for (EventData event : model.getEvents()) {
            drawEvent(g, event);
            if (event.equals(selectedEvent)) {
                drawEventControls(g, event);
            }
        }

        g2d.setComposite(AlphaComposite.SrcOver);

        g2d.dispose();
    }

    private void drawEventControls(Graphics g, EventData event) {
        double x = event.col() * tileSize;
        double y = event.row() * tileSize;
        double width = event.width() * (tileSize / 48.0);
        double height = event.height() * (tileSize / 48.0);

        // Usando HANDLE_SIZE constante en lugar de variable no definida
        g.fillRect((int) (x - HANDLE_SIZE/2.0), (int) (y - HANDLE_SIZE/2.0), HANDLE_SIZE, HANDLE_SIZE);
        g.fillRect((int) (x + width - HANDLE_SIZE/2.0), (int) (y - HANDLE_SIZE/2.0), HANDLE_SIZE, HANDLE_SIZE);
        g.fillRect((int) (x - HANDLE_SIZE/2.0), (int) (y + height - HANDLE_SIZE/2.0), HANDLE_SIZE, HANDLE_SIZE);
        g.fillRect((int) (x + width - HANDLE_SIZE/2.0), (int) (y + height - HANDLE_SIZE/2.0), HANDLE_SIZE, HANDLE_SIZE);
        g.fillRect((int) (x + width/2.0 - HANDLE_SIZE/2.0), (int) (y + height/2.0 - HANDLE_SIZE/2.0), HANDLE_SIZE, HANDLE_SIZE);
        g.fillRect((int) (x + width - HANDLE_SIZE/2.0), (int) (y + height/2.0 - HANDLE_SIZE/2.0), HANDLE_SIZE, HANDLE_SIZE);
        g.fillRect((int) (x + width/2.0 - HANDLE_SIZE/2.0), (int) (y + height - HANDLE_SIZE/2.0), HANDLE_SIZE, HANDLE_SIZE);
    }

    private void drawEvents(Graphics2D g2d) {
        // Dibujar spawn del jugador (azul)
        if (model.getPlayerSpawn() != null) {
            drawEvent(g2d, model.getPlayerSpawn(), Color.BLUE);
        }

        // Dibujar spawns de entidades (verde)
        for (EntitySpawnEvent spawn : model.getEntitySpawns()) {
            drawEvent(g2d, new CeldaCoord(spawn.row(), spawn.col()), Color.GREEN);
        }

        // Dibujar teleports
        for (TeleportEvent teleport : model.getTeleports()) {
            // Origen (naranja)
            drawEvent(g2d, new CeldaCoord(teleport.row(), teleport.col()), Color.ORANGE);

            // Destino (cian)
            drawEvent(g2d, new CeldaCoord(teleport.targetRow(), teleport.targetCol()), Color.CYAN);

            // Línea conectando
            g2d.setColor(Color.YELLOW);
            g2d.drawLine(
                    teleport.col() * tileSize + tileSize / 2,
                    teleport.row() * tileSize + tileSize / 2,
                    teleport.targetCol() * tileSize + tileSize / 2,
                    teleport.targetRow() * tileSize + tileSize / 2
            );
        }

        // Dibujar origen temporal para teleport
        if (model.getTeleportSource() != null) {
            drawEvent(g2d, model.getTeleportSource(), Color.ORANGE);
        }
    }

    private void drawEvent(Graphics g, EventData event) {
        Graphics2D g2d = (Graphics2D) g;
        // Coordenadas sin conversión de escala
        double x = event.col() * tileSize;
        double y = event.row() * tileSize;
        double width = event.width() * tileSize; // 1:1 con tiles
        double height = event.height() * tileSize; // 1:1 con tiles

        // Dibujar área del evento
        g2d.setColor(new Color(255, 0, 255, 100));
        g2d.fill(new Rectangle2D.Double(x, y, width, height));
        g2d.setColor(Color.MAGENTA);
        g2d.draw(new Rectangle2D.Double(x, y, width, height));

        if (event.texturePath() != null && !event.texturePath().isEmpty()) {
            BufferedImage texture = textureController.getTextureByPath(event.texturePath());
            if (texture != null) {
                AffineTransform transform = new AffineTransform();
                // Escalar textura proporcionalmente al evento
                double scaleX = width / texture.getWidth();
                double scaleY = height / texture.getHeight();
                transform.translate(x, y);
                transform.scale(scaleX, scaleY);
                g2d.drawImage(texture, transform, null);
            }
        }
    }

    private void drawEvent(Graphics2D g2d, CeldaCoord coord, Color color) {
        g2d.setColor(color);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2d.fillRect(coord.col() * tileSize, coord.row() * tileSize, tileSize, tileSize);
        g2d.setComposite(AlphaComposite.SrcOver);
    }

    public void loadMapData(int[][] data, Set<CeldaCoord> collisions) {
        int rows = data.length;
        int cols = data.length > 0 ? data[0].length : 0;

        // Actualizar modelo con nuevas dimensiones
        model.setDimensions(rows, cols); // ¡Ahora existe!

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

    public int getSelectedTexture() {
        return selectedTextureId;
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
        if (active) {
            complexEventMode = false; // Desactivar modo complejo
            eventMode = EventMode.NONE;
        }
        repaint();
    }

    public boolean isCollisionMode() {
        return collisionMode;
    }

    private void openEventDialog() {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);

        if (parentWindow instanceof Frame parentFrame) {
            EventDialog dialog = new EventDialog(parentFrame, eventBuilder.getEventData());
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                model.addEvent(dialog.getEventData());
            }

            // Resetear para nuevo evento (sin salir del modo)
            eventBuilder = new EventDataBuilder();
            previewRect = null;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void showComplexEventInstructions() {
        boolean showInstructions = PREFS.getBoolean(SHOW_INSTRUCTIONS_KEY, true);
        if (!showInstructions) return;

        JCheckBox dontShowAgain = new JCheckBox("No mostrar más");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("<html>Modo Eventos Complejos activado:<br>"
                        + "1. Haz clic en la primera esquina del evento<br>"
                        + "2. Haz clic en la esquina opuesta<br>"
                        + "3. Completa los detalles en el diálogo</html>"),
                BorderLayout.CENTER);
        panel.add(dontShowAgain, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel, "Eventos Complejos",
                JOptionPane.INFORMATION_MESSAGE);

        PREFS.putBoolean(SHOW_INSTRUCTIONS_KEY, !dontShowAgain.isSelected());
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public boolean isComplexEventMode() {return complexEventMode;}
}