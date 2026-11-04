package ensenamex.ensenamex;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.nio.file.Files;
import java.nio.file.Path;

import java.io.File;
import java.io.IOException;
import ensenamex.ensenamex.Servicios.SenaServicio;
import ensenamex.ensenamex.Servicios.Autenticacion;

public class AgregarSenaController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private ComboBox cmbTipo;

    @FXML
    private Label lblArchivoSeleccionado;
    private File imagenSeleccionada;
    private final SenaServicio senaServicio = new SenaServicio();

    @FXML
    private void seleccionarImagen() {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Seleccionar imagen");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Imágenes",
                        "*.png",
                        "*.jpg",
                        "*.jpeg"
                )
        );

        File archivo = fileChooser.showOpenDialog(null);

        if (archivo == null) {
            return;
        }

        imagenSeleccionada = archivo;
        lblArchivoSeleccionado.setText(archivo.getName());
    }

    @FXML
    private void guardarSena() {

        try {

            if (imagenSeleccionada == null) {
                throw new Exception("Selecciona una imagen.");
            }

            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            int tipo = cmbTipo.getSelectionModel().getSelectedIndex()+1;

            if (nombre.isEmpty()) {
                throw new Exception("Escribe el nombre de la seña.");
            }

            if (descripcion.isEmpty()) {
                throw new Exception("Escribe una descripción.");
            }

            // 1. Copiar imagen al proyecto
            copiarImagenAlProyecto();

            // 2. Generar el nombre que irá a la BD
            String url = nombre
                    .toLowerCase()
                    .replace(" ", "_");

            // 3. Guardar en la base como petición pendiente de aprobación
            int idUsuario = Autenticacion.getUsuarioActual().getId();
            senaServicio.agregarSena(
                    nombre,
                    descripcion,
                    url,
                    tipo,
                    idUsuario
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Tu seña fue enviada y quedará pendiente hasta que un moderador la apruebe.");
            alert.showAndWait();

            App.setRoot("principal");

        } catch (Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        }

    }

    @FXML
    private void volver() throws IOException {
        App.setRoot("principal");
    }
    private void copiarImagenAlProyecto() throws IOException {

        if (imagenSeleccionada == null) {
            throw new IOException("No se seleccionó ninguna imagen.");
        }

        String nombreSena = txtNombre.getText().trim();

        if (nombreSena.isEmpty()) {
            throw new IOException("Primero escribe el nombre de la seña.");
        }

        String nombreArchivo = nombreSena
                .toLowerCase()
                .replace(" ", "_")
                + ".png";

        BufferedImage imagen = ImageIO.read(imagenSeleccionada);

        if (imagen == null) {
            throw new IOException(
                    "El archivo seleccionado no es una imagen válida."
            );
        }

        Path destinoResources = Path.of(
                "src",
                "main",
                "resources",
                "ensenamex",
                "ensenamex",
                "senas",
                nombreArchivo
        );

        Files.createDirectories(destinoResources.getParent());

        ImageIO.write(
                imagen,
                "png",
                destinoResources.toFile()
        );

        Path destinoTarget = Path.of(
                "target",
                "classes",
                "ensenamex",
                "ensenamex",
                "senas",
                nombreArchivo
        );

        Files.createDirectories(destinoTarget.getParent());

        ImageIO.write(
                imagen,
                "png",
                destinoTarget.toFile()
        );

        System.out.println("Imagen convertida correctamente.");
    }

}