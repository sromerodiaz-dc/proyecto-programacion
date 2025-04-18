package com.game.entity.factory;

import com.game.controller.TeisPanel;
import com.game.data.Properties;
import com.game.entity.Entity;
import com.game.entity.enemy.Dinoseto;
import com.game.entity.npc.Viello;

public class EntityFactory implements IEntityFactory {
    @Override
    public Entity createEntity(EntityType entityType, TeisPanel teisPanel, Properties properties, int worldX, int worldY) {
        return switch (entityType) {
            case VIELLO -> new Viello(teisPanel, properties, worldX, worldY);
            case DINOSETO -> new Dinoseto(teisPanel, properties);
            default -> throw new IllegalArgumentException("Tipo de entidad invalido");
        };
    }
}
