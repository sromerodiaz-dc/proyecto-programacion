package com.game.ui;

import com.game.data.GameState;
import com.game.entity.Entity;
import com.game.data.Properties;
import com.game.entity.Player;
import com.game.entity.stats.Vida;
import com.game.ui.dialogue.Dialogable;
import com.game.ui.dialogue.DialogueSystem;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

public class UserInterface {
    private final int SCREEN_WIDTH = TeisPanel.screenWidth;
    private final int SCREEN_HEIGHT = TeisPanel.screenHeight;

    private static final Color NPC_WINDOW_BG = new Color(0, 0, 0, 220);
    private static final Color PLAYER_WINDOW_BG = new Color(0, 0, 0, 220);
    private static final Color WINDOW_BORDER = Color.WHITE;
    private static final Color SCROLL_INDICATOR = Color.YELLOW;
    private static final int DIALOG_FONT_SIZE = 22;
    private static final int OPTION_FONT_SIZE = 20;
    private static final int TEXT_PADDING = 20;
    private static final int LINE_SPACING = 25;
    private static final int SCROLL_INDICATOR_OFFSET = 30;

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
        System.out.println("daño: " + damage);
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

    private void drawDialog() {
        final int x = SIZE_FINAL * 2;
        final int width = SCREEN_WIDTH - SIZE_FINAL * 4;

        Entity currentNpcEntity = teisPanel.controller.currentTalkingNpc;
        updateTypingAnimation(currentNpcEntity);

        // Precalcular métricas de fuente
        g2.setFont(pixeledFont.deriveFont(Font.PLAIN, DIALOG_FONT_SIZE));
        FontMetrics fm = g2.getFontMetrics();

        if (currentNpcEntity == null) {
            drawEventDialog(x, SCREEN_HEIGHT - SIZE_FINAL * 10, width, SIZE_FINAL * 4, fm);
            return;
        }

        // Ventana NPC
        drawDialogWindow(
                x, SCREEN_HEIGHT - SIZE_FINAL * 10,
                width, SIZE_FINAL * 4,
                getNpcDialogText(currentNpcEntity),
                NPC_WINDOW_BG,
                WINDOW_BORDER,
                fm
        );

        // Ventana jugador SOLO si se completó el texto
        boolean typingComplete = !currentNpcEntity.isTyping;
        if (typingComplete && currentNpcEntity instanceof Dialogable dialogableNpc) {
            List<String> options = dialogableNpc.getCurrentOptions();
            if (options != null && !options.isEmpty()) {
                drawOptionsWindow(
                        x, SCREEN_HEIGHT - SIZE_FINAL * 5,
                        width, SIZE_FINAL * 3,
                        options,
                        currentNpcEntity.selectedOption,
                        PLAYER_WINDOW_BG,
                        SCROLL_INDICATOR,
                        fm
                );
            }
        }
    }

    private String getNpcDialogText(Entity npc) {
        String text = npc.currentDialog != null ? npc.currentDialog : "";
        return npc.isTyping ? text.substring(0, Math.min(npc.typingIndex, text.length())) : text;
    }

