package ensenamex.ensenamex;

import java.io.IOException;

import ensenamex.ensenamex.Clases.Usuarios;
import ensenamex.ensenamex.Servicios.Autenticacion;
import ensenamex.ensenamex.Servicios.Navegador;
import ensenamex.ensenamex.Servicios.SenaServicio;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import ensenamex.ensenamex.Clases.Sena;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import ensenamex.ensenamex.Componentes.CreadorTarjetas;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PantallaPrincipalController implements Initializable {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private HBox carouselContainer;

    @FXML
    private ImageView iconoUsuario;

    @FXML
    private Text usernameLbl;

    @FXML
    private Text correoLbl;

    private SenaServicio senaService = new SenaServicio();

    @FXML
    private void irAgregarSena() throws IOException {
        App.setRoot("AgregarSena");
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // 1. Obtener los datos desde tu Base de Datos
            List<Sena> senas = senaService.obtenerSenas("Si xd");

            // Limpiamos el contenedor por si acaso antes de rellenar
            if (carouselContainer != null) {
                carouselContainer.getChildren().clear();
                carouselContainer.setMinWidth(HBox.USE_PREF_SIZE);
                Platform.runLater(() -> {
                    // Ahora sí, cuando esto se ejecute, la escena ya existe
                    Stage stageActual = (Stage) scrollPane.getScene().getWindow();
                    System.out.println(stageActual);

                    for (Sena s : senas) {
                        VBox tarjeta = CreadorTarjetas.crearCard(Autenticacion.getUsuarioActual().getEdad(), s, Navegador.prepararNavegacion(stageActual));
                        if (tarjeta != null) {
                            carouselContainer.getChildren().add(tarjeta);
                        }
                    }

                });
            }
        } catch (Exception e) {
            System.out.println("Error al cargar los elementos del carrusel.");
            e.printStackTrace();
        }

        Usuarios usuario = Autenticacion.getUsuarioActual();

        if (usuario != null) {
            usernameLbl.setText(usuario.getUser());
            correoLbl.setText(usuario.getCorreo());
        }

    }
    @FXML
    private void scrollLeft() {
        if (scrollPane != null && carouselContainer != null && !carouselContainer.getChildren().isEmpty()) {
            // Calculamos cuánto equivale una tarjeta (160px + 30px de espacio entre ellas) en el porcentaje del Scroll
            double widthTarjeta = 190.0;
            double widthTotal = carouselContainer.getBoundsInLocal().getWidth();
            double incremento = widthTarjeta / widthTotal;

            scrollPane.setHvalue(scrollPane.getHvalue() - incremento);
        }
    }

    @FXML
    private void scrollRight() {
        if (scrollPane != null && carouselContainer != null && !carouselContainer.getChildren().isEmpty()) {
            // Calculamos cuánto equivale una tarjeta (160px + 30px de espacio entre ellas) en el porcentaje del Scroll
            double widthTarjeta = 190.0;
            double widthTotal = carouselContainer.getBoundsInLocal().getWidth();
            double incremento = widthTarjeta / widthTotal;

            scrollPane.setHvalue(scrollPane.getHvalue() + incremento);
        }
    }
    @FXML
    private void irABusqueda() throws IOException {
        App.setRoot("busqueda");
        System.out.println("Intenta cambiar de pagina");
    }

    @FXML
    private void mostrarMenuFlotante(MouseEvent event) {
        Usuarios usuario = Autenticacion.getUsuarioActual();

        if (usuario != null) {
            ContextMenu menu = usuario.menu();

            // forzamas la carga css
            if (iconoUsuario.getScene().getStylesheets().isEmpty()) {
                String css = getClass().getResource("styles.css").toExternalForm();
                iconoUsuario.getScene().getStylesheets().add(css);
            }

            // Muestra el menú justo debajo del ícono de usuario
            menu.show(iconoUsuario, Side.BOTTOM, -150, 5);
        }
    }

}