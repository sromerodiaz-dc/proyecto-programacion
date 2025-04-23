package com.game.ui;

import com.game.controller.GameController;
import com.game.entity.Entity;
import com.game.data.Properties;
import com.game.controller.TeisPanel;
import com.game.entity.stats.Vida;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserInterface {
    private static final List<String> DEFAULT_TITLES = List.of(
            "Teis non\né Chapela.",
            "\"É Vigo\nmáis ca\nun dinoseto?\"",
            "Concello de\nTeis:\nO XOGO",
            "\"eres de Cangas\"",
            "Bombardeen a\nUVigo",
            "Bombardeen o\nVialia",
            "\"Porriño pertence\na Mos\"",
            "V de Vitrasa!"
    );

    private final TeisPanel teisPanel;
    private final Font pixeledFont;
    private final BufferedImage vidaFull, vidaHalf, vidaEmpty;

    private Graphics2D g2;
    private String title;
    public String dialogo;
    private int messageTime = 0;
    private boolean isFinished = false;
    public int titleCounter = 1;

    public UserInterface(TeisPanel teisPanel, Properties properties) {
        this.teisPanel = teisPanel;
        this.pixeledFont = loadFont();
        getRandomTitle();

        Entity vida = new Vida(teisPanel, properties);
        this.vidaFull = vida.image;
        this.vidaHalf = vida.image2;
        this.vidaEmpty = vida.image3;
    }

    private Font loadFont() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("font/newPixeledFont.ttf")) {
            if (is == null) throw new IOException("Font resource not found");
            return Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException("Failed to load font", e);
        }
    }

    public void getRandomTitle() {
        title = DEFAULT_TITLES.get(new Random().nextInt(DEFAULT_TITLES.size()));
    }

    public void showMessage(String message) {
        boolean messageOn = true;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(pixeledFont);
        g2.setColor(Color.WHITE);

        switch (teisPanel.controller.currentGameState) {
            case GameController.GameState.LOAD:
                drawLoadingScreen();
                break;
            case GameController.GameState.PLAY:
                drawPlayerLife();
                break;
            case GameController.GameState.PAUSE:
                drawPlayerLife();
                drawPauseScreen();
                break;
            case GameController.GameState.DIALOG:
                drawPlayerLife();
                drawDialog();
                break;
            case GameController.GameState.STATS:
                drawCharacterScreen();
                break;
        }
    }

    public void drawPlayerLife() {
        int x = teisPanel.sizeFinal / 2;
        int y = teisPanel.sizeFinal / 2;

        // Draw empty hearts for max life
        for (int i = 0; i < teisPanel.model.maxLife / 2; i++) {
            g2.drawImage(vidaEmpty, x, y, null);
            x += teisPanel.sizeFinal;
        }

        // Draw current life
        x = teisPanel.sizeFinal / 2;
        for (int i = 0; i < teisPanel.model.life; i++) {
            g2.drawImage((i % 2 == 0) ? vidaHalf : vidaFull, x, y, null);
            if (i % 2 != 0) x += teisPanel.sizeFinal;
        }
    }

    private void drawLoadingScreen() {
        g2.setFont(pixeledFont.deriveFont(Font.BOLD, 75F));
        int y = teisPanel.screenHeight / 3;

        for (String line : title.split("\n")) {
            int x = getCenteredX(line);
            drawTextWithShadow(line, x, y);
            y += 100;
        }

        drawMenuOptions();
    }

    private void drawMenuOptions() {
        g2.setFont(pixeledFont.deriveFont(Font.BOLD, 30F));
        g2.setColor(Color.YELLOW);

        String[] options = {"SAÍR", "DALLE", "CARGAR PARTIDA"};
        int y = teisPanel.screenHeight - 100;

        // Draw first two options on same line
        drawMenuOption(options[0], (int)(teisPanel.screenWidth * 0.87), y, 0);
        drawMenuOption(options[1], (int)(teisPanel.screenWidth * 0.15), y, 1);

        // Draw third option below
        drawMenuOption(options[2], getCenteredX(options[2]), y + 50, 2);
    }

    private void drawMenuOption(String text, int x, int y, int optionIndex) {
        g2.drawString(text, x, y);
        if (titleCounter == optionIndex) {
            g2.drawString(">>", x - teisPanel.sizeFinal, y);
        }
    }

    private void drawPauseScreen() {
        g2.setFont(pixeledFont.deriveFont(Font.PLAIN, 80));
        String text = "PAUSA";
        g2.drawString(text, getCenteredX(text), teisPanel.screenHeight / 2);
    }

    private void drawDialog() {
        int x = teisPanel.sizeFinal * 2;
        int y = teisPanel.sizeFinal / 2;
        int width = teisPanel.screenWidth - teisPanel.sizeFinal * 4;
        int height = teisPanel.sizeFinal * 5;

        drawWindow(x, y, width, height);

        g2.setFont(pixeledFont.deriveFont(Font.PLAIN, 22));
        x += teisPanel.sizeFinal;
        y += teisPanel.sizeFinal;

        for (String line : dialogo.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawCharacterScreen() {
        final int frameXY = teisPanel.sizeFinal * 2;
        final int frameWidth = teisPanel.sizeFinal * 8;
        final int frameHeight = teisPanel.sizeFinal * 5;
        final int labelX = frameXY + 40;
        final int valueX = frameWidth - 40;
        int labelY = frameXY + 40;
        g2.setColor(Color.WHITE);

        String[] labels = {
                "Nivel",
                "Vida",
                "Herramienta\ncorporativa",
                "Fentanilo\nen sangre"
        };

        String[] values = {
                String.valueOf(teisPanel.model.getLevel()),
                String.valueOf(teisPanel.model.getLife()),
                String.valueOf(teisPanel.model.getAttackVal()),
                String.valueOf(teisPanel.model.getDefenseVal())
        };

        // FRAME
        drawWindow(frameXY, frameXY, frameWidth, frameHeight);

        // NAMES
        drawStringOrdenado(labelX, labelY, labels, 24F);

        // VALUES
        drawStringOrdenado(valueX, labelY, values, 28F);
    }

    // Ahora acepta un tamaño de fuente también
    private void drawStringOrdenado(int x, int y, String[] texts, float fontSize) {
        g2.setFont(g2.getFont().deriveFont(fontSize));

        for (String text : texts) {
            String[] lines = text.split("\n");

            for (String line : lines) {
                g2.drawString(line, x, y);
                y += 30; // separación entre líneas
            }

            y += 10; // espacio extra entre bloques
        }
    }

    private void drawEquipmentImages(int x, int y) {
        g2.drawImage(teisPanel.model.getCurrentWeapon().down1, x, y, null);
        g2.drawImage(teisPanel.model.getCurrentShield().down1, x + teisPanel.sizeFinal, y + 10, null);
    }

    private void drawWindow(int x, int y, int width, int height) {
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(x, y, width, height, 35, 35);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }


    private void drawTextWithShadow(String text, int x, int y) {
        g2.setColor(Color.GRAY);
        g2.drawString(text, x + 5, y + 5);
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
    }

    private int getCenteredX(String text) {
        return teisPanel.screenWidth / 2 - g2.getFontMetrics().stringWidth(text) / 2;
    }

    public int getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(int messageTime) {
        this.messageTime = messageTime;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}