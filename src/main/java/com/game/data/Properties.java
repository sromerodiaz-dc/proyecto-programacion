package com.game.data;

import java.io.InputStream;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Clase que maneja las propiedades de las entidades desde un JSON.
 * Autor: Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 */
public class Properties implements AutoCloseable { //TODO para cada entidad un json diferente
    private static Properties instance;
    private List<Entidad> entidades;

    // Clase interna para mapear el JSON
    private static class Entidad {
        public String id;
        public int who;
        public String sentido;
        public int speed;
        public int intervalo;
        public int width;
        public int height;
        public int solidArea_x;
        public int solidArea_y;
        public int solidArea_width;
        public int solidArea_height;
        public int maxlife;
        public int life;
    }

    /**
     * Constructor privado para evitar instanciación externa.
     */
    private Properties() {
        cargarDatosDesdeJSON();
    }

    /**
     * Carga los datos desde el archivo JSON.
     */
    private void cargarDatosDesdeJSON() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/entity/entity.json")) {
            ObjectMapper mapper = new ObjectMapper();
            entidades = mapper.readValue(inputStream, mapper.getTypeFactory().constructCollectionType(List.class, Entidad.class));
        } catch (Exception e) {
            System.out.println("Error al cargar el JSON: " + e.getMessage());
        }
    }

    /**
     * Obtiene la instancia única de Properties.
     */
    public static synchronized Properties getInstance() {
        if (instance == null) {
            instance = new Properties();
        }
        return instance;
    }

    /**
     * Obtiene los datos de las entidades en formato Object[][].
     */
    public Object[][] obtenerDatosEntidad() {
        Object[][] datos = new Object[entidades.size()][13];
        for (int i = 0; i < entidades.size(); i++) {
            Entidad e = entidades.get(i);
            datos[i][0] = e.id;
            datos[i][1] = e.who;
            datos[i][2] = e.sentido.charAt(0);
            datos[i][3] = e.speed;
            datos[i][4] = e.intervalo;
            datos[i][5] = e.width;
            datos[i][6] = e.height;
            datos[i][7] = e.solidArea_x;
            datos[i][8] = e.solidArea_y;
            datos[i][9] = e.solidArea_width;
            datos[i][10] = e.solidArea_height;
            datos[i][11] = e.maxlife;
            datos[i][12] = e.life;
        }
        return datos;
    }

    @Override
    public void close() {
        instance = null; // Limpiar instancia al cerrar
    }
}