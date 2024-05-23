package EDITOR.FX;

import EDITOR.GUI.GUI;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Clase que proporciona métodos para generar un mapa de sprites y obtener el nombre del mapa y las dimensiones.
 *
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class SpriteUtils {
    public SpriteUtils(GUI gui){}
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

    /**
     * Solicita al usuario que introduzca el nombre del mapa y devuelve el nombre del mapa sin la extensión .txt.
     *
     * @return El nombre del mapa sin la extensión .txt.
     */
    public String mapName() {
        String userInput;
        String mapName = null;

        // Solicitar al usuario que introduzca el nombre del mapa hasta que se introduzca un nombre válido
        do {
            userInput = JOptionPane.showInputDialog(null, "Introduzca el nombre del mapa:");
            if (userInput != null && !userInput.isEmpty()) {
                mapName = userInput.trim();
                // Quitar extensión .txt si existe
                if (mapName.toLowerCase().endsWith(".txt")) {
                    mapName = mapName.substring(0, mapName.length() - 4);
                }
            }
        } while (mapName == null || mapName.isEmpty());

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
    public int[] numRowsCols(GUI gui) {
        int[] rowscols = new int[2];

        // Solicitar al usuario que introduzca el número de filas hasta que se introduzca un valor válido
        do {
            rowscols[0] = Integer.parseInt(JOptionPane.showInputDialog(null, "Introduzca el número de filas:"));
            if (rowscols[0] < 1 || rowscols[0] > 50) {
                JOptionPane.showMessageDialog(null, "El número de filas debe estar entre 1 y 50.");
            }
        } while (rowscols[0] < 1 || rowscols[0] > 50);

        // Solicitar al usuario que introduzca el número de columnas hasta que se introduzca un valor válido
        do {
            rowscols[1] = Integer.parseInt(JOptionPane.showInputDialog(null, "Introduzca el número de columnas:"));
            if (rowscols[1] < 1 || rowscols[1] > 50) {
                JOptionPane.showMessageDialog(null, "El número de columnas debe estar entre 1 y 50.");
            }
        } while (rowscols[1] < 1 || rowscols[1] > 50);

        if (rowscols[0]>30 && rowscols[1]>30){
            gui.MENU_IZQUIERDA_X = 100;
            gui.MENU_IZQUIERDA_ALTO = 800;
            gui.MENU_IZQUIERDA_ANCHO = 1050;

            gui.MENU_INFERIOR_X = 1150;
            gui.MENU_INFERIOR_Y = 670;
        }
        return rowscols;
    }
}
