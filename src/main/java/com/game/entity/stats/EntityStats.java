package com.game.entity.stats;

public class EntityStats {
    private int level;
    private int strength;
    private int dexterity;
    private int vitality;
    private int baseAttack;
    private int baseDefense;
    private int exp;
    private int nextLevelThreshold;
    private int baseSpeed;
    private int speedBuff;
    private int speedDebuff;

    private EntityStats(Builder builder) {
        this.strength = builder.strength;
        this.dexterity = builder.dexterity;
        this.vitality = builder.vitality;
        this.baseAttack = builder.baseAttack;
        this.baseDefense = builder.baseDefense;
        this.baseSpeed = builder.baseSpeed;
        this.level = 1;
        this.exp = 0;
        this.nextLevelThreshold = 100;
        this.speedBuff = 0;
        this.speedDebuff = 0;
    }

    public static class Builder {
        private int strength;
        private int dexterity;
        private int vitality;
        private int baseAttack;
        private int baseDefense;
        private int baseSpeed;

        public Builder strength(int strength) {
            if(strength < 0) throw new IllegalArgumentException("Strength cannot be negative");
            this.strength = strength;
            return this;
        }

        public Builder dexterity(int dexterity) {
            if(dexterity < 0) throw new IllegalArgumentException("Dexterity cannot be negative");
            this.dexterity = dexterity;
            return this;
        }

        public Builder vitality(int vitality) {
            if(vitality < 0) throw new IllegalArgumentException("Vitality cannot be negative");
            this.vitality = vitality;
            return this;
        }

        public Builder baseAttack(int baseAttack) {
            if(baseAttack < 0) throw new IllegalArgumentException("BaseAttack cannot be negative");
            this.baseAttack = baseAttack;
            return this;
        }

        public Builder baseDefense(int baseDefense) {
            if(baseDefense < 0) throw new IllegalArgumentException("BaseDefense cannot be negative");
            this.baseDefense = baseDefense;
            return this;
        }

        public Builder baseSpeed(int baseSpeed) {
            if(baseSpeed < 0) throw new IllegalArgumentException("BaseSpeed cannot be negative");
            this.baseSpeed = baseSpeed;
            return this;
        }

        public EntityStats build() {
            return new EntityStats(this);
        }
    }

    public void addExp(int amount) {
        if(amount < 0) throw new IllegalArgumentException("Experience cannot be negative");
        exp += amount;
        checkLevelUp();
    }

    private void checkLevelUp() {
        while (exp >= nextLevelThreshold && level < 100) {
            levelUp();
        }
    }

    public void levelUp() {
        level++;
        exp -= nextLevelThreshold;

        // Incremento controlado del umbral
        nextLevelThreshold += (int) (nextLevelThreshold * 0.3);

        // Aumento base de estadísticas
        strength += 1;
        dexterity += 1;
        vitality += 1;

        // Bonus cada 5 niveles
        if(level % 5 == 0) {
            strength += 1;
            dexterity += 1;
            vitality += 1;
        }
    }

    public int calculateAttack() {
        return baseAttack + (strength * 2) + (dexterity / 2);
    }

    public int calculateDefense() {
        return baseDefense + (dexterity * 2);
    }

    public int calculateMaxHealth() {
        return 100 + (level * 20) + (vitality * 10);
    }

    public void setBaseAttack(int baseAttack) {
        this.baseAttack = baseAttack;
    }

    public void setBaseDefense(int baseDefense) {
        this.baseDefense = baseDefense;
    }

    public int getCurrentSpeed() {
        int speed = baseSpeed + speedBuff - speedDebuff;
        return Math.max(0, speed); // Velocidad nunca negativa
    }

    public void addSpeedBuff(int amount) {
        this.speedBuff += amount;
    }

    public void addSpeedDebuff(int amount) {
        this.speedDebuff += amount;
    }

    // Getters
    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getVitality() {
        return vitality;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int getNextLevelThreshold() {
        return nextLevelThreshold;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getBaseDefense() {
        return baseDefense;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }
}