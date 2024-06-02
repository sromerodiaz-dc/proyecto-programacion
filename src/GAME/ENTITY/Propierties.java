package GAME.ENTITY;

import java.sql.*;

/**
 * @author Santiago Agustin Romero Diaz
 * CFP Daniel Castelao
 * Proyecto: Teis
 * -
 * Clase que maneja las propiedades de la base de datos.
 */
public class Propierties {
    private Connection conexion;

    /**
     * Constructor que establece la conexión a la base de datos.
     *
     * @param url La URL de la base de datos.
     * @param usuario El usuario de la base de datos.
     * @param password La contraseña de la base de datos.
     */
    public Propierties(String url, String usuario, String password) {
        try {
            // Carga el driver de PostgreSQL
            Class.forName("org.postgresql.Driver");
            // Establece la conexión a la base de datos
            conexion = DriverManager.getConnection(url, usuario, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    /**
     * Crea la tabla "entidad" en la base de datos.
     */
    public void crearTablaEntidad() {
        try (Statement stmt = conexion.createStatement()) {
            // Borra la tabla anterior si existe
            stmt.executeUpdate("DROP TABLE IF EXISTS public.entidad");

            // Crea la tabla
            String sql = "CREATE TABLE public.entidad (" +
                    "id VARCHAR(255) PRIMARY KEY, " +
                    "who INTEGER NOT NULL, " +
                    "sentido CHAR(1) NOT NULL DEFAULT '0', " +
                    "speed INTEGER NOT NULL, " +
                    "intervalo INTEGER NOT NULL, " +
                    "width INTEGER NOT NULL, " +
                    "height INTEGER NOT NULL, " +
                    "solidArea_x INTEGER NOT NULL, " +
                    "solidArea_y INTEGER NOT NULL, " +
                    "solidArea_width INTEGER NOT NULL, " +
                    "solidArea_height INTEGER NOT NULL," +
                    "maxlife INTEGER NOT NULL," +
                    "life INTEGER NOT NULL)";
            stmt.executeUpdate(sql);

            // Inserta los datos iniciales
            stmt.executeUpdate("INSERT INTO public.entidad (id, who, sentido, speed, intervalo, width, height, solidArea_x, solidArea_y, solidArea_width, solidArea_height, maxlife, life)" +
                    "VALUES ('player', 0, '0', 6, 7, 48, 48, 10, 22, 32, 20, 10, 10)");
            stmt.executeUpdate("INSERT INTO public.entidad (id, who, sentido, speed, intervalo, width, height, solidArea_x, solidArea_y, solidArea_width, solidArea_height, maxlife, life)" +
                    "VALUES ('Viello', 1, '0', 1, 14, 54, 72, 10, 25, 30, 38, 300, 300)");
            stmt.executeUpdate("INSERT INTO public.entidad (id, who, sentido, speed, intervalo, width, height, solidArea_x, solidArea_y, solidArea_width, solidArea_height, maxlife, life)" +
                    "VALUES ('Dinoseto_elegante', 2, '0', 2, 15, 92, 96, 10, 25, 66, 60, 20, 20)");
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla entidad: " + e.getMessage());
        }
    }

    /**
     * Obtiene los datos de la tabla "entidad".
     *
     * @return Un arreglo de objetos con los datos de la tabla.
     */
    public Object[][] obtenerDatosEntidad() {
        Object[][] datos = new Object[3][13];
        try (Statement stmt = conexion.createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM public.entidad");
            int i = 0;
            while (resultSet.next()) {
                datos[i][0] = resultSet.getString("id");
                datos[i][1] = resultSet.getInt("who");
                datos[i][2] = resultSet.getString("sentido").charAt(0);
                datos[i][3] = resultSet.getInt("speed");
                datos[i][4] = resultSet.getInt("intervalo");
                datos[i][5] = resultSet.getInt("width");
                datos[i][6] = resultSet.getInt("height");
                datos[i][7] = resultSet.getInt("solidArea_x");
                datos[i][8] = resultSet.getInt("solidArea_y");
                datos[i][9] = resultSet.getInt("solidArea_width");
                datos[i][10] = resultSet.getInt("solidArea_height");
                datos[i][11] = resultSet.getInt("maxlife");
                datos[i][12] = resultSet.getInt("life");
                i++;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener datos de la tabla entidad: " + e.getMessage());
        }
        return datos;
    }

    /**
     * Cierra la conexión a la base de datos.
     */
    public void cerrarConexion() {
        try {
            conexion.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}