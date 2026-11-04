package ensenamex.ensenamex;

import ensenamex.ensenamex.Bdd.ConexionDB;
import ensenamex.ensenamex.Bdd.DbUtil;
import ensenamex.ensenamex.Clases.Peticion;
import ensenamex.ensenamex.Clases.Usuarios;
import ensenamex.ensenamex.Servicios.Autenticacion;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML private TableView<Peticion> tablaPeticiones;
    @FXML private TableColumn<Peticion, Integer> colIdPeticion;
    @FXML private TableColumn<Peticion, String> colNombrePeticion;
    @FXML private TableColumn<Peticion, String> colDescripcion;
    @FXML private TableColumn<Peticion, String> colTipoPend;

    @FXML private TableView<Usuarios> tablaUsuarios;
    @FXML private TableColumn<Usuarios, Integer> colIdUsuario;
    @FXML private TableColumn<Usuarios, String> colUser;
    @FXML private TableColumn<Usuarios, String> colCorreo;
    @FXML private TableColumn<Usuarios, String> colTelefono;
    @FXML private TableColumn<Usuarios, String> colRol;

    @FXML private TextField txtUser;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtTelefono;
    @FXML private ComboBox<String> cmbRol;
    @FXML private TabPane tabPaneAdmin;
    private final DbUtil dbUtil = new DbUtil();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colIdPeticion.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombrePeticion.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colTipoPend.setCellValueFactory(new PropertyValueFactory<>("tipoTexto"));
        cargarPeticiones();

        colIdUsuario.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("user"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rolTexto"));
        cargarUsuarios();

        //Necesario editar cuando se agrega un nuevo usuario
        cmbRol.setItems(FXCollections.observableArrayList(
                "Suspendido", "Administrador", "Moderador", "Usuario"
        ));

        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null) {
                txtUser.setText(nuevo.getUser());
                txtCorreo.setText(nuevo.getCorreo());
                txtTelefono.setText(nuevo.getTelefono());
                cmbRol.setValue(nuevo.getRolTexto());
            }
        });
    }

    private void cargarPeticiones() {
        try (Connection conn = ConexionDB.conectar()) {
            tablaPeticiones.setItems(FXCollections.observableArrayList(
                    dbUtil.getPeticionesPendientes(conn)
            ));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void cargarUsuarios() {
        try (Connection conn = ConexionDB.conectar()) {
            tablaUsuarios.setItems(FXCollections.observableArrayList(
                    dbUtil.getTodos(conn)
            ));
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void aceptarPeticion() {
        Peticion sel = tablaPeticiones.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta(Alert.AlertType.WARNING, "Selecciona una petición."); return; }
        try (Connection conn = ConexionDB.conectar()) {
            dbUtil.aceptarPeticion(conn, sel.getId(), Autenticacion.getUsuarioActual().getId());
            alerta(Alert.AlertType.INFORMATION, "Petición aceptada.");
            cargarPeticiones();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void rechazarPeticion() {
        Peticion sel = tablaPeticiones.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta(Alert.AlertType.WARNING, "Selecciona una petición."); return; }
        try (Connection conn = ConexionDB.conectar()) {
            dbUtil.rechazarPeticion(conn, sel.getId(), Autenticacion.getUsuarioActual().getId());
            alerta(Alert.AlertType.INFORMATION, "Petición rechazada.");
            cargarPeticiones();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void modificarUsuario() {
        Usuarios sel = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta(Alert.AlertType.WARNING, "Selecciona un usuario."); return; }
        int rolIndex = cmbRol.getSelectionModel().getSelectedIndex();
        try (Connection conn = ConexionDB.conectar()) {
            dbUtil.modificar(conn, sel.getId(), txtUser.getText(), txtCorreo.getText(), txtTelefono.getText(), rolIndex);
            alerta(Alert.AlertType.INFORMATION, "Usuario modificado.");
            cargarUsuarios();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void eliminarUsuario() {
        Usuarios sel = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta(Alert.AlertType.WARNING, "Selecciona un usuario."); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar a " + sel.getUser() + "?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection conn = ConexionDB.conectar()) {
                dbUtil.eliminar(conn, sel.getId());
                alerta(Alert.AlertType.INFORMATION, "Usuario eliminado.");
                cargarUsuarios();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    @FXML
    public void cerrarSesion() throws IOException { App.setRoot("login"); }

    private void alerta(Alert.AlertType tipo, String msg) {
        Alert a = new Alert(tipo); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
    @FXML
    public void irAPrincipal() throws IOException { App.setRoot("principal"); }

    public void mostrarControlUsuarios() {
        tabPaneAdmin.getSelectionModel().select(1);
    }

    public void mostrarVerPeticiones() {
        tabPaneAdmin.getSelectionModel().select(0);
    }
}