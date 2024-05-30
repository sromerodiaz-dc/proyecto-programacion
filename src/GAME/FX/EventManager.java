package GAME.FX;

import GAME.GAME.TeisPanel;

/**
 *
 *
 * Clase muy importante, ya que maneja todos los eventos del juego.
 * Gracias a esta clase se pueden manejar cambios de mapa, de puntos de vida, cambios de sprites, etc.
 * */
public class EventManager {
    TeisPanel teisPanel;
    EventRectangle[][] eventRectangle;

    public EventManager (TeisPanel teisPanel) {
        this.teisPanel = teisPanel;

        eventRectangle = new EventRectangle[teisPanel.maxWorldCol][teisPanel.maxWorldRow];

        int col = 0;
        int row = 0;
        while (col < teisPanel.maxWorldCol && row < teisPanel.maxWorldRow){
            // Define un área de colision en el centro de un sprite de 2x2 píxeles.
            eventRectangle[col][row] = new EventRectangle();
            eventRectangle[col][row].x = 23;
            eventRectangle[col][row].y = 23;
            eventRectangle[col][row].width = 2;
            eventRectangle[col][row].height = 2;

            eventRectangle[col][row].defaultX = eventRectangle[col][row].x;
            eventRectangle[col][row].defaultY = eventRectangle[col][row].y;

            col++;
            if (col == teisPanel.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void checkEvent(){
        if (hit(10, 12, 'a')) damage(10,12, teisPanel.controller.dialogoState, "\"Encontras tirado no chan un periódico...\nO Celta volveu perder, non che sorprende,\nsó entrischécete\"");

        if (hit(14,13,'s')) heal(14,13,teisPanel.controller.dialogoState);

        eventRectangle[10][12].resetCooldowns(30);
    }

    public boolean hit (int col,int row,char sentido){
        boolean doesHit = false;

        teisPanel.model.solidArea.x = teisPanel.model.worldX + teisPanel.model.solidArea.x;
        teisPanel.model.solidArea.y = teisPanel.model.worldY + teisPanel.model.solidArea.y;

        eventRectangle[col][row].x = col*teisPanel.sizeFinal + eventRectangle[col][row].x;
        eventRectangle[col][row].y = row*teisPanel.sizeFinal + eventRectangle[col][row].y;

        if (teisPanel.model.solidArea.intersects(eventRectangle[col][row]) && !eventRectangle[col][row].done){
            if (teisPanel.model.sentido == sentido || sentido != '0') {
                doesHit = true;
            }
        }

        teisPanel.model.solidArea.x = teisPanel.model.defaultSolidAreaX;
        teisPanel.model.solidArea.y = teisPanel.model.defaultSolidAreaY;
        eventRectangle[col][row].x = eventRectangle[col][row].defaultX;
        eventRectangle[col][row].y = eventRectangle[col][row].defaultY;

        return doesHit;
    }

    public void damage(int col, int row, int estado, String mensaje) {
        teisPanel.controller.estado = estado;
        teisPanel.controller.ui.dialogo = mensaje;
        teisPanel.model.life -= 5;

        eventRectangle[col][row].done = true;
    }

    public void heal(int col, int row, int estado) {
        if (teisPanel.model.keyManager.isPressed) {
            teisPanel.controller.estado = estado;
            teisPanel.controller.ui.dialogo = "\"Bebiches unha estrela.\nSíntese coma se o Vialia nunca fora edificado\"";
            teisPanel.model.life += 3;
            eventRectangle[col][row].done = true;
        }
    }
}
