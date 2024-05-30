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
        left1 = setEntitySprite("npc/enemies/dinoseto_left1.png");
        left2 = setEntitySprite("npc/enemies/dinoseto_left2.png");
        right1 = setEntitySprite("npc/enemies/dinoseto_right1.png");
        right2 = setEntitySprite("npc/enemies/dinoseto_right2.png");
        up1 = setEntitySprite("npc/enemies/dinoseto_right1.png");
        up2 = setEntitySprite("npc/enemies/dinoseto_right2.png");
        down1 = setEntitySprite("npc/enemies/dinoseto_right1.png");
        down2 = setEntitySprite("npc/enemies/dinoseto_right2.png");
        stop = setEntitySprite("npc/enemies/dinoseto_right2.png");
        stop2 = setEntitySprite("npc/enemies/dinoseto_right2.png");
    }

    public void setEvent() {
        capEvent++;
        if (capEvent == 120) {
            sentido = drawUtils.moveRandomEntity();
            capEvent = 0;
        }
    }
}
