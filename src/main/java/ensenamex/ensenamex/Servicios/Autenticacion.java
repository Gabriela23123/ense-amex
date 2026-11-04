package ensenamex.ensenamex.Servicios;
//Servicios paquete donde se guarda esta logica
import ensenamex.ensenamex.Bdd.ConexionDB;
import ensenamex.ensenamex.Interfaces.Login;
import ensenamex.ensenamex.Clases.Admin;
import ensenamex.ensenamex.Clases.Comun;
import ensenamex.ensenamex.Clases.Moderador;
import ensenamex.ensenamex.Clases.Usuarios;

import java.sql.*;//trae todas las herramientas de base de datos SQL

public class Autenticacion implements Login {
    //Cualquier cosa implementada debe tener un metodo que valida los usuarios

    private static Usuarios usuarioActual;
    //static: la variable pertenece a la clase y no a un objeto

    //Setter, asigna valor a UsuarioActual, recibe un objeto de tipo Usuarios
    public static void setUsuarioActual(Usuarios u) {
        usuarioActual = u;
    }

    //Getter
    public static Usuarios getUsuarioActual() {
        return usuarioActual;
    }

    @Override
    public boolean validaUsuarios(String user, String contra) {
        String query = "SELECT * FROM usuarios WHERE user = ? AND contra = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            /*
            PreparedStatement = "Sentencia preparada" herramienta de SQL que toma el query
            (con los signos de interrogación) y la prepara para ser ejecutada de manera segura.
             */
            System.out.println("Autenticando....");
            pstmt.setString(1, user);//Aqui se rellenan los ?
            pstmt.setString(2, contra);

            ResultSet rs = pstmt.executeQuery();
            //ResulSet = conjunto de resultados,guarda la respuesta que devuelve la BDD
            //executeQuery() = Ejecuta la busqueda en la BDD
            if (rs.next() == true) {
                //rs.next() = mueve el cursor de la tabla virtual a la primera fila de respuestas
                //si devuelve true encontro al usuario con esas credenciales

                String u = rs.getString("user");
                //trae la info de la BDD y las guarda en variables temporales
                String c = rs.getString("contra");
                String t = rs.getString("telefono");
                String co = rs.getString("correo");
                int e = rs.getInt("edad");
                int id = rs.getInt("id");
                int r = rs.getInt("rol");


                //Necesario editar cuando se agrega un nuevo usuario
                switch (rs.getInt("rol")) {
                    case 0:
                        usuarioActual = null;
                        return false;
                        //0 = (Suspendido), borra el usuario actual (null) y devuelve false (no lo deja entrar).
                    case 1:
                        usuarioActual = new Admin(id, u, c, t, co, e, r);
                        //toma la variable global y le mete un objeto de la clase Admin
                        break;
                    case 2:
                        usuarioActual = new Moderador(id, u, c, t, co, e, r);
                        break;
                    case 3:
                        usuarioActual = new Comun(id, u, c, t, co, e, r);
                        break;
                }
                usuarioActual.setId(id);
                return true;
                //se creó el usuario, devuelve verdadero y Login exitoso
            } else {
                return false;
                //No se encontro el usuario y devuelve falso
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
