package com.game.ui;

import com.game.controller.TeisPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Random;

import static java.lang.Math.clamp;

public class DamageDinamicMessages {
    String[] lines;
    int damageAmount;
    int msgX, msgY;
    int initialY;
    int lifetime;
    int maxLifetime;
    Color color;
    float currentSize;
    Random random = new Random();
    boolean isZeroDamage = false;
    boolean isIronicalMessage = false;
    boolean useRainbowEffect = false;
    float initialSize = 20F;
    float maxSize = 60F;
    int margin = 40;
    int screenWidth = TeisPanel.screenWidth;
    int screenHeight = TeisPanel.screenHeight;

    // Lista de mensajes irónicos
    private static final String[] ZERO_DAMAGE_MESSAGES = {
            "YN dealt 0 dmg...",
            "Cómo síntese saber que\no Celta podería facer máis?",
            "Estas to' bugueao",
            "XD",
            "...vas con lag? el jueguito\nno va ni con internete",
            "manin?",
            "Ya si eso haces algo cuando\nrespawnees, no te rayes bro",
            "Prueba con otra build que\nno sea inútil, primo",
            "GG",
            "[**gg**]",
            "Abusadol",
            "Uno es pila bakano con to' y\nal final le hacen pila\n'e palomerias, sabes?",
            "Welcome to the\nuseless player\ncompilation",
            "Te has parado a pensar\nalguna vez que igual\nel npc eres tú?",
            "Cada vez que pienses bien de ti\nrecuerda que una secuencia\nde unos y ceros está pudiendo contigo",
            "aplicaste tu mejor 999\ny te llevaste un acceso\ndirecto al respawn manin",
            "bro estás sudao ya, respira un poco no?\nfokin binario",
            "0 DMG, -100000000 aura, 0 InfoJobs,\n0 curriculums, 0 skill, 0 hoes,\n0 nómina, 0 kills, 0 racks"
    };

    public DamageDinamicMessages(int damageAmount, int enemyX, int enemyY, int playerX, int playerY, int screenX, int screenY) {
        this.damageAmount = damageAmount;
        this.lifetime = 0;

        if (damageAmount == 0) {
            this.isZeroDamage = true;

            if (random.nextFloat() < 0.25f) {
                // 🎯 Mensaje irónico - aparece en HUD (posición fija)
                int messageIndex = random.nextInt(ZERO_DAMAGE_MESSAGES.length);
                String rawText = ZERO_DAMAGE_MESSAGES[messageIndex];
                this.lines = rawText.split("\n");

                this.currentSize = 22F;
                this.color = Color.LIGHT_GRAY;
                this.maxLifetime = 150;
                this.useRainbowEffect = random.nextFloat() < 0.3f;
                this.isIronicalMessage = true; // ✅ <- esto faltaba

            } else {
                // 🎯 Daño 0 sin mensaje - solo "0", debe salir cerca del enemigo
                this.lines = new String[]{"0"};
                this.currentSize = 30F;
                this.color = Color.LIGHT_GRAY;
                this.maxLifetime = 80;
                this.useRainbowEffect = false;

                // ✅ POSICIÓN CERCA DEL ENEMIGO
            }
            this.msgX = clamp(enemyX - playerX + screenX + random.nextInt(40) - 20, margin, screenWidth - margin);
            this.msgY = clamp(enemyY - playerY + screenY + random.nextInt(20) - 10, margin, screenHeight - margin);
        } else {
            // 🎯 Daño normal - aparece cerca del enemigo
            this.isZeroDamage = false;
            this.lines = new String[]{String.valueOf(damageAmount)};
            this.color = Color.WHITE;

            float damageRatio = Math.min(1.0f, (float) damageAmount / 500.0f);
            this.currentSize = initialSize + (maxSize - initialSize) * damageRatio;
            this.maxLifetime = 60;
            this.useRainbowEffect = false;

            // ✅ POSICIÓN CERCA DEL ENEMIGO
            this.msgX = clamp(enemyX - playerX + screenX + random.nextInt(40) - 20, margin, screenWidth - margin);
            this.msgY = clamp(enemyY - playerY + screenY + random.nextInt(20) - 10, margin, screenHeight - margin);
        }
        this.initialY = msgY;
    }

    public void update() {
        lifetime++;
        int speed = (isZeroDamage && !isIronicalMessage) ? 4 : 2; // Los mensajes "0" cerca enemigo suben más lento
        msgY = initialY - (lifetime / speed);
    }

    public void draw(Graphics2D g2) {
        if (!isAlive()) return;

        float alpha = 1.0f - ((float) lifetime / maxLifetime);
        alpha = Math.max(0, Math.min(1, alpha));
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, currentSize));

        for (int i = 0; i < lines.length; i++) {
            int lineY = msgY + i * (int) currentSize;

            if (isZeroDamage && useRainbowEffect) {
                float hue = (lifetime * 0.03f + i * 0.1f) % 1.0f;
                Color shadowColor = Color.getHSBColor(hue, 1.0f, 1.0f);
                Color dynamicShadow = new Color(
                        shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(), (int) (alpha * 200)
                );
                g2.setColor(dynamicShadow);
                g2.drawString(lines[i], msgX + 2, lineY + 2);
            } else {
                g2.setColor(new Color(0, 0, 0, (int) (alpha * 128)));
                g2.drawString(lines[i], msgX + 2, lineY + 2);
            }

            Color drawColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255));
            g2.setColor(drawColor);
            g2.drawString(lines[i], msgX, lineY);
        }
    }

    public boolean isAlive() {
        return lifetime < maxLifetime;
    }
}
