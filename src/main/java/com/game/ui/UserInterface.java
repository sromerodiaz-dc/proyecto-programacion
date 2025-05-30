package com.game.ui;

import com.game.data.GameState;
import com.game.entity.Entity;
import com.game.data.Properties;
import com.game.entity.Player;
import com.game.entity.npc.passive.Viello;
import com.game.entity.stats.Vida;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

public class UserInterface { //TODO desarrollar mensajes de daño, experiencia, etc. Implementar niveles de experiencia
    private final int SCREEN_WIDTH = TeisPanel.screenWidth;
    private final int SCREEN_HEIGHT = TeisPanel.screenHeight;

    private static final List<String> DEFAULT_TITLES = List.of(
            "Teis non\né Chapela.",
            "\"É Vigo\nmáis ca\nun dinoseto?\"",
            "Concello de\nTeis:\nO XOGO",
            "\"eres de Cangas\"",
            "Bombardeen a\nUVigo",
            "Bombardeen o\nVialia",
            "\"Porriño pertence\na Mos\"",
            "V de Vitrasa!",
            "\"Sonido de Teis\""
    );

    private final TeisPanel teisPanel;
    private final Font pixeledFont;
    private final BufferedImage vidaFull, vidaHalf, vidaEmpty;

    private final int SIZE_FINAL = TeisPanel.SIZE_FINAL;

    private Graphics2D g2;
    private String title;
    public String dialogo;
    private int messageTime = 0;
    private boolean isFinished = false;
    private ArrayList<String> messages = new ArrayList<>();
    private ArrayList<Integer> messageCounter = new ArrayList<>();
    private ArrayList<DamageDinamicMessages> damageMessages = new ArrayList<>();
    public int titleCounter = 1;

    public String[] dialogOptions = new String[0];
    public int selectedOption = 0;


    public UserInterface(TeisPanel teisPanel, Properties properties) {
        this.teisPanel = teisPanel;
        this.pixeledFont = loadFont();
        getRandomTitle();

        Entity vida = new Vida(teisPanel, properties);
        this.vidaFull = vida.image;
        this.vidaHalf = vida.image2;
        this.vidaEmpty = vida.image3;
    }

    public void addMessage(String message) {
        messages.add(message);
        messageCounter.add(1);
    }

    public void addMessage(int damage, Entity enemy, Player player) {
        damageMessages.add(new DamageDinamicMessages(damage, enemy, player));
    }

