package ensenamex.ensenamex.Clases;

import ensenamex.ensenamex.App;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.io.IOException;
import ensenamex.ensenamex.ModeradorController;
public class Moderador extends Usuarios{

    public Moderador(int id, String user, String contra, String telefono, String correo, int edad, int rol) {
        super(id, user, contra, telefono, correo, edad, rol);
    }

    @Override
    public ContextMenu menu() {
        ContextMenu contextMenu = new ContextMenu();//Creacion del menu visual vacio en pantalla

        // Asignamos la clase CSS al contenedor principal
        contextMenu.getStyleClass().add("menu-personalizado");//conecta el código con el diseño visual (CSS)

        MenuItem item1 = new MenuItem("Ver mis señas");
        MenuItem item2 = new MenuItem("Ver peticiones");
        MenuItem item3 = new MenuItem("Estado peticiones");
        MenuItem itemSalir = new MenuItem("Cerrar sesión");
        itemSalir.getStyleClass().add("item-salir");

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
                }
        );
        item1.setOnAction(e -> {
            try { App.setRoot("misSenas"); }//Intenta cambiar a pantalla misSenas
            catch (IOException ex) { throw new RuntimeException(ex); }
        });

        item2.setOnAction(e -> {
            try {
                ModeradorController controller = App.setRootWithController("moderador");//cambia a la pantalla moderador y obtiene su controlador
                controller.mostrarVerPeticiones();//ejecuta el metodo mostrarVerPeticiones() dentro de AdminController
            }
            catch (IOException ex) { throw new RuntimeException(ex); }
        });

        item3.setOnAction(e -> {
            try {
                ModeradorController controller = App.setRootWithController("moderador");
                controller.mostrarEstadoPeticiones();
            }
            catch (IOException ex) { throw new RuntimeException(ex); }
        });
        contextMenu.getItems().addAll(item1, item2, item3, itemSalir);
        return contextMenu;
        //Agrega las opciones al menu contextual y devuelve listo el menu
    }
}
