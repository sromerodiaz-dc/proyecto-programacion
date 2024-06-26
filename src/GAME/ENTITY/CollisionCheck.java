package GAME.ENTITY;

import GAME.GAME.TeisPanel;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Clase que se encarga de verificar las colisiones entre entidades y objetos en el mapa.
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
        // Calcula las coordenadas de la entidad en el mapa
        int entityLeft = entity.worldX + entity.solidArea.x;
        int entityRight = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTop = entity.worldY + entity.solidArea.y;
        int entityBottom = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        // Calcula las columnas y filas de la entidad en el mapa
        int entityLeftCol = entityLeft / teisPanel.sizeFinal;
        int entityRightCol = entityRight / teisPanel.sizeFinal;
        int entityTopRow = entityTop / teisPanel.sizeFinal;
        int entityBottomRow = entityBottom / teisPanel.sizeFinal;

        int pieza1, pieza2;

        // Verifica la dirección de la entidad y calcula las piezas correspondientes
        switch (entity.sentido) {
            case 'w': // Arriba
                // Calcula la fila superior de la entidad después de moverse
                entityTopRow = (entityTop - entity.speed) / teisPanel.sizeFinal;
                // Calcula las piezas correspondientes a la fila superior
                pieza1 = teisPanel.controller.getPiezaManager().mapaPiezaNum[entityLeftCol][entityTopRow];
                pieza2 = teisPanel.controller.getPiezaManager().mapaPiezaNum[entityRightCol][entityTopRow];
                // Verifica si hay colisión con alguna de las piezas
                if (teisPanel.controller.getPiezaManager().pieza[pieza1].colision || teisPanel.controller.getPiezaManager().pieza[pieza2].colision) {
                    entity.collisionOn = true;
                }
                break;
            case 's': // Abajo
                // Calcula la fila inferior de la entidad después de moverse
                entityBottomRow = (entityBottom + entity.speed) / teisPanel.sizeFinal;
                // Calcula las piezas correspondientes a la fila inferior
                pieza1 = teisPanel.controller.getPiezaManager().mapaPiezaNum[entityLeftCol][entityBottomRow];
                pieza2 = teisPanel.controller.getPiezaManager().mapaPiezaNum[entityRightCol][entityBottomRow];
                // Verifica si hay colisión con alguna de las piezas
                if (teisPanel.controller.getPiezaManager().pieza[pieza1].colision || teisPanel.controller.getPiezaManager().pieza[pieza2].colision) {
                    entity.collisionOn = true;
                }
                break;
            case 'a': // Izquierda
                // Calcula la columna izquierda de la entidad después de moverse
                entityLeftCol = (entityLeft - entity.speed) / teisPanel.sizeFinal;
                // Calcula las piezas correspondientes a la columna izquierda
                pieza1 = teisPanel.controller.getPiezaManager().mapaPiezaNum[entityLeftCol][entityTopRow];
                pieza2 = teisPanel.controller.getPiezaManager().mapaPiezaNum[entityLeftCol][entityBottomRow];
                // Verifica si hay colisión con alguna de las piezas
                if (teisPanel.controller.getPiezaManager().pieza[pieza1].colision || teisPanel.controller.getPiezaManager().pieza[pieza2].colision) {
                    entity.collisionOn = true;
                }
                break;
            case 'd': // Derecha
                // Calcula la columna derecha de la entidad después de moverse
                entityRightCol = (entityRight + entity.speed) / teisPanel.sizeFinal;
                // Calcula las piezas correspondientes a la columna derecha
                pieza1 = teisPanel.controller.getPiezaManager().mapaPiezaNum[entityRightCol][entityTopRow];
                pieza2 = teisPanel.controller.getPiezaManager().mapaPiezaNum[entityRightCol][entityBottomRow];
                // Verifica si hay colisión con alguna de las piezas
                if (teisPanel.controller.getPiezaManager().pieza[pieza1].colision || teisPanel.controller.getPiezaManager().pieza[pieza2].colision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    /**
     * Verifica si la entidad colisiona con algún objeto en el mapa y devuelve el índice del objeto si es el caso.
     *
     * @param entity la entidad que se va a verificar
     * @param player indica si la entidad es el jugador
     * @return el índice del objeto con el que colisiona la entidad, o 999 si no hay colisión
     */
    public int checkObject(Entity entity, boolean player) {
        // Indice por defecto, si se mantiene intacto indica que no hay colision
        int i = 999;

        // Recorre todos los objetos del mapa
        for (int x = 0; x < teisPanel.controller.obj.length; x++) {
            // Verifica si el objeto no es nulo
            if (teisPanel.controller.obj[x] != null) {
                // Calcula las coordenadas de la entidad y el objeto en el mapa
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                teisPanel.controller.obj[x].solidArea.x = teisPanel.controller.obj[x].worldX + teisPanel.controller.obj[x].solidArea.x;
                teisPanel.controller.obj[x].solidArea.y = teisPanel.controller.obj[x].worldY + teisPanel.controller.obj[x].solidArea.y;

                // Verifica la dirección de la entidad y calcula las coordenadas correspondientes
                checkSolidArea(entity);

                // Verifica si la entidad colisiona con el objeto
                if (entity.solidArea.intersects(teisPanel.controller.obj[x].solidArea)) {
                    // Verifica si el objeto tiene colisión
                    if (teisPanel.controller.obj[x].collision) {
                        entity.collisionOn = true;
                    }
                    // Si la entidad es el jugador, guarda el índice del objeto con el que colisiona
                    if (player) {
                        i = x;
                    }
                }

                // Restaura las coordenadas originales de la entidad y el objeto para que no sumen al infinito
                entity.solidArea.x = entity.defaultSolidAreaX;
                entity.solidArea.y = entity.defaultSolidAreaY;
                teisPanel.controller.obj[x].solidArea.x = teisPanel.controller.obj[x].defaultSolidAreaX;
                teisPanel.controller.obj[x].solidArea.y = teisPanel.controller.obj[x].defaultSolidAreaY;
            }
        }

        // Devuelve el índice del objeto con el que colisiona la entidad, o 999 si no hay colisión
        return i;
    }

    /**
     * Verifica si una entidad colisiona con otros objetos en el mapa.
     *
     * @param entity La entidad que se va a verificar.
     * @param target El array de objetos en el mapa.
     * @return El índice del objeto con el que colisionó, o 999 si no hay colisión.
     */
    public int checkEntity(Entity entity, Entity[] target) {
        // Indice por defecto, si se mantiene intacto indica que no hay colision
        int i = 999;

        // Recorre todos los objetos del mapa
        for (int x = 0; x < target.length; x++) {
            // Verifica si el objeto no es nulo
            if (target[x]!= null) {
                // Calcula las coordenadas de la entidad y el objeto en el mapa
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                target[x].solidArea.x = target[x].worldX + target[x].solidArea.x;
                target[x].solidArea.y = target[x].worldY + target[x].solidArea.y;

                // Verifica la dirección de la entidad y calcula las coordenadas correspondientes
                checkSolidArea(entity);

                // Verifica si la entidad colisiona con el objeto
                if (entity.solidArea.intersects(target[x].solidArea)) {
                    if (target[x]!= entity) {
                        // Verifica si el objeto tiene colisión
                        entity.collisionOn = true;
                        i = x;
                    }
                }

                // Restaura las coordenadas originales de la entidad y el objeto para que no sumen al infinito
                entity.solidArea.x = entity.defaultSolidAreaX;
                entity.solidArea.y = entity.defaultSolidAreaY;
                target[x].solidArea.x = target[x].defaultSolidAreaX;
                target[x].solidArea.y = target[x].defaultSolidAreaY;
            }
        }

        return i;
    }

    /**
     * Verifica si una entidad colisiona con el jugador.
     *
     * @param entity La entidad que se va a verificar.
     * @return True si la entidad colisiona con el jugador, false en caso contrario.
     */
    public boolean checkPlayer(Entity entity) {
        boolean hitPlayer = false;

        // Calcula las coordenadas de la entidad y el objeto en el mapa
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        teisPanel.model.solidArea.x = teisPanel.model.worldX + teisPanel.model.solidArea.x;
        teisPanel.model.solidArea.y = teisPanel.model.worldY + teisPanel.model.solidArea.y;

        // Verifica la dirección de la entidad y calcula las coordenadas correspondientes
        checkSolidArea(entity);

        // Verifica si la entidad colisiona con el objeto
        if (entity.solidArea.intersects(teisPanel.model.solidArea)) {
            if (teisPanel.model!= entity) {
                // Verifica si el objeto tiene colisión
                entity.collisionOn = true;
                hitPlayer = true;
            }
        }

        // Restaura las coordenadas originales de la entidad y el objeto para que no sumen al infinito
        entity.solidArea.x = entity.defaultSolidAreaX;
        entity.solidArea.y = entity.defaultSolidAreaY;
        teisPanel.model.solidArea.x = teisPanel.model.defaultSolidAreaX;
        teisPanel.model.solidArea.y = teisPanel.model.defaultSolidAreaY;

        return hitPlayer;
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
}