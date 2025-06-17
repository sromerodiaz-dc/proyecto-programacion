package com.game.ui.message;

import com.game.entity.Entity;
import com.game.entity.Player;
import com.game.ui.TeisPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Random;

import static java.lang.Math.clamp;

public class DynamicDamageMessage {
    String[] lines;
    int damageAmount;
    int msgX, msgY;
    int lifetime;
    int maxLifetime;
    Color color;
    float currentSize;
    Random random = new Random();
    boolean isZeroDamage;
    boolean isIronicalMessage = false;
    boolean useRainbowEffect;
    float initialSize = 20F;
    float maxSize = 60F;
    int margin = 40;
    int screenWidth = TeisPanel.screenWidth;
    int screenHeight = TeisPanel.screenHeight;

    // Referencias al enemigo y al jugador
    private final Entity enemy;
    private final Player player;

    // Offset radial para posición alrededor del enemigo
    private int offsetX;
    private int offsetY;


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

    public DynamicDamageMessage(int damageAmount, Entity enemy, Player player) {
        this.damageAmount = damageAmount;
        this.lifetime = 0;
        this.enemy = enemy;
        this.player = player;

        float vidaPrev = enemy.life + damageAmount;

        if (damageAmount <= 0.2f * vidaPrev) {
            this.isZeroDamage = true;
            System.out.println("Poco daño");
            if (random.nextFloat() < 0.25f) {
                // Mensaje irónico (posición fija)
                int messageIndex = random.nextInt(ZERO_DAMAGE_MESSAGES.length);
                String rawText = ZERO_DAMAGE_MESSAGES[messageIndex];
                this.lines = rawText.split("\n");

                this.currentSize = 22F;
                this.color = Color.LIGHT_GRAY;
                this.maxLifetime = 150;
                this.useRainbowEffect = random.nextFloat() < 0.3f;
                this.isIronicalMessage = true;

                // Offset cero para mensajes fijos
                this.offsetX = 0;
                this.offsetY = 0;
            } else {
                // Daño normal
                this.lines = new String[]{String.valueOf(damageAmount)};
                float damageRatio = Math.min(1.0f, (float) damageAmount / 300.0f);
                this.currentSize = initialSize + (maxSize - initialSize) * damageRatio;
                this.color = Color.LIGHT_GRAY;
                this.maxLifetime = 50;
                this.useRainbowEffect = false;

                // Generar offset radial
                this.offsetX = generateRadialOffsetX();
                this.offsetY = generateRadialOffsetY();
            }
        } else if (damageAmount >= 0.85f * vidaPrev){
            System.out.println("daño mayor");
            // Daño normal
            this.isZeroDamage = false;
            this.lines = new String[]{String.valueOf(damageAmount)};
            this.color = Color.WHITE;

            float damageRatio = Math.min(1.0f, (float) damageAmount / 300.0f);
            this.currentSize = initialSize + (maxSize - initialSize) * damageRatio;
            this.maxLifetime = 60;
            this.useRainbowEffect = false;

            // Generar offset radial
            this.offsetX = generateRadialOffsetX();
            this.offsetY = generateRadialOffsetY();
        }
        updatePosition();
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

    private int generateRadialOffsetX() {
        double angle = random.nextDouble() * 2 * Math.PI;
        double distance = 30 + random.nextDouble() * 30;
        return (int) (Math.cos(angle) * distance);
    }

    private int generateRadialOffsetY() {
        double angle = random.nextDouble() * 2 * Math.PI;
        double distance = 30 + random.nextDouble() * 30;
        return (int) (Math.sin(angle) * distance);
    }

    private void updatePosition() {
        if (isIronicalMessage) {
            // Posición fija centrada
            this.msgX = screenWidth / 2 - 100;
            this.msgY = screenHeight / 2;
        } else {
            // Obtener el centro del enemigo
            int enemyCenterX = enemy.worldX + (enemy.width / 2);
            int enemyCenterY = enemy.worldY + (enemy.height / 2);

            // Posición relativa al centro del enemigo
            int screenX = enemyCenterX - player.worldX + player.getScreenX();
            int screenY = enemyCenterY - player.worldY + player.getScreenY();

            this.msgX = clamp(screenX + offsetX, margin, screenWidth - margin);
            this.msgY = clamp(screenY + offsetY, margin, screenHeight - margin);
        }
    }

    public void update() {
        lifetime++;
        updatePosition();
        int speed = (isZeroDamage && !isIronicalMessage) ? 4 : 2;
        msgY -= (lifetime / speed);
    }

    public boolean isAlive() {
        return lifetime < maxLifetime;
    }
}
