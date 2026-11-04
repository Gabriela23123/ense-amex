package ensenamex.ensenamex.Bdd;

import ensenamex.ensenamex.Clases.*;
import javafx.scene.control.ContextMenu;

import java.sql.Connection;
import java.sql.PreparedStatement;//prepara consultas SQL de forma segura
import java.sql.ResultSet;//Es donde se guardan los resultados de una consulta select
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;//obtiene el ID generado automáticamente despues de un insert

public class DbUtil {


    //Metodo que crea una nueva petición de una seña
    public void agregarSena(Connection conn,
                            String nombre,
                            String descripcion,
                            String url,
                            int tipo,
                            int idUsuario) throws SQLException {
        String sqlPeticion = "INSERT INTO peticiones (nombre, descripcion, estado, idusuario, tipo) VALUES (?, ?, ?, ?, ?)";//consulta
        //PreparedStatement: Permite sustituir los ? de forma segura
        PreparedStatement psPeticion =
                conn.prepareStatement(
                        sqlPeticion,
                        Statement.RETURN_GENERATED_KEYS
                );//despues de insertar el registro: RETURN_GENERATED_KEYS devuelve el ID que se generó automáticamente
        psPeticion.setString(1, nombre);
        psPeticion.setString(2, descripcion);
        psPeticion.setInt(3, 2);
        psPeticion.setInt(4, idUsuario);
        psPeticion.setInt(5, tipo);
        psPeticion.executeUpdate();//ejecuta la consulta en la BDD
        ResultSet generatedKeys = psPeticion.getGeneratedKeys();//da el ID que MySQL creo
        if (!generatedKeys.next()) {//Si no existe un ID generado
            throw new SQLException("No se pudo obtener el id de la petición.");
        }
        int idPeticion = generatedKeys.getInt(1);
        //Obtiene el ID que MySQL genero automáticamente al insertar la petición y lo guarda en la variable idPeticion
        String sqlImagen = "INSERT INTO imagenes (url, idpeticion) VALUES (?, ?)";
        PreparedStatement psImagen =
                conn.prepareStatement(sqlImagen);
        psImagen.setString(1, url);
        psImagen.setInt(2, idPeticion);
        psImagen.executeUpdate();//ejecuta la consulta en la BDD
    }


    //Metodo que busca una sola seña específica
    public Sena getSena(Connection conn, String nombre) throws SQLException {
        String sql = "SELECT i.url, s.nombre, s.descripcion, p.tipo \n" +
                "FROM imagenes i\n" +
                "JOIN peticiones p ON i.idpeticion = p.id\n" +
                "JOIN senas s ON s.idpeticion = p.id\n" +
                "WHERE s.nombre = ?;";

        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, nombre);
        ResultSet rs = pstm.executeQuery();//executeQuery(): ejecuta la consulta SQL y devuelve los datos encontrados

        if (!rs.next()) throw new SQLException("No hay senas registradas para esa palabra.");//si la consulta NO devolvió datos
        switch( rs.getInt("tipo")){
            case 1:
                return new General(rs.getString("url"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getInt("tipo"));

            case 2:
                return new Ofensiva(rs.getString("url"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getInt("tipo"));

            default:
                System.out.println("Hubo un problema");
                break;
        }

        return null;
    }


    //Metodo que obtiene todas las señas registradas
    public List<Sena> getSenas(Connection conn) throws SQLException {
        String sql = "SELECT i.url, s.nombre, s.descripcion, p.tipo \n" +
                    "FROM imagenes i\n" +
                    "JOIN peticiones p ON i.idpeticion = p.id\n" +
                    "JOIN senas s ON s.idpeticion = p.id;";

        ArrayList<Sena> registros = new ArrayList<>();
        try (PreparedStatement pstm = conn.prepareStatement(sql); ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                String url = rs.getString("url");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                int tipo = rs.getInt("tipo");;
                switch(tipo){
                    case 1:
                        General sena1 = new General(url, nombre, descripcion, tipo);
                        registros.add(sena1);
                        break;
                    case 2:
                        Ofensiva sena2 = new Ofensiva(url, nombre, descripcion, tipo);
                        registros.add(sena2);
                        break;
                    default:
                        System.out.println("Hubo un problema");
                        break;
                }
            }
        }
        return registros;
    }


