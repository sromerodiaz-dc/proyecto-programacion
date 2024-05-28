package GAME.EFFECT;

import GAME.GAME.TeisPanel;
import GAME.OBJECT.OBJS.Passvigo;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

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
    Font font, arial_endBold; // Fuente de texto utilizada para dibujar texto en la interfaz de usuario.

    public boolean messageOn = false; // Dependiendo de las interacciones del player
    public String message = ""; // Mensaje mostrado dependiendo del objeto
    int messageTime = 0; // Tiempo que se muestra el mensaje

    public boolean isFinished = false; // Termina el juego

    double speedRun; // Tiempo de juego
    DecimalFormat decimalFormat = new DecimalFormat("#0.00"); // Formato del tiempo

    /**
     * Constructor de la clase UserInterface.
     * @param teisPanel Panel donde se dibujará la interfaz de usuario.
     */
    public UserInterface(TeisPanel teisPanel) {
        this.teisPanel = teisPanel;

        // Se crea una fuente de texto Arial de 20 puntos y estilo plano.
        font = new Font("Arial", Font.PLAIN, 20);
        arial_endBold = new Font("Arial", Font.BOLD, 40);


    }

    public void showMessage (String message) {
        this.message = message;
        messageOn = true;
    }

    /**
     * Método que dibuja la interfaz de usuario en el Graphics2D proporcionado.
     * @param g2 Graphics2D donde se dibujará la interfaz de usuario.
     */
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(font);
        g2.setColor(Color.WHITE);

        if (!teisPanel.pauseState){
            //
        } else {
            drawPauseState();
        }
    }

    public void drawPauseState() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80));

        String text = "PAUSA";
        int y = teisPanel.screenHeight/2;

        g2.drawString(text,centrado(text),y);
    }

    public int centrado(String text) {
        int lenght = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();

        return teisPanel.screenWidth/2 - lenght/2;
    }
}
