package com.game.manager;

import javax.swing.*;
import java.io.*;
import java.util.Objects;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Contiene los metodos que seleccionan el mapa inicial de juego.
 * */
public class MapSelector {
    /**
     * Selecciona un mapa desde la carpeta "Assets/maps" y devuelve la ruta del mapa seleccionado.
     * Este metodo lista todos los archivos de texto (.txt) en la carpeta "Assets/maps", los muestra en un diálogo de selección
     * y devuelve la ruta del archivo seleccionado.
     *
     * @return la ruta del mapa seleccionado
     */
    public String selectMap() {
        // Obtiene un arreglo de archivos de la carpeta "Assets/maps" que terminan en ".txt"
        File[] mapFiles = getFiles();

        // Crea un arreglo de opciones para el diálogo de selección
        String[] options = new String[mapFiles.length];
        for (int i = 0; i < mapFiles.length; i++) {
            // Crea una opción con el número de opción y el nombre del archivo
            options[i] = (i + 1) + ". " + mapFiles[i].getName();
        }

        // Muestra un diálogo de selección con las opciones
        int choice = JOptionPane.showOptionDialog(
                null,
                "Select a map:", // Título del diálogo
                "Map Selector", // Título de la ventana
                JOptionPane.DEFAULT_OPTION, // Opción predeterminada
                JOptionPane.PLAIN_MESSAGE, // Tipo de mensaje
                null, // Icono (ninguno)
                options, // Opciones
                options[0] // Opción predeterminada
        );

        // Verifica si la selección es válida
        if (choice < 0 || choice >= options.length) {
            // Lanza una excepción si la selección es inválida
            throw new RuntimeException("Invalid map selection.");
        }

        // Devuelve la ruta del archivo seleccionado, reemplazando '\' por '/' y eliminando "Assets/"
        return mapFiles[choice].getPath().replace("\\", "/").replace("Assets/", "");
    }

    /**
     * Obtiene un arreglo de archivos de la carpeta "Assets/maps" que terminan en ".txt".
     *
     * Este método crea un objeto `File` para la carpeta "Assets/maps" y verifica si existe y es una carpeta.
     * Luego, lista todos los archivos de texto (.txt) en la carpeta y devuelve un arreglo de archivos.
     *
     * @return un arreglo de archivos de mapas
     */
    public File[] getFiles() {
        // Crea un objeto File para la carpeta "Assets/maps"
        File mapsFolder = new File("maps");

        // Verifica si la carpeta existe y es una carpeta
        if (!mapsFolder.exists() || !mapsFolder.isDirectory()) {
            // Lanza una excepción si la carpeta no existe o no es una carpeta
            throw new RuntimeException("The maps folder does not exist or is not a directory.");
        }

        // Obtiene un arreglo de archivos de texto (.txt) en la carpeta
        File[] mapFiles = mapsFolder.listFiles((_, name) -> Objects.requireNonNull(name).toLowerCase().endsWith(".txt"));

        // Verifica si el arreglo de archivos es nulo o vacío
        if (mapFiles == null || mapFiles.length == 0) {
            // Lanza una excepción si no se encuentran archivos de texto en la carpeta
            throw new RuntimeException("No text files found in the maps folder.");
        }

        // Devuelve el arreglo de archivos de mapas
        return mapFiles;
    }

    /**
     * Obtiene el tamaño del mapa leyéndolo desde un archivo de texto.
     *
     * @return Un objeto MapSize con el tamaño del mapa.
     */
    public MapSize getMapSize() {
        //String fileName = selectMap(); // Selecciona el nombre del archivo del mapa

        String fileName = "maps/pruebas.txt";

        InputStream is; // Flujo de entrada para leer el archivo
        BufferedReader br; // Lector de búfer para leer el archivo línea por línea
        int maxCol = 0, maxRow = 0; // Variables para almacenar el tamaño del mapa

        try {
            is = getClass().getClassLoader().getResourceAsStream(fileName); // Obtiene el flujo de entrada para el archivo
            if (is != null) {
                br = new BufferedReader(new InputStreamReader(is)); // Crea un lector de búfer para leer el archivo

                String linea; // Variable para almacenar cada línea del archivo
                while ((linea = br.readLine()) != null) { // Lee cada línea del archivo
                    String[] mapID = linea.split(" "); // Divide la línea por espacios en blanco
                    if (mapID.length > maxCol) { // Si la longitud del array es mayor que el número de columnas actual
                        maxCol = mapID.length; // Actualiza el número de columnas
                    }
                    maxRow++; // Incrementa el número de filas
                }

                br.close(); // Cierra el lector de búfer
            } else {
                System.out.println("Error: ¡No se encontró el archivo del mapa '" + fileName + "'!");
            }
        } catch (IOException e) { // Maneja las excepciones de entrada/salida
            System.out.println("Error: ¡Ocurrió un error al leer el archivo del mapa!");
            e.printStackTrace();
        }

        return new MapSize(maxCol, maxRow, fileName); // Devuelve un objeto MapSize con el tamaño del mapa
    }
}