    //Metodo que obtiene las 10 señas más recientes
    public List<Sena> getSenas(Connection conn,String x) throws SQLException {
        String sql = "SELECT i.url, s.nombre, s.descripcion, p.tipo \n" +
                "FROM imagenes i\n" +
                "JOIN peticiones p ON i.idpeticion = p.id\n" +
                "JOIN senas s ON s.idpeticion = p.id\n" +
                "ORDER BY s.id DESC LIMIT 10;";

        ArrayList<Sena> registros = new ArrayList<>();

        try (PreparedStatement pstm = conn.prepareStatement(sql); ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                String url = rs.getString("url");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                int tipo = rs.getInt("tipo");
                switch(tipo) {
                    case 1:
                        General sena1 = new General(url, nombre, descripcion, tipo);
                        registros.add(sena1);
                        break;
                    case 2:
                        Ofensiva sena2 = new Ofensiva(url, nombre, descripcion, tipo);
                        registros.add(sena2);
                        break;
                    default:
                        System.out.println("Hubo un problema");
                        break;
                }
            }
        }
        return registros;
    }


    //Metodo que Busca señas por texto, LIKE Hola= h,ho,hol,hola sirve para buscador
    public List<Sena> buscarSenas(Connection conn, String texto) throws SQLException {
        String sql =
                "SELECT i.url, s.nombre, s.descripcion " +
                        "FROM imagenes i " +
                        "JOIN peticiones p ON i.idpeticion = p.id " +
                        "JOIN senas s ON s.idpeticion = p.id " +
                        "WHERE LOWER(s.nombre) LIKE LOWER(?);";//imagenes = i, peticiones = p, senas = s
        //en el where Solo obtiene las señas que coincidan con lo que se esta buscando, lower minusculas
        List<Sena> lista = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + texto + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            switch(rs.getInt("tipo")){
                case 1:
                    lista.add(new General(
                            rs.getString("url"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getInt("tipo")
                    ));
                    break;
                case 2:
                    lista.add(new Ofensiva(
                            rs.getString("url"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getInt("tipo")
                    ));
                    break;
                default:
                    System.out.println("Hubo un problema");
                    break;
            }
        }
        return lista;
    }



    //Metodo que obtiene las peticiones que todavía no han sido revisadas
    public List<Peticion> getPeticionesPendientes(Connection conn) throws SQLException {
        List<Peticion> lista = new ArrayList<>();
        String sql = "SELECT * FROM peticiones WHERE estado = 2";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new Peticion(
                    rs.getInt("id"), rs.getString("nombre"),
                    rs.getString("descripcion"), rs.getInt("estado"),
                    rs.getInt("idusuario"),rs.getInt("tipo")
            ));
        }
        return lista;
    }


    //Metodo que cambia el estado de la peticion y Copia la petición a la tabla señas
    public void aceptarPeticion(Connection conn, int id, int idModerador) throws SQLException {
        String sqlUpdate = "UPDATE peticiones SET estado = 1, idmoderador = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sqlUpdate);
        ps.setInt(1, idModerador);
        ps.setInt(2, id);
        ps.executeUpdate();

        String sqlSena = "INSERT INTO senas (nombre, descripcion, idpeticion) " +
                "SELECT nombre, descripcion, id FROM peticiones WHERE id = ?";
        PreparedStatement psSena = conn.prepareStatement(sqlSena);
        psSena.setInt(1, id);
        psSena.executeUpdate();
    }


    //Metodo que rechaza una petición.
    public void rechazarPeticion(Connection conn, int id, int idModerador) throws SQLException {
        String sql = "UPDATE peticiones SET estado = 0, idmoderador = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idModerador);
        ps.setInt(2, id);
        ps.executeUpdate();
    }

    //Metodo que trae las peticiones que un moderador ya revisó
    public List<Peticion> getPeticionesRevisadas(Connection conn, int idModerador) throws SQLException {
        List<Peticion> lista = new ArrayList<>();
        String sql = "SELECT * FROM peticiones WHERE idmoderador = ? AND estado != 2";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idModerador);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new Peticion(
                    rs.getInt("id"), rs.getString("nombre"),
                    rs.getString("descripcion"), rs.getInt("estado"),
                    rs.getInt("idusuario"),
                    rs.getInt("tipo")));
        }
        return lista;
    }


    //Metodo que obtiene las peticiones hechas por un usuario específico
    public List<Peticion> getPeticionesByUsuario(Connection conn, int idUsuario) throws SQLException {
        List<Peticion> lista = new ArrayList<>();
        String sql = "SELECT * FROM peticiones WHERE idusuario = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, idUsuario);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            lista.add(new Peticion(
                    rs.getInt("id"), rs.getString("nombre"),
                    rs.getString("descripcion"), rs.getInt("estado"),
                    rs.getInt("idusuario"),
                    rs.getInt("tipo")));
        }
        return lista;
    }


    //Metodo que trae todos los usuarios
    public List<Usuarios> getTodos(Connection conn) throws SQLException {
        List<Usuarios> lista = new ArrayList<>();

        String sql = "SELECT * FROM usuarios";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            lista.add(new Usuarios(
                    rs.getInt("id"),
                    rs.getString("user"),
                    rs.getString("contra"),
                    rs.getString("telefono"),
                    rs.getString("correo"),
                    rs.getInt("edad"),
                    rs.getInt("rol")
            ) {
                @Override
                public ContextMenu menu() {
                    return null;
                }
            });
        }

        return lista;
    }


    //Metodo que Actualiza datos de un usuario
    public void modificar(Connection conn, int id, String user, String correo, String telefono, int rol) throws SQLException {
        String sql = "UPDATE usuarios SET user=?, correo=?, telefono=?, rol=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, user); ps.setString(2, correo);
        ps.setString(3, telefono); ps.setInt(4, rol);
        ps.setInt(5, id);
        ps.executeUpdate();
    }

    //Metodo que elimina un usuario
    public void eliminar(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

}
