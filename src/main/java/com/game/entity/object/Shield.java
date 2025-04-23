package com.game.entity.object;

import com.game.controller.TeisPanel;
import com.game.data.Properties;
import com.game.entity.Entity;

public class Shield extends Entity {
    /**
     * Constructor parametrizado
     *
     * @param teisPanel  gráfico del juego
     */
    public Shield(TeisPanel teisPanel, Properties properties) {
        super(teisPanel, properties);

        name = "Fent";
        down1 = setEntitySprite("objects/shield.png",32,32);
        defenseVal = 1;
    }
}
