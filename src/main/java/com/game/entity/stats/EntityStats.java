package com.game.entity.stats;

public class EntityStats {
    private int level;
    private int strength;
    private int dexterity;
    private final int baseAttack;
    private final int baseDefense;
    private int exp;
    private int nextLevelThreshold;
    private int baseSpeed;

    private EntityStats(Builder builder) {
        this.strength = builder.strength;
        this.dexterity = builder.dexterity;
        this.baseAttack = builder.baseAttack;
        this.baseDefense = builder.baseDefense;
        this.baseSpeed = builder.baseSpeed;
        this.level = 1;
        this.exp = 0;
        this.nextLevelThreshold = 100;
    }

    public static class Builder {
        private int strength;
        private int dexterity;
        private int baseAttack;
        private int baseDefense;
        private int baseSpeed;

        public Builder strength(int strength) {
            this.strength = strength;
            return this;
        }

        public Builder dexterity(int dexterity) {
            this.dexterity = dexterity;
            return this;
        }

        public Builder baseAttack(int baseAttack) {
            this.baseAttack = baseAttack;
            return this;
        }

        public Builder baseDefense(int baseDefense) {
            this.baseDefense = baseDefense;
            return this;
        }

        public Builder baseSpeed(int baseSpeed) {
            this.baseSpeed = baseSpeed;
            return this;
        }

        public EntityStats build() {
            return new EntityStats(this);
        }
    }

    /**
     * Añade experiencia y verifica si se sube de nivel.
     */
    public void addExp(int amount) {
        exp += amount;
        checkLevelUp();
    }

    /**
     * Verifica si la experiencia acumulada alcanza el umbral para subir de nivel.
     */
    private void checkLevelUp() {
        while (exp >= nextLevelThreshold) {
            levelUp();
        }
    }

    /**
     * Incrementa el nivel y ajusta los umbrales de experiencia.
     */
    public void levelUp() {
        level++;
        exp -= nextLevelThreshold;
        nextLevelThreshold = (int) (nextLevelThreshold * 1.5); // Incremento exponencial
        strength += 1;
        dexterity += 1;
    }

    /**
     * Calcula el ataque total (base + modificadores de atributos).
     */
    public int calculateAttack() {
        return (baseAttack + strength) * dexterity;
    }

    /**
     * Calcula la defensa total (base + modificadores de atributos).
     */
    public int calculateDefense() {
        return baseDefense * dexterity;
    }

    // Getters
    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
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