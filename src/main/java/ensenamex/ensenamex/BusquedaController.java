package ensenamex.ensenamex;

import ensenamex.ensenamex.Componentes.CreadorTarjetas;
import ensenamex.ensenamex.Clases.Sena;
import ensenamex.ensenamex.Servicios.Autenticacion;
import ensenamex.ensenamex.Servicios.Navegador;
import ensenamex.ensenamex.Servicios.SenaServicio;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.io.IOException;

import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class BusquedaController implements Initializable {
    @FXML
    private TextField txtBuscar;

    @FXML
    private ComboBox<String> cmbTipo;

    @FXML
    private FlowPane resultadosContainer;

    private SenaServicio senaService = new SenaServicio();
    private List<Sena> todasLasSenas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Después de cargar el fxml
        resultadosContainer.setMinWidth(Region.USE_COMPUTED_SIZE);
        resultadosContainer.setMaxWidth(Region.USE_COMPUTED_SIZE);
        try {
            todasLasSenas = senaService.obtenerSenas();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtBuscar.textProperty().addListener((observable, viejo, nuevo) -> {
            // Obtenemos el stage de forma segura
            Stage stageActual = (Stage) txtBuscar.getScene().getWindow();
            buscar(nuevo, stageActual);
        });

        // Cada vez que cambie el filtro, se vuelve a buscar con el texto actual
        cmbTipo.valueProperty().addListener((observable, viejo, nuevo) -> {
            Stage stageActual = (Stage) txtBuscar.getScene().getWindow();
            buscar(txtBuscar.getText(), stageActual);
        });

        // Usamos runLater para la carga inicial
        Platform.runLater(() -> {
            Stage stageActual = (Stage) txtBuscar.getScene().getWindow();
            buscar("", stageActual);
        });
    }

    private void buscar(String texto, Stage stageActual){
        boolean encontro = false;
        int tipo = cmbTipo.getSelectionModel().getSelectedIndex();
        resultadosContainer.getChildren().clear();

        // Filtrado
        if (tipo == 2 && Autenticacion.getUsuarioActual().getEdad() < 18) {
            Label Noxd = new Label("Un chocomilk y a la camita, estas peque XD");
            Noxd.setStyle(
                    "-fx-font-size:22;" +
                            "-fx-font-family:'Wendy One';" +
                            "-fx-text-fill:#9E9E9E;"
            );

            resultadosContainer.getChildren().add(Noxd);
            return;
        }
        for (Sena s : todasLasSenas) {
            // Validar que el texto coincida
            if (s.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                // Validar que el tipo coincida
                if (tipo == -1 || tipo == 0 || s.getTipo() == tipo) {
                    resultadosContainer.getChildren().add(
                            CreadorTarjetas.crearCard(
                                    Autenticacion.getUsuarioActual().getEdad(),
                                    s,
                                    Navegador.prepararNavegacion(stageActual)
                            )
                    );
                    encontro = true;
                }
            }
        }

        if (!encontro) {
            Label sinResultados = new Label("Sin coincidencias");

            sinResultados.setStyle(
                    "-fx-font-size:22;" +
                            "-fx-font-family:'Wendy One';" +
                            "-fx-text-fill:#9E9E9E;"
            );

            resultadosContainer.getChildren().add(sinResultados);
        }
    }
    @FXML
    private void volverAPrincipal() throws IOException {
        App.setRoot("principal");
    }
}
