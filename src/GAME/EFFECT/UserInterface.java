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
    TeisPanel teisPanel; // Referencia al panel donde se dibujará la interfaz de usuario.
    Font font, arial_endBold; // Fuente de texto utilizada para dibujar texto en la interfaz de usuario.
    BufferedImage imagePass; // Implementación de imágen para la UI y acompañar al texto
    public boolean messageOn = false;
    public String message = "";
    int messageTime = 0;

    public boolean isFinished = false;

    double speedRun;
    DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    /**
     * Constructor de la clase UserInterface.
     * @param teisPanel Panel donde se dibujará la interfaz de usuario.
     */
    public UserInterface(TeisPanel teisPanel) {

        this.teisPanel = teisPanel;

        // Se crea una fuente de texto Arial de 20 puntos y estilo plano.
        font = new Font("Arial", Font.PLAIN, 20);
        arial_endBold = new Font("Arial", Font.BOLD, 40);

        // Se crea una instancia de la clase Passvigo para obtener la imagen.
        Passvigo passvigo = new Passvigo();

        // Se asigna la imagen obtenida a la variable imagePass.
        imagePass = passvigo.image;

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
        if (!isFinished) {
            // Se establece la fuente de texto y el color blanco para dibujar texto.
            g2.setFont(font);
            g2.setColor(Color.white);

            // Se dibuja el texto "tenPassVigo" en la posición (130, 80).
            /*g2.drawString(teisPanel.model.tenPass, 130, 80);*/

            // Se verifica si la condición "tenPass" es verdadera.
            if (teisPanel.model.tenPass) {
                // Si es verdadera, se dibuja la imagen en la posición y tamaño especificados.
                g2.drawImage(imagePass, teisPanel.sizeFinal/2, teisPanel.sizeFinal/3, 75, 65, null);
            }

            // Tiempo de Juego
            speedRun += (double) 1 /60;
            g2.drawString("Tiempo de juego: "+ decimalFormat.format(speedRun), teisPanel.sizeFinal * 15, 65);

            if (messageOn) {
                g2.setFont(g2.getFont().deriveFont(10f));
                g2.drawString(message, teisPanel.sizeFinal/2,teisPanel.sizeFinal*2);

                messageTime++;

                // El juego corre a 30 Frames por Segundo y como este método se llama 30 veces por segundo
                // esto quiere decir que si el counter llega a 90 han pasado 3 segundos.
                if (messageTime > 90) {
                    messageTime = 0;
                    messageOn = false;
                }
            }
        } else {
            // Se establece la fuente de texto y el color blanco para dibujar texto.
            g2.setFont(font);
            g2.setColor(Color.LIGHT_GRAY);

            String text;
            /*int textLenght; textLenght = (int) g2.getFontMetrics().getStringBounds(text,g2).getWidth();*/

            int x ,y;
            text = "Tiempo de juego: "+decimalFormat.format(speedRun);
            x = teisPanel.screenWidth - 200;
            y = 65;
            g2.drawString(text, x, y);

            text = "\"outra botella de mayerlin baleira, outra vez será...\"";
            g2.drawString(text, teisPanel.screenWidth/2 - 200, teisPanel.screenHeight/3);

            teisPanel.teisThread = null;
        }
    }
}
