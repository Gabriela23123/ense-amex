package ensenamex.ensenamex.Servicios;
//Servicios paquete donde se guarda esta logica
import ensenamex.ensenamex.App;
import ensenamex.ensenamex.VerMasController;
import ensenamex.ensenamex.Clases.Sena;
import javafx.stage.Stage;//ventana de una aplicación JavaFX
import java.io.IOException;
import java.util.function.Consumer;//interfaz funcional:recibe un dato, lo utiliza y no devuelve nada
/*
con esta clase reutilizamos codigo y solo llamamos a:
Consumer<Sena> accion = Navegador.prepararNavegacion(stage);
y prepara una acción que se ejecutará cuando el usuario haga clic sobre una seña.
Mostrando la pantalla VerMas
 */
public class Navegador {

    // Este metodo devuelve la función (Consumer) que el botón ejecutará al hacer clic
    public static Consumer<Sena> prepararNavegacion(Stage stageActual) {
        return (sena) -> {//expresión lambda, devuelve el resultado del metodo
            try {
                // Si te marca error aquí, es porque App no tiene ese metodo público y estático
                VerMasController controller = App.setRootWithController("VerMas");

                controller.initData(sena);//llama al metodo iniData dentro del controlador y le pasa la sena
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
}
