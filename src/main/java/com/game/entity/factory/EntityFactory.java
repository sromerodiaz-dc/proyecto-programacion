package com.game.entity.factory;

import com.game.ui.TeisPanel;
import com.game.data.Properties;
import com.game.entity.Entity;
import com.game.entity.npc.aggressive.Dinoseto;
import com.game.entity.npc.passive.Viello;

public class EntityFactory implements IEntityFactory {
    @Override
    public Entity createEntity(EntityType entityType, TeisPanel teisPanel, Properties properties, int worldX, int worldY) {
        return switch (entityType) {
            case VIELLO -> new Viello(teisPanel, properties, worldX, worldY);
            case DINOSETO -> new Dinoseto(teisPanel, properties, worldX, worldY);
            default -> throw new IllegalArgumentException("Tipo de entidad invalido");
        };
    }
}
