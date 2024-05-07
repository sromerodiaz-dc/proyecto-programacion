package ENTITY;

import FX.KeyManager;
import FX.TeisPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Esta clase define la interacción del jugador con el entorno asi como su movimiento y uso de gráficos en 2D.
 * */

public class Player extends Entity{
    // Atributos
    TeisPanel teisPanel;
    KeyManager keyManager;

    // Constructor
    public Player (TeisPanel t, KeyManager k) {
        this.teisPanel = t;
        this.keyManager = k;
        // Inicializa valores por defecto
        setValoresPorDefecto();
        getPlayerImage();
    }

    // Metodos
    public void setValoresPorDefecto() {
        x = 100;
        y = 100;
        speed = 5;
        sentido = 'f';
    }

    /**
     * Este metodo recibe una ruta por parametro en getResourceAsStream para luego acceder a dicho recurso
     * y pasarlo por un Loader para poder leerlo como archivo decodificando el stream de datos de la imagen.
     * Esta imagen rehecha por ImageIO.read() es almacenada en cada uno de los posibles movimientos del jugador.
     * */
    public void getPlayerImage(){
        try {
            up1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/upWalkingBehind1.png"));
            up2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/upWalkingBehind2.png"));
            down1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/downWalking1.png"));
            down2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/downWalking2.png"));
            left1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/leftWalking1.png"));
            left2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/leftWalking2.png"));
            right1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/rightWalking1.png"));
            right2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/rightWalking2.png"));
            stop = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/frontStanding.png"));
            stopR = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/rightStanding.png"));
            stopL = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/leftStanding.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Metodo que actualiza la posición del jugador mediante una llamada a otro metodo heredado de Entity
    public void actualiza(){
        move(keyManager);
    }

    // Metodo empleado para mostrar por pantalla al jugador.
    // Este metodo instancia un Buffer de Imagenes en "image". Este dependiendo de la accion entrante por teclado
    // cambia el sprite empleado por otro nuevo.
    public void pinta(Graphics2D g2){
    //    g2.setColor(Color.white);//   g2.fillRect(x,y,TeisPanel.sizeFinal,TeisPanel.sizeFinal);
        BufferedImage image = switch (sentido) {
            case 'w' -> up1;
            case 'a' -> left1;
            case 's' -> down1;
            case 'd' -> right1;
            default -> stop;
        };
        // Dibuja la imagen con IMAGE en la posicion por defecto (100, 100) con los valores por defecto de
        // resolucion 16x16 y su respectivo escalado "sizeFinal")
        g2.drawImage(image,x,y,TeisPanel.sizeFinal,TeisPanel.sizeFinal,null);
    }
}
