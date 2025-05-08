//package com.game.entity.NewEntity;
//
//import com.game.entity.Entity;
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//
//// ------------------ Componente de Renderizado ------------------ //
//public class RenderComponent {
//    private final AEntity parent;
//
//    public RenderComponent(Entity parent) {
//        this.parent = parent;
//    }
//
//    public void drawEntity(Graphics2D g2, BufferedImage image) {
//        int screenX = parent.worldX - parent.teisPanel.model.worldX + parent.teisPanel.model.screenX;
//        int screenY = parent.worldY - parent.teisPanel.model.worldY + parent.teisPanel.model.screenY;
//
//        applyStateEffects(g2);
//        g2.drawImage(image, screenX, screenY, null);
//        resetEffects(g2);
//    }
//
//    private void applyStateEffects(Graphics2D g2) {
//        if (parent.state.isInvincible()) {
//            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
//        }
//    }
//
//    private void resetEffects(Graphics2D g2) {
//        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
//    }
//}