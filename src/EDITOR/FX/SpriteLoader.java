package EDITOR.FX;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Esta clase define la interacción del jugador con el entorno asi como su movimiento y uso de gráficos en 2D.
 * */
public class SpriteLoader {
    public List<ImageIcon> loadSprites(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("No es una carpeta: " + folderPath);
        }

        List<ImageIcon> sprites = new ArrayList<>();
        List<String> imagePaths = new ArrayList<>();

        for (File file : folder.listFiles()) {
            try {
                BufferedImage image = ImageIO.read(file);
                sprites.add(new ImageIcon(image));
                imagePaths.add(file.getPath());
            } catch (IOException e) {
                e.printStackTrace();
                sprites.add(null);
            }
        }

        File file = new File("Assets/maps_correspondencia/c_assets.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
                for (int i = 0; i < sprites.size(); i++) {
                    if (sprites.get(i) != null) {
                        writeToFile(i, imagePaths.get(i));
                    } else {
                        writeToFile(i, null);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sprites;
    }

    private void writeToFile(int index, String path) {
        File file = new File("Assets/maps_correspondencia/c_assets.txt");
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            StringBuilder sb = new StringBuilder();
            if (path != null) {
                sb.append(index).append(": ").append(path);
            } else {
                sb.append(index).append(": Error loading image");
            }
            writer.println(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
