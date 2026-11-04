package ensenamex.ensenamex.Clases;

import javafx.scene.control.ContextMenu;//sirve para crear un menú desplegable

public abstract class Usuarios {
    private int id;
    private String user;
    private String contra;
    private String telefono;
    private String correo;
    private int edad;
    private int rol;

    // Constructor unificado para la TablaView de JavaFX donde se muestran los usuarios y la BD
    public Usuarios(int id, String user, String contra, String telefono, String correo, int edad, int rol) {
        this.id = id;
        this.user = user;
        this.contra = contra;
        this.telefono = telefono;
        this.correo = correo;
        this.edad = edad;
        this.rol = rol;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    public String getContra() {
        return contra;
    }
    public void setContra(String contra) {
        this.contra = contra;
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getEdad() {
        return edad;
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getRol() {
        return rol;
    }
    public void setRol(int rol) {
        this.rol = rol;
    }

    public abstract ContextMenu menu();
    //metodo abstracto que devuelve un objeto de tipo ContextMenu llamado menu
    //usage=veces que se llamo, Y implementations= clases que implementaron el metodo menu


    /*
    usuario.getRolTexto();
    Metodo que muestra el rol de un usuario en texto, en lugar de mostrar el número
    que está guardado en la base de datos.
    Solo lo usa el administrador porque necesita ver y editar los roles de otros usuarios.
     */

    //Necesario editar cuando se agrega un nuevo usuario
    public String getRolTexto() {
        if (rol == 0) return "Suspendido";
        else if (rol == 1) return "Administrador";
        else if (rol == 2) return "Moderador";
        else if (rol == 3) return "Usuario";
        else return "Desconocido";
    }
}
