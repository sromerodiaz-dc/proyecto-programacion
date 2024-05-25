package GAME.EFFECT;

import GAME.GAME.TeisPanel;
import GAME.OBJECT.OBJS.Passvigo;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Clase que representa la interfaz de usuario del programa.
 */
public class UserInterface {
    // Panel y Fuente de texto
    /**
     * Referencia al panel donde se dibujará la interfaz de usuario.
     */
    TeisPanel teisPanel;
    /**
     * Fuente de texto utilizada para dibujar texto en la interfaz de usuario.
     */
    Font font;

    // Implementación de imágen para la UI y acompañar al texto
    /**
     * Imagen utilizada para acompañar al texto en la interfaz de usuario.
     */
    BufferedImage imagePass;

    /**
     * Constructor de la clase UserInterface.
     * @param teisPanel Panel donde se dibujará la interfaz de usuario.
     */
    public UserInterface(TeisPanel teisPanel) {
        this.teisPanel = teisPanel;
        // Se crea una fuente de texto Arial de 20 puntos y estilo plano.
        font = new Font("Arial", Font.PLAIN, 20);
        // Se crea una instancia de la clase Passvigo para obtener la imagen.
        Passvigo passvigo = new Passvigo();
        // Se asigna la imagen obtenida a la variable imagePass.
        imagePass = passvigo.image;
    }

    /**
     * Método que dibuja la interfaz de usuario en el Graphics2D proporcionado.
     * @param g2 Graphics2D donde se dibujará la interfaz de usuario.
     */
    public void draw(Graphics2D g2) {
        // Se establece la fuente de texto y el color blanco para dibujar texto.
        g2.setFont(font);
        g2.setColor(Color.white);
        // Se dibuja el texto "tenPassVigo" en la posición (130, 80).
        g2.drawString(teisPanel.model.tenPassVigo, 130, 80);
        // Se verifica si la condición "tenPass" es verdadera.
        if (teisPanel.model.tenPass) {
            // Si es verdadera, se dibuja la imagen en la posición y tamaño especificados.
            g2.drawImage(imagePass, teisPanel.sizeFinal/2, teisPanel.sizeFinal/3, teisPanel.sizeFinal*2, teisPanel.sizeFinal*2, null);
        }
    }
}
