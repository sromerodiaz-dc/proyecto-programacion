package com.game.entity;

import com.game.data.Properties;
import com.game.controller.TeisPanel;
import com.game.entity.factory.EntityFactory;
import com.game.entity.factory.EntityType;

public class Placer {
    private final TeisPanel teisPanel;
    private final Properties propierties;
    private final EntityFactory entityFactory;

    public Placer(TeisPanel teisPanel, Properties propierties) {
        this.teisPanel = teisPanel;
        this.propierties = propierties;
        this.entityFactory = new EntityFactory();
    }

    public void setRecursos() {
        setNPC();
        setEnemy();
    }

    public void setNPC() {
        Entity npc = entityFactory.createEntity(EntityType.VIELLO, teisPanel, propierties, teisPanel.sizeFinal * 19,teisPanel.sizeFinal * 12);
        teisPanel.controller.npc.add(npc);
    }

    public void setEnemy() {
        Entity enemy = entityFactory.createEntity(EntityType.DINOSETO, teisPanel, propierties, teisPanel.sizeFinal * 5, teisPanel.sizeFinal * 12);
        teisPanel.controller.enemy.add(enemy);
    }
}