    private void drawDialogWindow(int x, int y, int width, int height,
                                  String text, Color bgColor, Color borderColor,
                                  FontMetrics fm) {
        // Dibujar ventana
        drawRoundedRect(x, y, width, height, 25, bgColor, borderColor, 3);

        // Configurar texto
        g2.setColor(Color.WHITE);
        int textX = x + TEXT_PADDING;
        int textY = y + TEXT_PADDING + fm.getAscent();
        int maxWidth = width - TEXT_PADDING * 2;

        // Procesar texto
        List<String> wrappedLines = wrapText(text, maxWidth, fm);
        int maxVisibleLines = (height - TEXT_PADDING * 2) / LINE_SPACING;

        // Calcular inicio del scroll
        int startLine = 0;
        if (wrappedLines.size() > maxVisibleLines) {
            startLine = wrappedLines.size() - maxVisibleLines;

            // Ajustar para animación en progreso
            if (teisPanel.controller.currentTalkingNpc != null &&
                    teisPanel.controller.currentTalkingNpc.isTyping) {
                startLine = Math.max(0, startLine - 1);
            }
        }

        // Dibujar líneas visibles
        for (int i = startLine; i < wrappedLines.size(); i++) {
            if (textY > y + height - TEXT_PADDING) break;

            g2.drawString(wrappedLines.get(i), textX, textY);
            textY += LINE_SPACING;
        }

        // Indicador de scroll
        if (wrappedLines.size() > maxVisibleLines) {
            g2.setColor(SCROLL_INDICATOR);
            if (startLine > 0) {
                g2.drawString("▲", x + width - SCROLL_INDICATOR_OFFSET, y + TEXT_PADDING);
            }
            g2.drawString("▼", x + width - SCROLL_INDICATOR_OFFSET, y + height - TEXT_PADDING);
        }
    }

    private void drawOptionsWindow(int x, int y, int width, int height,
                                   List<String> options, int selectedOption,
                                   Color bgColor, Color highlightColor,
                                   FontMetrics fm) {
        // Dibujar ventana
        drawRoundedRect(x, y, width, height, 20, bgColor, WINDOW_BORDER, 3);

        // Configurar texto
        g2.setFont(pixeledFont.deriveFont(Font.PLAIN, OPTION_FONT_SIZE));
        int textX = x + TEXT_PADDING;
        int textY = y + TEXT_PADDING + fm.getAscent();
        int maxWidth = width - TEXT_PADDING * 2;
        int maxOptionsHeight = (height - TEXT_PADDING * 2);
        int lineHeight = LINE_SPACING;
        int linesDrawn = 0;
        int maxVisibleLines = maxOptionsHeight / lineHeight;

        // Dibujar opciones con envoltura de texto
        for (int i = 0; i < options.size() && linesDrawn < maxVisibleLines; i++) {
            boolean isSelected = (i == selectedOption);
            String prefix = isSelected ? "> " : "  ";
            String option = options.get(i);

            // Envolver texto de la opción
            List<String> wrappedLines = wrapOptionText(option, maxWidth - fm.stringWidth(prefix), fm);

            // Dibujar cada línea de la opción
            for (String line : wrappedLines) {
                if (linesDrawn >= maxVisibleLines) break;

                g2.setColor(isSelected ? highlightColor : Color.WHITE);
                g2.drawString(prefix + line, textX, textY);
                textY += lineHeight;
                linesDrawn++;

                // Solo mostrar prefijo en la primera línea
                prefix = "  ";
            }
        }

        // Indicador de más opciones
        if (!options.isEmpty() && linesDrawn < options.size()) {
            g2.setColor(highlightColor);
            g2.drawString("▼", x + width - SCROLL_INDICATOR_OFFSET, y + height - TEXT_PADDING);
        }
    }

    private void drawEventDialog(int x, int y, int width, int height, FontMetrics fm) {
        if (dialogo == null) return;

        drawDialogWindow(
                x, y, width, height,
                dialogo,
                NPC_WINDOW_BG,
                WINDOW_BORDER,
                fm
        );
    }

