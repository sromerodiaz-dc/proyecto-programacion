//package com.game.entity.NewEntity;
//
//import com.game.entity.Entity;
//
//public class MovementComponent {
//    private final AEntity parent;
//    private int spriteInterval = 10;
//    private int spriteCounter = 0;
//    private int spriteState = 1;
//
//    public MovementComponent(Entity parent) {
//        this.parent = parent;
//    }
//
//    public void updatePosition(char direction) {
//        if (!parent.collision.isColliding()) {
//            switch (direction) {
//                case 'w' -> parent.worldY -= parent.speed;
//                case 's' -> parent.worldY += parent.speed;
//                case 'a' -> parent.worldX -= parent.speed;
//                case 'd' -> parent.worldX += parent.speed;
//            }
//            updateSpriteState();
//        }
//    }
//
//    private void updateSpriteState() {
//        if (++spriteCounter > spriteInterval) {
//            spriteState = (spriteState == 1) ? 2 : 1;
//            spriteCounter = 0;
//        }
//    }
//}