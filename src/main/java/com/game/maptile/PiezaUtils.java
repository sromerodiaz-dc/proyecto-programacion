package com.game.maptile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * La clase PiezaUtils proporciona métodos de utilidad para el procesamiento de imágenes.
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class PiezaUtils {
    /**
     * Escala la BufferedImage dada a las dimensiones especificadas de ancho y alto.
     *
     * @param origin la BufferedImage original para escalar
     * @param width el ancho deseado de la imagen escalada
     * @param height la altura deseada de la imagen escalada
     * @return la BufferedImage escalada
     */
    public BufferedImage escalado(BufferedImage origin, int width, int height) {
        // Crea una nueva BufferedImage con las dimensiones deseadas y el mismo tipo que la imagen original
        BufferedImage escalado = new BufferedImage(width, height, origin.getType());

        // Crea un objeto Graphics2D para dibujar la imagen
        Graphics2D g2 = escalado.createGraphics();

        // Dibuja la imagen original en la nueva BufferedImage, escalándola a las dimensiones deseadas
        g2.drawImage(origin, 0, 0, width, height, null);

        // Libera los recursos del objeto Graphics2D
        g2.dispose();

        // Devuelve la imagen escalada
        return escalado;
    }

    /**
     * Obtiene las rutas de las imágenes desde el archivo "c_assets.txt".
     *
     * @return un arreglo de cadenas con las rutas de las imágenes
     */
    public String[] getImagePaths() {
        ArrayList<String> imagePaths = new ArrayList<>();

        System.out.println("Directorio actual: " + System.getProperty("user.dir"));
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("graphic/maps_correspondencia/c_assets.txt")) {
            assert is != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line.trim());
                    imagePaths.add(line.trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage() + " !PiezaManager");
        }

        return imagePaths.toArray(new String[0]);
    }
}
