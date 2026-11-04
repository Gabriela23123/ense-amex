package ensenamex.ensenamex.Clases;

import ensenamex.ensenamex.App;
import javafx.scene.control.Alert;//permite crear ventanas emergentes de aviso.
import javafx.scene.control.ContextMenu;//crea un menú desplegable
import javafx.scene.control.MenuItem;//opciones que van dentro de ContextMenu
import java.io.IOException;

public class Comun extends Usuarios {

    public Comun(int id, String user, String contra, String telefono, String correo, int edad, int rol) {
        super(id, user, contra, telefono, correo, edad, rol);
    }

    @Override
    public ContextMenu menu() {
        ContextMenu contextMenu = new ContextMenu();//Creacion del menu visual vacio en pantalla

        // Asignamos la clase CSS al contenedor principal
        contextMenu.getStyleClass().add("menu-personalizado");//conecta el código con el diseño visual (CSS)

        MenuItem item1 = new MenuItem("Ver mis señas");
        MenuItem item2 = new MenuItem("Ser moderador");
        MenuItem itemSalir = new MenuItem("Cerrar sesión");
        itemSalir.getStyleClass().add("item-salir");

        // Acción para cerrar sesión
        itemSalir.setOnAction(e -> {
            /*
            setOnAction le dice al programa que hacer cuando el usuario da clic en cerrar sesion
            e -> {: "expresión Lambda". Cuando ocurra el evento (e), se ejecuta el código que está entre las llaves
             */
            System.out.println("Cerrando sesión...");
            try {
                App.setRoot("login");//Intenta cambiar a pantalla login
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        item1.setOnAction(e -> {
            try { App.setRoot("misSenas"); }//Intenta cambiar a pantalla misSenas
            catch (IOException ex) { throw new RuntimeException(ex); }
        });

        // Lógica para el item2: Contactar al administrador
        item2.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ser moderador");
            alert.setHeaderText(null);
            alert.setContentText("Comuníquese al correo del administrador: admin@ensenamex.com");
            alert.showAndWait();//muestra y espera hasta que de clic en ok
        });

        contextMenu.getItems().addAll(item1, item2, itemSalir);
        return contextMenu;
        /*
        Agrega las opciones al menu contextual y devuelve listo el menu
         */
    }
}
