module ensenamex.ensenamex {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires java.sql;
    requires java.desktop;

    opens ensenamex.ensenamex to javafx.fxml;
    exports ensenamex.ensenamex;
    exports ensenamex.ensenamex.Bdd;
    opens ensenamex.ensenamex.Bdd to javafx.fxml;
    exports ensenamex.ensenamex.Clases;
    opens ensenamex.ensenamex.Clases to javafx.fxml;
    exports ensenamex.ensenamex.Interfaces;
    opens ensenamex.ensenamex.Interfaces to javafx.fxml;
    exports ensenamex.ensenamex.Servicios;
    opens ensenamex.ensenamex.Servicios to javafx.fxml;
    exports ensenamex.ensenamex.Componentes;
    opens ensenamex.ensenamex.Componentes to javafx.fxml;
}