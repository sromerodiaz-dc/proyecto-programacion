package GAME.GPHICS;

import GAME.FX.MapSelector;
import GAME.GAME.TeisPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Esta clase define el algoritmo de mapeo del juego.
 * */
public class PiezaManager {
    // Atributos
    TeisPanel t;
    public Pieza[] pieza;
    public int[][] mapaPiezaNum;
    public String[] imagePaths = getImagePaths();
    public String mapName;

    /**
     * Constructor de la clase `PiezaManager`. Este constructor inicializa el gestor de piezas y el mapa.
     */
    public PiezaManager(TeisPanel teis, String mapName) {
        this.t = teis;
        this.mapName = mapName;

        // Crea un arreglo de objetos `Pieza` con un tamaño de 10. Este arreglo contendrá diferentes tipos de piezas.
        pieza = new Pieza[imagePaths.length];

        // Crea un arreglo bidimensional de enteros con dimensiones basadas en `TeisPanel.maxScreenColumnas` y `TeisPanel.maxScreenFilas`.
        // Este arreglo representa un mapa donde cada entero corresponde a un tipo específico de pieza.
        mapaPiezaNum = new int[teis.maxWorldCol][teis.maxWorldRow];

        // Carga las imágenes de las piezas.
        getPiezaImage();

        // Carga el mapa.
        loadMap();
    }


    /**
     * Carga las imágenes de las Piezas predefinidas (pasto, muro, agua).
     */
    public void getPiezaImage() {
        try {
            for (int i = 0; i < pieza.length && i < imagePaths.length; i++) {
                pieza[i] = new Pieza();
                pieza[i].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imagePaths[i]));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String[] getImagePaths() {
        ArrayList<String> imagePaths = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("Assets/maps_correspondencia/c_assets.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("^\\d+:\\s*Assets\\\\", "");
                line = line.replaceAll("\\\\", "/");
                imagePaths.add(line.trim());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return imagePaths.toArray(new String[0]);
    }

    /**
     * Carga el diseño del mapa desde un archivo ubicado en el classpath con el nombre especificado en `mapName`.
     */
    public void loadMap() {
        InputStream is;
        BufferedReader br;
        try {
            // Intenta obtener un InputStream para el archivo del mapa utilizando el classpath.
            is = getClass().getClassLoader().getResourceAsStream(mapName);
            if (is != null) {
                // Encuentra el archivo del mapa, procede a leerlo.
                br = new BufferedReader(new InputStreamReader(is));

                // Variables para controlar la columna (col) y la fila (fil) en el mapa.
                int col = 0, fil = 0;

                // Bucle hasta que tanto las columnas como las filas alcancen sus límites máximos.
                while (col < t.maxWorldCol && fil < t.maxWorldRow) {
                    String linea = br.readLine();

                    // Bucle por cada elemento (separado por espacios) en la línea actual.
                    while (col < t.maxWorldCol) {
                        String[] mapID = linea.split(" ");

                        // Extrae el primer elemento (suponiendo que representa el ID del tipo de Pieza).
                        int map = Integer.parseInt(mapID[col]);

                        // Almacena el ID del tipo de Pieza en la posición correspondiente de mapaPiezaNum.
                        mapaPiezaNum[col][fil] = map;
                        col++;
                    }
                    // Reinicia la columna (col) e incrementa la fila (fil) para la siguiente línea.
                    if (col == t.maxWorldCol) {
                        col = 0;
                        fil++;
                    }
                }
                // Cierra el BufferedReader para liberar recursos.
                br.close();
            } else {
                // No se encontró el archivo del mapa.
                System.out.println("Error: ¡No se encontró el archivo del mapa '" + mapName + "'!");
            }
        } catch (IOException e) {
            // Maneja cualquier IOException que pueda ocurrir durante la lectura del archivo.
            System.out.println("Error: ¡Ocurrió un error al leer el archivo del mapa!");
            e.printStackTrace(); // Opcional: Imprime la traza de pila para depuración.
        }
    }

    /**
     * Dibuja las piezas del mapa en el lienzo gráfico.
     *
     * @param g2 El objeto Graphics2D utilizado para dibujar en el lienzo.
     */
    public void pinta(Graphics2D g2) {
        // Variables para controlar las columnas (col) y las filas (fil) del mapa.
        int worldCol = 0;
        int worldFil = 0;

        // Bucle que recorre el mapa fila por fila, dibujando las piezas correspondientes.
        while (worldCol < t.maxWorldCol && worldFil < t.maxWorldRow) {
            // Obtiene el ID del tipo de Pieza en la posición actual del mapa.
            int id = mapaPiezaNum[worldCol][worldFil];

            /*
             Para tener una cámara estática encima de nuestro PJ se necesita conocer la posición
             del PJ en siempre y por ello se necesitan 2 tipos de coordenadas:

             Coordenadas de pantalla y del mapa entero y coordenadas relativas al jugador
            */

            // Coordenadas relativas al jugador
            int playerWorldX = t.model.worldX;
            int playerWorldY = t.model.worldY;
            int playerScreenX = t.model.screenX;
            int playerScreenY = t.model.screenY;

            // Coordenadas de pantalla relativas al jugador
            int worldX = worldCol * t.sizeFinal;
            int worldY = worldFil * t.sizeFinal;
            int screenX = worldX - playerWorldX + playerScreenX;
            int screenY = worldY - playerWorldY + playerScreenY;

            if (worldX > playerWorldX - playerScreenX && worldX < playerWorldX + playerScreenX &&
                worldY > playerWorldY - playerScreenY && worldY < playerWorldY + playerScreenY) {
                // Dibuja la imagen de la Pieza correspondiente en la posición actual.
                g2.drawImage(pieza[id].image, screenX, screenY, t.sizeFinal, t.sizeFinal, null);
            }

            // Incrementa la columna (col) para pasar a la siguiente posición horizontal.
            worldCol++;

            // Si se llega al final de la fila actual (col == TeisPanel.maxScreenColumnas),
            // reinicia la columna (col) y la coordenada X, e incrementa la fila (fil)
            // para pasar a la siguiente fila.
            if (worldCol == t.maxWorldCol) {
                worldCol = 0;
                worldFil++;
            }
        }
    }
}



