package com.game.entity;

import com.game.data.Properties;
import com.game.ui.TeisPanel;
import com.game.entity.factory.EntityFactory;
import com.game.entity.factory.EntityType;

public class EntityPlacer {
    private final TeisPanel teisPanel;
    private final Properties propierties;
    private final EntityFactory entityFactory;

    private final int SIZE_FINAL = TeisPanel.SIZE_FINAL;

    public EntityPlacer(TeisPanel teisPanel, Properties propierties) {
        this.teisPanel = teisPanel;
        this.propierties = propierties;
        this.entityFactory = new EntityFactory();
    }

    public void setRecursos() {
        setNPC();
        setEnemy();
    }

    public void setNPC() {
        Entity npc = entityFactory.createEntity(EntityType.VIELLO, teisPanel, propierties, SIZE_FINAL * 19,SIZE_FINAL * 12);
        teisPanel.controller.npc.add(npc);
    }

    public void setEnemy() {
        Entity enemy = entityFactory.createEntity(EntityType.DINOSETO, teisPanel, propierties, SIZE_FINAL * 5, SIZE_FINAL * 12);
        Entity enemy2 = entityFactory.createEntity(EntityType.DINOSETO, teisPanel, propierties, SIZE_FINAL * 15, SIZE_FINAL * 10);
        teisPanel.controller.enemy.add(enemy);
        teisPanel.controller.enemy.add(enemy2);
    }

    private Entity generateEntity(int x, int y) {
        return entityFactory.createEntity(EntityType.DINOSETO, teisPanel, propierties, SIZE_FINAL * x, SIZE_FINAL * y);
    }
}