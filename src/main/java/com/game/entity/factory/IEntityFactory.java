package com.game.entity.factory;

import com.game.ui.TeisPanel;
import com.game.entity.Entity;
import com.game.data.Properties;

public interface IEntityFactory {
    Entity createEntity(EntityType entityType, TeisPanel teisPanel, Properties properties, int worldX, int worldY);
}
