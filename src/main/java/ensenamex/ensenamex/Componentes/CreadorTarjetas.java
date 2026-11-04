package ensenamex.ensenamex.Componentes;

import ensenamex.ensenamex.Clases.Sena;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.function.Consumer;

public class CreadorTarjetas {


    public static VBox crearCard(int edad, Sena sena , Consumer<Sena> onClick) {
        VBox card = new VBox();

        Region espaciador = new Region();
        VBox.setVgrow(espaciador, javafx.scene.layout.Priority.ALWAYS);

        //Cambiamos el alineamiento del VBox de CENTER a TOP_CENTER
        card.setAlignment(Pos.TOP_CENTER);

        card.setSpacing(10); //espacio entre cartas

        card.setMinWidth(200);   // Ancho mínimo
        card.setPrefWidth(250);  // Ancho ideal
        card.setMaxWidth(280);   // Ancho maximo

        // En lugar de una altura estricta de 400 que causa conflicto:
        //card.setMinHeight(Region.USE_COMPUTED_SIZE);
        //card.setPrefHeight(Region.USE_COMPUTED_SIZE);
        //card.setMaxHeight(Region.USE_PREF_SIZE);

        //card.setMinHeight(400);
        //card.setPrefHeight(400);

        card.setStyle(
                "-fx-border-color: #EDE7F6;" +
                        "-fx-border-radius:30;" +
                        "-fx-background-radius:30;" +
                        "-fx-padding:15;" +
                        "-fx-background-color:#ECE6F0;" +
                        "-fx-effect:dropshadow(gaussian, rgba(0,0,0,0.10),10,0,0,4);"
        );

        String nombreArchivo = sena.getUrl() + ".png";

        URL imgUrl = CreadorTarjetas.class.getResource("/ensenamex/ensenamex/Senas/" + nombreArchivo);

        ImageView img = new ImageView();

        img.setPreserveRatio(true);
        img.setFitWidth(150); // Tamaño máximo dentro del marco
        img.setFitHeight(150);

        // Creamos el marco para la imagen
        StackPane marcoImagen = new StackPane();
        marcoImagen.setPrefSize(160, 160); // Tamaño fijo del marco
        marcoImagen.setMaxSize(160, 160);
        marcoImagen.setAlignment(Pos.CENTER);

        if (imgUrl != null) {
            marcoImagen.setStyle(
                    "-fx-background-image: url('" + imgUrl.toExternalForm() + "');" +
                            "-fx-background-size: cover;" +
                            "-fx-background-position: center;" +
                            "-fx-background-radius: 50;" +
                            "-fx-background-insets: 0;" +
                            "-fx-border-radius: 50;" +
                            "-fx-border-color: transparent;"
            );
        } else {
            // Opcional: Pon un color de fondo para saber que la imagen falló
            marcoImagen.setStyle("-fx-background-color: #cccccc; -fx-background-radius: 50;");
            System.err.println("No se encontró el archivo en: /ensenamex/ensenamex/senas/" + nombreArchivo);
        }

        //Da un poco de espacio a la imagen arriba
        VBox.setMargin(marcoImagen, new javafx.geometry.Insets(30, 0, 0, 0));


        Label nombre = new Label(
                sena.getNombre()
        );

        //label tipo
        Label Tipo = new Label( sena.getTipoTexto() );

        nombre.setStyle(
                "-fx-font-family:'Ysabeau SC Regular';" +
                        "-fx-font-size:42;" +
                        "-fx-text-fill:#000000;"
        );

        Tipo.setStyle(
                "-fx-font-family:'Itim';" +
                        "-fx-font-size:20;" +
                        "-fx-text-fill:#000000;"
        );

        Button btnVerMas = new Button("Ver más");


        String estiloNormal = "-fx-background-color:#40A05A; " +

                "-fx-background-radius:30; " +

                "-fx-text-fill:white; " +

                "-fx-cursor:hand; " +

                "-fx-font-size: 22px;" +

                "-fx-padding: 5 20 5 20;" +

                "-fx-font-family:'Itim';" +

                "-fx-min-width: 50px;";


        String estiloHover = "-fx-background-color:#1F6733; " +
                "-fx-background-radius:30; " +
                "-fx-text-fill:white; " +
                "-fx-cursor:hand; " +
                "-fx-font-size: 22px;" +
                "-fx-padding: 5 20 5 20;" +
                "-fx-font-family:'Itim';" +
                "-fx-min-width: 50px;";

        btnVerMas.setStyle(estiloNormal);
        btnVerMas.setOnMouseEntered(e -> {

            btnVerMas.setStyle(estiloHover);
            btnVerMas.setScaleX(1.1);
            btnVerMas.setScaleY(1.1);

        });


        btnVerMas.setOnMouseExited(e -> {
            btnVerMas.setStyle(estiloNormal);
            btnVerMas.setScaleX(1.0);
            btnVerMas.setScaleY(1.0);
        });

        // Cuando den clic, ejecutamos la función que nos pasaron
        btnVerMas.setOnAction(event -> onClick.accept(sena));

        // margen al boton
        VBox.setMargin(btnVerMas, new javafx.geometry.Insets(0, 0, 50, 0)); // 10px de espacio abajo
        card.getChildren().addAll(marcoImagen, espaciador, nombre, Tipo, btnVerMas);

        if (edad < 18 && sena.getTipo()==2) {
            return null;
        } else {
            return card;
        }

    }


}