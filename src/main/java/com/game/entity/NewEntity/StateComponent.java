//package com.game.entity.NewEntity;
//
//// ------------------ Componente de Estado ------------------ //
//public class StateComponent {
//    private int maxLife;
//    private int currentLife;
//    private boolean invincible;
//    private boolean alive;
//    private boolean dying;
//
//    public StateComponent() {
//        this.alive = true;
//    }
//
//    public void initialize(int maxLife) {
//        this.maxLife = this.currentLife = maxLife;
//    }
//
//    public void takeDamage(int damage) {
//        if (!invincible && alive) {
//            currentLife = Math.max(currentLife - damage, 0);
//            invincible = true;
//            if (currentLife == 0) setDying();
//        }
//    }
//
//    private void setDying() {
//        dying = true;
//        alive = false;
//    }
//
//    // Getters
//    public boolean isAlive() { return alive; }
//    public boolean isInvincible() { return invincible; }
//}