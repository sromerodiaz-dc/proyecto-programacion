package com.game.entity;

import com.game.controller.TeisPanel;
import com.game.gphics.PiezaManager;

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

        int sizeFinal = teisPanel.sizeFinal;
        PiezaManager pm = teisPanel.controller.getPiezaManager();

        switch (entity.sentido) {
            case 'w':
                checkTileCollision(pm, entity,
                        entityLeft / sizeFinal,
                        entityRight / sizeFinal,
                        (entityTop - entity.speed) / sizeFinal,
                        (entityTop - entity.speed) / sizeFinal);
                break;

            case 's':
                checkTileCollision(pm, entity,
                        entityLeft / sizeFinal,
                        entityRight / sizeFinal,
                        (entityBottom + entity.speed) / sizeFinal,
                        (entityBottom + entity.speed) / sizeFinal);
                break;

            case 'a':
                checkTileCollision(pm, entity,
                        (entityLeft - entity.speed) / sizeFinal,
                        (entityLeft - entity.speed) / sizeFinal,
                        entityTop / sizeFinal,
                        entityBottom / sizeFinal);
                break;

            case 'd':
                checkTileCollision(pm, entity,
                        (entityRight + entity.speed) / sizeFinal,
                        (entityRight + entity.speed) / sizeFinal,
                        entityTop / sizeFinal,
                        entityBottom / sizeFinal);
                break;
        }
    }


    /**
     * Verifica la colisión de la entidad con las piezas del mapa en base a las coordenadas de su borde y dirección.
     * Compara dos posiciones del mapa (usualmente esquinas) para determinar si hay colisión.
     *
     * @param pm El gestor de piezas que contiene el mapa y sus propiedades.
     * @param entity La entidad que se está moviendo y se desea verificar.
     * @param col1 Primera columna a comprobar (usualmente izquierda o derecha de la entidad).
     * @param col2 Segunda columna a comprobar (puede ser la misma que col1 si solo ocupa una columna).
     * @param row1 Primera fila a revisar (usualmente arriba o abajo de la entidad).
     * @param row2 Segunda fila a revisar (puede ser igual a row1 si solo ocupa una fila).
     * -
     * Maneja colisiones con tiles en una dirección específica (sobrecarga para vertical/horizontal).
     * ¿Cómo funciona?
     *     En movimientos verticales, las filas (row1, row2) son iguales y las columnas son diferentes.
     *     En movimientos horizontales, las columnas (col1, col2) son iguales y las filas son diferentes.
     * -
     */
    private void checkTileCollision(PiezaManager pm, Entity entity, int col1, int col2, int row1, int row2) {
        int pieza1 = pm.mapaPiezaNum[col1][row1];
        int pieza2 = pm.mapaPiezaNum[col2][row2];
        entity.collisionOn = pm.pieza[pieza1].colision || pm.pieza[pieza2].colision;
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
                if (checkCollisionBetween(entity, currentTarget)) {
                    entity.collisionOn = true;
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
        boolean collision = checkCollisionBetween(entity, teisPanel.model);

        if (collision && teisPanel.model != entity) {
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