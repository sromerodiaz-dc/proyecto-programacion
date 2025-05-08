/*
package com.game.entity;

import com.game.controller.TeisPanel;
import com.game.data.Properties;
import com.game.entity.NewEntity.CollisionComponent;
import com.game.entity.NewEntity.MovementComponent;
import com.game.entity.NewEntity.RenderComponent;
import com.game.entity.NewEntity.StateComponent;

import java.awt.*;

//TODO implementas las clases de abajo
 *     EntityFactory - Para creación de entidades con diferentes configuraciones
 *     AnimationManager - Manejo especializado de animaciones
 *     DialogueComponent - Gestión de diálogos NPC
 *     AIBehavior - Comportamientos inteligentes para NPCs/Enemigos
 *     AttackComponent - Gestión de sistema de combate
 *     InventoryComponent - Manejo de objetos/inventario

//TODO Siguiente paso: Implementar las clases concretas de entidades (Player, NPC, Enemy) que heredarán de Entity y usarán estos componentes de manera específica.

public abstract class AEntity {
    protected final TeisPanel teisPanel;
    protected final Properties properties;
    protected final State state;
    protected final Movement movement;
    protected final Render render;
    protected final Collision collision;

    protected String name;
    protected int worldX, worldY;
    protected int speed;
    protected char direction = '0';

    public Entity(TeisPanel teisPanel, Properties properties) {
        this.teisPanel = teisPanel;
        this.properties = properties;
        this.state = new State();
        this.movement = new Movement(this);
        this.render = new Render(this);
        this.collision = new Collision(this);
    }

    public abstract void update();
    public abstract void draw(Graphics2D g2);

    // Getters base
    public int getWorldX() { return worldX; }
    public int getWorldY() { return worldY; }
    public char getDirection() { return direction; }
    public State getState() { return state; }
}
*/