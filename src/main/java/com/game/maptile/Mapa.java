package com.game.maptile;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Mapa {
    public int[][] capaTerreno;
    public Set<Point> capaColisiones;
    public int width;
    public int height;

    public Mapa(int width, int height) {
        this.width = width;
        this.height = height;
        this.capaTerreno = new int[width][height];
        this.capaColisiones = new HashSet<>();
    }
}