package com.game.entity.object;

import com.game.controller.TeisPanel;
import com.game.data.Properties;
import com.game.entity.Entity;

public class Weapon extends Entity {
    /**
     * Constructor parametrizado
     *
     * @param teisPanel  gráfico del juego
     */
    public Weapon(TeisPanel teisPanel, Properties properties) {
        super(teisPanel, properties);

        name = "Corporative BarberCut";
        down1 = setEntitySprite("objects/sword.png",32,32);
        attackVal = 1;
    }
}
