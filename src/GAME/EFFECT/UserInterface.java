package GAME.EFFECT;

import GAME.GAME.TeisPanel;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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

    //DrawUtils drawUtils = new DrawUtils();

    public boolean messageOn = false; // Dependiendo de las interacciones del player
    public String message = ""; // Mensaje mostrado dependiendo del objeto
    int messageTime = 0; // Tiempo que se muestra el mensaje

    public boolean isFinished = false; // Termina el juego
    public String dialogo;

    ArrayList<String> titulos = new ArrayList<>();

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
    }

    public void showMessage (String message) {
        this.message = message;
        messageOn = true;
    }

    public void setPosiblesTitulos() {
        titulos.add("Teis non é Chapela.");
        titulos.add("\"É Vigo máis ca un dinoseto?\"");
        titulos.add("Concello de Teis: O XOGO");
        titulos.add("\nSamil prime das praias\n");
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
        if (teisPanel.controller.estado == teisPanel.controller.carga){
            drawPantallaCarga();
        }

        if (teisPanel.controller.estado == teisPanel.controller.playState){
            //
        }

        if (teisPanel.controller.estado == teisPanel.controller.pauseState) {
            drawPauseState();
        }

        if (teisPanel.controller.estado == teisPanel.controller.dialogo) {
           drawDialogo();
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
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,96F));
        String title = "";
        int x = centrado(title);
        int y = teisPanel.sizeFinal * 3;

        g2.setColor(Color.WHITE);
        g2.drawString(title,x,y);
    }

    public int centrado(String text) {
        int lenght = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();

        return teisPanel.screenWidth/2 - lenght/2;
    }

    public String getTitulo() {

    }
}
