package ensenamex.ensenamex;

import ensenamex.ensenamex.Bdd.ConexionDB;
import ensenamex.ensenamex.Bdd.DbUtil;
import ensenamex.ensenamex.Clases.Peticion;
import ensenamex.ensenamex.Servicios.Autenticacion;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;//Initializable
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class MisSenasController implements Initializable {//import javafx.fxml.Initializable;

    @FXML private TableView<Peticion> tablaPeticiones;
    @FXML private TableColumn<Peticion, Integer> colId;
    @FXML private TableColumn<Peticion, String> colDescripcion;
    @FXML private TableColumn<Peticion, String> colEstado;

    private final DbUtil peticionDAO = new DbUtil();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadoTexto"));

        colEstado.setCellFactory(col -> new TableCell<Peticion, String>() {
            @Override
            protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                if (empty || estado == null) {
                    setText(null);
                } else {
                    Text text = new Text(estado);
                    if (estado.equals("Aceptada")) {
                        text.setFill(Color.web("#40A05A"));
                    } else if (estado.equals("Rechazada")) {
                        text.setFill(Color.web("#C0392B"));
                    } else {
                        text.setFill(Color.BLACK);
                    }
                    text.setStyle("-fx-font-family: 'Wendy One'; -fx-font-size: 16;");
                    setGraphic(text);
                    setText(null);
                }
            }
        });

        cargarPeticiones();
    }

    private void cargarPeticiones() {
        try (Connection conn = ConexionDB.conectar()) {
            tablaPeticiones.setItems(FXCollections.observableArrayList(
                    peticionDAO.getPeticionesByUsuario(conn, Autenticacion.getUsuarioActual().getId())
            ));
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void volver() throws IOException {
        App.setRoot("principal");
    }
}