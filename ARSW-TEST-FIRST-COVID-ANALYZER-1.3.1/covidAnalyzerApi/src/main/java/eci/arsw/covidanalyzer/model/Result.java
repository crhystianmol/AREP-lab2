package eci.arsw.covidanalyzer.model;

public class Result {
    public String nombre;
    public String apellido;
    public int id;
    public String telefono;
    public ResultType tipo;
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public ResultType getTipo() {
        return tipo;
    }
    public void setTipo(ResultType tipo) {
        this.tipo = tipo;
    }
    public Result(String nombre, String apellido, int id, String telefono, ResultType tipo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.id = id;
        this.telefono = telefono;
        this.tipo = tipo;
    }
}
