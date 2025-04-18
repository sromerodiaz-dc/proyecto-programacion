package com.game.gui;

import com.game.entity.Entity;
import com.game.data.Properties;
import com.game.controller.TeisPanel;
import com.game.entity.stats.Vida;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Clase que representa la interfaz de usuario del programa.
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class UserInterface {
    Graphics2D g2; // Gráficos 2D

    TeisPanel teisPanel; // Referencia al panel donde se dibujará la interfaz de usuario.
    Font pixeledFont; // Fuente de texto utilizada para dibujar texto en la interfaz de usuario.

    BufferedImage vida_entera, vida_mitad, vida_vacia; // Sprites de los corazones de vida

    //DrawUtils drawUtils = new DrawUtils();

    public boolean messageOn = false; // Dependiendo de las interacciones del player
    public String message = ""; // Mensaje mostrado dependiendo del objeto
    int messageTime = 0; // Tiempo que se muestra el mensaje

    public boolean isFinished = false; // Termina el juego
    public String dialogo;

    ArrayList<String> titulos = new ArrayList<>();
    String title;
    public int contadorTitulo = 1;


    // De momento no se usa:
    // double speedRun; // Tiempo de juego
    // DecimalFormat decimalFormat = new DecimalFormat("#0.00"); // Formato del tiempo

    /**
     * Constructor de la interfaz de usuario.
     *
     * @param teisPanel El panel de juego de Teis.
     * @param properties Las propiedades del juego.
     */
    public UserInterface(TeisPanel teisPanel, Properties properties) {
        // Asigna el panel de juego de Teis a la variable de instancia teisPanel
        this.teisPanel = teisPanel;

        // Carga la fuente de letra "newPixeledFont.ttf" desde el recurso de clase
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("font/newPixeledFont.ttf");
            assert is != null;
            pixeledFont = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (IOException | FontFormatException e) {
            // Lanza una excepción RuntimeException si ocurre un error al cargar la fuente
            throw new RuntimeException(e);
        }

        // Inicializa la lista de títulos posibles y selecciona uno al azar
        setPosiblesTitulos();
        getPosibleTitulo();

        // Crea una entidad de vida para el jugador y obtiene sus imágenes
        Entity vida = new Vida(teisPanel, properties);
        vida_entera = vida.image; // Imagen de vida completa
        vida_mitad = vida.image2; // Imagen de vida a la mitad
        vida_vacia = vida.image3; // Imagen de vida vacía
    }

    /**
     * Muestra un mensaje en la pantalla.
     *
     * @param message El texto del mensaje a mostrar.
     */
    public void showMessage(String message) {
        // Asigna el mensaje recibido a la variable de instancia message
        this.message = message;

        // Indica que el mensaje está activo y debe ser mostrado
        messageOn = true;
    }

    /**
     * Establece la lista de títulos posibles para el juego.
     */
    public void setPosiblesTitulos() {
        // Agrega títulos a la lista de títulos
        titulos.add("Teis non\né Chapela.");
        titulos.add("\"É Vigo\nmáis ca\nun dinoseto?\"");
        titulos.add("Concello de\nTeis:\nO XOGO");
        titulos.add("\"eres de Cangas\"");
        titulos.add("Bombardeen a\nUVigo");
        titulos.add("Bombardeen o\nVialia");
        titulos.add("\"Porriño pertence\na Mos\"");
        titulos.add("V de Vitrasa!");
    }

    /**
     * Obtiene un título aleatorio de la lista de títulos.
     */
    public void getPosibleTitulo() {
        // Crea un objeto Random para generar números aleatorios
        Random random = new Random();

        // Genera un índice aleatorio dentro del rango de la lista de títulos
        int i = random.nextInt(titulos.size());

        // Asigna el título en la posición aleatoria a la variable title
        title = titulos.get(i);
    }

    /**
     * Dibuja los diferentes elementos en la pantalla según el estado del juego.
     *
     * @param g2 El objeto Graphics2D para dibujar en la pantalla.
     */
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        // Establece la fuente y el color del texto
        g2.setFont(pixeledFont);
        g2.setColor(Color.WHITE);

        // Dibuja diferentes elementos en la pantalla según el estado del juego
        if (teisPanel.controller.estado == teisPanel.controller.cargaState) {
            drawPantallaCarga();
        }

        if (teisPanel.controller.estado == teisPanel.controller.playState) {
            drawPlayerVida();
        }

        if (teisPanel.controller.estado == teisPanel.controller.pauseState) {
            drawPlayerVida();
            drawPauseState();
        }

        if (teisPanel.controller.estado == teisPanel.controller.dialogoState) {
            drawPlayerVida();
            drawDialogo();
        }
    }

    /**
     * Dibuja la vida del jugador en la pantalla.
     */
    public void drawPlayerVida() {
        // Inicializa la posición del dibujo
        int x = teisPanel.sizeFinal / 2;
        int y = teisPanel.sizeFinal / 2;
        int i = 0;

        // Dibuja corazones vacíos para representar la vida máxima del jugador
        while (i < teisPanel.model.maxLife / 2) {
            g2.drawImage(vida_vacia, x, y, null);
            i++;
            x += teisPanel.sizeFinal; // Incrementa la posición x para dibujar el siguiente corazón
        }

        // Dibuja corazones dependiendo de la vida actual del jugador
        x = teisPanel.sizeFinal / 2; // Reinicia la posición x
        i = 0;
        while (i < teisPanel.model.life) {

            // Dibuja un corazón medio si la vida del jugador es impar
            g2.drawImage(vida_mitad, x, y, null);
            i++;

            // Dibuja un corazón completo si la vida del jugador es par y mayor que 0
            if (i < teisPanel.model.life) {
                g2.drawImage(vida_entera, x, y, null);
            }

            i++;
            x += teisPanel.sizeFinal; // Incrementa la posición x para dibujar el siguiente corazón
        }
    }

    /**
     * Dibuja la pantalla de pausa en la pantalla.
     */
    public void drawPauseState() {
        // Establece la fuente para el texto de la pantalla de pausa
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80));

        // Texto a dibujar
        String text = "PAUSA";

        // Calcula la posición vertical del texto
        int y = teisPanel.screenHeight / 2;

        // Dibuja el texto en la pantalla, centrado horizontalmente
        g2.drawString(text, centrado(text), y);
    }

    /**
     * Dibuja un diálogo en la pantalla.
     */
    public void drawDialogo() {
        // Calcula la posición y tamaño de la ventana de diálogo
        int x = teisPanel.sizeFinal * 2; // Posición horizontal X
        int y = teisPanel.sizeFinal / 2; // Posición vertical Y
        int width = teisPanel.screenWidth - teisPanel.sizeFinal * 4; // Ancho de la ventana
        int height = teisPanel.sizeFinal * 5; // Alto de la ventana

        // Dibuja la ventana de diálogo
        drawVentana(x, y, width, height);

        // Establece la fuente para el texto del diálogo
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 22));

        // Calcula la posición inicial del texto del diálogo
        x += teisPanel.sizeFinal;
        y += teisPanel.sizeFinal;

        // Dibuja cada línea del diálogo
        for (String line : dialogo.split("\n")) {
            g2.drawString(line, x, y);
            y += 40; // La siguiente línea se dibuja 40 píxeles por debajo
        }
    }

    /**
     * Dibuja una ventana con un borde redondeado y un fondo transparente.
     *
     * @param x La posición horizontal X de la ventana.
     * @param y La posición vertical Y de la ventana.
     * @param width El ancho de la ventana.
     * @param height La altura de la ventana.
     */
    public void drawVentana(int x, int y, int width, int height) {
        Color color = new Color(0, 0, 0, 200); // Establece el color del fondo de la ventana (negro con 200 de transparencia)
        g2.setColor(color);

        // Dibuja el fondo de la ventana con un rectángulo redondeado
        g2.fillRoundRect(x, y, width, height, 35, 35);

        // Establece el color del borde de la ventana (blanco)
        color = new Color(255, 255, 255);
        g2.setColor(color);

        // Establece el grosor del borde
        g2.setStroke(new BasicStroke(5));

        // Dibuja el borde de la ventana con un rectángulo redondeado
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    /**
     * Dibuja la pantalla de carga del juego.
     */
    public void drawPantallaCarga() {
        // Establece la fuente del título del juego en negrita y tamaño 75
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 75F));

        int x;
        int y = teisPanel.screenHeight / 3; // Posición vertical del título

        // Dibuja cada línea del título del juego
        for (String line : title.split("\n")) {
            x = centrado(line); // Calcula la posición horizontal X para centrar el texto

            // Dibuja el sombreado del título
            g2.setColor(Color.GRAY);
            g2.drawString(line, x + 5, y + 5);

            // Dibuja el título en sí
            g2.setColor(Color.WHITE);
            g2.drawString(line, x, y);
            y += 100; // La siguiente línea se dibuja 120px por debajo
        }

        // Establece la fuente para los botones en negrita y tamaño 30
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
        g2.setColor(Color.YELLOW);

        // Dibuja el botón "SAÍR"
        String text = "SAÍR";
        x = (int) (teisPanel.screenWidth * 0.87) - ((int) g2.getFontMetrics().getStringBounds(text, g2).getWidth());
        y = teisPanel.screenHeight - 100;
        g2.drawString(text, x, y);
        if (contadorTitulo == 0) {
            g2.drawString(">>", x - teisPanel.sizeFinal, y); // Dibuja la flecha de selección
        }

        // Dibuja el botón "DALLE"
        text = "DALLE";
        x = (int) (teisPanel.screenWidth * 0.15);
        g2.drawString(text, x, y);
        if (contadorTitulo == 1) {
            g2.drawString(">>", x - teisPanel.sizeFinal, y); // Dibuja la flecha de selección
        }

        // Dibuja el botón "CARGAR PARTIDA"
        text = "CARGAR PARTIDA";
        x = centrado(text);
        y += 50;
        g2.drawString(text, x, y);
        if (contadorTitulo == 2) {
            g2.drawString(">>", x - teisPanel.sizeFinal, y); // Dibuja la flecha de selección
        }
    }


    /**
     * Devuelve la posición horizontal X donde debe ser dibujado el texto centrado en un panel.
     *
     * @param text El texto a centrar.
     * @return La posición horizontal X donde debe ser dibujado el texto centrado.
     */
    public int centrado(String text) {
        // Obtiene el ancho del texto utilizando la métrica de fuente actual
        int lenght = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();

        // Calcula la posición horizontal X donde debe ser dibujado el texto centrado
        // al restar la mitad del ancho del texto a la mitad del ancho del panel
        return teisPanel.screenWidth/2 - lenght/2;
    }
}
