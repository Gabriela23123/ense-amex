package ensenamex.ensenamex.Bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//leer o modificar

public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/ensenamex";
    private static final String USER = "root";
    //private static final String PASSWORD = "Chocolate_901";
    private  static final String PASSWORD = "1234";

    /*
    Metodo estatico
    Connection con = ConexionDB.conectar();
     */
    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return null;
        }
    }
}
