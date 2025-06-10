package com.game.maptile;

import com.game.ui.TeisPanel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;
import java.util.Objects;

public class PiezaManager {
    TeisPanel t;
    public Pieza[] pieza;
    public Mapa mapa;
    public String mapName;

    public PiezaUtils piezaUtils = new PiezaUtils();

    public PiezaManager(TeisPanel teis) {
        this.t = teis;
        this.mapName = t.datos.fileName;

        // Primero cargamos el tileset
        cargarTileset();

        // Luego cargamos el mapa
        cargarMapa();
    }

    private void cargarTileset() {
        String tilesetPath = "graphic/tiles.json";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(tilesetPath)) {
            assert is != null;
            JSONTokener tokener = new JSONTokener(is);
            JSONObject root = new JSONObject(tokener);
            JSONArray tiles = root.getJSONArray("tiles");

            pieza = new Pieza[tiles.length()];
            for (int i = 0; i < tiles.length(); i++) {
                JSONObject tile = tiles.getJSONObject(i);
                int id = tile.getInt("id");
                String path = "graphic/background/" + tile.getString("path");

                pieza[id] = new Pieza();
                pieza[id].image = ImageIO.read(
                        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path))
                );
                pieza[id].image = piezaUtils.escalado(pieza[id].image, 48, 48);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarMapa() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(mapName)) {
            JSONTokener tokener = new JSONTokener(is);
            JSONObject root = new JSONObject(tokener);
            String firstKey = root.keys().next();
            JSONObject mapData = root.getJSONObject(firstKey);

            // 1. Cargar capa de terreno
            int width = mapData.getInt("width");
            int height = mapData.getInt("height");
            mapa = new Mapa(width, height);

            JSONArray data = mapData.getJSONArray("data");
            for (int y = 0; y < height; y++) {
                JSONArray row = data.getJSONArray(y);
                for (int x = 0; x < width; x++) {
                    mapa.capaTerreno[x][y] = row.getInt(x);
                }
            }

            // 2. Cargar capa de colisiones
            JSONArray collisions = mapData.getJSONArray("colisiones");
            for (int i = 0; i < collisions.length(); i++) {
                JSONArray coord = collisions.getJSONArray(i);
                int x = coord.getInt(1);
                int y = coord.getInt(0);
                mapa.capaColisiones.add(new Point(x, y));
            }

            // Actualizar dimensiones en TeisPanel
            t.maxWorldCol = width;
            t.maxWorldRow = height;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pinta(Graphics2D g2) {
        if (mapa == null) return;

        int playerWorldX = t.player.getWorldX();
        int playerWorldY = t.player.getWorldY();
        int playerScreenX = t.player.getScreenX();
        int playerScreenY = t.player.getScreenY();
        int SIZE_FINAL = TeisPanel.SIZE_FINAL;

        for (int y = 0; y < mapa.height; y++) {
            for (int x = 0; x < mapa.width; x++) {
                int id = mapa.capaTerreno[x][y];
                int worldX = x * SIZE_FINAL;
                int worldY = y * SIZE_FINAL;
                int screenX = worldX - playerWorldX + playerScreenX;
                int screenY = worldY - playerWorldY + playerScreenY;

                // Renderizar solo tiles visibles
                if (worldX + SIZE_FINAL > playerWorldX - playerScreenX &&
                        worldX - SIZE_FINAL < playerWorldX + playerScreenX &&
                        worldY + SIZE_FINAL > playerWorldY - playerScreenY &&
                        worldY - SIZE_FINAL < playerWorldY + playerScreenY) {

                    g2.drawImage(pieza[id].image, screenX, screenY, null);
                }
            }
        }
    }
}