    private Font loadFont() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("graphic/font/newPixeledFont.ttf")) {
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
            case GameState.LOAD:
                drawLoadingScreen();
                break;
            case GameState.PLAY:
                drawPlayerLife();
                drawMessages();
                break;
            case GameState.PAUSE:
                drawPlayerLife();
                drawPauseScreen();
                break;
            case GameState.DIALOG:
                drawPlayerLife();
                drawDialog();
                break;
            case GameState.STATS:
                drawCharacterScreen();
                break;
        }
    }

    public void drawPlayerLife() {
        int x = SIZE_FINAL / 2;
        int y = SIZE_FINAL / 2;

        // Draw empty hearts for max life
        for (int i = 0; i < teisPanel.player.maxLife / 2; i++) {
            g2.drawImage(vidaEmpty, x, y, null);
            x += SIZE_FINAL;
        }

        // Draw current life
        x = SIZE_FINAL / 2;
        for (int i = 0; i < teisPanel.player.life; i++) {
            g2.drawImage((i % 2 == 0) ? vidaHalf : vidaFull, x, y, null);
            if (i % 2 != 0) x += SIZE_FINAL;
        }
    }

    private void drawMessages() {
        Iterator<DamageDinamicMessages> iterator = damageMessages.iterator();

        while (iterator.hasNext()) {
            DamageDinamicMessages msg = iterator.next();
            msg.update(); // Actualiza la posición, lifetime, etc.

            if (msg.isAlive()) {
                msg.draw(g2); // Dibuja el mensaje
            } else {
                iterator.remove(); // Elimina el mensaje si ha expirado
            }
        }
    }

    private void drawLoadingScreen() {
        g2.setFont(pixeledFont.deriveFont(Font.BOLD, 75F));
        int y = SCREEN_HEIGHT / 3;

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
        int y = SCREEN_HEIGHT - 100;

        // Draw first two options on same line
        drawMenuOption(options[0], (int)(SCREEN_WIDTH * 0.87), y, 0);
        drawMenuOption(options[1], (int)(SCREEN_WIDTH * 0.15), y, 1);

        // Draw third option below
        drawMenuOption(options[2], getCenteredX(options[2]), y + 50, 2);
    }

    private void drawMenuOption(String text, int x, int y, int optionIndex) {
        g2.drawString(text, x, y);
        if (titleCounter == optionIndex) {
            g2.drawString(">>", x - SIZE_FINAL, y);
        }
    }

    private void drawPauseScreen() {
        g2.setFont(pixeledFont.deriveFont(Font.PLAIN, 80));
        String text = "PAUSA";
        g2.drawString(text, getCenteredX(text), SCREEN_HEIGHT / 2);
    }

    // Actualizar drawDialog()
    private void drawDialog() {
        // Dibujar ventana de diálogo en la parte inferior
        int x = SIZE_FINAL * 2;
        int y = SCREEN_HEIGHT - SIZE_FINAL * 6;
        int width = SCREEN_WIDTH - SIZE_FINAL * 4;
        int height = SIZE_FINAL * 5;

        drawWindow(x, y, width, height);

        g2.setFont(pixeledFont.deriveFont(Font.PLAIN, 22));
        g2.setColor(Color.WHITE);

        Entity npc = teisPanel.controller.currentTalkingNpc;
        if (npc == null) return;

        // Manejo seguro de currentDialog
        String dialogText = npc.currentDialog != null ? npc.currentDialog : "";

        // Dibujar texto del diálogo con efecto de escritura
        int textY = y + SIZE_FINAL;

        // Manejo del efecto de escritura
        if (npc.isTyping) {
            npc.typingCounter++;
            if (npc.typingCounter >= 0.5f) { // Velocidad de escritura
                npc.typingCounter = 0;
                if (npc.typingIndex < dialogText.length()) {
                    npc.typingIndex++;
                } else {
                    npc.isTyping = false;
                }
            }

            // Mostrar texto parcial
            String partialText = dialogText.substring(0, npc.typingIndex);
            for (String line : partialText.split("\n")) {
                g2.drawString(line, x + SIZE_FINAL, textY);
                textY += g2.getFontMetrics().getHeight();
            }
        } else {
            // Mostrar texto completo
            for (String line : dialogText.split("\n")) {
                g2.drawString(line, x + SIZE_FINAL, textY);
                textY += g2.getFontMetrics().getHeight();
            }
        }

        // Dibujar opciones solo si no está escribiendo y hay opciones disponibles
        if (!npc.isTyping && npc instanceof Dialogable dialogableNpc) {
            List<String> options = dialogableNpc.getCurrentOptions();

            if (!options.isEmpty() && teisPanel.controller.currentGameState == GameState.DIALOG) {
                int optionY = textY + SIZE_FINAL;

                // Asegurarse de que hay opciones para mostrar
                for (int i = 0; i < options.size(); i++) {
                    String option = options.get(i);
                    if (option != null) {
                        if (i == npc.selectedOption) {
                            g2.setColor(Color.YELLOW);
                            g2.drawString("> " + option, x + SIZE_FINAL, optionY);
                        } else {
                            g2.setColor(Color.WHITE);
                            g2.drawString(option, x + SIZE_FINAL, optionY);
                        }
                        optionY += g2.getFontMetrics().getHeight();
                    }
                }
            }
        }
    }

    public void drawCharacterScreen() {
        final int frameXY     = SIZE_FINAL * 2;
        final int frameWidth  = SIZE_FINAL * 8;
        final int frameHeight = SIZE_FINAL * 5;
        final int labelX      = frameXY + 40;
        final int valueX      = frameWidth - 40;
        int y                 = frameXY + 40;

        // Dibuja el fondo
        drawWindow(frameXY, frameXY, frameWidth, frameHeight);

        g2.setColor(Color.WHITE);
        y = drawTextBlock("Nivel", labelX, y, 24f, 10);
        y = drawTextBlock("Vida", labelX, y, 24f, 10);
        y = drawTextBlock("Herramienta\ncorporativa", labelX, y, 24f, 15);
        drawTextBlock("Fentanilo\nen sangre", labelX, y, 24f, 0);

        // Ahora los valores, reiniciamos y o los desplazamos igual que etiquetas
        y = frameXY + 40;
        g2.setColor(Color.WHITE);
        y = drawTextBlock(String.valueOf(teisPanel.player.getLevel()), valueX, y, 28f, 10);
        y = drawTextBlock(String.valueOf(teisPanel.player.getLife()), valueX, y, 28f, 20);
        y = drawTextBlock(String.valueOf(teisPanel.player.getAttackVal()), valueX, y, 28f, 30);
        drawTextBlock(String.valueOf(teisPanel.player.getDefenseVal()), valueX, y, 28f, 0);
    }

    /**
     * Dibuja un bloque de texto (posible multilínea) y devuelve
     * la nueva posición Y, aplicando el espacio extra pasado.
     *
     * @param text           texto a dibujar (puede llevar '\n')
     * @param x              coordenada X
     * @param startY         coordenada Y inicial
     * @param fontSize       tamaño de fuente
     * @param spacingAfter   píxeles extra tras el bloque
     * @return               nueva coordenada Y tras dibujar y el spacing
     */
    private int drawTextBlock(String text, int x, int startY, float fontSize, int spacingAfter) {
        g2.setFont(g2.getFont().deriveFont(fontSize));
        FontMetrics fm = g2.getFontMetrics();
        int y = startY;

        for (String line : text.split("\n")) {
            g2.drawString(line, x, y);
            y += fm.getHeight();      // saltamos la altura de la línea
        }
        return y + spacingAfter;      // añadimos el espacio extra
    }


    private void drawWindow(int x, int y, int width, int height) {
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(x, y, width, height, 35, 35);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    private void drawEquipmentImages(int x, int y) {
        g2.drawImage(teisPanel.player.getCurrentWeapon().down1, x, y, null);
        g2.drawImage(teisPanel.player.getCurrentShield().down1, x + SIZE_FINAL, y + 10, null);
    }

    private void drawTextWithShadow(String text, int x, int y) {
        g2.setColor(Color.GRAY);
        g2.drawString(text, x + 5, y + 5);
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
    }

    private int getCenteredX(String text) {
        return SCREEN_WIDTH / 2 - g2.getFontMetrics().stringWidth(text) / 2;
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