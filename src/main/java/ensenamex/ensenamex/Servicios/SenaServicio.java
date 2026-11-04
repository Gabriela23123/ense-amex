package ensenamex.ensenamex.Servicios;

import ensenamex.ensenamex.Bdd.ConexionDB;
import ensenamex.ensenamex.Bdd.DbUtil;
import ensenamex.ensenamex.Clases.Sena;

import java.sql.Connection;//canal de comunicacion entre Java y la BDD
import java.sql.SQLException;
import java.util.List;

//operaciones relacionadas con las señas

public class SenaServicio {

    private final DbUtil crudBdd = new DbUtil();//para poder utilizar los métodos de acceso a la base de datos.

    Connection conn = ConexionDB.conectar();
    //comunicacion con la BDD

    public List<Sena> obtenerSenas() throws SQLException {
        List<Sena> senas = crudBdd.getSenas(conn);
        return senas;//obtenerSenas(): metodo que devuelve lista completa de objetos tipo sena
    }

    public List<Sena> obtenerSenas(String i) throws SQLException {
        List<Sena> senas = crudBdd.getSenas(conn,i);
        return senas;//trae las senas que coincidan con i
    }

    public void agregarSena(String nombre, String descripcion, String url, int idUsuario, int tipo) throws SQLException {
        crudBdd.agregarSena(conn,nombre,descripcion,url,idUsuario, tipo);
        //este metodo le ordena a la BDD insertar el nuevo registro de sena
    }
}

