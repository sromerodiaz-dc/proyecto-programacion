package GAME.EFFECT;

import GAME.GAME.TeisPanel;
import GAME.OBJECT.OBJS.Passvigo;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UserInterface {

    // Panel y Fuente de texto
    TeisPanel teisPanel;
    Font font;

    // Implementación de imágen para la UI y acompañar al texto
    BufferedImage imagePass;

    public UserInterface(TeisPanel teisPanel) {
        this.teisPanel = teisPanel;
        font = new Font("Arial",Font.ITALIC,40);
        Passvigo passvigo = new Passvigo();
        imagePass = passvigo.image;
    }

    // Metodos de dibujado
    public void draw(Graphics2D g2) {
        g2.setFont(font);
        g2.setColor(Color.YELLOW);
        g2.drawImage(imagePass, teisPanel.sizeFinal/2, teisPanel.sizeFinal/2, teisPanel.sizeFinal*2, teisPanel.sizeFinal*2, null);
        g2.drawString(teisPanel.model.tenPassVigo, 100, 80);
    }
}
