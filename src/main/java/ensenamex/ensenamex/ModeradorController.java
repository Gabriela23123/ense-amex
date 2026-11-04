package ensenamex.ensenamex;

import ensenamex.ensenamex.Bdd.ConexionDB;
import ensenamex.ensenamex.Bdd.DbUtil;
import ensenamex.ensenamex.Clases.Peticion;
import ensenamex.ensenamex.Servicios.Autenticacion;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class ModeradorController implements Initializable {
    @FXML private TabPane tabPaneModerador;
    @FXML private TableView<Peticion> tablaPendientes;
    @FXML private TableColumn<Peticion, Integer> colIdPend;
    @FXML private TableColumn<Peticion, String> colNombrePend;
    @FXML private TableColumn<Peticion, String> colDescPend;
    @FXML private TableColumn<Peticion, String> colTipoPend;

    @FXML private TableView<Peticion> tablaRevisadas;
    @FXML private TableColumn<Peticion, Integer> colIdRev;
    @FXML private TableColumn<Peticion, String> colNombreRev;
    @FXML private TableColumn<Peticion, String> colEstadoRev;


    private final DbUtil peticionDAO = new DbUtil();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colIdPend.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombrePend.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescPend.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colTipoPend.setCellValueFactory(new PropertyValueFactory<>("tipoTexto"));
        cargarPendientes();

        colIdRev.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreRev.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEstadoRev.setCellValueFactory(new PropertyValueFactory<>("estadoTexto"));
        cargarRevisadas();
    }

    private void cargarPendientes() {
        try (Connection conn = ConexionDB.conectar()) {
            tablaPendientes.setItems(FXCollections.observableArrayList(
                    peticionDAO.getPeticionesPendientes(conn)
            ));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void cargarRevisadas() {
        try (Connection conn = ConexionDB.conectar()) {
            tablaRevisadas.setItems(FXCollections.observableArrayList(
                    peticionDAO.getPeticionesRevisadas(conn, Autenticacion.getUsuarioActual().getId())
            ));
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void aceptarPeticion() {
        Peticion sel = tablaPendientes.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta(Alert.AlertType.WARNING, "Selecciona una petición."); return; }
        try (Connection conn = ConexionDB.conectar()) {
            peticionDAO.aceptarPeticion(conn, sel.getId(), Autenticacion.getUsuarioActual().getId());
            alerta(Alert.AlertType.INFORMATION, "Petición aceptada.");
            cargarPendientes(); cargarRevisadas();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void rechazarPeticion() {
        Peticion sel = tablaPendientes.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta(Alert.AlertType.WARNING, "Selecciona una petición."); return; }
        try (Connection conn = ConexionDB.conectar()) {
            peticionDAO.rechazarPeticion(conn, sel.getId(), Autenticacion.getUsuarioActual().getId());
            alerta(Alert.AlertType.INFORMATION, "Petición rechazada.");
            cargarPendientes(); cargarRevisadas();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void cerrarSesion() throws IOException { App.setRoot("login"); }

    private void alerta(Alert.AlertType tipo, String msg) {
        Alert a = new Alert(tipo); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
    @FXML
    public void irAPrincipal() throws IOException { App.setRoot("principal"); }

    public void mostrarVerPeticiones() {
        tabPaneModerador.getSelectionModel().select(0);
    }

    public void mostrarEstadoPeticiones() {
        tabPaneModerador.getSelectionModel().select(1);
    }
}