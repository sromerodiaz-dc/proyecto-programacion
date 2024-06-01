package GAME.ENTITY.ENEMY;

import GAME.ENTITY.Entity;
import GAME.GAME.TeisPanel;

public class Dinoseto extends Entity {
    public Dinoseto (TeisPanel teisPanel) {
        super(teisPanel);

        who = 2; // 2 = enemy

        id = "Dinoseto_elegante";

        sentido = '0';

        speed = 1;
        maxLife = 20;
        life = maxLife;

        height = teisPanel.sizeFinal * 2;
        width = height - 5;
        solidArea.x = 10;
        solidArea.y = 25;
        solidArea.width = width - 25;
        solidArea.height = height - 35;
        defaultSolidAreaX = solidArea.x;
        defaultSolidAreaY = solidArea.y;

        intervalo = 15;

        getImage();
    }

    public void getImage() {
        left1 = setEntitySprite("npc/enemies/dinoseto_left1.png", width, height);
        left2 = setEntitySprite("npc/enemies/dinoseto_left2.png", width, height);
        right1 = setEntitySprite("npc/enemies/dinoseto_right1.png", width, height);
        right2 = setEntitySprite("npc/enemies/dinoseto_right2.png", width, height);
        up1 = setEntitySprite("npc/enemies/dinoseto_right1.png", width, height);
        up2 = setEntitySprite("npc/enemies/dinoseto_right2.png", width, height);
        down1 = setEntitySprite("npc/enemies/dinoseto_right1.png", width, height);
        down2 = setEntitySprite("npc/enemies/dinoseto_right2.png", width, height);
        stop = setEntitySprite("npc/enemies/dinoseto_stop1.png", width, height);
        stop2 = setEntitySprite("npc/enemies/dinoseto_stop2.png", width, height);
    }

    public void setEvent() {
        capEvent++;
        if (capEvent == 120) {
            sentido = drawUtils.moveRandomEntity();
            capEvent = 0;
        }
    }
}
