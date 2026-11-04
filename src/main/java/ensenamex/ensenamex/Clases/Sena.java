package ensenamex.ensenamex.Clases;
//Clase que sirve para representar una seña como un objeto y guarda la información de una Seña
public abstract class Sena {
    private String url;
    private String nombre;
    private String descripcion;
    private int tipo;

    public Sena(String url, String nombre, String descripcion, int tipo) {
        this.url = url;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }


    public String getUrl() {
        return url;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getTipo() {
        return tipo;
    }

    public String getTipoTexto() {
        switch (tipo) {
            case 1: return "General";
            case 2: return "Ofensivo";
            default: return "Desconocido";
        }
    }
}
