package EDITOR.FX;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Clase que proporciona métodos para generar un mapa de sprites y obtener el nombre del mapa y las dimensiones.
 *
 * @author Santiago Agustin Romero Diaz
 */
public class SpriteUtils {
    /**
     * Genera un mapa de sprites en un archivo de texto.
     *
     * @param sprites matriz de ImageIcon que representan los sprites
     * @param formato matriz de ImageIcon que representan el formato del mapa
     * @throws IOException si ocurre un error al escribir el archivo
     */
    public void generateSpriteMap(ImageIcon[] sprites, ImageIcon[][] formato) throws IOException {
        String mapName = mapName();
        try (FileWriter fileWriter = new FileWriter("Assets/maps/"+mapName+".txt")) {

            for (ImageIcon[] imageIcons : formato) {
                for (ImageIcon imageIcon : imageIcons) {
                    boolean foundMatch = false;
                    int spriteIndex = 0;

                    if (imageIcon != null) {
                        for (ImageIcon sprite : sprites) {
                            if (imageIcon == sprite) {
                                fileWriter.write(spriteIndex + " ");
                                foundMatch = true;
                            }
                            spriteIndex++;
                        }
                    }

                    if (!foundMatch) {
                        fileWriter.write("0 ");
                    }
                }
                fileWriter.write(System.lineSeparator());
            }
        }
    }

    /**
     * Obtiene el nombre del mapa.
     *
     * @return nombre del mapa
     */
    public String mapName(){
        String userInput;
        String mapName = null;
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
     * Obtiene las dimensiones del mapa.
     *
     * @return matriz de enteros con el número de filas y columnas
     */
    public int[] numRowsCols() {
        int[] rowscols = new int[2];
        do {
            rowscols[0] = Integer.parseInt(JOptionPane.showInputDialog(null, "Introduzca el numero de filas:"));
            if (rowscols[0] < 1 || rowscols[0] > 30) {
                JOptionPane.showMessageDialog(null, "El numero de filas debe estar entre 1 y 30.");
            }
        } while (rowscols[0] < 1 || rowscols[0] > 30);

        do {
            rowscols[1] = Integer.parseInt(JOptionPane.showInputDialog(null, "Introduzca el numero de columnas:"));
            if (rowscols[1] < 1 || rowscols[1] > 30) {
                JOptionPane.showMessageDialog(null, "El numero de columnas debe estar entre 1 y 30.");
            }
        } while (rowscols[1] < 1 || rowscols[1] > 30);

        return rowscols;
    }
}
