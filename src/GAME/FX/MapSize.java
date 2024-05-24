package GAME.FX;

/**
 * Clase que representa el tamaño de un mapa, incluyendo el número de filas y columnas, y el nombre del archivo.
 * @author Santiago Agustin Romero Diaz
 * @version 1.0
 */
public class MapSize {
    /**
     * Número de columnas del mapa.
     */
    public int maxCol;

    /**
     * Número de filas del mapa.
     */
    public int maxRow;

    /**
     * Nombre del archivo del mapa.
     */
    public String fileName;

    /**
     * Constructor de la clase MapSize.
     *
     * @param maxCol Número de columnas del mapa.
     * @param maxRow Número de filas del mapa.
     * @param fileName Nombre del archivo del mapa.
     */
    public MapSize(int maxCol, int maxRow, String fileName) {
        this.maxCol = maxCol;
        this.maxRow = maxRow;
        this.fileName = fileName;
    }
}