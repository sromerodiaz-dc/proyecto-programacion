package EDITOR.FX;

import EDITOR.GUI.GUI;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.spi.AbstractResourceBundleProvider;

/**
 * Clase que proporciona métodos para generar un mapa de sprites y obtener el nombre del mapa y las dimensiones.
 *
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class SpriteUtils {
    public SpriteUtils(){}
    /**
     * Genera un mapa de sprites escrito en un archivo de texto a modo de matriz de números.
     * Cada número corresponde a un sprite!
     *
     * @param sprites La lista de ImageIcon que representan los sprites.
     * @param formato El formato del mapa de sprites.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    public void generateSpriteMap(ImageIcon[] sprites, ImageIcon[][] formato) throws IOException {
        // Recoge el nombre del mapa a través de otro método de la misma clase
        String mapName = mapName();
        if (mapName != null) {
            // Escribir el mapa de sprites en un archivo de texto
            try (FileWriter fileWriter = new FileWriter("Assets/maps/" + mapName + ".txt")) {

                // Recorrer las filas y columnas del formato y buscar la correspondencia con los sprites
                for (ImageIcon[] imageIcons : formato) {
                    for (ImageIcon imageIcon : imageIcons) {
                        boolean foundMatch = false;
                        int spriteIndex = 0;

                        if (imageIcon != null) {
                            // Recorrer los sprites y comparar con el ImageIcon actual
                            for (ImageIcon sprite : sprites) {
                                if (imageIcon.equals(sprite)) {
                                    // Si hay una correspondencia, escribir el índice del sprite en el archivo de texto
                                    fileWriter.write(spriteIndex + " ");
                                    foundMatch = true;
                                }
                                spriteIndex++;
                            }
                        }

                        // Si no se encontró una correspondencia, escribir un cero en el archivo de texto
                        if (!foundMatch) {
                            fileWriter.write("0 ");
                        }
                    }
                    fileWriter.write(System.lineSeparator());
                }
            }
        }
    }

    /**
     * Solicita al usuario que introduzca el nombre del mapa y devuelve el nombre del mapa sin la extensión .txt.
     *
     * @return El nombre del mapa sin la extensión .txt.
     */
    public String mapName() {
        String userInput;
        String mapName = null;

        // Solicitar al usuario que introduzca el nombre del mapa hasta que se introduzca un nombre válido
        while (true) {
            userInput = JOptionPane.showInputDialog(null, "Introduzca el nombre del mapa:");
            if (userInput == null) {
                // El usuario canceló la operación, salir del método
                break; // o lanzar una excepción, según sea necesario
            } else if (!userInput.isEmpty()) {
                mapName = userInput.trim();
                // Quitar extensión .txt si existe
                if (mapName.toLowerCase().endsWith(".txt")) {
                    mapName = mapName.substring(0, mapName.length() - 4);
                }
                break; // salir del bucle si se introdujo un nombre válido
            }
        }

        return mapName;
    }

    /**
     * Solicita al usuario que introduzca el número de filas y columnas del mapa y devuelve un array de enteros con
     * el número de filas y columnas. El método continúa solicitando al usuario que introduzca el número de filas
     * y columnas hasta que se introduzcan valores válidos entre 1 y 50.
     * -
     * En otras palabras, especifica las dimensiones del mapa que se quiere crear
     *
     * @return Un array de enteros con el número de filas y columnas.
     */
    public int[] numRowsCols() {
        int[] rowscols = new int[2];

        rowscols[0] = solicitarEntero('f');
        rowscols[1] = solicitarEntero('c');

        return rowscols;
    }

    /**
     * Solicita al usuario que introduzca un entero entre min y max, y se asegura de que el valor esté entre min y max.
     *
     * @return el entero introducido por el usuario
     */
    private int solicitarEntero(char c) {
        int valor = 0;
        String entrada;
        boolean entradaValida = false;

        String string = "Introduce el número de filas";
        if (c != 'f')
            string = "Introduce el número de columnas";

        do {
            entrada = JOptionPane.showInputDialog(null, string);

            if (entrada == null) {
                // Si la entrada es nula, se vuelve a solicitar un valor
                JOptionPane.showMessageDialog(null, "La entrada no puede ser nula.");
            } else {
                try {
                    // Se intenta convertir la entrada en un entero
                    valor = Integer.parseInt(entrada);
                } catch (NumberFormatException e) {
                    // Si la entrada no se puede convertir en un entero, se vuelve a solicitar un valor
                    JOptionPane.showMessageDialog(null, "La entrada debe ser un número entero.");
                }
            }

            if (valor < 1 || valor > 50) {
                // Si el valor está fuera del rango permitido, se vuelve a solicitar un valor
                JOptionPane.showMessageDialog(null, "El valor debe estar entre " + 1 + " y " + 50 + ".");
            } else entradaValida = true;
        } while (!entradaValida);

        return valor;
    }
}
