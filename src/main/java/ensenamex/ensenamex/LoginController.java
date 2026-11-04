package ensenamex.ensenamex;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ensenamex.ensenamex.Interfaces.Login;
import ensenamex.ensenamex.Servicios.Autenticacion;
import ensenamex.ensenamex.Servicios.SenaServicio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class LoginController implements Initializable {

    @FXML
    private ImageView logoImageView;

    @FXML
    private void switchToPrincipal() throws IOException {
        App.setRoot("principal");
    }

    @FXML
    private TextField username;

    @FXML
    private PasswordField contra;

    @FXML
    private Button btnEntrar;

    @FXML
    private HBox carruselContenedor;

    private SenaServicio senaService = new SenaServicio();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Cargar la imagen desde la carpeta de recursos
            Image logo = new Image(getClass().getResourceAsStream("Logo.png"));

            // Asignarla al ImageView
            logoImageView.setImage(logo);

        } catch (Exception e) {
            System.out.println("Error: No se pudo cargar la imagen del logo.");
            e.printStackTrace();
        }

        // --- hover ---
        String estiloNormal = "-fx-background-color: #40A05A; -fx-background-radius: 25; -fx-text-fill: white;";
        String estiloHover = "-fx-background-color: #296C3B; -fx-background-radius: 25; -fx-text-fill: white; -fx-cursor: hand;";

        btnEntrar.setStyle(estiloNormal);

        // Cuando el mouse ENTRA
        btnEntrar.setOnMouseEntered(e -> {
            btnEntrar.setStyle(estiloHover);
            btnEntrar.setScaleX(1.05); // Aumenta 5% el ancho
            btnEntrar.setScaleY(1.05); // Aumenta 5% la altura
        });

        // Cuando el mouse SALE
        btnEntrar.setOnMouseExited(e -> {
            btnEntrar.setStyle(estiloNormal);
            btnEntrar.setScaleX(1.0); // Regresa al tamaño original
            btnEntrar.setScaleY(1.0);
        });

        //usa lambda
        username.setOnAction(event -> contra.requestFocus());

        contra.setOnAction(event -> {
            try {
                entrar();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public void entrar() {
        String u = username.getText();
        String c = contra.getText();

        //Validar que no estén vacíos antes de ir a la base de datos
        if (u.isEmpty() || c.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Por favor, llena todos los campos.");
            alert.showAndWait();
            return;
        }

        Login login = new Autenticacion();

        //Intentamos validar las credenciales
        if (login.validaUsuarios(u, c)) {

            //Si las credenciales son correctas, verificamos el ROL del usuario
            if (Autenticacion.getUsuarioActual() != null && Autenticacion.getUsuarioActual().getRol() == 0) {
                // Es rol 0, está suspendido.
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Acceso denegado");
                alert.setHeaderText("Cuenta suspendida");
                alert.setContentText("Tu cuenta ha sido suspendida. Contacta al administrador.");
                alert.showAndWait();

            } else {
                // Las credenciales son correctas y NO está suspendido. ¡Lo dejamos pasar!
                try {
                    switchToPrincipal();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            //Si validaUsuarios fue falso, simplemente se equivocó en sus credenciales
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de inicio de sesión");
            alert.setHeaderText("Credenciales incorrectas");
            alert.setContentText("El usuario o la contraseña no son correctos.");
            alert.showAndWait();
        }
    }

    @FXML
    private void irARegistro(ActionEvent event) throws IOException {
        App.setRoot("Registro");
        System.out.println("Intenta cambiar de pagina");
    }

}
