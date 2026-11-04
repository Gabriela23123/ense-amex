package ensenamex.ensenamex;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static javafx.stage.Stage.*;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setMaximized(true);
        
        // --- Fuentes ---
        try {
            Font.loadFont(getClass().getResourceAsStream("fonts/WendyOne-Regular.ttf"), 48);
            Font.loadFont(getClass().getResourceAsStream("fonts/Itim-Regular.ttf"), 32);
            //Font ysabeau = 
            Font.loadFont(getClass().getResourceAsStream("fonts/YsabeauSC-Regular.ttf"), 40);
            Font.loadFont(getClass().getResourceAsStream("fonts/Roboto-VariableFont_wdth,wght.ttf"), 20);
            
            /*if (ysabeau != null) {
                System.out.println("Fuente cargada");
                System.out.println("Nombre exacto: " + ysabeau.getName());
                System.out.println("Familia: " + ysabeau.getFamily());
            } else {
                System.out.println("Error: Javafx no encontró el archivo");
            }*/
        } catch (Exception e) {
            System.out.println("⚠️ Alerta: No se pudieron registrar los archivos TTF.");
        }
        // ----------------------------------

        scene = new Scene(loadFXML("login"), 800, 600);
        //título a la ventana
        stage.setTitle("EnseñaMex");

        //Aquí se carga Logo.png como ícono de la aplicación
        stage.getIcons().add(new Image(App.class.getResourceAsStream("IconoEnsenamex.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        // 1. Construimos la ruta exacta
        String ruta = fxml + ".fxml";
        URL url = App.class.getResource(ruta);

        // 2. Verificamos si realmente existe
        if (url == null) {
            System.err.println("¡ERROR CRÍTICO! No se encontró el archivo: " + ruta);
            System.err.println("Revisa si está en src/main/resources/");
            return;
        }

        // 3. Si existe, cargamos
        System.out.println("Cargando archivo: " + url.toExternalForm());
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        scene.setRoot(fxmlLoader.load());
    }

    public static <T> T setRootWithController(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        scene.setRoot(root);

        // Forzamos la maximización aquí cada vez que se cambia la raíz
        Stage stage = (Stage) scene.getWindow();
        stage.setMaximized(true);

        return fxmlLoader.getController(); // Retornamos el controlador
    }

    private static Parent loadFXML(String fxml) throws IOException {
        // Aquí es donde está fallando. Vamos a imprimir la ruta que está buscando.
        String ruta = fxml + ".fxml";
        java.net.URL url = App.class.getResource(ruta);

        if (url == null) {
            // Esto se imprimirá en la conso la justo antes de que el programa truene
            throw new IOException("¡No se pudo encontrar el archivo FXML en: " + ruta);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(url);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}