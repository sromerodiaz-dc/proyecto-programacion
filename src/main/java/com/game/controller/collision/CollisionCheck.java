package com.game.controller.collision;

import com.game.ui.TeisPanel;
import com.game.entity.Entity;
import com.game.maptile.PiezaManager;

import java.awt.*;
import java.util.ArrayList;

/**
 * Clase que se encarga de verificar las colisiones entre entidades y objetos en el mapa.
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class CollisionCheck {
    /**
     * Referencia al panel de juego que contiene los objetos y entidades.
     */
    private final TeisPanel teisPanel;

    /**
     * Constructor que inicializa la referencia al panel de juego.
     *
     * @param teisPanel el panel de juego que contiene los objetos y entidades.
     */
    public CollisionCheck(TeisPanel teisPanel) {
        this.teisPanel = teisPanel;
    }

    /**
     * Verifica si la entidad colisiona con una pieza en la dirección especificada.
     *
     * @param entity la entidad que se va a verificar.
     */
    public void checkPieza(Entity entity) {
        int entityLeft = entity.worldX + entity.solidArea.x;
        int entityRight = entityLeft + entity.solidArea.width;
        int entityTop = entity.worldY + entity.solidArea.y;
        int entityBottom = entityTop + entity.solidArea.height;

        int SIZE_FINAL = TeisPanel.SIZE_FINAL;
        PiezaManager pm = teisPanel.controller.getPiezaManager();

        switch (entity.sentido) {
            case 'w': // Arriba
                checkTileCollision(pm, entity,
                        entityLeft / SIZE_FINAL,
                        entityRight / SIZE_FINAL,
                        (entityTop - entity.speed) / SIZE_FINAL,
                        (entityTop - entity.speed) / SIZE_FINAL,
                        true); // Vertical
                break;

            case 's': // Abajo
                checkTileCollision(pm, entity,
                        entityLeft / SIZE_FINAL,
                        entityRight / SIZE_FINAL,
                        (entityBottom + entity.speed) / SIZE_FINAL,
                        (entityBottom + entity.speed) / SIZE_FINAL,
                        true); // Vertical
                break;

            case 'a': // Izquierda
                checkTileCollision(pm, entity,
                        (entityLeft - entity.speed) / SIZE_FINAL,
                        (entityLeft - entity.speed) / SIZE_FINAL,
                        entityTop / SIZE_FINAL,
                        entityBottom / SIZE_FINAL,
                        false); // Horizontal
                break;

            case 'd': // Derecha
                checkTileCollision(pm, entity,
                        (entityRight + entity.speed) / SIZE_FINAL,
                        (entityRight + entity.speed) / SIZE_FINAL,
                        entityTop / SIZE_FINAL,
                        entityBottom / SIZE_FINAL,
                        false); // Horizontal
                break;
        }
    }

    private void checkTileCollision(PiezaManager pm, Entity entity,
                                    int colStart, int colEnd,
                                    int rowStart, int rowEnd,
                                    boolean isVertical) {

        // Para movimiento vertical: verificar todas las columnas en la fila objetivo
        if (isVertical) {
            for (int col = colStart; col <= colEnd; col++) {
                Point tilePos = new Point(col, rowStart);
                if (isCollidable(pm, tilePos)) {
                    entity.collisionOn = true;
                    return;
                }
            }
        }
        // Para movimiento horizontal: verificar todas las filas en la columna objetivo
        else {
            for (int row = rowStart; row <= rowEnd; row++) {
                Point tilePos = new Point(colStart, row);
                if (isCollidable(pm, tilePos)) {
                    entity.collisionOn = true;
                    return;
                }
            }
        }
        entity.collisionOn = false;
    }

    private boolean isCollidable(PiezaManager pm, Point tilePos) {
        // Verificar si está fuera del mapa
        if (tilePos.x < 0 || tilePos.x >= pm.mapa.width ||
                tilePos.y < 0 || tilePos.y >= pm.mapa.height) {
            return true;
        }

        // Verificar colisión en el tile
        return pm.mapa.capaColisiones.contains(tilePos);
    }

    /**
     * Verifica si la entidad colisiona con algún objeto en el mapa y devuelve el índice del objeto si es el caso.
     *
     * @param entity la entidad que se va a verificar
     * @param player indica si la entidad es el jugador
     * @return el índice del objeto con el que colisiona la entidad, o 999 si no hay colisión
     */
    public int checkObject(Entity entity, boolean player) {
        int index = 999;
        int objCount = teisPanel.controller.obj.size();

        for (int x = 0; x < objCount; x++) {
            Entity obj = teisPanel.controller.obj.get(x);

            if (obj != null && checkCollisionBetween(entity, obj)) {
                if (obj.collision) entity.collisionOn = true;
                if (player) index = x;
            }
        }

        // Devuelve el índice del objeto con el que colisiona la entidad, o 999 si no hay colisión
        return index;
    }

    /**
     * Verifica si una entidad colisiona con otros objetos en el mapa.
     *
     * @param entity La entidad que se va a verificar.
     * @param target El array de objetos en el mapa.
     * @return El índice del objeto con el que colisionó, o 999 si no hay colisión.
     */
    public int checkEntity(Entity entity, ArrayList<Entity> target) {
        int index = 999;
        int targetSize = target.size();

        for (int x = 0; x < targetSize; x++) {
            Entity currentTarget = target.get(x);

            if (currentTarget != null && currentTarget != entity) {
                // Solo verificar colisión si la entidad objetivo no está en estado de invencibilidad
                if (!currentTarget.invencible && checkCollisionBetween(entity, currentTarget)) {
                    entity.collisionOn = true;
                    System.out.println("Colision");
                    index = x;
                }
            }
        }
        return index;
    }

    /**
     * Verifica si una entidad colisiona con el jugador.
     *
     * @param entity La entidad que se va a verificar.
     * @return True si la entidad colisiona con el jugador, false en caso contrario.
     */
    public boolean checkPlayer(Entity entity) {
        boolean collision = checkCollisionBetween(entity, teisPanel.player);

        if (collision && teisPanel.player != entity) {
            entity.collisionOn = true;
            return true;
        }
        return false;
    }

    /**
     * Verifica la dirección de la entidad y ajusta las coordenadas de su área sólida según sea necesario.
     *
     * @param entity La entidad que se va a verificar.
     */
    private void checkSolidArea(Entity entity) {
        switch (entity.sentido) {
            case 'w': entity.solidArea.y -= entity.speed; break;
            case 's': entity.solidArea.y += entity.speed; break;
            case 'd': entity.solidArea.x += entity.speed; break;
            case 'a': entity.solidArea.x -= entity.speed; break;
        }
    }

    /**
     * Prepara las áreas sólidas de dos entidades para una verificación de colisión.
     * Transforma las coordenadas relativas del área sólida a coordenadas absolutas en el mundo.
     * También ajusta temporalmente el área sólida de la entidad en movimiento según su dirección.
     *
     * @param entity La entidad en movimiento.
     * @param target La entidad con la que se quiere comprobar la colisión.
     */
    private void prepareSolidAreas(Entity entity, Entity target) {
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        target.solidArea.x = target.worldX + target.solidArea.x;
        target.solidArea.y = target.worldY + target.solidArea.y;
        checkSolidArea(entity);
    }

    /**
     * Restaura las áreas sólidas de ambas entidades a su estado original (relativo).
     * Esto es necesario después de simular una colisión, para no alterar permanentemente su estado.
     *
     * @param entity La entidad en movimiento.
     * @param target La entidad objetivo.
     */
    private void resetSolidAreas(Entity entity, Entity target) {
        entity.solidArea.x = entity.defaultSolidAreaX;
        entity.solidArea.y = entity.defaultSolidAreaY;
        target.solidArea.x = target.defaultSolidAreaX;
        target.solidArea.y = target.defaultSolidAreaY;
    }

    /**
     * Verifica si hay una colisión entre dos entidades usando sus áreas sólidas ajustadas temporalmente.
     *
     * @param entity La entidad en movimiento.
     * @param target La entidad objetivo con la que se desea comprobar colisión.
     * @return true si las áreas sólidas se intersectan; false en caso contrario.
     */
    private boolean checkCollisionBetween(Entity entity, Entity target) {
        prepareSolidAreas(entity, target);
        boolean collision = entity.solidArea.intersects(target.solidArea);
        resetSolidAreas(entity, target);
        return collision;
    }
}