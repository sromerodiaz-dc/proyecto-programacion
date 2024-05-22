package GAME.ENTITY;

import GAME.FX.KeyManager;
import GAME.GAME.TeisPanel;

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

    public final int screenX;
    public final int screenY;

    // Constructor
    public Player (TeisPanel t, KeyManager k) {
        this.teisPanel = t;
        this.keyManager = k;

        screenX = teisPanel.screenWidth/2 - (teisPanel.sizeFinal/2);
        screenY = teisPanel.screenHeight/2 - (teisPanel.sizeFinal/2);

        // teisPanel.sizeFinal = 48
        // Como quiero que el área de colision sea MENOR al del tamaño del PJ, reduzco los pixeles de alto y ancho
        // además de la posición del propio Rectangle en (8,16), recordemos que en Java (0,0) es topLeftCorner.
        // Así que poner unas coordenadas (8,16) quiere decir que el área colisionable comienza cerca del centro del PJ.

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        defaultSolidAreaX = solidArea.x;
        defaultSolidAreaY = solidArea.y;

        // Inicializa valores por defecto
        setValoresPorDefecto();
        getPlayerImage();
    }

    // METODOS
    /**
     * Metodo que define el estado inicial del jugador
     * */
    public void setValoresPorDefecto() {
        worldX = teisPanel.sizeFinal * 2;
        worldY = teisPanel.sizeFinal * 2;
        speed = 4;
        sentido = '0';
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
            stop2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("player/stop2.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo que actualiza la posición del jugador mediante una llamada a otro metodo heredado de Entity
     * */
    public void actualiza(){
        move(keyManager,teisPanel,this);
    }


}
