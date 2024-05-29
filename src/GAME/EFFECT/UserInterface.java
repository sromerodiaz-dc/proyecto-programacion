package GAME.EFFECT;

import GAME.ENTITY.Entity;
import GAME.GAME.TeisPanel;
import GAME.OBJECT.OBJS.Vida;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Clase que representa la interfaz de usuario del programa.
 */
public class UserInterface {
    Graphics2D g2;

    TeisPanel teisPanel; // Referencia al panel donde se dibujará la interfaz de usuario.
    Font pixeledFont; // Fuente de texto utilizada para dibujar texto en la interfaz de usuario.

    BufferedImage vida_entera, vida_mitad, vida_vacia;

    //DrawUtils drawUtils = new DrawUtils();

    public boolean messageOn = false; // Dependiendo de las interacciones del player
    public String message = ""; // Mensaje mostrado dependiendo del objeto
    int messageTime = 0; // Tiempo que se muestra el mensaje

    public boolean isFinished = false; // Termina el juego
    public String dialogo;

    ArrayList<String> titulos = new ArrayList<>();
    String title;
    public int contadorTitulo = 1;

    /*
    * De momento no se usa:
    * double speedRun; // Tiempo de juego
    * DecimalFormat decimalFormat = new DecimalFormat("#0.00"); // Formato del tiempo
    */
    /**
     * Constructor de la clase UserInterface.
     * @param teisPanel Panel donde se dibujará la interfaz de usuario.
     */
    public UserInterface(TeisPanel teisPanel) {
        this.teisPanel = teisPanel;

        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("font/newPixeledFont.ttf");
            assert is != null;
            pixeledFont = Font.createFont(Font.TRUETYPE_FONT,is);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }

        setPosiblesTitulos();
        getPosibleTitulo();

        // Recibe datos de la vida y sprite de vida del player
        Entity vida = new Vida(teisPanel);
        vida_entera = vida.image;
        vida_mitad = vida.image2;
        vida_vacia = vida.image3;
    }

    public void showMessage (String message) {
        this.message = message;
        messageOn = true;
    }

    public void setPosiblesTitulos() {
        titulos.add("Teis non\né Chapela.");
        titulos.add("\"É Vigo\nmáis ca\nun dinoseto?\"");
        titulos.add("Concello de\nTeis:\nO XOGO");
        titulos.add("\"eres de Cangas\"");
        titulos.add("Bombardeen a\nUVigo");
        titulos.add("Bombardeen o\nVialia");
        titulos.add("\"Porriño pertence\na Mos\"");
    }

    public void getPosibleTitulo() {
        Random random = new Random();
        int i = random.nextInt(titulos.size());
        title = titulos.get(i);
    }

    /**
     * Método que dibuja la interfaz de usuario en el Graphics2D proporcionado.
     * @param g2 Graphics2D donde se dibujará la interfaz de usuario.
     */
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(pixeledFont);
        g2.setColor(Color.WHITE);

        // Estado de juego
        if (teisPanel.controller.estado == teisPanel.controller.cargaState){
            drawPantallaCarga();
        }

        if (teisPanel.controller.estado == teisPanel.controller.playState){
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

    public void drawPlayerVida() {
        int x = teisPanel.sizeFinal / 2;
        int y = teisPanel.sizeFinal / 2;
        int i = 0;

        // Corazones vacios
        while (i < teisPanel.model.maxLife / 2) {
            g2.drawImage(vida_vacia,x,y,null);
            i++;
            x+= teisPanel.sizeFinal;
        }

        // Dibujado de corazon dependiendo de los HP del jugador
        x = teisPanel.sizeFinal / 2;
        i = 0;
        while (i < teisPanel.model.life) {
            g2.drawImage(vida_mitad, x, y, null);
            i++;
            if (i < teisPanel.model.life) g2.drawImage(vida_entera,x,y,null);
            i++;
            x += teisPanel.sizeFinal;
        }
    }

    public void drawPauseState() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80));

        String text = "PAUSA";
        int y = teisPanel.screenHeight/2;

        g2.drawString(text,centrado(text),y);
    }

    public void drawDialogo () {
        // Propiedades de la ventana de diálogo
        int x = teisPanel.sizeFinal * 2;
        int y = teisPanel.sizeFinal / 2;
        int width = teisPanel.screenWidth - teisPanel.sizeFinal*4;
        int height = teisPanel.sizeFinal * 5;

        drawVentana(x,y,width,height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,22));
        x += teisPanel.sizeFinal;
        y += teisPanel.sizeFinal;

        for (String line : dialogo.split("\n")){
            g2.drawString(line,x,y);
            y+=40;
        }
    }

    public void drawVentana (int x, int y, int width, int height) {
        Color color = new Color(0,0,0,200);
        g2.setColor(color);
        g2.fillRoundRect(x,y,width,height,35,35);

        color = new Color(255,255,255);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5,y+5,width-10,height-10,25,25);
    }

    public void drawPantallaCarga() {
        // Título del juego
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,75F));

        int x;
        int y = teisPanel.screenHeight / 3;

        for (String line : title.split("\n")){
            x = centrado(line);

            // Sombreado
            g2.setColor(Color.GRAY);
            g2.drawString(line,x+5,y+5);

            // Titulo
            g2.setColor(Color.WHITE);
            g2.drawString(line,x,y);
            y+=100; // La siguiente linea se dibuja 120px por debajo
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD,30F));
        g2.setColor(Color.YELLOW);

        String text = "SAÍR";
        x = (int) (teisPanel.screenWidth * 0.87) - ((int)g2.getFontMetrics().getStringBounds(text,g2).getWidth());
        y = teisPanel.screenHeight - 100;
        g2.drawString(text,x,y);
        if (contadorTitulo == 0) {
            g2.drawString(">>", x - teisPanel.sizeFinal, y);
        }

        text = "DALLE";
        x = (int) (teisPanel.screenWidth * 0.15);
        g2.drawString(text,x,y);
        if (contadorTitulo == 1) {
            g2.drawString(">>", x - teisPanel.sizeFinal, y);
        }

        text = "CARGAR PARTIDA";
        x = centrado(text);
        y += 50;
        g2.drawString(text,x,y);
        if (contadorTitulo == 2) {
            g2.drawString(">>", x - teisPanel.sizeFinal, y);
        }
    }


    public int centrado(String text) {
        int lenght = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();

        return teisPanel.screenWidth/2 - lenght/2;
    }
}
