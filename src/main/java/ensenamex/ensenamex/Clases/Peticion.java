package ensenamex.ensenamex.Clases;

import javafx.beans.property.*;
/*
Permite usar las clases de tipo Property.
Estas representan datos que pueden ser observados y notificar automáticamente cuando cambian.
 */

public class Peticion {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty descripcion = new SimpleStringProperty();
    private final IntegerProperty estado = new SimpleIntegerProperty();
    private final IntegerProperty idusuario = new SimpleIntegerProperty();
    private final IntegerProperty tipoint = new SimpleIntegerProperty();
    /*
    Final: significa que la "caja" que guarda el dato nunca va a cambiar de lugar en la memoria,
    aunque el dato de adentro sí cambie.
    IntegerProperty / StringProperty: En vez de usar un int o un String normal, usas esto.
    Son variables de JavaFX. Si el valor de una de estas variables cambia, automáticamente
    avisan a la pantalla para que se actualice sola, sin que tengamos que recargar la tabla.
     */

    public Peticion(int id, String nombre, String descripcion, int estado, int idusuario, int tipo) {
        this.id.set(id);//.set() cambia el valor de la Property, es decir, guarda el id recibido dentro de la propiedad id
        this.nombre.set(nombre);
        this.descripcion.set(descripcion);
        this.estado.set(estado);
        this.idusuario.set(idusuario);
        this.tipoint.set(tipo);
    }

    public int getId() { return id.get(); }
    //La propiedad guarda el dato, pero para obtenerlo es id.get()
    public IntegerProperty idProperty() { return id; }
    //metodo que devuelve la propiedad completa
    public String getNombre() { return nombre.get(); }
    public StringProperty nombreProperty() { return nombre; }
    public String getDescripcion() { return descripcion.get(); }
    public StringProperty descripcionProperty() { return descripcion; }
    public int getEstado() { return estado.get(); }
    public IntegerProperty estadoProperty() { return estado; }
    public int getIdusuario() { return idusuario.get(); }
    public IntegerProperty idusuarioProperty() { return idusuario; }

    public String getEstadoTexto() {
        //metodo que convierte el numero del estado en texto
        if (estado.get() == 2) return "Pendiente";
        else if (estado.get() == 1) return "Aceptada";
        else if (estado.get() == 0) return "Rechazada";
        else return "Desconocido";
    }

    public String getTipoTexto() {
        //metodo que convierte el numero del tipo en texto
        if (tipoint.get() == 2) return "Ofensiva";
        else if (tipoint.get() == 1) return "General";
        else return "Desconocido";
    }

}