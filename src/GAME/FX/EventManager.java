package GAME.FX;

import GAME.GAME.TeisPanel;

import java.awt.*;

/**
 *
 *
 * Clase muy importante, ya que maneja todos los eventos del juego.
 * Gracias a esta clase se pueden manejar cambios de mapa, de puntos de vida, cambios de sprites, etc.
 * */
public class EventManager {
    TeisPanel teisPanel;
    Rectangle eventAct;
    int defaultRectX,defaultRectY, cooldownD=0, cooldownH=0;
    boolean doneDamage = false, doneHeal = false;

    public EventManager (TeisPanel teisPanel) {
        this.teisPanel = teisPanel;

        // Define un área de colision en el centro de un sprite de 2x2 píxeles.
        eventAct = new Rectangle();
        eventAct.x = 23;
        eventAct.y = 23;
        eventAct.width = 2;
        eventAct.height = 2;

        defaultRectX = eventAct.x;
        defaultRectY = eventAct.y;
    }

    public void checkEvent(){
        if (!doneDamage) if (hit(10, 12, 'a')) damage(teisPanel.controller.dialogoState);

        if (!doneHeal) if (hit(14,13,'s')) heal(teisPanel.controller.dialogoState);

        resetCooldowns();
    }

    public boolean hit (int eventCol,int eventRow,char sentido){
        boolean doesHit = false;

        teisPanel.model.solidArea.x = teisPanel.model.worldX + teisPanel.model.solidArea.x;
        teisPanel.model.solidArea.y = teisPanel.model.worldY + teisPanel.model.solidArea.y;

        eventAct.x = eventCol*teisPanel.sizeFinal + eventAct.x;
        eventAct.y = eventRow*teisPanel.sizeFinal + eventAct.y;

        if (teisPanel.model.solidArea.intersects(eventAct)){
            if (teisPanel.model.sentido == sentido || sentido != '0') {
                doesHit = true;
            }
        }

        teisPanel.model.solidArea.x = teisPanel.model.defaultSolidAreaX;
        teisPanel.model.solidArea.y = teisPanel.model.defaultSolidAreaY;
        eventAct.x = defaultRectX;
        eventAct.y = defaultRectY;

        return doesHit;
    }

    public void damage(int estado) {
        doneDamage = true;
        teisPanel.controller.estado = estado;
        teisPanel.controller.ui.dialogo = "\"Encontras tirado no chan un periódico...\nO Celta volveu perder, non che sorprende\nsó entrischécete\"";
        teisPanel.model.life -= 5;
    }

    public void heal(int estado) {
        if (teisPanel.model.keyManager.isPressed) {
            doneHeal = true;
            teisPanel.controller.estado = estado;
            teisPanel.controller.ui.dialogo = "\"Bebiches unha estrela.\nSíntese coma se o Vialia nunca fora edificado\"";
            teisPanel.model.life += 3;
        }
    }

    public void resetCooldowns() {
        if (cooldownD > 300) {
            doneDamage = false;
            cooldownD = 0;
        }
        if (cooldownH > 300) {
            doneHeal = false;
            cooldownH = 0;
        }
        cooldownD++;
        cooldownH++;
    }
}
