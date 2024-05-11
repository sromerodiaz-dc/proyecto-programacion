package GPHICS;

import FX.TeisPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;

public class PiezaManager {
    // Atributos
    Pieza[] pieza;
    int[][] mapaPiezaNum;
    String mapName; // variable que usaré cuando haya más de un mapa

    public PiezaManager() {
        pieza = new Pieza[10];
        mapaPiezaNum = new int[TeisPanel.maxScreenColumnas][TeisPanel.maxScreenFilas];

        getPiezaImage();
        loadMap("maps/default.txt");
    }

    public void getPiezaImage(){
        try {
            pieza[0] = new Pieza();
            pieza[0].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("background/grass01.png"));
            pieza[1] = new Pieza();
            pieza[1].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("background/wall.png"));
            pieza[2] = new Pieza();
            pieza[2].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("background/water01.png"));
            pieza[2] = new Pieza();
            pieza[2].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("background/water01.png"));
            pieza[2] = new Pieza();
            pieza[2].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("background/water01.png"));
            pieza[2] = new Pieza();
            pieza[2].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("background/water01.png"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void loadMap(String mapName) {
        InputStream is;
        BufferedReader br;
        try {
            is = getClass().getClassLoader().getResourceAsStream(mapName);
            if (is != null) {
                br = new BufferedReader(new InputStreamReader(is));
                int col = 0, fil = 0;
                while (col < TeisPanel.maxScreenColumnas && fil < TeisPanel.maxScreenFilas) {

                    String linea = br.readLine();

                    while (col < TeisPanel.maxScreenColumnas) {
                        String[] mapID = linea.split(" ");
                        int map = Integer.parseInt(mapID[col]);
                        mapaPiezaNum[col][fil] = map;
                        col++;
                    }

                    if (col == TeisPanel.maxScreenColumnas) {
                        col = 0;
                        fil++;
                    }
                }
                br.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }
    }
    public void pinta(Graphics2D g2) {
        // Filas y columnas
        int col = 0;
        int fil = 0;

        // Coordenadas
        int x = 0;
        int y = 0;

        // Automatización de generacion de texturas de fondo
        while (col < TeisPanel.maxScreenColumnas && fil < TeisPanel.maxScreenFilas) {

            int id = mapaPiezaNum[col][fil];
            g2.drawImage(pieza[id].image, x, y, TeisPanel.sizeFinal, TeisPanel.sizeFinal, null);
            col++;
            x += TeisPanel.sizeFinal;

            if (col == TeisPanel.maxScreenColumnas) {
                col = 0;
                x = 0;
                fil++;
                y += TeisPanel.sizeFinal;
            }
        }
    }
}



