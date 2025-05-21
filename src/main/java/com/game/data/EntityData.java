package com.game.data;

public class EntityData {
    public String id;
    public int who;
    public int speed;
    public int width;
    public int height;
    public int[] solidArea;
    public int maxLife;
    public Stats baseStats;
    public Sprites sprites;

    public static class Stats {
        public int strength;
        public int dexterity;
        public int vitality;
        public int baseAttack;
        public int baseDefense;
    }

    public static class Sprites {
        public String up1;
        public String up2;

    }
}