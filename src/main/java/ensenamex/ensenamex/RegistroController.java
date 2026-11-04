package ensenamex.ensenamex;

import ensenamex.ensenamex.Bdd.ConexionDB;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegistroController {
    @FXML private TextField txtUser, txtTelefono, txtCorreo;
    @FXML private PasswordField txtContra;
    @FXML private Spinner<Integer> spinnerEdad;
    @FXML private Button btnRegistro;

    @FXML
    public void initialize() {
        System.out.println("Si entro a registro");
        // Esto asegura que el spinner tenga un rango válido desde el inicio
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 18); // Rango 1-100, valor inicial 18
        spinnerEdad.setValueFactory(valueFactory);

        //usa lambda
        txtUser.setOnAction(event -> txtContra.requestFocus());
        txtContra.setOnAction(event -> txtTelefono.requestFocus());
        txtTelefono.setOnAction(event -> txtCorreo.requestFocus());
        txtCorreo.setOnAction(event -> spinnerEdad.requestFocus());

        // Solución para el Spinner: acceder a su editor interno
        spinnerEdad.getEditor().setOnAction(event -> btnRegistro.requestFocus());

        btnRegistro.setOnAction(event -> {
            try {
                registrarUsuario();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void registrarUsuario() {
        //validacion de campos
        if (txtUser.getText().trim().isEmpty() ||
                txtContra.getText().trim().isEmpty() ||
                txtTelefono.getText().trim().isEmpty() ||
                txtCorreo.getText().trim().isEmpty() ||
                spinnerEdad.getValue() == null ||
                spinnerEdad.getEditor().getText().trim().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos incompletos");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, llena todos los campos antes de registrarte.");
            alert.showAndWait();
            return; // Detiene la ejecución si hay campos vacíos
        }

        String sql = "INSERT INTO usuarios (user, contra, correo, telefono, rol, edad) VALUES (?, ?, ?, ?, 3, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtUser.getText());
            pstmt.setString(2, txtContra.getText());
            pstmt.setString(3, txtCorreo.getText());
            pstmt.setString(4, txtTelefono.getText());
            pstmt.setInt(5, spinnerEdad.getValue());

            pstmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Usuario registrado con éxito");
            alert.showAndWait();
            App.setRoot("login"); // Regresar al login

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            //Error de usuario duplicado
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Usuario no disponible");
            alert.setHeaderText(null);
            alert.setContentText("El nombre de usuario '" + txtUser.getText() + "' ya está ocupado. Por favor, elige otro.");
            alert.showAndWait();

            //Le regresamos el cursor al campo de usuario para que lo corrija rápido
            txtUser.requestFocus();
            txtUser.selectAll();

        } catch (Exception e) {
            // Este catch general se queda para cualquier otro error (falla de internet, base de datos caída, etc.)
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al registrar: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void volver() throws IOException {
        App.setRoot("login");
    }
}
