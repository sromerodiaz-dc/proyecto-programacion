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
    MapSelector mapSelector = new MapSelector();
    String mapName = mapSelector.selectMap(); // variable que usaré cuando haya más de un mapa

    /**
     * Constructor de la clase `PiezaManager`. Este constructor inicializa el gestor de piezas y el mapa.
     */
    public PiezaManager(TeisPanel teis) {
        this.t = teis;

        // Crea un arreglo de objetos `Pieza` con un tamaño de 10. Este arreglo contendrá diferentes tipos de piezas.
        pieza = new Pieza[imagePaths.length];

        // Crea un arreglo bidimensional de enteros con dimensiones basadas en `TeisPanel.maxScreenColumnas` y `TeisPanel.maxScreenFilas`.
        // Este arreglo representa un mapa donde cada entero corresponde a un tipo específico de pieza.
        mapaPiezaNum = new int[teis.maxWorldCol][teis.maxWorldRow];

        // Carga las imágenes de las piezas.
        getPiezaImage();

        // Carga el mapa.
        loadMap(mapName);
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
                while (col < t.maxScreenColumnas && fil < t.maxScreenFilas) {

                    String linea = br.readLine();
                    // Bucle por cada elemento (separado por espacios) en la línea actual.
                    while (col < t.maxScreenColumnas) {
                        String[] mapID = linea.split(" ");
                        // Extrae el primer elemento (suponiendo que representa el ID del tipo de Pieza).
                        int map = Integer.parseInt(mapID[col]);
                        // Almacena el ID del tipo de Pieza en la posición correspondiente de mapaPiezaNum.
                        mapaPiezaNum[col][fil] = map;
                        col++;
                    }

                    // Reinicia la columna (col) e incrementa la fila (fil) para la siguiente línea.
                    if (col == t.maxScreenColumnas) {
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
        while (col < t.maxScreenColumnas && fil < t.maxScreenFilas) {
            // Obtiene el ID del tipo de Pieza en la posición actual del mapa.
            int id = mapaPiezaNum[col][fil];

            // Dibuja la imagen de la Pieza correspondiente en la posición actual.
            g2.drawImage(pieza[id].image, x, y, t.sizeFinal, t.sizeFinal, null);

            // Incrementa la columna (col) para pasar a la siguiente posición horizontal.
            col++;

            // Actualiza la coordenada X para la siguiente posición horizontal.
            x += t.sizeFinal;

            // Si se llega al final de la fila actual (col == TeisPanel.maxScreenColumnas),
            // reinicia la columna (col) y la coordenada X, e incrementa la fila (fil)
            // para pasar a la siguiente fila.
            if (col == t.maxScreenColumnas) {
                col = 0;
                x = 0;
                fil++;
                y += t.sizeFinal;
            }
        }
    }
}



