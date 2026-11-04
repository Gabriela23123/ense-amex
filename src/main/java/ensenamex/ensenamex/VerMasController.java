package ensenamex.ensenamex;

import ensenamex.ensenamex.Clases.Sena;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;

public class VerMasController {

    @FXML private ImageView imgSena;
    @FXML private Text txtTituloSena;
    @FXML private Text txtDescripcionSena;
    @FXML private Label lblTipo;

    // Metodo para recibir los datos desde la pantalla principal
    public void initData(Sena sena) {
        txtTituloSena.setText(sena.getNombre());
        txtDescripcionSena.setText(sena.getDescripcion());

        System.out.println("DEBUG: Recibiendo datos de la seña: " + (sena != null ? sena.getNombre() : "NULO"));

        if (sena != null) {
            // Verificamos si los componentes FXML están inyectados (no son null)
            if (txtTituloSena != null) {
                txtTituloSena.setText(sena.getNombre());
            } else {
                System.err.println("ERROR: txtTituloSena es NULL. ¿Está bien el @FXML?");
            }

            //Para el label del tipo
            if (lblTipo != null) {
                if (sena.getTipo() == 1) {
                    lblTipo.setText("General");
                } else if (sena.getTipo() == 2) {
                    lblTipo.setText("Ofensiva");
                } else {
                    lblTipo.setText("Desconocido");
                }

            } else {
                System.err.println("ERROR: lblTipo es NULL. ¿Está bien el @FXML?");
            }

            txtDescripcionSena.setText(sena.getDescripcion());
        }

        // Cargamos la imagen desde la URL que viene en tu DTO
        if (sena.getUrl() != null && !sena.getUrl().isEmpty()) {
            // Asegúrate de que el nombre del archivo coincida con lo que guardaste
            // Si en la BD guardas "imagen1", le añadimos el .png
            String nombreArchivo = sena.getUrl() + ".png";

            // Buscamos en el mismo lugar que el CardFactory
            String rutaCompleta = "/ensenamex/ensenamex/Senas/" + nombreArchivo;

            try {
                // Usamos getResourceAsStream para asegurar que lea desde el JAR/Classpath
                Image image = new Image(getClass().getResourceAsStream(rutaCompleta));
                imgSena.setImage(image);
            } catch (Exception e) {
                System.err.println("No se pudo cargar la imagen: " + rutaCompleta);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void volver() throws IOException {
        App.setRoot("principal");
    }
}