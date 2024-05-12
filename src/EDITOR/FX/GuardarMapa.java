package EDITOR.FX;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GuardarMapa {

    int altoMapa, anchoMapa;
    int[][] mapa;

    public GuardarMapa (int altoMap, int anchoMap,int[][] mapa){
        this.altoMapa = altoMap;
        this.anchoMapa = anchoMap;
        this.mapa = mapa;
    }

    private void guardarMapa(File archivo) throws IOException {
        FileWriter escritor = new FileWriter(archivo);

        for (int y = 0; y < altoMapa; y++) {
            for (int x = 0; x < anchoMapa; x++) {
                escritor.write(mapa[y][x] + " ");
            }
            escritor.write("\n");
        }

        escritor.close();
        System.out.println("Mapa guardado en: " + archivo.getAbsolutePath());
    }
}