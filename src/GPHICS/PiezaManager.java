package GPHICS;

import FX.TeisPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Esta clase define el algoritmo de mapeo del juego.
 * */
public class PiezaManager {
    // Atributos
    Pieza[] pieza;
    int[][] mapaPiezaNum;
    String mapName; // variable que usaré cuando haya más de un mapa

    /**
     * Constructor de la clase `PiezaManager`. Este constructor inicializa el gestor de piezas y el mapa.
     */
    public PiezaManager() {

        // Crea un arreglo de objetos `Pieza` con un tamaño de 10. Este arreglo contendrá diferentes tipos de piezas.
        Pieza[] pieza = new Pieza[10];


        // Crea un arreglo bidimensional de enteros con dimensiones basadas en `TeisPanel.maxScreenColumnas` y `TeisPanel.maxScreenFilas`.
        // Este arreglo representa un mapa donde cada entero corresponde a un tipo específico de pieza.
        int[][] mapaPiezaNum = new int[TeisPanel.maxScreenColumnas][TeisPanel.maxScreenFilas];


        // Carga las imágenes de las piezas.
        getPiezaImage();
        // Carga el mapa.
        loadMap("maps/default.txt");
    }


    /**
     * Carga las imágenes de las Piezas predefinidas (pasto, muro, agua).
     */
    public void getPiezaImage() {
        try {
            // Crea una Pieza en la posición 0 del arreglo 'pieza' y carga la imagen "background/grass01.png" para su atributo 'image'.
            // Esta es la dinamica de cada par de lineas de codigo dentro del try.
            pieza[0] = new Pieza();
            pieza[0].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("background/grass01.png"));

            pieza[1] = new Pieza();
            pieza[1].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("background/wall.png"));

            pieza[2] = new Pieza();
            pieza[2].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("background/water01.png"));

            pieza[3] = new Pieza();
            pieza[3].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("background/alcantarilla.png"));

            pieza[4] = new Pieza();
            pieza[4].image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("background/sueloEstandarPlaza.png"));
        } catch (Exception e) {
            // En caso de ocurrir una excepción al cargar las imágenes, se imprime el mensaje de error.
            System.out.println(e.getMessage());
        }
    }

    /**
     * Carga el diseño del mapa desde un archivo ubicado en el classpath con el nombre especificado en `mapName`.
     *
     * @param mapName El nombre del archivo del mapa (por ejemplo, "maps/default.txt").
     */
    public void loadMap(String mapName) {
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
                while (col < TeisPanel.maxScreenColumnas && fil < TeisPanel.maxScreenFilas) {

                    String linea = br.readLine();
                    // Bucle por cada elemento (separado por espacios) en la línea actual.
                    while (col < TeisPanel.maxScreenColumnas) {
                        String[] mapID = linea.split(" ");
                        // Extrae el primer elemento (suponiendo que representa el ID del tipo de Pieza).
                        int map = Integer.parseInt(mapID[col]);
                        // Almacena el ID del tipo de Pieza en la posición correspondiente de mapaPiezaNum.
                        mapaPiezaNum[col][fil] = map;
                        col++;
                    }

                    // Reinicia la columna (col) e incrementa la fila (fil) para la siguiente línea.
                    if (col == TeisPanel.maxScreenColumnas) {
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
        int col = 0;
        int fil = 0;

        // Variables coordenadas X e Y.
        int x = 0;
        int y = 0;

        // Bucle que recorre el mapa fila por fila, dibujando las piezas correspondientes.
        while (col < TeisPanel.maxScreenColumnas && fil < TeisPanel.maxScreenFilas) {
            // Obtiene el ID del tipo de Pieza en la posición actual del mapa.
            int id = mapaPiezaNum[col][fil];

            // Dibuja la imagen de la Pieza correspondiente en la posición actual.
            g2.drawImage(pieza[id].image, x, y, TeisPanel.sizeFinal, TeisPanel.sizeFinal, null);

            // Incrementa la columna (col) para pasar a la siguiente posición horizontal.
            col++;

            // Actualiza la coordenada X para la siguiente posición horizontal.
            x += TeisPanel.sizeFinal;

            // Si se llega al final de la fila actual (col == TeisPanel.maxScreenColumnas),
            // reinicia la columna (col) y la coordenada X, e incrementa la fila (fil)
            // para pasar a la siguiente fila.
            if (col == TeisPanel.maxScreenColumnas) {
                col = 0;
                x = 0;
                fil++;
                y += TeisPanel.sizeFinal;
            }
        }
    }
}



