package com.game.ui.message;

import com.game.entity.Entity;
import com.game.entity.Player;

import java.awt.*;

public class DynamicMessage {
    private final String message;
    private final Entity entity;
    private final Player player;
    private int lifetime;
    private final int maxLifetime = 180; // 3 segundos a 60 FPS
    private int x, y;
    private float alpha = 1.0f;
    private final Color color = Color.LIGHT_GRAY;

    public DynamicMessage(String message, Entity entity, Player player) {
        this.message = message;
        this.entity = entity;
        this.player = player;
        this.lifetime = 0;
        updatePosition();
    }

    public void update() {
        lifetime++;
        updatePosition();
        alpha = 1.0f - ((float) lifetime / maxLifetime);
        y -= 1; // Mover hacia arriba
    }

    private void updatePosition() {
        // Posición centrada sobre el NPC
        int entityCenterX = entity.worldX + (entity.width / 2);
        int entityTopY = entity.worldY - 10; // 10px arriba del NPC

        x = entityCenterX - player.worldX + player.getScreenX();
        y = entityTopY - player.worldY + player.getScreenY();
    }

    public void draw(Graphics2D g2) {
        // Configurar fuente más pequeña
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 14F)); // Tamaño reducido

        // Calcular posición centrada
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(message);
        int centeredX = x - (textWidth / 2);

        // Dibujar texto centrado
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(0, 0, 0, (int)(alpha * 128)));
        g2.drawString(message, centeredX + 1, y + 1); // Sombra

        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 255)));
        g2.drawString(message, centeredX, y); // Texto principal

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public boolean isAlive() {
        return lifetime < maxLifetime;
    }
}