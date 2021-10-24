package models;

public class Habilidad {

    private int id;
    private String name;
    private String nombre;
    private String url;
    private String descripcion;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Habilidad (String name, String url){
        this.name=name;
        this.url=url;
    }

    public int getId() {
        String [] urlPartes= url.split("/");
        return Integer.parseInt(urlPartes[urlPartes.length-1]);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
