package GAME;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CONNECT_BBDD {
    private Connection conn;
    private String url = "jdbc:postgresql://localhost:5432/ydatabase";
    private String user = "yuser";
    private String password = "ypassword";

    public CONNECT_BBDD() {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public void createTableAndInsertData() {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS enemies (" +
                    "id VARCHAR(50) PRIMARY KEY NOT NULL, " +
                    "who INTEGER NOT NULL, " +
                    "sentido CHAR(1) NOT NULL, " +
                    "speed INTEGER NOT NULL, " +
                    "max_life INTEGER NOT NULL, " +
                    "life INTEGER NOT NULL, " +
                    "height INTEGER NOT NULL, " +
                    "width INTEGER NOT NULL, " +
                    "solid_area_x INTEGER NOT NULL, " +
                    "solid_area_y INTEGER NOT NULL, " +
                    "solid_area_width INTEGER NOT NULL, " +
                    "solid_area_height INTEGER NOT NULL, " +
                    "default_solid_area_x INTEGER NOT NULL, " +
                    "default_solid_area_y INTEGER NOT NULL, " +
                    "intervalo INTEGER NOT NULL" +
                    ");");

            // Insertar registros
            stmt.execute("INSERT INTO enemies (who, id, sentido, speed, max_life, life, height, width, solid_area_x, solid_area_y, solid_area_width, solid_area_height, default_solid_area_x, default_solid_area_y, intervalo) VALUES (2, 'Viello_feroz', '1', 2, 30, 30, 50, 40, 15, 30, 30, 30, 15, 30, 20)");
            stmt.execute("INSERT INTO enemies (who, id, sentido, speed, max_life, life, height, width, solid_area_x, solid_area_y, solid_area_width, solid_area_height, default_solid_area_x, default_solid_area_y, intervalo) VALUES (2, 'Dinoseto_elegante', '0', 1, 20, 20, 40, 35, 10, 25, 25, 25, 10, 25, 15)");

            // Asignar variables locales
            String query = "SELECT * FROM enemies";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String id = rs.getString("id");
                int who = rs.getInt("who");
                String sentido = rs.getString("sentido");
                int speed = rs.getInt("speed");
                int maxLife = rs.getInt("max_life");
                int life = rs.getInt("life");
                int height = rs.getInt("height");
                int width = rs.getInt("width");
                int solidAreaX = rs.getInt("solid_area_x");
                int solidAreaY = rs.getInt("solid_area_y");
                int solidAreaWidth = rs.getInt("solid_area_width");
                int solidAreaHeight = rs.getInt("solid_area_height");
                int defaultSolidAreaX = rs.getInt("default_solid_area_x");
                int defaultSolidAreaY = rs.getInt("default_solid_area_y");
                int intervalo = rs.getInt("intervalo");

                // Ahora puedes utilizar estas variables locales
                System.out.println("ID: " + id);
                System.out.println("Who: " + who);
                System.out.println("Sentido: " + sentido);
                System.out.println("Speed: " + speed);
                System.out.println("Max Life: " + maxLife);
                System.out.println("Life: " + life);
                System.out.println("Height: " + height);
                System.out.println("Width: " + width);
                System.out.println("Solid Area X: " + solidAreaX);
                System.out.println("Solid Area Y: " + solidAreaY);
                System.out.println("Solid Area Width: " + solidAreaWidth);
                System.out.println("Solid Area Height: " + solidAreaHeight);
                System.out.println("Default Solid Area X: " + defaultSolidAreaX);
                System.out.println("Default Solid Area Y: " + defaultSolidAreaY);
                System.out.println("Intervalo: " + intervalo);
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
    }
}