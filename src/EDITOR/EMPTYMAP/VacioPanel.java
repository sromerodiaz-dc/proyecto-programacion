package EDITOR.EMPTYMAP;

import EDITOR.FX.SpriteLoader;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VacioPanel extends JPanel {
    private final List<CeldaVacia> celdas; // Lista de paneles cuadrados

    TileSelected sprite;

    public VacioPanel(int rows, int cols) throws IOException {
        celdas = new ArrayList<>();

        setBackground(Color.DARK_GRAY);
        setLayout(new GridLayout(rows,cols)); // Usar GridBagLayout para el diseño
        GridBagConstraints constraints = new GridBagConstraints();

        int contador = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
               /* TileSelected[] sprites = SpriteLoader.loadSprites("Assets/player");
                if (sprites.length > contador){
                    sprite = sprites[contador];
                    contador++;
                }*/

                //completar
                CeldaVacia celda = new CeldaVacia(new ImageIcon());
                celdas.add(celda);

                constraints.gridx = col;
                constraints.gridy = row;
                add(celda, constraints);
            }
        }
    }

    public List<CeldaVacia> getCeldas() {
        return celdas;
    }
}
