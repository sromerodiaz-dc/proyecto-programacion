package GAME.ENTITY;

import GAME.GAME.TeisPanel;

public class CollisionCheck {
    TeisPanel teisPanel;

    public CollisionCheck(TeisPanel teisPanel){
        this.teisPanel = teisPanel;
    }

    /**
     * Comprobará colisiones entre bloques y entidades, no solo de players.
     * @param entity Instancia de entidad para comprobar colisiones.
     * */
    public void checkPieza(Entity entity) {
        int entityLeft = entity.worldX + entity.solidArea.x;
        int entityRight = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTop = entity.worldY + entity.solidArea.y;
        int entityBottom = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeft/teisPanel.sizeFinal;
        int entityRightCol = entityRight/teisPanel.sizeFinal;
        int entityTopRow = entityTop/teisPanel.sizeFinal;
        int entityBottomRow = entityBottom/teisPanel.sizeFinal;

        int pieza1,pieza2;

        switch (entity.sentido) {
            case 'w':
                entityTopRow = (entityTop - entity.speed)/teisPanel.sizeFinal;
                pieza1 = teisPanel.controller.getPiezaManager().mapaPiezaNum[entityLeftCol][entityTopRow];
                pieza2 = teisPanel.controller.getPiezaManager().mapaPiezaNum[entityRightCol][entityTopRow];
                if (teisPanel.controller.getPiezaManager().pieza[pieza1].colision || teisPanel.controller.getPiezaManager().pieza[pieza2].colision) {
                    entity.collisionOn = true;
                }
                break;
            case 's':
                entityBottomRow = (entityBottom + entity.speed)/teisPanel.sizeFinal;
                pieza1 = teisPanel.controller.getPiezaManager().mapaPiezaNum[entityLeftCol][entityBottomRow];
                pieza2 = teisPanel.controller.getPiezaManager().mapaPiezaNum[entityRightCol][entityBottomRow];
                if (teisPanel.controller.getPiezaManager().pieza[pieza1].colision || teisPanel.controller.getPiezaManager().pieza[pieza2].colision) {
                    entity.collisionOn = true;
                }
                break;
            case 'a':
                entityLeftCol = (entityLeft - entity.speed)/teisPanel.sizeFinal;
                pieza1 = teisPanel.controller.getPiezaManager().mapaPiezaNum[entityLeftCol][entityTopRow];
                pieza2 = teisPanel.controller.getPiezaManager().mapaPiezaNum[entityLeftCol][entityBottomRow];
                if (teisPanel.controller.getPiezaManager().pieza[pieza1].colision || teisPanel.controller.getPiezaManager().pieza[pieza2].colision) {
                    entity.collisionOn = true;
                }
                break;
            case 'd':
                entityRightCol = (entityRight + entity.speed)/teisPanel.sizeFinal;
                pieza1 = teisPanel.controller.getPiezaManager().mapaPiezaNum[entityRightCol][entityTopRow];
                pieza2 = teisPanel.controller.getPiezaManager().mapaPiezaNum[entityRightCol][entityBottomRow];
                if (teisPanel.controller.getPiezaManager().pieza[pieza1].colision || teisPanel.controller.getPiezaManager().pieza[pieza2].colision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    public int checkObject(Entity entity, boolean player) {
        int i = 999;
        for (int x = 0;i < teisPanel.obj.length; x++) {
            if (teisPanel.obj[x] != null) {
                // Area de la entidad
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Area del objeto
                teisPanel.obj[x].solidArea.x = teisPanel.obj[x].worldX + teisPanel.obj[x].solidArea.x;
                teisPanel.obj[x].solidArea.y = teisPanel.obj[x].worldY + teisPanel.obj[x].solidArea.y;

                switch (entity.sentido) {
                    case 'w':
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(teisPanel.obj[x].solidArea)) {
                            System.out.println("colision");
                        }
                        break;
                    case 's':
                        entity.solidArea.y += entity.speed;
                        if (entity.solidArea.intersects(teisPanel.obj[x].solidArea)) {
                            System.out.println("colision");
                        }
                        break;
                    case 'd':
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(teisPanel.obj[x].solidArea)) {
                            System.out.println("colision");
                        }
                        break;
                    case 'a':
                        entity.solidArea.x += entity.speed;if (entity.solidArea.intersects(teisPanel.obj[x].solidArea)) {
                        System.out.println("colision");
                    }
                        break;
                }

                entity.solidArea.x = entity.defaultSolidAreaX;
                entity.solidArea.y = entity.defaultSolidAreaY;
                teisPanel.obj[x].solidArea.x = teisPanel.obj[x].defaultObjectSolidAreaX;
                teisPanel.obj[x].solidArea.y = teisPanel.obj[x].defaultObjectSolidAreaY;
            }
        }
        return i;
    }
}