    private void drawRoundedRect(int x, int y, int width, int height, int arc,
                                 Color fillColor, Color borderColor, int strokeWidth) {
        g2.setColor(fillColor);
        g2.fillRoundRect(x, y, width, height, arc, arc);

        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(strokeWidth));
        g2.drawRoundRect(x, y, width, height, arc, arc);
    }

    private void updateTypingAnimation(Entity npc) {
        if (npc == null) return;

        if (npc.isTyping) {
            // Calcular límite seguro
            int safeIncrement = Math.min(2, npc.currentDialog.length() - npc.typingIndex);
            npc.typingIndex += safeIncrement;

            // Verificar si completó
            if (npc.typingIndex >= npc.currentDialog.length()) {
                npc.typingIndex = npc.currentDialog.length();
                npc.isTyping = false;
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

    private List<String> wrapOptionText(String text, int maxWidth, FontMetrics fm) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) return lines;

        StringBuilder currentLine = new StringBuilder();

        for (String word : text.split("\\s+")) {
            currentLine = getStringBuilder(maxWidth, fm, lines, currentLine, word);
        }

        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    private StringBuilder getStringBuilder(int maxWidth, FontMetrics fm, List<String> lines, StringBuilder currentLine, String word) {
        String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;

        if (fm.stringWidth(testLine) <= maxWidth) {
            currentLine = new StringBuilder(testLine);
        } else {
            if (!currentLine.isEmpty()) {
                lines.add(currentLine.toString());
            }
            currentLine = new StringBuilder(word);
        }
        return currentLine;
    }

    private void drawWindow(int x, int y, int width, int height) {
        // Calcular altura necesaria basada en el contenido
        int neededHeight = calculateDialogHeight();
        if (neededHeight > height) {
            height = neededHeight;
            // Mover hacia arriba para mantenerlo visible
            y = SCREEN_HEIGHT - height - SIZE_FINAL;
        }

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(x, y, width, height, 35, 35);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    private List<String> wrapText(String text, int maxWidth, FontMetrics fm) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) return lines;

        // Dividir por saltos de línea explícitos primero
        String[] hardLines = text.split("\n");
        for (String hardLine : hardLines) {
            StringBuilder currentLine = new StringBuilder();
            String[] words = hardLine.split("\\s+");

            for (String word : words) {
                // Verificar si la palabra es demasiado larga
                while (fm.stringWidth(word) > maxWidth) {
                    int splitIndex = 1;
                    while (splitIndex < word.length() &&
                            fm.stringWidth(word.substring(0, splitIndex)) <= maxWidth) {
                        splitIndex++;
                    }
                    splitIndex--; // Retroceder al último índice válido

                    if (splitIndex == 0) splitIndex = 1; // Prevenir bucle infinito

                    if (!currentLine.isEmpty()) {
                        lines.add(currentLine.toString());
                        currentLine = new StringBuilder();
                    }

                    lines.add(word.substring(0, splitIndex));
                    word = word.substring(splitIndex);
                }

                currentLine = getStringBuilder(maxWidth, fm, lines, currentLine, word);
            }

            if (!currentLine.isEmpty()) {
                lines.add(currentLine.toString());
            }
        }
        return lines;
    }

    private int calculateDialogHeight() {
        int baseHeight = 50; // Altura base reducida
        int extraHeight = 0;
        FontMetrics fm = g2.getFontMetrics();
        int lineHeight = fm.getHeight();

        if (teisPanel.controller.currentTalkingNpc != null) {
            // Calcular altura para texto NPC
            String npcText = teisPanel.controller.currentTalkingNpc.currentDialog;
            if (npcText != null) {
                List<String> wrappedLines = wrapText(npcText, SCREEN_WIDTH - SIZE_FINAL * 6, fm);
                extraHeight += wrappedLines.size() * lineHeight;
            }

            // Calcular altura para opciones
            if (teisPanel.controller.currentTalkingNpc instanceof Dialogable dialogableNpc) {
                List<String> options = dialogableNpc.getCurrentOptions();
                if (options != null) {
                    int maxVisibleOptions = 3;
                    int optionCount = Math.min(options.size(), maxVisibleOptions);

                    for (int i = 0; i < optionCount; i++) {
                        String option = options.get(i);
                        List<String> optionLines = wrapOptionText(option, SCREEN_WIDTH - SIZE_FINAL * 6, fm);
                        extraHeight += optionLines.size() * lineHeight + 5; // +5 por espacio entre opciones
                    }
                }
            }
        }

        return baseHeight + extraHeight;
    }

    private void drawDarkSoulsYouDied() {
        //TODO desarrollar pantalla de muerte del darksouls
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

    public void setDialogueSystem(DialogueSystem system) {
    }
}